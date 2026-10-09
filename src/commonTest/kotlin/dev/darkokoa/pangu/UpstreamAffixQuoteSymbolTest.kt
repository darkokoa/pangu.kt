package dev.darkokoa.pangu

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// Active plain-text cases from pangu.js v10.4.1 tests/shared for affixes, quotes,
// backticks, dot-calls, interpuncts, dingbats, and letterlike symbols.
// it.fails cases are omitted. Product-name suffix lists are not ported. HTML tags live in UpstreamHtmlTagHashtagTest.
class UpstreamAffixQuoteSymbolTest {

    @Test
    fun plusSign() {
        assertEqualsSpacingText("前面+後面", "前面 + 後面")
        assertEqualsSpacingText("陳上進+Vinta", "陳上進 + Vinta")
        assertEqualsSpacingText("Vinta+陳上進", "Vinta + 陳上進")
        assertEqualsSpacingText("你+我=我們", "你 + 我 = 我們")
        assertEqualsSpacingText("Switch+健身環套組", "Switch + 健身環套組")
        assertEqualsSpacingText("MacBook Air M2+滑鼠組合", "MacBook Air M2 + 滑鼠組合")
        assertEqualsSpacingText("前面 + 後面", "前面 + 後面")
        assertEqualsSpacingText("Vinta + Abc123", "Vinta + Abc123")
        assertEqualsSpacingText("Vinta + 陳上進", "Vinta + 陳上進")
        assertEqualsSpacingText("陳上進 + Vinta", "陳上進 + Vinta")
        assertEqualsSpacingText("得到一個 A + B 的結果", "得到一個 A + B 的結果")
        assertEqualsSpacingText("Switch OLED+健身環+保護貼", "Switch OLED + 健身環 + 保護貼")
        assertEqualsSpacingText("陳上進+Vinta+Abc123", "陳上進 + Vinta + Abc123")
        assertEqualsSpacingText("HiNet光世代+MOD+Wi-Fi全屋通", "HiNet 光世代 + MOD + Wi-Fi 全屋通")
        assertEqualsSpacingText("套餐含MOD+Netflix+Disney", "套餐含 MOD+Netflix+Disney")
        // Skipped: 影劇館+/ , 影劇館+( , and 「影劇館+」 stay glued only via the product-name suffix list.
        assertEqualsSpacingText("自選餐(全選)+「影劇館」", "自選餐 (全選) +「影劇館」")
        assertEqualsSpacingText("Vinta+Abc123", "Vinta+Abc123")
        assertEqualsSpacingText("前面A+B後面", "前面 A+B 後面")
        assertEqualsSpacingText("得到一個A+B的結果", "得到一個 A+B 的結果")
        assertEqualsSpacingText("答案是5+5的和", "答案是 5+5 的和")
        assertEqualsSpacingText("得到一個C++的結果", "得到一個 C++ 的結果")
        assertEqualsSpacingText("得到一個 C++的結果", "得到一個 C++ 的結果")
        assertEqualsSpacingText("得到一個i++的結果", "得到一個 i++ 的結果")
        assertEqualsSpacingText("我會寫C++的程式", "我會寫 C++ 的程式")
        assertEqualsSpacingText("得到一個A+的結果", "得到一個 A+ 的結果")
        assertEqualsSpacingText("得到一個 A+ 的結果", "得到一個 A+ 的結果")
        assertEqualsSpacingText("成績是A+的等級", "成績是 A+ 的等級")
        assertEqualsSpacingText("打+886這個號碼", "打 +886 這個號碼")
        assertEqualsSpacingText("氣溫是+5度左右", "氣溫是 +5 度左右")
        assertEqualsSpacingText("有100+的選擇", "有 100+ 的選擇")
        assertEqualsSpacingText("這裡有18+的內容", "這裡有 18+ 的內容")
        assertEqualsSpacingText("評分3.5+的餐廳", "評分 3.5+ 的餐廳")
        assertEqualsSpacingText("Python 3+的版本", "Python 3+ 的版本")
    }

