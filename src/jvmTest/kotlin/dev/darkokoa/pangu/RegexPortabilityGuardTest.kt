package dev.darkokoa.pangu

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Pangu.kt stays inside the regex subset that JVM (including Java 8), Android ICU, JS and wasmJs
 * (Kotlin compiles those with the u flag), and Kotlin/Native all accept. CI's JDK is newer than
 * the bytecode target, and it does not run Android's ICU engine, so a pattern can pass CI and
 * still throw the first time the Pangu object is touched on Android.
 */
class RegexPortabilityGuardTest {

    @Test
    fun compiledPatternsStayInThePortableSubset() {
        // Touch the object so every field initializer has run, including the dynamic patterns.
        Pangu.spacingText("前面 https://example.com/a 后面")
        Pangu.spacingText("前面`中間`後面")
        val patterns = Pangu::class.java.declaredFields.mapNotNull { field ->
            if (!Regex::class.java.isAssignableFrom(field.type)) return@mapNotNull null
            field.isAccessible = true
            val regex = field.get(Pangu) as Regex
            field.name to regex.pattern
        }
        assertTrue(patterns.size > 20, "expected Regex fields on Pangu")
        val offenders = patterns.flatMap { (name, pattern) ->
            portableRegexViolations(pattern).map { "$name: $it" }
        }
        assertEquals(emptyList(), offenders)
    }

    @Test
    fun sourceRegexCallsStayInThePortableSubset() {
        val literals = regexCallLiterals(panguSource())
        assertTrue(literals.any { "(?<!" in it || "(?<=" in it }, "expected lookbehinds in Pangu.kt")
        assertTrue(literals.any { "[0-9]+" in it }, "expected the placeholder pattern")
        val offenders = literals.flatMap { literal ->
            portableRegexViolations(literal).map { "$it in $literal" }
        }
        assertEquals(emptyList(), offenders)
    }

    @Test
    fun detectorAcceptsThePortableSubset() {
        assertEquals(emptyList(), portableRegexViolations("(?<![A-Za-z0-9_])"))
        assertEquals(emptyList(), portableRegexViolations("(?<=[^ \\t+])"))
        assertEquals(emptyList(), portableRegexViolations("(?<=ab)"))
        assertEquals(emptyList(), portableRegexViolations("(?<!\\n)"))
        assertEquals(emptyList(), portableRegexViolations("[\\s\\S]+?"))
        assertEquals(emptyList(), portableRegexViolations("(?:a+|b)*"))
        assertEquals(emptyList(), portableRegexViolations("a*(?=b+)"))
        assertEquals(emptyList(), portableRegexViolations("(?!a*)"))
        assertEquals(emptyList(), portableRegexViolations("https?://[^ \\t]+"))
    }

