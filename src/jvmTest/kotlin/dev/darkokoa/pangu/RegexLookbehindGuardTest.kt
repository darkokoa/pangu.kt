package dev.darkokoa.pangu

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * Android java.util.regex is ICU and rejects a lookbehind with no maximum length.
 * Java 8 does too. CI runs a newer JVM, so this test is what keeps those patterns out.
 */
class RegexLookbehindGuardTest {

    @Test
    fun compiledPatternsHaveNoUnboundedLookbehind() {
        // Touch the object so every field initializer has run.
        Pangu.spacingText("中")
        val patterns = Pangu::class.java.declaredFields.mapNotNull { field ->
            if (!Regex::class.java.isAssignableFrom(field.type)) return@mapNotNull null
            field.isAccessible = true
            val regex = field.get(Pangu) as Regex
            field.name to regex.pattern
        }
        assertTrue(patterns.isNotEmpty(), "expected Regex fields on Pangu")
        val offenders = patterns.flatMap { (name, pattern) ->
            unboundedLookbehinds(pattern).map { "$name: $it" }
        }
        assertEquals(emptyList(), offenders)
    }

    @Test
    fun sourceHasNoUnboundedLookbehind() {
        val source = panguSource()
        val offenders = unboundedLookbehinds(source)
        assertEquals(emptyList(), offenders, "Pangu.kt lookbehind")
    }

    @Test
    fun detectorFlagsUnboundedQuantifiersAndIgnoresCharacterClasses() {
        assertEquals(listOf("(?<!a*)"), unboundedLookbehinds("(?<!a*)"))
        assertEquals(listOf("(?<=ab+)"), unboundedLookbehinds("(?<=ab+)"))
        assertEquals(listOf("(?<!a{1,})"), unboundedLookbehinds("(?<!a{1,})"))
        assertEquals(emptyList(), unboundedLookbehinds("(?<=[^\\s+])"))
        assertEquals(emptyList(), unboundedLookbehinds("(?<![ab])"))
        assertEquals(emptyList(), unboundedLookbehinds("(?<=a{1,3})"))
        assertEquals(emptyList(), unboundedLookbehinds("(?<!\\*)"))
        // A star outside a lookbehind is fine.
        assertEquals(emptyList(), unboundedLookbehinds("a*+(?=b+)"))
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

internal fun unboundedLookbehinds(pattern: String): List<String> {
    val found = ArrayList<String>()
    var index = 0
    while (index < pattern.length) {
        val isLookbehind = pattern.startsWith("(?<=", index) || pattern.startsWith("(?<!", index)
        if (!isLookbehind) {
            index += 1
            continue
        }
        val start = index
        index += 4
        val bodyStart = index
        var depth = 1
        var inClass = false
        while (index < pattern.length && depth > 0) {
            if (pattern[index] == '\\') {
                index += 2
                continue
            }
            if (inClass) {
                if (pattern[index] == ']') inClass = false
                index += 1
                continue
            }
            when (pattern[index]) {
                '[' -> {
                    inClass = true
                    index += 1
                }
                '(' -> {
                    depth += 1
                    index += 1
                }
                ')' -> {
                    depth -= 1
                    index += 1
                }
                else -> index += 1
            }
        }
        val bodyEnd = (index - 1).coerceAtLeast(bodyStart)
        if (lookbehindBodyIsUnbounded(pattern.substring(bodyStart, bodyEnd))) {
            found += pattern.substring(start, index.coerceAtMost(pattern.length))
        }
    }
    return found
}

private fun lookbehindBodyIsUnbounded(body: String): Boolean {
    var index = 0
    var inClass = false
    while (index < body.length) {
        if (body[index] == '\\') {
            index += 2
            continue
        }
        if (inClass) {
            if (body[index] == ']') inClass = false
            index += 1
            continue
        }
        when (body[index]) {
            '[' -> {
                inClass = true
                index += 1
            }
            '*', '+' -> return true
            '{' -> {
                val end = body.indexOf('}', index)
                if (end < 0) return true
                val spec = body.substring(index + 1, end)
                if (',' in spec && spec.substringAfter(',').isEmpty()) return true
                index = end + 1
            }
            else -> index += 1
        }
    }
    return false
}
