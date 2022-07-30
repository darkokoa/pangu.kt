import dev.ryz3n.pangu.toSpacingText
import kotlin.test.Test
import kotlin.test.assertEquals

class PanguTest {

    private fun assertEqualsSpacingText(input: String, expected: String) {
        return assertEquals(expected, input.toSpacingText())
    }

    @Test
    fun testSpacingText() {
        assertEqualsSpacingText(
            "請問Jackie的鼻子有幾個？123個！",
            "請問 Jackie 的鼻子有幾個？123 個！"
        )
        assertEqualsSpacingText(
            "請問 Jackie 的鼻子有幾個？123 個！",
            "請問 Jackie 的鼻子有幾個？123 個！"
        )
    }

    @Test
    fun testTilde() {
        assertEqualsSpacingText(
            "前面~後面",
            "前面 ~ 後面"
        )
        assertEqualsSpacingText(
            "前面 ~ 後面",
            "前面 ~ 後面"
        )
    }

    @Test
    fun testBackQuote() {
        assertEqualsSpacingText(
            "前面`後面",
            "前面 ` 後面"
        )
    }

    @Test
    fun testExclamationMark() {
        assertEqualsSpacingText(
            "前面!後面",
            "前面! 後面"
        )
        assertEqualsSpacingText(
            "前面! 後面",
            "前面! 後面"
        )
        assertEqualsSpacingText(
            "前面 ! 後面",
            "前面 ! 後面"
        )
    }

    @Test
    fun testAt1() {
        assertEqualsSpacingText(
            "請@vinta吃大餐",
            "請 @vinta 吃大餐"
        )
    }

    @Test
    fun testAt2() {
        assertEqualsSpacingText(
            "請@陳上進 吃大便",
            "請 @陳上進 吃大便"
        )
    }

    @Test
    fun testHash1() {
        assertEqualsSpacingText(
            "前面#H2G2後面",
            "前面 #H2G2 後面"
        )
    }

    @Test
    fun testHash2() {
        assertEqualsSpacingText(
            "前面#銀河便車指南 後面",
            "前面 #銀河便車指南 後面"
        )
    }

    @Test
    fun testHash3() {
        assertEqualsSpacingText(
            "前面#銀河公車指南 #銀河大客車指南 後面",
            "前面 #銀河公車指南 #銀河大客車指南 後面"
        )
    }

    @Test
    fun testHash4() {
        assertEqualsSpacingText(
            "前面#銀河閃電霹靂車指南#後面",
            "前面 #銀河閃電霹靂車指南# 後面"
        )
    }

    @Test
    fun testDollar() {
        assertEqualsSpacingText(
            "前面\$後面",
            "前面 $ 後面"
        )
    }

    @Test
    fun testPercent() {
        assertEqualsSpacingText(
            "前面%後面",
            "前面 % 後面"
        )
    }

    @Test
    fun testCarat() {
        assertEqualsSpacingText(
            "前面^後面",
            "前面 ^ 後面"
        )
    }

    @Test
    fun testAmpersand() {
        assertEqualsSpacingText(
            "前面&後面",
            "前面 & 後面"
        )
    }

    @Test
    fun testAsterisk() {
        assertEqualsSpacingText(
            "前面*後面",
            "前面 * 後面"
        )
    }

    @Test
    fun testParenthesis() {
        assertEqualsSpacingText(
            "前面(後面",
            "前面 ( 後面"
        )
        assertEqualsSpacingText(
            "前面 ( 後面",
            "前面 ( 後面"
        )
        assertEqualsSpacingText(
            "前面)後面",
            "前面 ) 後面"
        )
        assertEqualsSpacingText(
            "前面 ) 後面",
            "前面 ) 後面"
        )
    }

    @Test
    fun testParenthesisPair() {
        assertEqualsSpacingText(
            "前面(中文123漢字)後面",
            "前面 (中文 123 漢字) 後面"
        )
        assertEqualsSpacingText(
            "前面(中文123)後面",
            "前面 (中文 123) 後面"
        )
        assertEqualsSpacingText(
            "前面(123漢字)後面",
            "前面 (123 漢字) 後面"
        )
        assertEqualsSpacingText(
            "前面(中文123漢字) tail",
            "前面 (中文 123 漢字) tail"
        )
        assertEqualsSpacingText(
            "head (中文123漢字)後面",
            "head (中文 123 漢字) 後面"
        )
        assertEqualsSpacingText(
            "head (中文123漢字) tail",
            "head (中文 123 漢字) tail"
        )
    }

