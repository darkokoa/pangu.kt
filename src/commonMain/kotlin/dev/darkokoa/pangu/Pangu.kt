package dev.darkokoa.pangu

public object Pangu {

    /**
     *
     * CJK is an acronym for Chinese, Japanese, and Korean.
     * CJK includes the following Unicode blocks:
     * \u2E80-\u2EFF CJK Radicals Supplement
     * \u2F00-\u2FDF Kangxi Radicals
     * \u3040-\u309F Hiragana
     * \u30A0-\u30FF Katakana
     * \u3100-\u312F Bopomofo
     * \u3200-\u32FF Enclosed CJK Letters and Months
     * \u3400-\u4DBF CJK Unified Ideographs Extension A
     * \u4E00-\u9FFF CJK Unified Ideographs
     * \uF900-\uFAFF CJK Compatibility Ideographs
     *
     */
    private const val CJK_UNICODE =
        "\u2E80-\u2EFF\u2F00-\u2FDF\u3040-\u309F\u30A0-\u30FA\u30FC-\u30FF\u3100-\u312F\u3200-\u32FF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF"
    private const val AN = "A-Za-z0-9"
    private const val ASCII_WORD = "A-Za-z0-9_"

    // Java and Kotlin/Native `\s` are [ \t\n\u000B\f\r]. Android ICU `\s` is Unicode whitespace,
    // and Kotlin/JS (compiled with the u flag) also counts NBSP and ideographic space.
    private const val ASCII_WHITESPACE = " \\t\\n\\u000B\\f\\r"

    // Portable subset of Kotlin Regex. The same pattern must compile and match on JVM
    // java.util.regex (including Java 8), Android ICU, JS and wasmJs (Kotlin sets the u flag),
    // and Kotlin/Native.
    // Allowed: literals, escapes (\n \r \t \f \uXXXX \xHH and escaped metacharacters), character
    // classes, alternation, capturing groups, (?:) groups, greedy and reluctant quantifiers,
    // and lookahead. [\s\S] is the any-character class, because each engine defines \S as the
    // complement of \s. A lookbehind is a fixed-length run of literals and character classes:
    // no quantifier, group, or alternation. Java 8 and Android ICU reject a lookbehind with no
    // maximum length, and Kotlin/Native only steps a lookbehind by a fixed character count when
    // the body is that kind of run.
    // Not in the subset: \d \w \s \b and their complements, \p{} and \P{}, possessive quantifiers,
    // atomic groups, inline flags, named groups, backreferences, \Q\E, class intersection (&&),
    // and \h \v \R (Java's \v is a whitespace class; JS \v is only a vertical tab, and \h \R
    // fail to compile under the u flag). Placeholder marks are spelled \uXXXX, never a quoted run.
    private val ANY_CJK = Regex("[$CJK_UNICODE]")
    private val CJK = Regex("([$CJK_UNICODE])")

    // Half-width punctuation stays half-width. A run gets one trailing space only when CJK, a letter, or a digit follows.
    private val CJK_PUNCTUATION = Regex("([$CJK_UNICODE])([!;,?:]+)(?=[$CJK_UNICODE$AN])")
    // ! ; , ? before CJK are spaced even when the left side is not CJK. Colon is intentionally absent.
    private val PUNCTUATION_CJK = Regex("([!;,?]+)(?=[$CJK_UNICODE])")
    // ~= is a single token, so a tilde run must not swallow the equals sign.
    private val CJK_TILDE = Regex("([$CJK_UNICODE])(~+)(?!=)(?=[$CJK_UNICODE$AN])")
    private val CJK_TILDE_EQUALS = Regex("([$CJK_UNICODE])(~=)")
    // A dot before a letter, digit, another dot, or a slash is a file extension or version, not a sentence stop.
    private val CJK_PERIOD = Regex("([$CJK_UNICODE])(\\.)(?![$AN./])(?=[$CJK_UNICODE$AN])")
    private val AN_PERIOD_CJK = Regex("([$AN])(\\.)([$CJK_UNICODE])")
    private val AN_COLON_CJK = Regex("([$AN])(:)([$CJK_UNICODE])")
    private val DOTS_CJK = Regex("(\\.{2,}|\u2026)" + CJK.pattern)
    // The only full-width conversion left: a colon glued to a following parenthesis.
    private val FIX_CJK_COLON_ANS = Regex(CJK.pattern + ":([A-Z0-9()])")

