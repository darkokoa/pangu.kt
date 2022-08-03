@file:Suppress("TestFunctionName", "NonAsciiCharacters")

import kotlin.test.Test

// tests from https://github.com/vinta/pangu.js/blob/master/test/shared/test_core.js
class PanguJsTest {

    @Test
    fun ignoreLowLine() { // \u005F
        assertEqualsSpacingText("前面_後面", "前面_後面")
        assertEqualsSpacingText("前面 _ 後面", "前面 _ 後面")
        assertEqualsSpacingText("Vinta_Molli", "Vinta_Molli")
        assertEqualsSpacingText("Vinta _ Mollie", "Vinta _ Mollie")
    }

    @Test
    fun handleAlphabets() {
        assertEqualsSpacingText("中文abc", "中文 abc")
        assertEqualsSpacingText("abc中文", "abc 中文")
    }

    @Test
    fun handleNumbers() {
        assertEqualsSpacingText("中文123", "中文 123")
        assertEqualsSpacingText("123中文", "123 中文")
    }

    @Test
    fun handleLatin1Supplement() {
        assertEqualsSpacingText("中文Ø漢字", "中文 Ø 漢字")
        assertEqualsSpacingText("中文 Ø 漢字", "中文 Ø 漢字")
    }

    @Test
    fun handleGreekAndCoptic() {
        assertEqualsSpacingText("中文β漢字", "中文 β 漢字")
        assertEqualsSpacingText("中文 β 漢字", "中文 β 漢字")
        assertEqualsSpacingText("我是α，我是Ω", "我是 α，我是 Ω")
    }

    @Test
    fun handleNumberForms() {
        assertEqualsSpacingText("中文Ⅶ漢字", "中文 Ⅶ 漢字")
        assertEqualsSpacingText("中文 Ⅶ 漢字", "中文 Ⅶ 漢字")
    }

    @Test
    fun handleCjkRadicalsSupplement() {
        assertEqualsSpacingText("abc⻤123", "abc ⻤ 123")
        assertEqualsSpacingText("abc ⻤ 123", "abc ⻤ 123")
    }

    @Test
    fun handleKangxiRadicals() {
        assertEqualsSpacingText("abc⾗123", "abc ⾗ 123")
        assertEqualsSpacingText("abc ⾗ 123", "abc ⾗ 123")
    }

    @Test
    fun handleHiragana() {
        assertEqualsSpacingText("abcあ123", "abc あ 123")
        assertEqualsSpacingText("abc あ 123", "abc あ 123")
    }

    @Test
    fun handleKatakana() {
        assertEqualsSpacingText("abcア123", "abc ア 123")
        assertEqualsSpacingText("abc ア 123", "abc ア 123")
    }

    @Test
    fun handleBopomofo() {
        assertEqualsSpacingText("abcㄅ123", "abc ㄅ 123")
        assertEqualsSpacingText("abc ㄅ 123", "abc ㄅ 123")
    }

    @Test
    fun handleEnclosedCjkLettersAndMonths() {
        assertEqualsSpacingText("abc㈱123", "abc ㈱ 123")
        assertEqualsSpacingText("abc ㈱ 123", "abc ㈱ 123")
    }

    @Test
    fun handleCjkUnifiedIdeographsExtensionA() {
        assertEqualsSpacingText("abc㐂123", "abc 㐂 123")
        assertEqualsSpacingText("abc 㐂 123", "abc 㐂 123")
    }

    @Test
    fun handleCjkUnifiedIdeographs() {
        assertEqualsSpacingText("abc丁123", "abc 丁 123")
        assertEqualsSpacingText("abc 丁 123", "abc 丁 123")
    }

    @Test
    fun handleCjkCompatibilityIdeographs() {
        assertEqualsSpacingText("abc車123", "abc 車 123")
        assertEqualsSpacingText("abc 車 123", "abc 車 123")
    }

    @Test
    fun handleDollarSign() { // \u0024
        assertEqualsSpacingText("前面\$後面", "前面 \$ 後面")
        assertEqualsSpacingText("前面 \$ 後面", "前面 \$ 後面")
        assertEqualsSpacingText("前面\$100後面", "前面 \$100 後面")
    }