    @Test
    fun testMinus() {
        assertEqualsSpacingText(
            "前面-後面",
            "前面 - 後面"
        )
    }

    @Test
    fun testUnderscore() {
        assertEqualsSpacingText(
            "前面_後面",
            "前面 _ 後面"
        )
        assertEqualsSpacingText(
            "前面 _ 後面",
            "前面 _ 後面"
        )
    }

    @Test
    fun testPlus() {
        assertEqualsSpacingText(
            "前面+後面",
            "前面 + 後面"
        )
    }

    @Test
    fun testEqual() {
        assertEqualsSpacingText(
            "前面=後面",
            "前面 = 後面"
        )
    }

    @Test
    fun testBrace() {
        assertEqualsSpacingText(
            "前面{後面",
            "前面 { 後面"
        )
    }

    @Test
    fun testBracket() {
        assertEqualsSpacingText(
            "前面[後面",
            "前面 [ 後面"
        )
    }

    @Test
    fun testPipe() {
        assertEqualsSpacingText(
            "前面|後面",
            "前面 | 後面"
        )
    }

    @Test
    fun testBackslash() {
        assertEqualsSpacingText(
            "前面\\後面",
            "前面 \\ 後面"
        )
    }

    @Test
    fun testColon() {
        assertEqualsSpacingText(
            "前面:後面",
            "前面: 後面"
        )
        assertEqualsSpacingText(
            "前面: 後面",
            "前面: 後面"
        )
        assertEqualsSpacingText(
            "前面 : 後面",
            "前面 : 後面"
        )
    }

    @Test
    fun testSemicolon() {
        assertEqualsSpacingText(
            "前面;後面",
            "前面; 後面"
        )
        assertEqualsSpacingText(
            "前面; 後面",
            "前面; 後面"
        )
        assertEqualsSpacingText(
            "前面 ; 後面",
            "前面 ; 後面"
        )
    }

    @Test
    fun testQuote() {
        assertEqualsSpacingText(
            "前面\"後面",
            "前面 \" 後面"
        )
        assertEqualsSpacingText(
            "前面\"中文123漢字\"後面",
            "前面 \"中文 123 漢字\" 後面"
        )
        assertEqualsSpacingText(
            "前面\"\"後面",
            "前面 \"\" 後面"
        )
        assertEqualsSpacingText(
            "前面\" \"後面",
            "前面 \" \" 後面"
        )
    }

    @Test
    fun testSingleQuote() {
        assertEqualsSpacingText(
            "前面'後面",
            "前面 ' 後面"
        )
        assertEqualsSpacingText(
            "前面'中文123漢字'後面",
            "前面 '中文 123 漢字' 後面"
        )
        assertEqualsSpacingText(
            "前面''後面",
            "前面 '' 後面"
        )
        assertEqualsSpacingText(
            "前面' '後面",
            "前面 ' ' 後面"
        )
    }

    @Test
    fun testLessThan() {
        assertEqualsSpacingText(
            "前面<後面",
            "前面 < 後面"
        )
    }

    @Test
    fun testComma() {
        assertEqualsSpacingText(
            "前面,後面",
            "前面, 後面"
        )
        assertEqualsSpacingText(
            "前面, 後面",
            "前面, 後面"
        )
        assertEqualsSpacingText(
            "前面, 後面",
            "前面, 後面"
        )
    }

    @Test
    fun testGreaterThan() {
        assertEqualsSpacingText(
            "前面>後面",
            "前面 > 後面"
        )
    }

    @Test
    fun testPeriod() {
        assertEqualsSpacingText(
            "前面.後面",
            "前面. 後面"
        )
        assertEqualsSpacingText(
            "前面. 後面",
            "前面. 後面"
        )
        assertEqualsSpacingText(
            "前面. 後面",
            "前面. 後面"
        )
    }

    @Test
    fun testQuestionMark() {
        assertEqualsSpacingText(
            "前面?後面",
            "前面? 後面"
        )
        assertEqualsSpacingText(
            "前面? 後面",
            "前面? 後面"
        )
        assertEqualsSpacingText(
            "前面? 後面",
            "前面? 後面"
        )
    }

    @Test
    fun testSlash() {
        assertEqualsSpacingText(
            "前面/後面",
            "前面 / 後面"
        )
    }
}