    private val QUOTE = Regex("([`\"\u05f4])")
    private val CJK_QUOTE = Regex(CJK.pattern + QUOTE.pattern)
    private val QUOTE_CJK = Regex(QUOTE.pattern + CJK.pattern)
    // [\s\S] pairs a quote across a newline. A dot would miss the closer and strip the space between two quotes.
    private val FIX_QUOTE_ANY_QUOTE = Regex("([`\"\u05f4]+)[ ]*([\\s\\S]+?)[ ]*([`\"\u05f4]+)")
    private val QUOTE_AN = Regex("(\u201d)([$AN])")
    // CJK"AN closes a quoted CJK phrase, so the space goes after the quote.
    private val CJK_QUOTE_AN = Regex("([$CJK_UNICODE])(\")([$AN])")

    private val CJK_SINGLE_QUOTE_BUT_POSSESSIVE = Regex(CJK.pattern + "('[^s])")
    private val SINGLE_QUOTE_CJK = Regex("(')" + CJK.pattern)
    private val FIX_POSSESSIVE_SINGLE_QUOTE = Regex("([$AN$CJK_UNICODE])( )('s)")
    private val SINGLE_QUOTE_PURE_CJK = Regex("(')([$CJK_UNICODE]+)(')")
    private val BACKTICK_PAIR = Regex("`([^`]+)`")

    private val HASH_ANS_CJK_HASH = Regex(CJK.pattern + "(#)" + "([$CJK_UNICODE]+)" + "(#)" + CJK.pattern)
    // A # glued to the next character is a hashtag. NBSP is a gap, the same as a space.
    // /#tag in a slash list is a hashtag, not a C# shape, so / does not count as the left side.
    private val CJK_HASH = Regex(CJK.pattern + "(#([^ \\u00a0]))")
    private val HASH_CJK = Regex("(([^ \\u00a0/])#)" + CJK.pattern)

    // Opening, closing, and self-closing tags with an ASCII name. Stray < > and comments are not tags.
    // The self-closing slash counts only after whitespace, so <br/> stays ordinary text. <br> and <br /> do not.
    private val HTML_TAG = Regex("</?[A-Za-z][A-Za-z0-9]*(?:[$ASCII_WHITESPACE]+[^>]*)?>")
    private val CLOSING_HTML_TAG = Regex("</([A-Za-z][A-Za-z0-9]*)")
    private val BARE_HTML_TAG = Regex("^<([A-Za-z][A-Za-z0-9]*)[$ASCII_WHITESPACE]*/?>$")
    // Double-quoted attributes only. A hyphenated name such as data-id is left as written.
    private val HTML_ATTRIBUTE = Regex("([A-Za-z0-9_]+)=\"([^\"]*)\"")
    private val VOID_HTML_TAGS = setOf(
        "area", "base", "br", "col", "embed", "hr", "img", "input",
        "link", "meta", "param", "source", "track", "wbr",
    )

    // + and | are read per line. < and > are comparison operators. / is not an operator.
    // A bracket counts as the half-width side: CJK-( and ]-CJK are operators. *[ stays a glob.
    private const val OPERATORS = "*=&\\-"
    private val CJK_OPERATOR_ANS = Regex("([$CJK_UNICODE])(?!\\*\\[)([$OPERATORS])([$AN(\\[{])")
    private val ANS_OPERATOR_CJK = Regex("([$AN)\\]}])([$OPERATORS])([$CJK_UNICODE])")
    private val CJK_LESS_THAN = Regex("([$CJK_UNICODE])(<)([$AN])")
    private val LESS_THAN_CJK = Regex("([$AN])(<)([$CJK_UNICODE])")
    private val CJK_GREATER_THAN = Regex("([$CJK_UNICODE])(>)([$AN])")
    private val GREATER_THAN_CJK = Regex("([$AN])(>)([$CJK_UNICODE])")