    @Test
    fun detectorRejectsWhatTheEnginesDoNotShare() {
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<!a*)"))
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<=ab+)"))
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<!a?)"))
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<=a{1,3})"))
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<=a{2})"))
        assertEquals(listOf("quantifier inside lookbehind"), portableRegexViolations("(?<!a{1,})"))
        assertEquals(listOf("alternation inside lookbehind"), portableRegexViolations("(?<=a|bb)"))
        assertEquals(listOf("group inside lookbehind"), portableRegexViolations("(?<=(?:ab))"))
        assertEquals(
            listOf("possessive quantifier", "quantifier inside lookbehind"),
            portableRegexViolations("(?<!a*+)"),
        )
        assertEquals(listOf("possessive quantifier"), portableRegexViolations("a*+"))
        assertEquals(listOf("possessive quantifier"), portableRegexViolations("a++"))
        assertEquals(listOf("possessive quantifier"), portableRegexViolations("a{1,2}+"))
        assertEquals(listOf("atomic group"), portableRegexViolations("(?>a)"))
        assertEquals(listOf("inline flag"), portableRegexViolations("(?i)a"))
        assertEquals(listOf("inline flag"), portableRegexViolations("(?i:a)"))
        assertEquals(listOf("named group"), portableRegexViolations("(?<name>a)"))
        assertEquals(listOf("shorthand \\d"), portableRegexViolations("\\d+"))
        assertEquals(listOf("shorthand \\s"), portableRegexViolations("\\s"))
        assertEquals(listOf("shorthand \\w"), portableRegexViolations("\\w"))
        assertEquals(listOf("shorthand \\b"), portableRegexViolations("\\b"))
        assertEquals(listOf("shorthand \\s"), portableRegexViolations("(?<=[^\\s+])"))
        assertEquals(listOf("unicode property"), portableRegexViolations("\\p{L}"))
        assertEquals(
            listOf("\\\\Q\\\\E quoting", "\\\\Q\\\\E quoting"),
            portableRegexViolations("\\Qa.\\E"),
        )
        assertEquals(listOf("backreference"), portableRegexViolations("(a)\\1"))
        assertEquals(listOf("non-portable escape \\v"), portableRegexViolations("\\v"))
        assertEquals(listOf("non-portable escape \\h"), portableRegexViolations("\\h"))
        assertEquals(listOf("character-class intersection"), portableRegexViolations("[a&&b]"))
        assertEquals(listOf("conditional group"), portableRegexViolations("(?(1)a|b)"))
    }

    private fun panguSource(): String {
        var dir: File? = File(System.getProperty("user.dir"))
        repeat(6) {
            val current = dir ?: return@repeat
            val candidate = File(current, "src/commonMain/kotlin/dev/darkokoa/pangu/Pangu.kt")
            if (candidate.isFile) return candidate.readText()
            dir = current.parentFile
        }
        fail("Pangu.kt not found from ${System.getProperty("user.dir")}")
    }
}

internal fun portableRegexViolations(pattern: String): List<String> {
    val found = ArrayList<String>()
    val groups = ArrayDeque<Boolean>()
    fun inLookbehind(): Boolean = groups.any { it }
    var index = 0
    while (index < pattern.length) {
        val char = pattern[index]
        when {
            char == '\\' -> {
                val escape = readEscape(pattern, index)
                escape.violation?.let { found += it }
                index = escape.next
            }
            char == '[' -> {
                val inspected = inspectClass(pattern, index)
                found += inspected.violations
                index = inspected.next
            }
            char == '(' -> index = openGroup(pattern, index, groups, found)
            char == ')' -> {
                if (groups.isNotEmpty()) groups.removeLast()
                index += 1
            }
            char == '|' && inLookbehind() -> {
                found += "alternation inside lookbehind"
                index += 1
            }
            char == '*' || char == '+' || char == '?' || char == '{' -> {
                val quantifier = readQuantifier(pattern, index)
                if (quantifier == null) {
                    index += 1
                } else {
                    if (quantifier.possessive) found += "possessive quantifier"
                    if (inLookbehind()) found += "quantifier inside lookbehind"
                    index = quantifier.next
                }
            }
            else -> index += 1
        }
    }
    return found
}

internal fun regexCallLiterals(source: String): List<String> {
    val literals = ArrayList<String>()
    var index = 0
    while (index < source.length) {
        if (source.startsWith("//", index)) {
            val lineEnd = source.indexOf('\n', index)
            index = if (lineEnd < 0) source.length else lineEnd + 1
            continue
        }
        if (source.startsWith("/*", index)) {
            val commentEnd = source.indexOf("*/", index + 2)
            index = if (commentEnd < 0) source.length else commentEnd + 2
            continue
        }
        if (source[index] == '"') {
            index = skipKotlinString(source, index)
            continue
        }
        val isRegexCall = source.startsWith("Regex", index) &&
            !isIdentifierPart(source, index - 1) &&
            !isIdentifierPart(source, index + "Regex".length)
        if (!isRegexCall) {
            index += 1
            continue
        }
        var cursor = index + "Regex".length
        while (cursor < source.length && source[cursor].isWhitespace()) cursor += 1
        if (cursor >= source.length || source[cursor] != '(') {
            index += 1
            continue
        }
        val call = readRegexCall(source, cursor)
        if (call.literals.isNotEmpty()) literals += call.literals.joinToString("")
        index = call.next
    }
    return literals
}

