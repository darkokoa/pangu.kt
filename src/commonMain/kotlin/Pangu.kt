package dev.ryz3n.pangu

private val CJK =
    Regex("([\u2E80-\u2EFF\u2F00-\u2FDF\u3040-\u309F\u30A0-\u30FA\u30FC-\u30FF\u3100-\u312F\u3200-\u32FF\u3400-\u4DBF\u4E00-\u9FFF\uF900-\uFAFF])")
private val CJK_ANS =
    Regex(CJK.pattern + "([A-Za-z\\u0370-\\u03FF0-9-~@\\\\\$%^&*+_=|/\\xA1-\\xFF\\u2150-\\u218F\\u2700\\u2014\\u27BF])")

private val ANS_CJK =
    Regex("([A-Za-z\\u0370-\\u03FF0-9-~\\\\\$%^&_*+=|/!;:,.?\\xA1-\\xFF\\u2150-\\u218F\\u2700\\u2014\\u27BF])" + CJK.pattern)

private val QUOTE = Regex("([`\"\\u05f4'])")

private val CJK_QUOTE = Regex(CJK.pattern + QUOTE.pattern)

private val QUOTE_CJK = Regex(QUOTE.pattern + CJK.pattern)

private val FIX_QUOTE = Regex(QUOTE.pattern + "(\\s*) (.+?)(\\s*)" + QUOTE.pattern)

private val CJK_BRACKET_CJK = Regex(CJK.pattern + "([({\\[]+(.*?)[)}\\]]+)" + CJK.pattern)

private val CJK_BRACKET = Regex(CJK.pattern + "([(){}\\[\\]<>])")

private val BRACKET_CJK = Regex("([(){}\\[\\]<>])" + CJK.pattern)

private val FIX_BRACKET = Regex("([({\\[)]+)(\\s*)(.+?)(\\s*)([)}\\]]+)")

private val CJK_HASH = Regex(CJK.pattern + "(#(\\S+))")

private val HASH_CJK = Regex("((\\S+)#)" + CJK.pattern)


public fun String.toSpacingText(): String {
    var text = StringBuilder(this).toString()

    text = text
        .let { CJK_QUOTE.replace(it, "$1 $2") }
        .let { QUOTE_CJK.replace(it, "$1 $2") }
        .let { FIX_QUOTE.replace(it, "$1$3$5") }

    val oldText = StringBuilder(text).toString()
    val newText = CJK_BRACKET_CJK.replace(text, "$1 $2 $4")
    text = newText

    if (oldText == newText) {
        text = text
            .let { CJK_BRACKET.replace(it, "$1 $2") }
            .let { BRACKET_CJK.replace(it, "$1 $2") }
    }

    text = text
        .let { FIX_BRACKET.replace(it, "$1$3$5") }
        .let { CJK_HASH.replace(it, "$1 $2") }
        .let { HASH_CJK.replace(it, "$1 $3") }
        .let { CJK_ANS.replace(it, "$1 $2") }
        .let { ANS_CJK.replace(it, "$1 $2") }

    return text
}