    @Test
    fun minusSign() {
        assertEqualsSpacingText("前面-後面", "前面 - 後面")
        assertEqualsSpacingText("Vinta-陳上進", "Vinta - 陳上進")
        assertEqualsSpacingText("陳上進-Vinta", "陳上進 - Vinta")
        assertEqualsSpacingText("博客來-Rewire-神經可塑性：用神經科學突破行為模式迴圈，終結焦慮、恐慌和憂鬱，實現最佳的心理健康", "博客來 - Rewire - 神經可塑性：用神經科學突破行為模式迴圈，終結焦慮、恐慌和憂鬱，實現最佳的心理健康")
        assertEqualsSpacingText("博客來-經濟學原理 10/e Mankiw (授權經銷版)", "博客來 - 經濟學原理 10/e Mankiw (授權經銷版)")
        assertEqualsSpacingText("財政部電子發票整合服務平台[自然人憑證]-歸戶設定通知", "財政部電子發票整合服務平台 [自然人憑證] - 歸戶設定通知")
        assertEqualsSpacingText("博客來-4%法則：讓錢活得比你久的提領金律(電子書)", "博客來 - 4% 法則：讓錢活得比你久的提領金律 (電子書)")
        assertEqualsSpacingText("长者的智慧和复杂的维斯特洛- 文章", "长者的智慧和复杂的维斯特洛 - 文章")
        assertEqualsSpacingText("1976年-2018年", "1976 年 - 2018 年")
        assertEqualsSpacingText("年增率-(GDP)", "年增率 - (GDP)")
        assertEqualsSpacingText("年增率-[GDP]", "年增率 - [GDP]")
        assertEqualsSpacingText("年增率-(季調)", "年增率 - (季調)")
        assertEqualsSpacingText("全球-實質國內生產毛額[GDP]-(年增率, IMF 預估)", "全球 - 實質國內生產毛額 [GDP] - (年增率, IMF 預估)")
        assertEqualsSpacingText("全球-名目國內生產毛額[GDP]-(NSA,美元,IMF 預估)", "全球 - 名目國內生產毛額 [GDP] - (NSA, 美元, IMF 預估)")
        assertEqualsSpacingText("台灣-消費者物價指數[CPI]-(年增率)", "台灣 - 消費者物價指數 [CPI] - (年增率)")
        assertEqualsSpacingText("美國-核心消費者物價指數[Core CPI]-(SA,年增率)", "美國 - 核心消費者物價指數 [Core CPI] - (SA, 年增率)")
        assertEqualsSpacingText("美國-個人消費支出物價指數[PCE]-(年增率)-第10百分位數", "美國 - 個人消費支出物價指數 [PCE] - (年增率) - 第 10 百分位數")
        assertEqualsSpacingText("前面 - 後面", "前面 - 後面")
        assertEqualsSpacingText("Vinta - Abc123", "Vinta - Abc123")
        assertEqualsSpacingText("Vinta - 陳上進", "Vinta - 陳上進")
        assertEqualsSpacingText("陳上進 - Vinta", "陳上進 - Vinta")
        assertEqualsSpacingText("得到一個 A - B 的結果", "得到一個 A - B 的結果")
        assertEqualsSpacingText("Vinta-Abc123", "Vinta-Abc123")
        assertEqualsSpacingText("得到一個A-B的結果", "得到一個 A-B 的結果")
        assertEqualsSpacingText("去5-A教室上課", "去 5-A 教室上課")
        assertEqualsSpacingText("搭2-A的公車", "搭 2-A 的公車")
        assertEqualsSpacingText("範圍是1-10的整數", "範圍是 1-10 的整數")
        assertEqualsSpacingText("用USB-C充電", "用 USB-C 充電")
        assertEqualsSpacingText("照X-RAY檢查", "照 X-RAY 檢查")
        assertEqualsSpacingText("毛額[GDP]-(NSA)", "毛額 [GDP]-(NSA)")
        assertEqualsSpacingText("英文姓名須與護照上相同，包含標點符號；範例：王小明，英文名為WANG, HSIAO-MING，請於英文姓(Surname)欄位填入WANG,、英文名(Given Names)欄位填入HSIAO-MING。", "英文姓名須與護照上相同，包含標點符號；範例：王小明，英文名為 WANG, HSIAO-MING，請於英文姓 (Surname) 欄位填入 WANG,、英文名 (Given Names) 欄位填入 HSIAO-MING。")
        assertEqualsSpacingText("Sci-Fi", "Sci-Fi")
        assertEqualsSpacingText("X-RAY", "X-RAY")
        assertEqualsSpacingText("USB Type-C", "USB Type-C")
        assertEqualsSpacingText("The company offered a state-of-the-art machine-learning-powered real-time fraud-detection system with end-to-end encryption and cutting-edge performance.", "The company offered a state-of-the-art machine-learning-powered real-time fraud-detection system with end-to-end encryption and cutting-edge performance.")
        assertEqualsSpacingText("這間公司提供了一套state-of-the-art、machine-learning-powered的real-time fraud-detection系統，具備end-to-end加密功能以及cutting-edge的效能。", "這間公司提供了一套 state-of-the-art、machine-learning-powered 的 real-time fraud-detection 系統，具備 end-to-end 加密功能以及 cutting-edge 的效能。")
        assertEqualsSpacingText("Anthropic的claude-4-opus模型", "Anthropic 的 claude-4-opus 模型")
        assertEqualsSpacingText("OpenAI的o3-pro模型", "OpenAI 的 o3-pro 模型")
        assertEqualsSpacingText("OpenAI的gpt-4o模型", "OpenAI 的 gpt-4o 模型")
        assertEqualsSpacingText("OpenAI的GPT-5模型", "OpenAI 的 GPT-5 模型")
        assertEqualsSpacingText("Google的gemini-2.5-pro模型", "Google 的 gemini-2.5-pro 模型")
        assertEqualsSpacingText("你可以使用uname -m指令來檢查你的Linux作業系統是32位元或是[敏感词已被屏蔽]位元", "你可以使用 uname -m 指令來檢查你的 Linux 作業系統是 32 位元或是 [敏感词已被屏蔽] 位元")
        assertEqualsSpacingText("參數要加-m的旗標", "參數要加 -m 的旗標")
        assertEqualsSpacingText("得到一個D-的結果", "得到一個 D- 的結果")
        assertEqualsSpacingText("得到一個D--的結果", "得到一個 D-- 的結果")
    }

