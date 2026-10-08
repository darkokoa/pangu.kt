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
    private val FIX_QUOTE_ANY_QUOTE = Regex("([`\"\u05f4]+) *(.+?) *([`\"\u05f4]+)")

    private val CJK_SINGLE_QUOTE_BUT_POSSESSIVE = Regex(CJK.pattern + "('[^s])")
    private val SINGLE_QUOTE_CJK = Regex("(')" + CJK.pattern)
    private val FIX_POSSESSIVE_SINGLE_QUOTE = Regex("([A-Za-z0-9$CJK_UNICODE])( )('s)")

    private val HASH_ANS_CJK_HASH = Regex(CJK.pattern + "(#)" + "([$CJK_UNICODE]+)" + "(#)" + CJK.pattern)
    private val CJK_HASH = Regex(CJK.pattern + "(#([^ ]))")
    private val HASH_CJK = Regex("(([^ ])#)" + CJK.pattern)

    // Slash is not an operator. It stays glued; file-path rules below insert spaces around a path as one unit.
    private val CJK_OPERATOR_ANS = Regex(CJK.pattern + "([+\\-*=&|<>])([A-Za-z0-9])")
    private val ANS_OPERATOR_CJK = Regex("([A-Za-z0-9])([+\\-*=&|<>])" + CJK.pattern)

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
    private val FIX_LEFT_BRACKET_ANY_RIGHT_BRACKET = Regex("([(\\[{<\u201c]+) *(.+?) *([)\\]}>\u201d]+)")
    private val ANS_CJK_LEFT_BRACKET_ANY_RIGHT_BRACKET =
        Regex("([A-Za-z0-9$CJK_UNICODE]) *(\u201c)([A-Za-z0-9$CJK_UNICODE\\-_ ]+)(\u201d)")
    private val LEFT_BRACKET_ANY_RIGHT_BRACKET_ANS_CJK =
        Regex("(\u201c)([A-Za-z0-9$CJK_UNICODE\\-_ ]+)(\u201d) *([A-Za-z0-9$CJK_UNICODE])")

    private val AN_LEFT_BRACKET = Regex("([A-Za-z0-9])([(\\[{])")
    private val RIGHT_BRACKET_AN = Regex("([)\\]}])([A-Za-z0-9])")

    // Slash and the half-width punctuation owned by the rules above are absent here, so those symbols are not spaced twice.
    private val CJK_ANS =
        Regex(CJK.pattern + "([A-Za-z\u0370-\u03ff0-9@$%^&*\\-+\\\\=|\u00a1-\u00ff\u2150-\u218f\u2700—\u27bf])")
    private val ANS_CJK =
        Regex("([A-Za-z\u0370-\u03ff0-9$%^&*\\-+\\\\=|\u00a1-\u00ff\u2150-\u218f\u2700—\u27bf])" + CJK.pattern)

    private val S_A = Regex("(%)([A-Za-z])")
    private val MIDDLE_DOT = Regex("( *)([\u00b7\u2022\u2027])( *)")

    // Scheme-anchored. CJK inside the URL stays part of the URL. Trailing prose punctuation is trimmed back out.
    private val HTTP_URL_LEAD =
        Regex("https?://[^\\s<>\"`\u3000-\u303F\uFF00-\uFFEF\u2018\u2019\u201C\u201D\u2026\uE000-\uF8FF]+")
    private val HTTP_URL_TRAILING_PUNCTUATION = Regex("[.,;:!?'\"]+$")
    private val HTTP_URL_PLACEHOLDER = Regex("\uE00A(\\d+)\uE00B")
    private val CJK_BEFORE_URL_PLACEHOLDER = Regex("([$CJK_UNICODE])\uE00A")


    public fun spacingText(text: String): String {
        if (text.length <= 1 || !ANY_CJK.containsMatchIn(text)) return text

        val (maskedText, urls) = maskHttpUrls(text)
        var newText = maskedText

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

            .replace(CJK_SINGLE_QUOTE_BUT_POSSESSIVE, "$1 $2")
            .replace(SINGLE_QUOTE_CJK, "$1 $2")
            .replace(FIX_POSSESSIVE_SINGLE_QUOTE, "$1's") // eslint-disable-line quotes

            .replace(HASH_ANS_CJK_HASH, "$1 $2$3$4 $5")
            .replace(CJK_HASH, "$1 $2")
            .replace(HASH_CJK, "$1 $3")

            .replace(CJK_OPERATOR_ANS, "$1 $2 $3")
            .replace(ANS_OPERATOR_CJK, "$1 $2 $3")

            .replace(CJK_UNIX_ABSOLUTE_FILE_PATH, "$1 $2")
            .replace(CJK_UNIX_RELATIVE_FILE_PATH, "$1 $2")
            .replace(UNIX_ABSOLUTE_FILE_PATH_SLASH_CJK, "$1 $2")
            .replace(UNIX_RELATIVE_FILE_PATH_SLASH_CJK, "$1 $2")

            .replace(CJK_LEFT_BRACKET, "$1 $2")
            .replace(RIGHT_BRACKET_CJK, "$1 $2")
            .replace(FIX_LEFT_BRACKET_ANY_RIGHT_BRACKET, "$1$2$3")
            .replace(ANS_CJK_LEFT_BRACKET_ANY_RIGHT_BRACKET, "$1 $2$3$4")
            .replace(LEFT_BRACKET_ANY_RIGHT_BRACKET_ANS_CJK, "$1$2$3 $4")

            .replace(AN_LEFT_BRACKET, "$1 $2")
            .replace(RIGHT_BRACKET_AN, "$1 $2")

            .replace(CJK_ANS, "$1 $2")
            .replace(ANS_CJK, "$1 $2")

            .replace(S_A, "$1 $2")

            .replace(MIDDLE_DOT, "・")
            .replace(CJK_BEFORE_URL_PLACEHOLDER, "$1 \uE00A")

        return restoreHttpUrls(newText, urls)
    }

    private fun maskHttpUrls(text: String): Pair<String, List<String>> {
        if (!text.contains("http://") && !text.contains("https://")) {
            return text to emptyList()
        }

        val urls = ArrayList<String>()
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
            out.append('\uE00A').append(urls.size).append('\uE00B')
            urls.add(url)
            if (url.length < raw.length) {
                out.append(raw, url.length, raw.length)
            }
            index = match.range.last + 1
        }
        out.append(text, index, text.length)
        return out.toString() to urls
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

    private fun restoreHttpUrls(text: String, urls: List<String>): String {
        if (urls.isEmpty()) return text
        return HTTP_URL_PLACEHOLDER.replace(text) { match ->
            urls[match.groupValues[1].toInt()]
        }
    }

    private fun Char.isAsciiAlphanumeric(): Boolean = this in 'A'..'Z' || this in 'a'..'z' || this in '0'..'9'
}

public fun String.spacingText(): String = Pangu.spacingText(this)