private class EscapeRead(val next: Int, val violation: String?)

private fun readEscape(pattern: String, slash: Int): EscapeRead {
    if (slash + 1 >= pattern.length) return EscapeRead(pattern.length, "trailing backslash")
    val marker = pattern[slash + 1]
    val violation = when (marker) {
        'd', 'D', 'w', 'W', 's', 'S', 'b', 'B' -> "shorthand \\$marker"
        'p', 'P' -> "unicode property"
        'Q', 'E' -> "\\\\Q\\\\E quoting"
        'h', 'H', 'v', 'V', 'R', 'G', 'A', 'Z', 'z', 'c' -> "non-portable escape \\$marker"
        in '1'..'9' -> "backreference"
        '0' -> if (pattern.getOrNull(slash + 2)?.isDigit() == true) "octal escape" else null
        'u' -> if (pattern.getOrNull(slash + 2) == '{') "\\\\u{} escape" else null
        'x' -> if (pattern.getOrNull(slash + 2) == '{') "\\\\x{} escape" else null
        else -> null
    }
    return EscapeRead(slash + escapeLength(pattern, slash), violation)
}

private fun escapeLength(pattern: String, slash: Int): Int {
    if (slash + 1 >= pattern.length) return 1
    return when (pattern[slash + 1]) {
        'u' -> when {
            pattern.getOrNull(slash + 2) == '{' -> lengthUntil(pattern, slash + 3, '}')
            else -> 6
        }
        'x' -> when {
            pattern.getOrNull(slash + 2) == '{' -> lengthUntil(pattern, slash + 3, '}')
            else -> 4
        }
        else -> 2
    }
}

private fun lengthUntil(pattern: String, start: Int, closer: Char): Int {
    val end = pattern.indexOf(closer, start)
    val absolute = if (end < 0) pattern.length else end + 1
    return absolute - (start - 3)
}

private class ClassRead(val next: Int, val violations: List<String>)

private fun inspectClass(pattern: String, open: Int): ClassRead {
    var index = open + 1
    if (index < pattern.length && pattern[index] == '^') index += 1
    val bodyStart = index
    while (index < pattern.length) {
        if (pattern[index] == '\\') {
            index += escapeLength(pattern, index)
            continue
        }
        if (pattern[index] == ']') break
        index += 1
    }
    val body = pattern.substring(bodyStart, index.coerceAtMost(pattern.length))
    val next = (index + 1).coerceAtMost(pattern.length)
    if (body == "\\s\\S" || body == "\\S\\s") return ClassRead(next, emptyList())
    val violations = ArrayList<String>()
    var cursor = 0
    while (cursor < body.length) {
        if (body[cursor] == '\\') {
            readEscape(body, cursor).violation?.let { violations += it }
            cursor += escapeLength(body, cursor)
            continue
        }
        if (body[cursor] == '&' && body.getOrNull(cursor + 1) == '&') {
            violations += "character-class intersection"
            cursor += 2
            continue
        }
        cursor += 1
    }
    return ClassRead(next, violations)
}

private class QuantifierRead(val next: Int, val possessive: Boolean)

private fun readQuantifier(pattern: String, start: Int): QuantifierRead? {
    val char = pattern[start]
    if (char == '*' || char == '+' || char == '?') {
        val possessive = pattern.getOrNull(start + 1) == '+'
        val reluctant = pattern.getOrNull(start + 1) == '?'
        val next = start + 1 + if (possessive || reluctant) 1 else 0
        return QuantifierRead(next, possessive)
    }
    if (char != '{') return null
    val end = pattern.indexOf('}', start + 1)
    if (end < 0) return null
    val spec = pattern.substring(start + 1, end)
    if (!spec.matches(QUANTIFIER_SPEC)) return null
    val possessive = pattern.getOrNull(end + 1) == '+'
    val reluctant = pattern.getOrNull(end + 1) == '?'
    val next = end + 1 + if (possessive || reluctant) 1 else 0
    return QuantifierRead(next, possessive)
}