    // ASCII word edges, not Unicode \b, so a CJK neighbor still counts as a boundary.
    private val SINGLE_LETTER_GRADE_CJK = Regex("(?<![$ASCII_WORD])([A-Za-z])([+\\-*])([$CJK_UNICODE])")
    private val CJK_SIGN_DIGIT = Regex("([$CJK_UNICODE])(\\+)([0-9])")
    private val CJK_HYPHEN_FLAG = Regex("([$CJK_UNICODE])(-)([a-z])(?![$ASCII_WORD])")
    private val DIGIT_PLUS_CJK = Regex("(?<![$ASCII_WORD])([0-9]+)(\\+)([$CJK_UNICODE])")

    private val PLUS_CJK_CONTACT = Regex("[$CJK_UNICODE]\\+|\\+[$CJK_UNICODE]")
    // Full-width punctuation stays glued to a plus even when another plus on the line is a separator.
    private const val PLUS_FULLWIDTH =
        "\\uFF0C\\u3002\\uFF1B\\uFF1A\\uFF01\\uFF1F\\u3001\\uFF08\\uFF09\\u300C\\u300D\\u300E\\u300F\\u3010\\u3011\\u300A\\u300B"
    private val PLUS_SEPARATOR =
        Regex("(?<=[^$ASCII_WHITESPACE+$PLUS_FULLWIDTH])\\+(?=[^$ASCII_WHITESPACE+$PLUS_FULLWIDTH])")
    private val RIGHT_BRACKET_PLUS_FULL_WIDTH_LEFT_BRACKET =
        Regex("(?<=[)\\]}])\\+(?=[\\uFF08\\u300C\\u300E\\u3010\\u300A])")

    private val HYPHEN_CJK_CONTACT = Regex("[$CJK_UNICODE]-|-[${CJK_UNICODE}]")
    private val HYPHEN_SEPARATOR = Regex("(?<=[)\\]}])-(?=[(\\[{])")

    private val PIPE_CJK_CONTACT = Regex("[$CJK_UNICODE]\\||\\|[$CJK_UNICODE]")
    private val PIPE_SEPARATOR =
        Regex("([^$ASCII_WHITESPACE|])[ ]*(\\|+)[ ]*(?=[^$ASCII_WHITESPACE|])")

    private const val FILE_PATH_DIRS =
        "home|root|usr|etc|var|opt|tmp|dev|mnt|proc|sys|bin|boot|lib|media|run|sbin|srv|node_modules|path|project|src|dist|test|tests|docs|templates|assets|public|static|config|scripts|tools|build|out|target|your|\\.claude|\\.git|\\.vscode"
    private const val FILE_PATH_CHARS = "[A-Za-z0-9_\\-.@+*]+"
    private const val UNIX_ABSOLUTE_FILE_PATH =
        "/(?:\\.?(?:$FILE_PATH_DIRS)|\\.(?:[A-Za-z0-9_\\-]+))(?:/$FILE_PATH_CHARS)*"
    private const val UNIX_RELATIVE_FILE_PATH =
        "(?:\\./)?(?:$FILE_PATH_DIRS)(?:/$FILE_PATH_CHARS)+"
    private val CJK_UNIX_ABSOLUTE_FILE_PATH = Regex("([$CJK_UNICODE])($UNIX_ABSOLUTE_FILE_PATH)")
    private val CJK_UNIX_RELATIVE_FILE_PATH = Regex("([$CJK_UNICODE])($UNIX_RELATIVE_FILE_PATH)")
    private val UNIX_ABSOLUTE_FILE_PATH_SLASH_CJK = Regex("($UNIX_ABSOLUTE_FILE_PATH/)([$CJK_UNICODE])")
    private val UNIX_RELATIVE_FILE_PATH_SLASH_CJK = Regex("($UNIX_RELATIVE_FILE_PATH/)([$CJK_UNICODE])")