    @Test
    fun handlePercentSign() { // \u0025
        assertEqualsSpacingText("前面%後面", "前面 % 後面")
        assertEqualsSpacingText("前面 % 後面", "前面 % 後面")
        assertEqualsSpacingText("前面100%後面", "前面 100% 後面")
        assertEqualsSpacingText(
            "新八的構造成分有95%是眼鏡、3%是水、2%是垃圾",
            "新八的構造成分有 95% 是眼鏡、3% 是水、2% 是垃圾"
        )
    }

    @Test
    fun handleCircumflexAccent() { // \u005E
        assertEqualsSpacingText("前面^後面", "前面 ^ 後面")
        assertEqualsSpacingText("前面 ^ 後面", "前面 ^ 後面")
    }

    @Test
    fun handleAmpersand() { // \u0026
        assertEqualsSpacingText("前面&後面", "前面 & 後面")
        assertEqualsSpacingText("前面 & 後面", "前面 & 後面")
        assertEqualsSpacingText("Vinta&Mollie", "Vinta&Mollie")
        assertEqualsSpacingText("Vinta&陳上進", "Vinta & 陳上進")
        assertEqualsSpacingText("陳上進&Vinta", "陳上進 & Vinta")
        assertEqualsSpacingText("得到一個A&B的結果", "得到一個 A&B 的結果")
    }

    @Test
    fun handleAsterisk() { // \u002A
        assertEqualsSpacingText("前面*後面", "前面 * 後面")
        assertEqualsSpacingText("前面 * 後面", "前面 * 後面")
        assertEqualsSpacingText("前面* 後面", "前面 * 後面")
        assertEqualsSpacingText("前面 *後面", "前面 * 後面")
        assertEqualsSpacingText("Vinta*Mollie", "Vinta*Mollie")
        assertEqualsSpacingText("Vinta*陳上進", "Vinta * 陳上進")
        assertEqualsSpacingText("陳上進*Vinta", "陳上進 * Vinta")
        assertEqualsSpacingText("得到一個A*B的結果", "得到一個 A*B 的結果")
    }

    @Test
    fun handleHyphenMinus() { // \u002D
        assertEqualsSpacingText("前面-後面", "前面 - 後面")
        assertEqualsSpacingText("前面 - 後面", "前面 - 後面")
        assertEqualsSpacingText("Vinta-Mollie", "Vinta-Mollie")
        assertEqualsSpacingText("Vinta-陳上進", "Vinta - 陳上進")
        assertEqualsSpacingText("陳上進-Vinta", "陳上進 - Vinta")
        assertEqualsSpacingText("得到一個A-B的結果", "得到一個 A-B 的結果")
        assertEqualsSpacingText("长者的智慧和复杂的维斯特洛- 文章", "长者的智慧和复杂的维斯特洛 - 文章")

        // todo
        // assertEqualsSpacingText("陳上進--Vinta", "陳上進 -- Vinta")
    }

    @Test
    fun handleEqualsSign() { // \u003D
        assertEqualsSpacingText("前面=後面", "前面 = 後面")
        assertEqualsSpacingText("前面 = 後面", "前面 = 後面")
        assertEqualsSpacingText("Vinta=Mollie", "Vinta=Mollie")
        assertEqualsSpacingText("Vinta=陳上進", "Vinta = 陳上進")
        assertEqualsSpacingText("陳上進=Vinta", "陳上進 = Vinta")
        assertEqualsSpacingText("得到一個A=B的結果", "得到一個 A=B 的結果")
    }

    @Test
    fun handlePlusSign() { // \u002B
        assertEqualsSpacingText("前面+後面", "前面 + 後面")
        assertEqualsSpacingText("前面 + 後面", "前面 + 後面")
        assertEqualsSpacingText("Vinta+Mollie", "Vinta+Mollie")
        assertEqualsSpacingText("Vinta+陳上進", "Vinta + 陳上進")
        assertEqualsSpacingText("陳上進+Vinta", "陳上進 + Vinta")
        assertEqualsSpacingText("得到一個A+B的結果", "得到一個 A+B 的結果")
        assertEqualsSpacingText("得到一個C++的結果", "得到一個 C++ 的結果")
    }