    @Test
    fun pipe() {
        assertEqualsSpacingText("前面|後面", "前面 | 後面")
        assertEqualsSpacingText("Vinta|貓咪", "Vinta | 貓咪")
        assertEqualsSpacingText("貓咪|Vinta", "貓咪 | Vinta")
        assertEqualsSpacingText("陳上進|貓咪|Abc123", "陳上進 | 貓咪 | Abc123")
        assertEqualsSpacingText("陳上進|Abc123|貓咪", "陳上進 | Abc123 | 貓咪")
        assertEqualsSpacingText("Abc123|Vinta|貓咪", "Abc123 | Vinta | 貓咪")
        assertEqualsSpacingText("Abc123|陳上進|貓咪", "Abc123 | 陳上進 | 貓咪")
        assertEqualsSpacingText("作詞|林夕", "作詞 | 林夕")
        assertEqualsSpacingText("文|張三 圖|李四", "文 | 張三 圖 | 李四")
        assertEqualsSpacingText("支援的 Apple TV 型號|Disney+ 幫助中心|TW", "支援的 Apple TV 型號 | Disney+ 幫助中心 | TW")
        assertEqualsSpacingText("前面 | 後面", "前面 | 後面")
        assertEqualsSpacingText("Vinta | Abc123", "Vinta | Abc123")
        assertEqualsSpacingText("Vinta | Abc123 | Kitten", "Vinta | Abc123 | Kitten")
        assertEqualsSpacingText("陳上進 | 貓咪 | Abc123", "陳上進 | 貓咪 | Abc123")
        assertEqualsSpacingText("陳上進 | Abc123 | 貓咪", "陳上進 | Abc123 | 貓咪")
        assertEqualsSpacingText("Abc123 | Vinta | 貓咪", "Abc123 | Vinta | 貓咪")
        assertEqualsSpacingText("Abc123 | 陳上進 | 貓咪", "Abc123 | 陳上進 | 貓咪")
        assertEqualsSpacingText("Vinta|Abc123", "Vinta|Abc123")
        assertEqualsSpacingText("Vinta|Abc123|Kitten", "Vinta|Abc123|Kitten")
        assertEqualsSpacingText("ps aux|grep node", "ps aux|grep node")
        assertEqualsSpacingText("條件是x|y的情況", "條件是 x|y 的情況")
        assertEqualsSpacingText("得到一個A|B的結果", "得到一個 A|B 的結果")
        assertEqualsSpacingText("得到一個A||B的結果", "得到一個 A||B 的結果")
    }