    private val CJK_LEFT_BRACKET = Regex(CJK.pattern + "([(\\[{<>\u201c])")
    private val RIGHT_BRACKET_CJK = Regex("([)\\]}<>\u201d])" + CJK.pattern)
    // Both quotes typed as ”. Space the outside only when no unclosed “ precedes the opener on
    // this line. The opener's stretch must start at the beginning, a newline, or another ”, and
    // must contain no “. That boundary is one character, so a fixed-length lookbehind can say it.
    // An unbounded lookbehind cannot, and it would not compile on Java 8 or Android ICU.
    private val ANS_CJK_RIGHT_QUOTE_ANY_RIGHT_QUOTE = Regex(
        "(?<![^\\n\u201d])([^\\u201c\\u201d\\n]*)([$AN$CJK_UNICODE])[ ]*(\\u201d)[ ]*" +
            "([$AN$CJK_UNICODE\\-_ ]+?)[ ]*(\\u201d)"
    )
    private val ANS_CJK_LEFT_BRACKET_ANY_RIGHT_BRACKET =
        Regex("([$AN$CJK_UNICODE]) *(\u201c)([$AN$CJK_UNICODE\\-_ ]+)(\u201d)")
    private val LEFT_BRACKET_ANY_RIGHT_BRACKET_ANS_CJK =
        Regex("(\u201c)([$AN$CJK_UNICODE\\-_ ]+)(\u201d) *([$AN$CJK_UNICODE])")

    // foo( is spaced; object.method( stays tight. The dot is the one character before the name.
    private val AN_LEFT_BRACKET = Regex("(?<![$AN.])([$AN]+)([(\\[{])")
    private val RIGHT_BRACKET_AN = Regex("([)\\]}])([$AN])")

    private val ANGLE_BRACKET_PAIR = Regex("<([^<>]*)>")
    private val ROUND_BRACKET_PAIR = Regex("\\(([^()]*)\\)")
    private val SQUARE_BRACKET_PAIR = Regex("\\[([^\\[\\]]*)\\]")
    private val CURLY_BRACKET_PAIR = Regex("\\{([^{}]*)\\}")

    // Superscripts stay attached on the left. ® ⁰¹²³⁴⁵⁶⁷⁸⁹ ⁱⁿ⁺⁻⁼⁾ ℠ ™. ⁽ is excluded on purpose.
    private const val SUPERSCRIPT_SUFFIXES =
        "\u00ae\u00b2\u00b3\u00b9\u2070\u2071\u2074-\u207c\u207e\u207f\u2120\u2122"
    // Dingbats are the real range U+2700-U+27BF. An em dash (U+2014) must not sit in this class.
    private const val ANS_CJK_AFTER =
        "A-Za-z\u0370-\u03ff0-9@\$%^&*+=\\\\\u00a1-\u00ff\u2150-\u218f\u2700-\u27bf\u2100-\u214f-"
    private const val ANS_BEFORE_CJK =
        "A-Za-z\u0370-\u03ff0-9\$%^&*+=\\\\\u00a1-\u00ff\u2150-\u218f\u2700-\u27bf\u2100-\u214f" +
            SUPERSCRIPT_SUFFIXES + "-"
    private val CJK_ANS = Regex("([$CJK_UNICODE])(?![$SUPERSCRIPT_SUFFIXES])([$ANS_CJK_AFTER])")
    private val ANS_CJK = Regex("([$ANS_BEFORE_CJK])([$CJK_UNICODE])")

    private val S_A = Regex("(%)([A-Za-z])")
    private val COPYRIGHT_DIGIT = Regex("(\u00a9)([0-9])")
    // A lone tight interpunct becomes ・. A spaced one, or a mask run such as ••••, stays as written.
    private val MIDDLE_DOT =
        Regex("(?<![ \u00a0\u00b7\u2022\u2027])[\u00b7\u2022\u2027](?![ \u00a0\u00b7\u2022\u2027])")

    // Scheme-anchored. CJK inside the URL stays part of the URL. Trailing prose punctuation is trimmed back out.
    private val HTTP_URL_LEAD =
        Regex("https?://[^$ASCII_WHITESPACE<>\"`\u3000-\u303F\uFF00-\uFFEF\u2018\u2019\u201C\u201D\u2026\uE000-\uF8FF]+")
    private val HTTP_URL_TRAILING_PUNCTUATION = Regex("[.,;:!?'\"]+$")


