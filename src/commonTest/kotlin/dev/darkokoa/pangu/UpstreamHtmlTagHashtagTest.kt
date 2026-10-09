package dev.darkokoa.pangu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Active plain-text cases from pangu.js v10.4.1 tests/shared for HTML tags, hashtags,
// NBSP next to #, and slash lists whose /#tag shape depends on the hashtag rule.
// it.fails cases and product-name suffix lists are omitted.
class UpstreamHtmlTagHashtagTest {

    @Test
    fun angleBracketsThatAreNotTags() {
        assertEqualsSpacingText("前面<中文123漢字>後面", "前面 <中文 123 漢字> 後面")
        assertEqualsSpacingText("前面<中文123>後面", "前面 <中文 123> 後面")
        assertEqualsSpacingText("前面<123漢字>後面", "前面 <123 漢字> 後面")
        assertEqualsSpacingText("前面<中文123> tail", "前面 <中文 123> tail")
        assertEqualsSpacingText("head <中文123漢字>後面", "head <中文 123 漢字> 後面")
        assertEqualsSpacingText("head <中文123漢字> tail", "head <中文 123 漢字> tail")
    }

    @Test
    fun pairedHtmlTagsStayTight() {
        assertEqualsSpacingText("<p>一行文本</p>", "<p>一行文本</p>")
        assertEqualsSpacingText("<p>文字<strong>加粗</strong></p>", "<p>文字<strong>加粗</strong></p>")
        assertEqualsSpacingText("<div>測試<span>內容</span>結束</div>", "<div>測試<span>內容</span>結束</div>")
        assertEqualsSpacingText("<a href=\"#\">連結</a>", "<a href=\"#\">連結</a>")
        assertEqualsSpacingText("<p>第一段</p><p>第二段</p>", "<p>第一段</p><p>第二段</p>")
        assertEqualsSpacingText("<h1>標題</h1><p>內容</p>", "<h1>標題</h1><p>內容</p>")
        assertEqualsSpacingText("<div><p>嵌套<strong>測試</strong></p></div>", "<div><p>嵌套<strong>測試</strong></p></div>")
        assertEqualsSpacingText("<ul><li>第一項</li><li>第二項</li></ul>", "<ul><li>第一項</li><li>第二項</li></ul>")
        assertEqualsSpacingText("<button disabled>送出表單</button>", "<button disabled>送出表單</button>")
        assertEqualsSpacingText(
            "<attackOnJava>那一天，人類終於回想起了，曾經一度被XML所支配的恐懼</attackOnJava> <!-- 進擊的Java -->",
            "<attackOnJava>那一天，人類終於回想起了，曾經一度被 XML 所支配的恐懼</attackOnJava> <!-- 進擊的 Java -->",
        )
        assertEqualsSpacingText("<p>用<code>標記程式碼</p>", "<p>用 <code> 標記程式碼</p>")
    }

    @Test
    fun voidTagsAndAttributeValues() {
        assertEqualsSpacingText("<input value=\"測試123\">", "<input value=\"測試 123\">")
        assertEqualsSpacingText("<img src=\"test.jpg\" alt=\"測試圖片\">", "<img src=\"test.jpg\" alt=\"測試圖片\">")
        assertEqualsSpacingText("文字<br>換行", "文字<br>換行")
        assertEqualsSpacingText("文字<br />換行", "文字<br />換行")
        assertEqualsSpacingText("第一段<hr>第二段", "第一段<hr>第二段")
        assertEqualsSpacingText("第一段<hr />第二段", "第一段<hr />第二段")
        assertEqualsSpacingText("<img src=\"photo.jpg\">上面是圖片", "<img src=\"photo.jpg\">上面是圖片")
    }

    @Test
    fun bareTagsAreMentions() {
        assertEqualsSpacingText("在這裡插入一個<div>標籤", "在這裡插入一個 <div> 標籤")
        assertEqualsSpacingText("型別是List<String>的容器", "型別是 List<String> 的容器")
        assertEqualsSpacingText("把文字包在<span>裡面", "把文字包在 <span> 裡面")
        assertEqualsSpacingText("每個<li>代表一個列表項目", "每個 <li> 代表一個列表項目")
        assertEqualsSpacingText("用<table>排版是過時的做法", "用 <table> 排版是過時的做法")
        assertEqualsSpacingText("HTML的<head>放的是metadata", "HTML 的 <head> 放的是 metadata")
        assertEqualsSpacingText("<html>是整份文件的根元素", "<html> 是整份文件的根元素")
        assertEqualsSpacingText("這裡放<Spinner />元件", "這裡放 <Spinner /> 元件")
        assertEqualsSpacingText("回傳Promise<string>就好", "回傳 Promise<string> 就好")
        assertEqualsSpacingText("用Vec<u8>儲存位元組", "用 Vec<u8> 儲存位元組")
        assertEqualsSpacingText("先引入<iostream>標頭檔", "先引入 <iostream> 標頭檔")
    }