    @Test
    fun middleDot() {
        assertEqualsSpacingText("前面·後面", "前面・後面")
        assertEqualsSpacingText("喬治·R·R·馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("M·奈特·沙马兰", "M・奈特・沙马兰")
        assertEqualsSpacingText("哥爾 · D · 羅傑", "哥爾 · D · 羅傑")
        assertEqualsSpacingText("看过 · · ·", "看过 · · ·")
        assertEqualsSpacingText("看过 · · · (2026部)", "看过 · · · (2026 部)")
        assertEqualsSpacingText("前面•後面", "前面・後面")
        assertEqualsSpacingText("喬治•R•R•馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("M•奈特•沙马兰", "M・奈特・沙马兰")
        assertEqualsSpacingText("前面 • 後面", "前面 • 後面")
        assertEqualsSpacingText("喬治 • R • R • 馬丁", "喬治 • R • R • 馬丁")
        assertEqualsSpacingText("M • 奈特 • 沙马兰", "M • 奈特 • 沙马兰")
        assertEqualsSpacingText("國泰CUBE卡 •••• 1234", "國泰 CUBE 卡 •••• 1234")
        assertEqualsSpacingText("ether.fi Cash Card •••• 5678", "ether.fi Cash Card •••• 5678")
        assertEqualsSpacingText("前面‧後面", "前面・後面")
        assertEqualsSpacingText("喬治‧R‧R‧馬丁", "喬治・R・R・馬丁")
        assertEqualsSpacingText("M‧奈特‧沙马兰", "M・奈特・沙马兰")
        assertEqualsSpacingText("前面 ‧ 後面", "前面 ‧ 後面")
        assertEqualsSpacingText("喬治 ‧ R ‧ R ‧ 馬丁", "喬治 ‧ R ‧ R ‧ 馬丁")
        assertEqualsSpacingText("M ‧ 奈特 ‧ 沙马兰", "M ‧ 奈特 ‧ 沙马兰")
    }

    @Test
    fun backtick() {
        assertEqualsSpacingText("前面`中間`後面", "前面 `中間` 後面")
        assertEqualsSpacingText("`! git commit -a -m \"蛤\"`", "`! git commit -a -m \"蛤\"`")
        assertEqualsSpacingText("从结果来看，当a.b销毁后，`a.getB()`返回值为null", "从结果来看，当 a.b 销毁后，`a.getB()` 返回值为 null")
        assertEqualsSpacingText("雖然知道可以在Claude Code直接執行shell指令，例如`! git commit -a -m \"蛤\"`，但是看了文件才知道原來在 http://command.md 裡面也可以用`!`啊#TIL", "雖然知道可以在 Claude Code 直接執行 shell 指令，例如 `! git commit -a -m \"蛤\"`，但是看了文件才知道原來在 http://command.md 裡面也可以用 `!` 啊 #TIL")
        assertEqualsSpacingText("雖然知道可以在 Claude Code 直接執行 shell 指令，例如 `! git commit -a -m \"蛤\"`，但是看了文件才知道原來在 http://command.md 裡面也可以用 `!` 啊 #TIL", "雖然知道可以在 Claude Code 直接執行 shell 指令，例如 `! git commit -a -m \"蛤\"`，但是看了文件才知道原來在 http://command.md 裡面也可以用 `!` 啊 #TIL")
    }

    @Test
    fun doubleQuotes() {
        assertEqualsSpacingText("前面\"中文123漢字\"後面", "前面 \"中文 123 漢字\" 後面")
        assertEqualsSpacingText("前面\"中文123\"後面", "前面 \"中文 123\" 後面")
        assertEqualsSpacingText("前面\"中文abc\"後面", "前面 \"中文 abc\" 後面")
        assertEqualsSpacingText("前面\"123漢字\"後面", "前面 \"123 漢字\" 後面")
        assertEqualsSpacingText("前面\"中文123\" tail", "前面 \"中文 123\" tail")
        assertEqualsSpacingText("head \"中文123漢字\"後面", "head \"中文 123 漢字\" 後面")
        assertEqualsSpacingText("head \"中文123漢字\" tail", "head \"中文 123 漢字\" tail")
        assertEqualsSpacingText("\"字+\"", "\"字 +\"")
        assertEqualsSpacingText("\"字|\"", "\"字 |\"")
        assertEqualsSpacingText("你好\"字+\"世界", "你好 \"字 +\" 世界")
        assertEqualsSpacingText("前面\"字|\"後面", "前面 \"字 |\" 後面")
        assertEqualsSpacingText("多行\"字+\"\n下行\"字|\"", "多行 \"字 +\"\n下行 \"字 |\"")
        assertEqualsSpacingText("我們也不可以說\"We invited the reverend to dinner.\"", "我們也不可以說 \"We invited the reverend to dinner.\"")
        assertEqualsSpacingText("\"We invited the Rev. Darling.\"我們也不可以說", "\"We invited the Rev. Darling.\" 我們也不可以說")
        assertEqualsSpacingText("它應該這樣使用：\"We invited\"", "它應該這樣使用：\"We invited\"")
        assertEqualsSpacingText("\"! git commit -a -m '蛤'\"", "\"! git commit -a -m '蛤'\"")
        assertEqualsSpacingText("Rev. (Reverend；牧師的尊稱)這個縮寫嚴格來說並不是一項頭銜，而是形容詞。所以，它應該這樣使用：\"We invited the Rev. Alan Darling.\" 或  \"We invited the Rev. Mr. Darling.\"，而非\"We invited the Rev. Darling.\"我們也不可以說\"We invited the reverend to dinner.\" -- Only a cad would invite the rev. (只有下流的人才會招致批評：句中的 rev. 是 review 的縮寫，算是雙關語)", "Rev. (Reverend；牧師的尊稱) 這個縮寫嚴格來說並不是一項頭銜，而是形容詞。所以，它應該這樣使用：\"We invited the Rev. Alan Darling.\" 或  \"We invited the Rev. Mr. Darling.\"，而非 \"We invited the Rev. Darling.\" 我們也不可以說 \"We invited the reverend to dinner.\" -- Only a cad would invite the rev. (只有下流的人才會招致批評：句中的 rev. 是 review 的縮寫，算是雙關語)")
        assertEqualsSpacingText("使用：\"We\ninvited Darling.\" 或 \"We invited.\"", "使用：\"We\ninvited Darling.\" 或 \"We invited.\"")
        assertEqualsSpacingText("Rev. (Reverend；牧師的尊稱) \n    這個縮寫嚴格來說並不是一項頭銜，而是形容詞。所以，它應該這樣使用：\"We \n    invited the Rev. Alan Darling.\" 或  \"We  invited the Rev. Mr. \n    Darling.\" ，而非 \"We invited the Rev. Darling.\" 我們也不可以說  \n    \"We invited the reverend to dinner.\" -- Only a cad would invite the rev. (只有下流的人才會招致批評：句中的 \n    rev. 是 review 的縮寫，算是雙關語) ", "Rev. (Reverend；牧師的尊稱) \n    這個縮寫嚴格來說並不是一項頭銜，而是形容詞。所以，它應該這樣使用：\"We \n    invited the Rev. Alan Darling.\" 或  \"We  invited the Rev. Mr. \n    Darling.\" ，而非 \"We invited the Rev. Darling.\" 我們也不可以說  \n    \"We invited the reverend to dinner.\" -- Only a cad would invite the rev. (只有下流的人才會招致批評：句中的 \n    rev. 是 review 的縮寫，算是雙關語) ")
        assertEqualsSpacingText("獲標準普爾長期信用評等“AA”全球電信業之首", "獲標準普爾長期信用評等 “AA” 全球電信業之首")
        assertEqualsSpacingText("阿里云开源“计算王牌”Blink，实时计算时代已来", "阿里云开源 “计算王牌” Blink，实时计算时代已来")
        assertEqualsSpacingText("苹果撤销Facebook“企业证书”后者股价一度短线走低", "苹果撤销 Facebook “企业证书” 后者股价一度短线走低")
        assertEqualsSpacingText("【UCG中字】“數毛社”DF的《戰神4》全新演示解析", "【UCG 中字】“數毛社” DF 的《戰神 4》全新演示解析")
        assertEqualsSpacingText("他说”你好”啊", "他说 ”你好” 啊")
        assertEqualsSpacingText("《战斧骨》里还有个镜头挺有意思，就是男主”见路不走”，不从峡谷入口走，而选择了从侧面翻越，还顺便借着口哨吸引出来一个食人族给杀了。", "《战斧骨》里还有个镜头挺有意思，就是男主 ”见路不走”，不从峡谷入口走，而选择了从侧面翻越，还顺便借着口哨吸引出来一个食人族给杀了。")
    }

    @Test
    fun singleQuotes() {
        assertEqualsSpacingText("Why are Python's 'private' methods not actually private?", "Why are Python's 'private' methods not actually private?")
        assertEqualsSpacingText("举个栗子，如果一道题只包含'A' ~ 'Z'意味着字符集大小是", "举个栗子，如果一道题只包含 'A' ~ 'Z' 意味着字符集大小是")
        assertEqualsSpacingText("后续会直接用iframe window.addEventListener('message')", "后续会直接用 iframe window.addEventListener('message')")
        assertEqualsSpacingText("'! git commit -a -m \"蛤\"'", "'! git commit -a -m \"蛤\"'")
        assertEqualsSpacingText("Remove '铁蕾' from 1 Folder?", "Remove '铁蕾' from 1 Folder?")
        assertEqualsSpacingText("陳上進 likes 林依諾's status.", "陳上進 likes 林依諾's status.")
    }

    @Test
    fun superscriptAndMarks() {
        assertEqualsSpacingText("前面E=mc²後面", "前面 E=mc² 後面")
        assertEqualsSpacingText("115年賽事加碼：7/21-10/8 新申請MOD+自選餐(全選)/影劇館⁺加碼", "115 年賽事加碼：7/21-10/8 新申請 MOD + 自選餐 (全選)/影劇館⁺ 加碼")
        assertEqualsSpacingText("甲⁰乙、甲¹乙、甲²乙、甲³乙、甲⁴乙、甲⁵乙、甲⁶乙、甲⁷乙、甲⁸乙、甲⁹乙", "甲⁰ 乙、甲¹ 乙、甲² 乙、甲³ 乙、甲⁴ 乙、甲⁵ 乙、甲⁶ 乙、甲⁷ 乙、甲⁸ 乙、甲⁹ 乙")
        assertEqualsSpacingText("甲ⁱ乙、甲ⁿ乙、甲⁺乙、甲⁻乙、甲⁼乙", "甲ⁱ 乙、甲ⁿ 乙、甲⁺ 乙、甲⁻ 乙、甲⁼ 乙")
        assertEqualsSpacingText("甲⁽註⁾乙", "甲⁽註⁾ 乙")
        assertEqualsSpacingText("Trademark™後面", "Trademark™ 後面")
        assertEqualsSpacingText("商標™後面", "商標™ 後面")
        assertEqualsSpacingText("Service Mark℠後面", "Service Mark℠ 後面")
        assertEqualsSpacingText("服務商標℠後面", "服務商標℠ 後面")
        assertEqualsSpacingText("Registered Trademark®後面", "Registered Trademark® 後面")
        assertEqualsSpacingText("註冊商標®公司", "註冊商標® 公司")
        assertEqualsSpacingText("註冊商標®與Trademark™", "註冊商標® 與 Trademark™")
        assertEqualsSpacingText("版權所有©2026東亞重工", "版權所有 © 2026 東亞重工")
        assertEqualsSpacingText("版權所有©2012-2026東亞重工", "版權所有 © 2012-2026 東亞重工")
        assertEqualsSpacingText("Copyright © 2026東亞重工", "Copyright © 2026 東亞重工")
        assertEqualsSpacingText("Copyright © 2012-2026東亞重工", "Copyright © 2012-2026 東亞重工")
    }

    @Test
    fun asterisk() {
        assertEqualsSpacingText("前面*後面", "前面 * 後面")
        assertEqualsSpacingText("Vinta*陳上進", "Vinta * 陳上進")
        assertEqualsSpacingText("陳上進*Vinta", "陳上進 * Vinta")
        assertEqualsSpacingText("標示*的欄位代表必填", "標示 * 的欄位代表必填")
        assertEqualsSpacingText("時薪*(平日時數+假日時數)", "時薪 * (平日時數 + 假日時數)")
        assertEqualsSpacingText("前面 * 後面", "前面 * 後面")
        assertEqualsSpacingText("Vinta * Abc123", "Vinta * Abc123")
        assertEqualsSpacingText("Vinta * 陳上進", "Vinta * 陳上進")
        assertEqualsSpacingText("陳上進 * Vinta", "陳上進 * Vinta")
        assertEqualsSpacingText("得到一個 A * B 的結果", "得到一個 A * B 的結果")
        assertEqualsSpacingText("Vinta*Abc123", "Vinta*Abc123")
        assertEqualsSpacingText("得到一個A*B的結果", "得到一個 A*B 的結果")
        assertEqualsSpacingText("算式是2*3的積", "算式是 2*3 的積")
        assertEqualsSpacingText("刪掉*.log的檔案", "刪掉 *.log 的檔案")
        assertEqualsSpacingText("刪掉*[0-9].log的檔案", "刪掉 *[0-9].log 的檔案")
        assertEqualsSpacingText("刪掉*[a-z].log的檔案", "刪掉 *[a-z].log 的檔案")
        assertEqualsSpacingText("刪掉*[!0-9].log的檔案", "刪掉 *[!0-9].log 的檔案")
        assertEqualsSpacingText("刪掉*[0-9].tar.gz的檔案", "刪掉 *[0-9].tar.gz 的檔案")
        assertEqualsSpacingText("刪掉*[0-9][0-9].log的檔案", "刪掉 *[0-9][0-9].log 的檔案")
        assertEqualsSpacingText("刪掉*[0-9]*.log的檔案", "刪掉 *[0-9]*.log 的檔案")
        assertEqualsSpacingText("刪掉 *[0-9].log 的檔案", "刪掉 *[0-9].log 的檔案")
        assertEqualsSpacingText("刪掉 *[a-z].log 的檔案", "刪掉 *[a-z].log 的檔案")
        assertEqualsSpacingText("刪掉 *[!0-9].log 的檔案", "刪掉 *[!0-9].log 的檔案")
        assertEqualsSpacingText("刪掉 *[0-9].tar.gz 的檔案", "刪掉 *[0-9].tar.gz 的檔案")
        assertEqualsSpacingText("刪掉 *[0-9][0-9].log 的檔案", "刪掉 *[0-9][0-9].log 的檔案")
        assertEqualsSpacingText("刪掉 *[0-9]*.log 的檔案", "刪掉 *[0-9]*.log 的檔案")
    }

    @Test
    fun equalsSign() {
        assertEqualsSpacingText("前面=後面", "前面 = 後面")
        assertEqualsSpacingText("Vinta=陳上進", "Vinta = 陳上進")
        assertEqualsSpacingText("陳上進=Vinta", "陳上進 = Vinta")
        assertEqualsSpacingText("年增率=(今年-去年)", "年增率 = (今年 - 去年)")
        assertEqualsSpacingText("總價=(單價*數量)", "總價 = (單價 * 數量)")
        assertEqualsSpacingText("前面 = 後面", "前面 = 後面")
        assertEqualsSpacingText("Vinta = Abc123", "Vinta = Abc123")
        assertEqualsSpacingText("Vinta = 陳上進", "Vinta = 陳上進")
        assertEqualsSpacingText("陳上進 = Vinta", "陳上進 = Vinta")
        assertEqualsSpacingText("得到一個 A = B 的結果", "得到一個 A = B 的結果")
        assertEqualsSpacingText("Vinta=Abc123", "Vinta=Abc123")
        assertEqualsSpacingText("得到一個A=B的結果", "得到一個 A=B 的結果")
        assertEqualsSpacingText("設定a=1之後執行", "設定 a=1 之後執行")
        assertEqualsSpacingText("網址是example.com?foo=bar&baz=1的頁面", "網址是 example.com?foo=bar&baz=1 的頁面")
        assertEqualsSpacingText("用=>寫箭頭函式", "用 => 寫箭頭函式")
    }

    @Test
    fun squareBrackets() {
        assertEqualsSpacingText("前面[中文123漢字]後面", "前面 [中文 123 漢字] 後面")
        assertEqualsSpacingText("前面[中文123]後面", "前面 [中文 123] 後面")
        assertEqualsSpacingText("前面[123漢字]後面", "前面 [123 漢字] 後面")
        assertEqualsSpacingText("前面[中文123] tail", "前面 [中文 123] tail")
        assertEqualsSpacingText("head [中文123漢字]後面", "head [中文 123 漢字] 後面")
        assertEqualsSpacingText("head [中文123漢字] tail", "head [中文 123 漢字] tail")
        assertEqualsSpacingText("[x \n]中", "[x \n] 中")
        assertEqualsSpacingText("中[ 多行\n內容 ]", "中 [多行\n內容]")
    }

    @Test
    fun dashKeptTight() {
        assertEqualsSpacingText("A—B", "A—B")
        assertEqualsSpacingText("2020—2024年", "2020—2024 年")
        assertEqualsSpacingText("A─B", "A─B")
        assertEqualsSpacingText("2020──2024", "2020──2024")
    }

    @Test
    fun letterlikeDingbatsAndEmDash() {
        // Em dash stays tight. Upstream still marks "他說 —— 不對" as it.fails.
        assertEqualsSpacingText("今天123℃很熱", "今天 123℃ 很熱")
        assertEqualsSpacingText("攝氏25℃到30℃之間", "攝氏 25℃ 到 30℃ 之間")
        assertEqualsSpacingText("水溫98℉了", "水溫 98℉ 了")
        assertEqualsSpacingText("5℃~10℃之間", "5℃~10℃ 之間")
        assertEqualsSpacingText("溫度是℃單位", "溫度是 ℃ 單位")
        assertEqualsSpacingText("第№5號", "第 №5 號")
        assertEqualsSpacingText("電阻10Ω很小", "電阻 10Ω 很小")
        assertEqualsSpacingText("中文ℝ漢字", "中文 ℝ 漢字")
        assertEqualsSpacingText("中文 ℝ 漢字", "中文 ℝ 漢字")
        assertEqualsSpacingText("符號ℓ表示長度", "符號 ℓ 表示長度")
        assertEqualsSpacingText("資訊ℹ圖示", "資訊 ℹ 圖示")
        assertEqualsSpacingText("估計℮500ml", "估計 ℮500ml")
        assertEqualsSpacingText("剪刀✂符號", "剪刀 ✂ 符號")
        assertEqualsSpacingText("完成✅了", "完成 ✅ 了")
        assertEqualsSpacingText("愛心❤符號", "愛心 ❤ 符號")
        assertEqualsSpacingText("A—B", "A—B")
        assertEqualsSpacingText("2020—2024年", "2020—2024 年")
        assertEqualsSpacingText("他說——不對", "他說——不對")
        assertEqualsSpacingText("A─B", "A─B")
        assertEqualsSpacingText("2020──2024", "2020──2024")
    }

    @Test
    fun hasProperSpacingMatchesSpaceText() {
        assertTrue("中文 abc".hasProperSpacing())
        assertFalse("中文abc".hasProperSpacing())
        assertTrue(Pangu.hasProperSpacing("中文 abc"))
        assertFalse(Pangu.hasProperSpacing("中文abc"))
        val already = "♫ 每條大街小巷，每個工程師的嘴裡，見面第一句話，就是不要在過年前 Deploy ♫"
        val tight = "♫每條大街小巷，每個工程師的嘴裡，見面第一句話，就是不要在過年前Deploy♫"
        assertTrue(already.hasProperSpacing())
        assertFalse(tight.hasProperSpacing())
        for (sample in listOf("\"字+\"", "\"字|\"", "你好\"字+\"世界", "多行\"字+\"\n下行\"字|\"", "聽說Hadoop工程師睡不著的時候都會MapReduce羊")) {
            val once = sample.spacingText()
            assertEquals(once, once.spacingText())
            assertTrue(once.hasProperSpacing())
        }
    }

    @Test
    fun urlPlaceholdersAlreadyInTheInputStayIntact() {
        val outOfRange = "前文\uE00A99\uE00B后文https://example.com/a"
        val spaced = outOfRange.spacingText()
        assertTrue(spaced.contains("https://example.com/a"))
        assertTrue(spaced.contains("\uE00A99\uE00B"))
        assertEquals("前文\uE00A99\uE00B后文 https://example.com/a", spaced)

        // The private-use run sits between CJK and the URL, so the URL is not a CJK boundary.
        val collision = "看\uE00A0\uE00Bhttps://example.com/path"
        assertEquals(collision, collision.spacingText())
    }
}