    public fun spacingText(text: String): String {
        if (text.length <= 1 || !ANY_CJK.containsMatchIn(text)) return text

        val backtickManager = PlaceholderReplacer(text, "\uE000", "\uE001")
        var newText = BACKTICK_PAIR.replace(text) { match ->
            "`" + backtickManager.store(match.groupValues[1]) + "`"
        }

        val urlManager = PlaceholderReplacer(newText, "\uE00A", "\uE00B")
        newText = maskHttpUrls(newText, urlManager)

        // Hide tags before any bracket rule. A bare non-void tag with no closer is a mention
        // (<div>, <String>, <Spinner />) and is spaced from CJK later. A void tag (<br>, <img>)
        // and any tag that has a closer stay markup and come back tight against the text.
        val mentionManager = if ('<' in newText) PlaceholderReplacer(newText, "\uE004", "\uE005") else null
        val htmlTagManager = if (mentionManager != null) PlaceholderReplacer(newText, "\uE002", "\uE003") else null
        if (mentionManager != null && htmlTagManager != null) {
            newText = maskHtmlTags(newText, mentionManager, htmlTagManager)
        }

        // Convert before the ANS rules, which would otherwise space a tight interpunct.
        newText = newText.replace(MIDDLE_DOT, "・")

        newText = newText
            .replace(DOTS_CJK, "$1 $2")
            .replace(CJK_PUNCTUATION, "$1$2 ")
            .replace(PUNCTUATION_CJK, "$1 ")
            .replace(CJK_TILDE, "$1$2 ")
            .replace(CJK_TILDE_EQUALS, "$1 $2 ")
            .replace(CJK_PERIOD, "$1$2 ")
            .replace(AN_PERIOD_CJK, "$1$2 $3")
            .replace(AN_COLON_CJK, "$1$2 $3")
            .replace(FIX_CJK_COLON_ANS, "$1：$2")

            .replace(CJK_QUOTE, "$1 $2")
            .replace(QUOTE_CJK, "$1 $2")
            .replace(FIX_QUOTE_ANY_QUOTE, "$1$2$3")
            .replace(QUOTE_AN, "$1 $2")
            .replace(CJK_QUOTE_AN, "$1$2 $3")

            .replace(FIX_POSSESSIVE_SINGLE_QUOTE, "$1's")

        val singleQuoteCjkManager = PlaceholderReplacer(newText, "\uE006", "\uE007")
        newText = SINGLE_QUOTE_PURE_CJK.replace(newText) { match ->
            singleQuoteCjkManager.store(match.value)
        }
        newText = newText
            .replace(CJK_SINGLE_QUOTE_BUT_POSSESSIVE, "$1 $2")
            .replace(SINGLE_QUOTE_CJK, "$1 $2")
        newText = singleQuoteCjkManager.restore(newText)

        newText = newText
            .replace(HASH_ANS_CJK_HASH, "$1 $2$3$4 $5")
            .replace(CJK_HASH, "$1 $2")
            .replace(HASH_CJK, "$1 $3")

            .replace(SINGLE_LETTER_GRADE_CJK, "$1$2 $3")
            .replace(CJK_SIGN_DIGIT, "$1 $2$3")
            .replace(CJK_HYPHEN_FLAG, "$1 $2$3")
            .replace(DIGIT_PLUS_CJK, "$1$2 $3")

        newText = spacePluses(newText)
        newText = spaceHyphens(newText)

        newText = newText
            .replace(CJK_OPERATOR_ANS, "$1 $2 $3")
            .replace(ANS_OPERATOR_CJK, "$1 $2 $3")
            .replace(CJK_LESS_THAN, "$1 $2 $3")
            .replace(LESS_THAN_CJK, "$1 $2 $3")
            .replace(CJK_GREATER_THAN, "$1 $2 $3")
            .replace(GREATER_THAN_CJK, "$1 $2 $3")

            .replace(CJK_UNIX_ABSOLUTE_FILE_PATH, "$1 $2")
            .replace(CJK_UNIX_RELATIVE_FILE_PATH, "$1 $2")
            .replace(UNIX_ABSOLUTE_FILE_PATH_SLASH_CJK, "$1 $2")
            .replace(UNIX_RELATIVE_FILE_PATH_SLASH_CJK, "$1 $2")

        newText = spacePipes(newText)
        // A separator space can land just inside a closing quote. Strip it in this same pass.
        newText = newText.replace(FIX_QUOTE_ANY_QUOTE, "$1$2$3")

        newText = newText
            .replace(CJK_LEFT_BRACKET, "$1 $2")
            .replace(RIGHT_BRACKET_CJK, "$1 $2")
            .replace(ANS_CJK_LEFT_BRACKET_ANY_RIGHT_BRACKET, "$1 $2$3$4")
            .replace(LEFT_BRACKET_ANY_RIGHT_BRACKET_ANS_CJK, "$1$2$3 $4")
            .replace(ANS_CJK_RIGHT_QUOTE_ANY_RIGHT_QUOTE, "$1$2 $3$4$5")
            .replace(AN_LEFT_BRACKET, "$1 $2")
            .replace(RIGHT_BRACKET_AN, "$1 $2")

            .replace(CJK_ANS, "$1 $2")
            .replace(ANS_CJK, "$1 $2")

            .replace(S_A, "$1 $2")
            .replace(COPYRIGHT_DIGIT, "$1 $2")

        newText = fixBracketSpacing(newText)

        if (mentionManager != null && mentionManager.hasItems) {
            // Match the whole placeholder. A one-character check on the end mark would also
            // space a private-use run that was already in the input.
            val cjkBeforeMention = Regex("([$CJK_UNICODE])" + privateUseLiteral(mentionManager.prefix))
            newText = newText.replace(cjkBeforeMention) { match ->
                match.groupValues[1] + " " + mentionManager.prefix
            }
            val mentionBeforeCjk = Regex(
                privateUseLiteral(mentionManager.prefix) + "[0-9]+" +
                    privateUseLiteral(mentionManager.suffix) + "([$CJK_UNICODE])"
            )
            newText = newText.replace(mentionBeforeCjk) { match ->
                match.value.dropLast(1) + " " + match.groupValues[1]
            }
        }
        if (mentionManager != null) newText = mentionManager.restore(newText)
        if (htmlTagManager != null) newText = htmlTagManager.restore(newText)

        if (urlManager.hasItems) {
            val cjkBeforeUrl = Regex("([$CJK_UNICODE])" + privateUseLiteral(urlManager.prefix))
            newText = newText.replace(cjkBeforeUrl) { match ->
                match.groupValues[1] + " " + urlManager.prefix
            }
        }
        newText = urlManager.restore(newText)
        return backtickManager.restore(newText)
    }