private val QUANTIFIER_SPEC = Regex("\\d+(,\\d*)?")

private fun openGroup(
    pattern: String,
    start: Int,
    groups: ArrayDeque<Boolean>,
    found: MutableList<String>,
): Int {
    fun rejectNested() {
        if (groups.any { it }) found += "group inside lookbehind"
    }
    if (!pattern.startsWith("(?", start)) {
        rejectNested()
        groups.addLast(false)
        return start + 1
    }
    val marker = pattern.getOrNull(start + 2)
    when (marker) {
        ':' -> {
            rejectNested()
            groups.addLast(false)
            return start + 3
        }
        '=', '!' -> {
            rejectNested()
            groups.addLast(false)
            return start + 3
        }
        '<' -> {
            val kind = pattern.getOrNull(start + 3)
            if (kind == '=' || kind == '!') {
                if (groups.any { it }) found += "nested lookaround inside lookbehind"
                groups.addLast(true)
                return start + 4
            }
            found += "named group"
            rejectNested()
            groups.addLast(false)
            val nameEnd = pattern.indexOf('>', start + 3)
            return if (nameEnd < 0) pattern.length else nameEnd + 1
        }
        '>' -> {
            found += "atomic group"
            rejectNested()
            groups.addLast(false)
            return start + 3
        }
        '#' -> {
            found += "inline comment"
            val commentEnd = pattern.indexOf(')', start + 3)
            return if (commentEnd < 0) pattern.length else commentEnd + 1
        }
        '(' -> {
            found += "conditional group"
            rejectNested()
            groups.addLast(false)
            return start + 3
        }
        else -> {
            found += "inline flag"
            var cursor = start + 2
            while (cursor < pattern.length && pattern[cursor] in FLAG_CHARS) cursor += 1
            if (pattern.getOrNull(cursor) == ':') {
                rejectNested()
                groups.addLast(false)
                return cursor + 1
            }
            return cursor
        }
    }
}

private const val FLAG_CHARS = "imsuxU-"

private class RegexCall(val next: Int, val literals: List<String>)

private fun readRegexCall(source: String, openParen: Int): RegexCall {
    val literals = ArrayList<String>()
    var index = openParen + 1
    var depth = 1
    while (index < source.length && depth > 0) {
        if (source.startsWith("//", index)) {
            val lineEnd = source.indexOf('\n', index)
            index = if (lineEnd < 0) source.length else lineEnd + 1
            continue
        }
        if (source[index] == '"') {
            val literal = readKotlinString(source, index)
            if (depth == 1) literals += literal.value
            index = literal.next
            continue
        }
        if (source[index] == '(') depth += 1
        if (source[index] == ')') depth -= 1
        index += 1
    }
    return RegexCall(index, literals)
}

private class KotlinString(val next: Int, val value: String)

private fun readKotlinString(source: String, start: Int): KotlinString {
    val builder = StringBuilder()
    var index = start + 1
    while (index < source.length) {
        val char = source[index]
        if (char == '\\') {
            val escaped = source.getOrNull(index + 1)
            when (escaped) {
                'n' -> builder.append('\n')
                'r' -> builder.append('\r')
                't' -> builder.append('\t')
                '\\', '"', '\'' -> builder.append(escaped)
                'u' -> {
                    val hex = source.substring(index + 2, (index + 6).coerceAtMost(source.length))
                    builder.append(hex.toIntOrNull(16)?.toChar() ?: ' ')
                    index += 6
                    continue
                }
                else -> if (escaped != null) builder.append(escaped)
            }
            index += 2
            continue
        }
        if (char == '"') return KotlinString(index + 1, builder.toString())
        builder.append(char)
        index += 1
    }
    return KotlinString(source.length, builder.toString())
}

private fun skipKotlinString(source: String, start: Int): Int = readKotlinString(source, start).next

private fun isIdentifierPart(source: String, index: Int): Boolean {
    if (index < 0 || index >= source.length) return false
    val char = source[index]
    return char == '_' || char.isLetterOrDigit()
}