    @Test
    fun handleVerticalLine() { // \u007C
        assertEqualsSpacingText("前面|後面", "前面 | 後面")
        assertEqualsSpacingText("前面 | 後面", "前面 | 後面")
        assertEqualsSpacingText("Vinta|Mollie", "Vinta|Mollie")
        assertEqualsSpacingText("Vinta|陳上進", "Vinta | 陳上進")
        assertEqualsSpacingText("陳上進|Vinta", "陳上進 | Vinta")
        assertEqualsSpacingText("得到一個A|B的結果", "得到一個 A|B 的結果")
    }

    @Test
    fun handleDoubleReverseSolidus() { // \u005C
        assertEqualsSpacingText("前面\\\\後面", "前面\\\\後面")
        assertEqualsSpacingText("前面 \\\\ 後面", "前面 \\\\ 後面")
    }

    @Test
    fun handleSolidus() { // \u002F
        assertEqualsSpacingText("前面/後面", "前面 / 後面")
        assertEqualsSpacingText("前面 / 後面", "前面 / 後面")
        assertEqualsSpacingText("Vinta/Mollie", "Vinta/Mollie")
        assertEqualsSpacingText("Vinta/陳上進", "Vinta / 陳上進")
        assertEqualsSpacingText("陳上進/Vinta", "陳上進 / Vinta")
        assertEqualsSpacingText("Mollie/陳上進/Vinta", "Mollie / 陳上進 / Vinta")
        assertEqualsSpacingText("得到一個A/B的結果", "得到一個 A/B 的結果")
        assertEqualsSpacingText(
            "2016-12-26(奇幻电影节) / 2017-01-20(美国) / 詹姆斯麦卡沃伊",
            "2016-12-26 (奇幻电影节) / 2017-01-20 (美国) / 詹姆斯麦卡沃伊"
        )
        assertEqualsSpacingText(
            "/home/和/root是Linux中的頂級目錄",
            "/home/ 和 /root 是 Linux 中的頂級目錄"
        )
        assertEqualsSpacingText(
            "當你用cat和od指令查看/dev/random和/dev/urandom的內容時",
            "當你用 cat 和 od 指令查看 /dev/random 和 /dev/urandom 的內容時"
        )
    }

    @Test
    fun handleLessThanSign() { // \u003C
        assertEqualsSpacingText("前面<後面", "前面 < 後面")
        assertEqualsSpacingText("前面 < 後面", "前面 < 後面")
        assertEqualsSpacingText("Vinta<Mollie", "Vinta<Mollie")
        assertEqualsSpacingText("Vinta<陳上進", "Vinta < 陳上進")
        assertEqualsSpacingText("陳上進<Vinta", "陳上進 < Vinta")
        assertEqualsSpacingText("得到一個A<B的結果", "得到一個 A<B 的結果")
    }

    @Test
    fun handleGreaterThanSign() { // \u003E
        assertEqualsSpacingText("前面>後面", "前面 > 後面")
        assertEqualsSpacingText("前面 > 後面", "前面 > 後面")
        assertEqualsSpacingText("Vinta>Mollie", "Vinta>Mollie")
        assertEqualsSpacingText("Vinta>陳上進", "Vinta > 陳上進")
        assertEqualsSpacingText("陳上進>Vinta", "陳上進 > Vinta")
        assertEqualsSpacingText("得到一個A>B的結果", "得到一個 A>B 的結果")
    }

    @Test
    fun handleCommercialAt() { // \u0040
        assertEqualsSpacingText("請@vinta吃大餐", "請 @vinta 吃大餐")
        assertEqualsSpacingText("請@陳上進 吃大餐", "請 @陳上進 吃大餐")
    }