    /**
     * Returns true when [spacingText] would leave [text] unchanged.
     */
    public fun hasProperSpacing(text: String): Boolean = spacingText(text) == text

    private fun spacePluses(text: String): String {
        return mapLines(text) { line ->
            val spaced = if (PLUS_CJK_CONTACT.containsMatchIn(line)) {
                line.replace(PLUS_SEPARATOR, " + ")
            } else {
                line
            }
            spaced.replace(RIGHT_BRACKET_PLUS_FULL_WIDTH_LEFT_BRACKET, " +")
        }
    }

    private fun spaceHyphens(text: String): String {
        return mapLines(text) { line ->
            if (HYPHEN_CJK_CONTACT.containsMatchIn(line)) {
                line.replace(HYPHEN_SEPARATOR, " - ")
            } else {
                line
            }
        }
    }

    private fun spacePipes(text: String): String {
        return mapLines(text) { line ->
            if (PIPE_CJK_CONTACT.containsMatchIn(line)) {
                line.replace(PIPE_SEPARATOR, "$1 $2 ")
            } else {
                line
            }
        }
    }

    private fun mapLines(text: String, transform: (String) -> String): String {
        if (!text.contains('\n')) return transform(text)
        return text.split('\n').joinToString("\n", transform = transform)
    }

    private fun fixBracketSpacing(text: String): String {
        var current = text
        val pairs = arrayOf(
            ANGLE_BRACKET_PAIR to ('<' to '>'),
            ROUND_BRACKET_PAIR to ('(' to ')'),
            SQUARE_BRACKET_PAIR to ('[' to ']'),
            CURLY_BRACKET_PAIR to ('{' to '}'),
        )
        for ((pattern, brackets) in pairs) {
            val (open, close) = brackets
            current = pattern.replace(current) { match ->
                "$open${match.groupValues[1].trim(' ')}$close"
            }
        }
        return current
    }