    @Test
    fun urlsInsideAnchors() {
        assertEqualsSpacingText(
            "<a href=\"https://zh.wikipedia.org/wiki/中文#歷史\">中文</a>",
            "<a href=\"https://zh.wikipedia.org/wiki/中文#歷史\">中文</a>",
        )
        assertEqualsSpacingText(
            "<a href=\"http://vinta.ws/中文網址with英文.html\">oh一個超連結with英文，網址包含中文</a>",
            "<a href=\"http://vinta.ws/中文網址with英文.html\">oh 一個超連結 with 英文，網址包含中文</a>",
        )
    }

    @Test
    fun hashtags() {
        assertEqualsSpacingText("前面#後面", "前面 #後面")
        assertEqualsSpacingText("前面#H2G2後面", "前面 #H2G2 後面")
        assertEqualsSpacingText("前面 #銀河便車指南 後面", "前面 #銀河便車指南 後面")
        assertEqualsSpacingText("前面#銀河便車指南 後面", "前面 #銀河便車指南 後面")
        assertEqualsSpacingText("前面#銀河公車指南 #銀河拖吊車指南 後面", "前面 #銀河公車指南 #銀河拖吊車指南 後面")
        assertEqualsSpacingText("前面C#後面", "前面 C# 後面")
        assertEqualsSpacingText("前面F#後面", "前面 F# 後面")
        assertEqualsSpacingText("前端/後端/資料庫：C#和Python", "前端/後端/資料庫：C# 和 Python")
        assertEqualsSpacingText(
            "dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/",
            "dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/",
        )
    }

    @Test
    fun nbspIsNotAHashtagGlue() {
        assertEqualsSpacingText("台北\u00a0#中文", "台北\u00a0#中文")
        assertEqualsSpacingText("中文#\u00a0abc", "中文#\u00a0abc")
    }

    @Test
    fun slashListsKeepHashTagsTight() {
        assertEqualsSpacingText(
            "8964/3★集會所接待員/克隆·麻煩大師/手卷師傅（已退休）/主程式毀滅者/dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/NS編號在banner裡/discord:史單力#3230",
            "8964/3★集會所接待員/克隆・麻煩大師/手卷師傅（已退休）/主程式毀滅者/dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/NS 編號在 banner 裡/discord: 史單力 #3230",
        )
        assertEqualsSpacingText(
            "8964/3★集會所接待員/克隆·麻煩大師/手卷師傅(已退休)/主程式毀滅者/dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/NS編號在banner裡/discord:史單力#3230",
            "8964/3★集會所接待員/克隆・麻煩大師/手卷師傅 (已退休)/主程式毀滅者/dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/NS 編號在 banner 裡/discord: 史單力 #3230",
        )
    }

    @Test
    fun htmlSpacingIsIdempotent() {
        val samples = listOf(
            "<p>一行文本</p>",
            "在這裡插入一個<div>標籤",
            "<input value=\"測試123\">",
            "<a href=\"http://vinta.ws/中文網址with英文.html\">oh一個超連結with英文，網址包含中文</a>",
            "dae-dae-o/#絕地家庭小會議/#今天大掃除了沒有/",
        )
        for (sample in samples) {
            val once = sample.spacingText()
            assertEquals(once, once.spacingText())
        }
    }

    @Test
    fun htmlPlaceholdersAlreadyInTheInputStayIntact() {
        val realTag = "前文\uE0020\uE003后文<p>中文abc</p>"
        assertEquals("前文\uE0020\uE003后文<p>中文 abc</p>", realTag.spacingText())

        val mention = "前文\uE0040\uE005后文在這裡插入一個<div>標籤"
        assertEquals("前文\uE0040\uE005后文在這裡插入一個 <div> 標籤", mention.spacingText())
    }

    @Test
    fun htmlPatternsStayLinearOnALongInput() {
        val angles = "<".repeat(8000)
        assertEquals("前面 $angles 後面", ("前面" + angles + "後面").spacingText())

        // No closing '>': the '<' stays a comparison operator, and the scan still finishes.
        val gaps = " ".repeat(4000)
        assertEquals("前文 < div${gaps}字", ("前文<div" + gaps + "字").spacingText())

        val tag = "<div class=\"字123\">字</div>"
        val many = tag.repeat(200)
        val spaced = ("前文" + many).spacingText()
        val spacedTag = "<div class=\"字 123\">字</div>"
        assertEquals("前文$spacedTag".length + spacedTag.length * 199, spaced.length)
        assertTrue(spaced.startsWith("前文$spacedTag"))
        assertTrue(spaced.endsWith(spacedTag))
    }
}