    @Test
    fun handleNumberSign() { // \u0023
        assertEqualsSpacingText("前面#後面", "前面 #後面")
        assertEqualsSpacingText("前面C#後面", "前面 C# 後面")
        assertEqualsSpacingText("前面#H2G2後面", "前面 #H2G2 後面")
        assertEqualsSpacingText("前面 #銀河便車指南 後面", "前面 #銀河便車指南 後面")
        assertEqualsSpacingText("前面#銀河便車指南 後面", "前面 #銀河便車指南 後面")
        assertEqualsSpacingText("前面#銀河公車指南 #銀河拖吊車指南 後面", "前面 #銀河公車指南 #銀河拖吊車指南 後面")
    }

    @Test
    fun handleMultiFullStop() { // \u002E
        assertEqualsSpacingText("前面...後面", "前面... 後面")
        assertEqualsSpacingText("前面.. 後面", "前面.. 後面")
    }

    @Test
    fun handleHorizontalEllipsis() { // \u2026
        assertEqualsSpacingText("前面…後面", "前面… 後面")
        assertEqualsSpacingText("前面 …… 後面", "前面…… 後面")
    }

    @Test
    fun handleTilde() { // \u007E
        assertEqualsSpacingText("前面~後面", "前面～後面")
        assertEqualsSpacingText("前面 ~ 後面", "前面～後面")
        assertEqualsSpacingText("前面~ 後面", "前面～後面")
        assertEqualsSpacingText("前面 ~後面", "前面～後面")
    }

    @Test
    fun handleExclamationMark() { // \u0021
        assertEqualsSpacingText("前面!後面", "前面！後面")
        assertEqualsSpacingText("前面 ! 後面", "前面！後面")
        assertEqualsSpacingText("前面! 後面", "前面！後面")
        assertEqualsSpacingText("前面 !後面", "前面！後面")
    }

    @Test
    fun handleSemicolon() { // \u003B
        assertEqualsSpacingText("前面;後面", "前面；後面")
        assertEqualsSpacingText("前面 ; 後面", "前面；後面")
        assertEqualsSpacingText("前面; 後面", "前面；後面")
        assertEqualsSpacingText("前面 ;後面", "前面；後面")
    }

    @Test
    fun handleColon() { // \u003A
        assertEqualsSpacingText("前面:後面", "前面：後面")
        assertEqualsSpacingText("前面 : 後面", "前面：後面")
        assertEqualsSpacingText("前面: 後面", "前面：後面")
        assertEqualsSpacingText("前面 :後面", "前面：後面")
        assertEqualsSpacingText("電話:123456789", "電話：123456789")
        assertEqualsSpacingText("前面:)後面", "前面：) 後面")
        assertEqualsSpacingText("前面:I have no idea後面", "前面：I have no idea 後面")
        assertEqualsSpacingText("前面: I have no idea後面", "前面: I have no idea 後面")
    }

    @Test
    fun handleComma() { // \u002C
        assertEqualsSpacingText("前面,後面", "前面，後面")
        assertEqualsSpacingText("前面 , 後面", "前面，後面")
        assertEqualsSpacingText("前面, 後面", "前面，後面")
        assertEqualsSpacingText("前面 ,後面", "前面，後面")
        assertEqualsSpacingText("前面,", "前面，")
        assertEqualsSpacingText("前面, ", "前面，")
    }

    @Test
    fun handleSingleFullStop() { // \u002E
        assertEqualsSpacingText("前面.後面", "前面。後面")
        assertEqualsSpacingText("前面 . 後面", "前面。後面")
        assertEqualsSpacingText("前面. 後面", "前面。後面")
        assertEqualsSpacingText("前面 .後面", "前面。後面")
        assertEqualsSpacingText("黑人問號.jpg 後面", "黑人問號.jpg 後面")
    }

    @Test
    fun handleQuestionMark() { // \u003F
        assertEqualsSpacingText("前面?後面", "前面？後面")
        assertEqualsSpacingText("前面 ? 後面", "前面？後面")
        assertEqualsSpacingText("前面? 後面", "前面？後面")
        assertEqualsSpacingText("前面 ?後面", "前面？後面")
        assertEqualsSpacingText("所以，請問Jackey的鼻子有幾個?3.14個", "所以，請問 Jackey 的鼻子有幾個？3.14 個")
    }