    private fun maskHtmlTags(
        text: String,
        mentions: PlaceholderReplacer,
        tags: PlaceholderReplacer,
    ): String {
        val closedTagNames = HashSet<String>()
        for (match in CLOSING_HTML_TAG.findAll(text)) {
            closedTagNames.add(match.groupValues[1].lowercase())
        }
        return HTML_TAG.replace(text) { match ->
            val whole = match.value
            val bare = BARE_HTML_TAG.matchEntire(whole)
            if (bare != null) {
                val tagName = bare.groupValues[1].lowercase()
                if (tagName !in VOID_HTML_TAGS && tagName !in closedTagNames) {
                    return@replace mentions.store(whole)
                }
            }
            val processed = HTML_ATTRIBUTE.replace(whole) { attr ->
                attr.groupValues[1] + "=\"" + spacingText(attr.groupValues[2]) + "\""
            }
            tags.store(processed)
        }
    }

    private fun maskHttpUrls(text: String, urls: PlaceholderReplacer): String {
        if (!text.contains("http://") && !text.contains("https://")) {
            return text
        }

        val out = StringBuilder()
        var index = 0
        while (index < text.length) {
            val match = HTTP_URL_LEAD.find(text, index) ?: break
            val start = match.range.first
            if (start > 0 && text[start - 1].isAsciiAlphanumeric()) {
                out.append(text, index, start + 1)
                index = start + 1
                continue
            }

            out.append(text, index, start)
            val raw = match.value
            val url = trimHttpUrl(raw)
            out.append(urls.store(url))
            if (url.length < raw.length) {
                out.append(raw, url.length, raw.length)
            }
            index = match.range.last + 1
        }
        out.append(text, index, text.length)
        return out.toString()
    }

    private fun trimHttpUrl(url: String): String {
        var unbalancedClosingParentheses = url.count { it == ')' } - url.count { it == '(' }
        var current = url
        while (true) {
            val trimmed = HTTP_URL_TRAILING_PUNCTUATION.replace(current, "")
            if (trimmed.endsWith(')') && unbalancedClosingParentheses > 0) {
                unbalancedClosingParentheses -= 1
                current = trimmed.dropLast(1)
                continue
            }
            if (trimmed == current) {
                return current
            }
            current = trimmed
        }
    }

    private fun Char.isAsciiAlphanumeric(): Boolean = this in 'A'..'Z' || this in 'a'..'z' || this in '0'..'9'

    // \uXXXX for each private-use mark. Those marks are not metacharacters, and a quoted
    // \Q...\E run would leave the subset above.
    private fun privateUseLiteral(text: String): String = buildString(text.length * 6) {
        for (char in text) {
            append("\\u")
            append(char.code.toString(16).uppercase().padStart(4, '0'))
        }
    }

    /**
     * Hides a span behind a private-use placeholder that is not already present in [source].
     * Restore ignores an index this replacer did not store, so a pre-existing lookalike is left as written.
     */
    private class PlaceholderReplacer(
        source: String,
        startDelimiter: String,
        endDelimiter: String,
    ) {
        private val items = ArrayList<String>()
        val prefix: String
        val suffix: String

        val hasItems: Boolean get() = items.isNotEmpty()

        init {
            var guard = ""
            var chosenPrefix = startDelimiter
            while (guard.length <= 32) {
                chosenPrefix = startDelimiter + guard
                val probe = Regex(privateUseLiteral(chosenPrefix) + "[0-9]+" + privateUseLiteral(endDelimiter))
                if (!probe.containsMatchIn(source)) break
                guard += "\uE00C"
            }
            prefix = chosenPrefix
            suffix = endDelimiter
        }

        fun store(item: String): String {
            val placeholder = prefix + items.size + suffix
            items.add(item)
            return placeholder
        }

        fun restore(text: String): String {
            if (items.isEmpty()) return text
            val pattern = Regex(privateUseLiteral(prefix) + "([0-9]+)" + privateUseLiteral(suffix))
            return pattern.replace(text) { match ->
                val index = match.groupValues[1].toIntOrNull()
                if (index != null && index in items.indices) items[index] else match.value
            }
        }
    }
}

public fun String.spacingText(): String = Pangu.spacingText(this)

public fun String.hasProperSpacing(): Boolean = Pangu.hasProperSpacing(this)
