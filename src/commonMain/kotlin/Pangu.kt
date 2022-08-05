package dev.ryz3n.pangu

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

private val ANY_CJK = Regex("[$CJK_UNICODE]")

private val CJK = Regex("([$CJK_UNICODE])")


private const val DOT = "\\."
private val CONVERT_TO_FULL_WIDTH_CJK_SYMBOLS_CJK = Regex(CJK.pattern + " *(:+|$DOT) *" + CJK.pattern)
private val CONVERT_TO_FULL_WIDTH_CJK_SYMBOLS = Regex(CJK.pattern + " *([~\\\\!;,?]+) *")
private val DOTS_CJK = Regex("(\\.{2,}|\u2026)" + CJK.pattern)
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


private val CJK_OPERATOR_ANS = Regex(CJK.pattern + "([+\\-*/=&|<>])([A-Za-z0-9])")
private val ANS_OPERATOR_CJK = Regex("([A-Za-z0-9])([+\\-*/=&|<>])" + CJK.pattern)


private val FIX_SLASH_AS = Regex("(/) ([a-z\\-_./]+)")
private val FIX_SLASH_AS_SLASH = Regex("([/.])([A-Za-z\\-_./]+) (/)")


private val CJK_LEFT_BRACKET = Regex(CJK.pattern + "([(\\[{<>\u201c])")
private val RIGHT_BRACKET_CJK = Regex("([)\\]}<>\u201d])" + CJK.pattern)
private val FIX_LEFT_BRACKET_ANY_RIGHT_BRACKET = Regex("([(\\[{<\u201c]+) *(.+?) *([)\\]}>\u201d]+)")
private val ANS_CJK_LEFT_BRACKET_ANY_RIGHT_BRACKET =
    Regex("([A-Za-z0-9$CJK_UNICODE]) *(\u201c)([A-Za-z0-9$CJK_UNICODE\\-_ ]+)(\u201d)")
private val LEFT_BRACKET_ANY_RIGHT_BRACKET_ANS_CJK =
    Regex("(\u201c)([A-Za-z0-9$CJK_UNICODE\\-_ ]+)(\u201d) *([A-Za-z0-9$CJK_UNICODE])")


private val AN_LEFT_BRACKET = Regex("([A-Za-z0-9])([(\\[{])")
private val RIGHT_BRACKET_AN = Regex("([)\\]}])([A-Za-z0-9])")


private val CJK_ANS =
    Regex(CJK.pattern + "([A-Za-z\u0370-\u03ff0-9@$%^&*\\-+\\\\=|/\u00a1-\u00ff\u2150-\u218f\u2700—\u27bf])")
private val ANS_CJK =
    Regex("([A-Za-z\u0370-\u03ff0-9~$%^&*\\-+\\\\=|/!;:,.?\u00a1-\u00ff\u2150-\u218f\u2700—\u27bf])" + CJK.pattern)


private val S_A = Regex("(%)([A-Za-z])")
private val MIDDLE_DOT = Regex("( *)([\u00b7\u2022\u2027])( *)")


@Suppress("ObjectPropertyName")
private val _convertToFullWidthKVs = mapOf(
    Regex("~") to "～",
    Regex("!") to "！",
    Regex(";") to "；",
    Regex(":") to "：",
    Regex(",") to "，",
    Regex(DOT) to "。",
    Regex("\\?") to "？",
)

private fun String.convertSomeSignsToFullWidth(): String {
    var convertText = this
    _convertToFullWidthKVs.forEach { (regex, text) ->
        convertText = convertText.replace(regex, text)
    }

    return convertText
}

public fun String.toSpacingText(): String {

    if (this.length <= 1 || !ANY_CJK.containsMatchIn(this)) return this

    var newText = StringBuilder(this).toString()

    newText = newText.replace(CONVERT_TO_FULL_WIDTH_CJK_SYMBOLS_CJK) {
//        val match = it.groupValues[0]
        val leftCjk = it.groupValues[1]
        val symbols = it.groupValues[2]
        val rightCjk = it.groupValues[3]

        val fullWidthSymbols = symbols.convertSomeSignsToFullWidth()

        buildString {
            append(leftCjk)
            append(fullWidthSymbols)
            append(rightCjk)
        }
    }

    newText = newText.replace(CONVERT_TO_FULL_WIDTH_CJK_SYMBOLS) {
//        val match = it.groupValues[0]
        val cjk = it.groupValues[1]
        val symbols = it.groupValues[2]

        val fullWidthSymbols = symbols.convertSomeSignsToFullWidth()

        buildString {
            append(cjk)
            append(fullWidthSymbols)
        }
    }

    newText = newText
        .replace(DOTS_CJK, "$1 $2")
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

        .replace(FIX_SLASH_AS, "$1$2")
        .replace(FIX_SLASH_AS_SLASH, "$1$2$3")

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

    return newText
}