    @Test
    fun handleMiddleDot() { // \u00B7
        assertEqualsSpacingText("前面·後面", "前面・後面")
        assertEqualsSpacingText("喬治·R·R·馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("S·柰子·杀马特", "S・柰子・杀马特")
    }

    @Test
    fun handleBullet() { // \u2022
        assertEqualsSpacingText("前面•後面", "前面・後面")
        assertEqualsSpacingText("喬治•R•R•馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("S•柰子•杀马特", "S・柰子・杀马特")
    }

    @Test
    fun handleHyphenationPoint() { // \u2027
        assertEqualsSpacingText("前面‧後面", "前面・後面")
        assertEqualsSpacingText("喬治‧R‧R‧馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("S‧柰子‧杀马特", "S・柰子・杀马特")
    }

    @Test
    fun handleAngleBrackets() { // `LESS-THAN SIGN` & `GREATER-THAN SIGN` \u003C \u003E
        assertEqualsSpacingText("前面<中文123漢字>後面", "前面 <中文 123 漢字> 後面")
        assertEqualsSpacingText("前面<中文123>後面", "前面 <中文 123> 後面")
        assertEqualsSpacingText("前面<123漢字>後面", "前面 <123 漢字> 後面")
        assertEqualsSpacingText("前面<中文123> tail", "前面 <中文 123> tail")
        assertEqualsSpacingText("head <中文123漢字>後面", "head <中文 123 漢字> 後面")
        assertEqualsSpacingText("head <中文123漢字> tail", "head <中文 123 漢字> tail")
    }

    @Test
    fun handleRoundBrackets() { // `LEFT PARENTHESIS` & `RIGHT PARENTHESIS` \u0028 \u0029
        assertEqualsSpacingText("前面(中文123漢字)後面", "前面 (中文 123 漢字) 後面")
        assertEqualsSpacingText("前面(中文123)後面", "前面 (中文 123) 後面")
        assertEqualsSpacingText("前面(123漢字)後面", "前面 (123 漢字) 後面")
        assertEqualsSpacingText("前面(中文123) tail", "前面 (中文 123) tail")
        assertEqualsSpacingText("head (中文123漢字)後面", "head (中文 123 漢字) 後面")
        assertEqualsSpacingText("head (中文123漢字) tail", "head (中文 123 漢字) tail")
        assertEqualsSpacingText("(or simply \"React\")", "(or simply \"React\")")
        assertEqualsSpacingText(
            "OperationalError: (2006, 'MySQL server has gone away')",
            "OperationalError: (2006, 'MySQL server has gone away')"
        )
        assertEqualsSpacingText("我看过的电影(1404)", "我看过的电影 (1404)")
        assertEqualsSpacingText(
            "Chang Stream(变更记录流)是指collection(数据库集合)的变更事件流",
            "Chang Stream (变更记录流) 是指 collection (数据库集合) 的变更事件流"
        )
    }

    @Test
    fun handleCurlyBrackets() { // `LEFT CURLY BRACKET` & `RIGHT CURLY BRACKET` \u007B \u007D
        assertEqualsSpacingText("前面{中文123漢字}後面", "前面 {中文 123 漢字} 後面")
        assertEqualsSpacingText("前面{中文123}後面", "前面 {中文 123} 後面")
        assertEqualsSpacingText("前面{123漢字}後面", "前面 {123 漢字} 後面")
        assertEqualsSpacingText("前面{中文123} tail", "前面 {中文 123} tail")
        assertEqualsSpacingText("head {中文123漢字}後面", "head {中文 123 漢字} 後面")
        assertEqualsSpacingText("head {中文123漢字} tail", "head {中文 123 漢字} tail")
    }

    @Test
    fun handleSquareBrackets() { // `LEFT SQUARE BRACKET` & `RIGHT SQUARE BRACKET` \u005B \u005D
        assertEqualsSpacingText("前面[中文123漢字]後面", "前面 [中文 123 漢字] 後面")
        assertEqualsSpacingText("前面[中文123]後面", "前面 [中文 123] 後面")
        assertEqualsSpacingText("前面[123漢字]後面", "前面 [123 漢字] 後面")
        assertEqualsSpacingText("前面[中文123] tail", "前面 [中文 123] tail")
        assertEqualsSpacingText("head [中文123漢字]後面", "head [中文 123 漢字] 後面")
        assertEqualsSpacingText("head [中文123漢字] tail", "head [中文 123 漢字] tail")
    }

    @Test
    fun handleDoubleQuotationMarks() { // `LEFT DOUBLE QUOTATION MARK` & `RIGHT DOUBLE QUOTATION MARK` \u201C \u201D
        assertEqualsSpacingText("前面“中文123漢字”後面", "前面 “中文 123 漢字” 後面")
    }

    @Test
    fun handleGraveAccentAround() { // \u0060
        assertEqualsSpacingText("前面`中間`後面", "前面 `中間` 後面")
    }

    @Test
    fun handleNumberSignAround() { // \u0023
        assertEqualsSpacingText("前面#H2G2#後面", "前面 #H2G2# 後面")
        assertEqualsSpacingText("前面#銀河閃電霹靂車指南#後面", "前面 #銀河閃電霹靂車指南# 後面")
    }

    @Test
    fun handleQuotationMarkAround() { // \u0022
        assertEqualsSpacingText("前面\"中文123漢字\"後面", "前面 \"中文 123 漢字\" 後面")
        assertEqualsSpacingText("前面\"中文123\"後面", "前面 \"中文 123\" 後面")
        assertEqualsSpacingText("前面\"123漢字\"後面", "前面 \"123 漢字\" 後面")
        assertEqualsSpacingText("前面\"中文123\" tail", "前面 \"中文 123\" tail")
        assertEqualsSpacingText("head \"中文123漢字\"後面", "head \"中文 123 漢字\" 後面")
        assertEqualsSpacingText("head \"中文123漢字\" tail", "head \"中文 123 漢字\" tail")
    }

    @Test
    fun handleApostrophes() { // eslint-disable-line quotes \u0027
        assertEqualsSpacingText(
            "Why are Python's 'private' methods not actually private?",
            "Why are Python's 'private' methods not actually private?"
        )
        assertEqualsSpacingText(
            "陳上進 likes 林依諾's status.",
            "陳上進 likes 林依諾's status."
        )
        assertEqualsSpacingText(
            "举个栗子，如果一道题只包含'A' ~ 'Z'意味着字符集大小是",
            "举个栗子，如果一道题只包含 'A' ~ 'Z' 意味着字符集大小是"
        )
    }

    @Test
    fun handleHebrewPunctuationGershayimAround() { // \u05F4
        assertEqualsSpacingText("前面״中間״後面", "前面 ״中間״ 後面")
    }

    @Test
    fun handleLatinsAndDoubleQuotationMarks() { // \u201C \u201D
        assertEqualsSpacingText(
            "阿里云 KPI 开源“计算王牌”Blink，实时计算时代已来",
            "阿里云 KPI 开源 “计算王牌” Blink，实时计算时代已来"
        )
        assertEqualsSpacingText(
            "苹果撤销Facebook“企业证书”后者股价一度短线走低",
            "苹果撤销 Facebook “企业证书” 后者股价一度短线走低"
        )
        assertEqualsSpacingText(
            "【UCG中字】“數毛社”DF的《戰神4》全新演示解析",
            "【UCG 中字】“數毛社” DF 的《戰神 4》全新演示解析"
        )
    }

    @Test
    fun handleLatinsAndPercentSign() { // \u0025
        assertEqualsSpacingText(
            "丹寧控注意Levi's全館任2件25%OFF滿額再享85折！",
            "丹寧控注意 Levi's 全館任 2 件 25% OFF 滿額再享 85 折！"
        )
    }
}