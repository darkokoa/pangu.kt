package dev.darkokoa.pangu

import kotlin.test.Test

// Active plain-text cases from pangu.js v10.4.1 tests/shared for punctuation, URLs, and slashes.
// it.fails cases are omitted. HTML tags, hashtags, and the /#tag slash lists live in UpstreamHtmlTagHashtagTest.
class UpstreamPunctuationUrlSlashTest {

    @Test
    fun tilde() {
        assertEqualsSpacingText("前面~", "前面~")
        assertEqualsSpacingText("前面~~", "前面~~")
        assertEqualsSpacingText("前面~~~", "前面~~~")
        assertEqualsSpacingText("前面~後面", "前面~ 後面")
        assertEqualsSpacingText("前面~~後面", "前面~~ 後面")
        assertEqualsSpacingText("前面~~~後面", "前面~~~ 後面")
        assertEqualsSpacingText("前面~abc", "前面~ abc")
        assertEqualsSpacingText("前面~123", "前面~ 123")
        assertEqualsSpacingText("前面 ~ 後面", "前面 ~ 後面")
        assertEqualsSpacingText("前面~ 後面", "前面~ 後面")
        assertEqualsSpacingText("前面 ~後面", "前面 ~後面")
        assertEqualsSpacingText("前面~=後面", "前面 ~= 後面")
        assertEqualsSpacingText("前面 ~= 後面", "前面 ~= 後面")
    }

    @Test
    fun exclamationMark() {
        assertEqualsSpacingText("前面!", "前面!")
        assertEqualsSpacingText("前面!!", "前面!!")
        assertEqualsSpacingText("前面!!!", "前面!!!")
        assertEqualsSpacingText("前面!後面", "前面! 後面")
        assertEqualsSpacingText("前面!!後面", "前面!! 後面")
        assertEqualsSpacingText("前面!!!後面", "前面!!! 後面")
        assertEqualsSpacingText("前面!abc", "前面! abc")
        assertEqualsSpacingText("前面!123", "前面! 123")
        assertEqualsSpacingText("前面2!的階乘", "前面 2! 的階乘")
        assertEqualsSpacingText("你還在用Yahoo!奇摩？", "你還在用 Yahoo! 奇摩？")
        assertEqualsSpacingText("! git commit -a -m \"蛤\"", "! git commit -a -m \"蛤\"")
        assertEqualsSpacingText("前面 ! 後面", "前面 ! 後面")
        assertEqualsSpacingText("前面! 後面", "前面! 後面")
    }

    @Test
    fun semicolon() {
        assertEqualsSpacingText("前面;後面", "前面; 後面")
        assertEqualsSpacingText("前面 ; 後面", "前面 ; 後面")
        assertEqualsSpacingText("前面; 後面", "前面; 後面")
    }

    @Test
    fun colon() {
        assertEqualsSpacingText("前面:後面", "前面: 後面")
        assertEqualsSpacingText("電話:123456789", "電話: 123456789")
        assertEqualsSpacingText("前面:I have no idea後面", "前面: I have no idea 後面")
        assertEqualsSpacingText("前面 : 後面", "前面 : 後面")
        assertEqualsSpacingText("前面: 後面", "前面: 後面")
        assertEqualsSpacingText("前面 :後面", "前面 :後面")
        assertEqualsSpacingText("前面: I have no idea後面", "前面: I have no idea 後面")
        // Colon glued to a parenthesis is still converted to full-width.
        assertEqualsSpacingText("前面:)後面", "前面：) 後面")
    }

    @Test
    fun comma() {
        assertEqualsSpacingText("前面,後面", "前面, 後面")
        assertEqualsSpacingText("\"你好\",她說", "\"你好\", 她說")
        assertEqualsSpacingText("每月只要1,000元", "每月只要 1,000 元")
        assertEqualsSpacingText(
            "精采5G購機方案(30個月),月繳599元購機優惠(30個月)",
            "精采 5G 購機方案 (30 個月), 月繳 599 元購機優惠 (30 個月)",
        )
        assertEqualsSpacingText("前面 , 後面", "前面 , 後面")
        assertEqualsSpacingText("前面, 後面", "前面, 後面")
    }

    @Test
    fun period() {
        assertEqualsSpacingText("前面.", "前面.")
        assertEqualsSpacingText("前面..", "前面..")
        assertEqualsSpacingText("前面...", "前面...")
        assertEqualsSpacingText("前面.後面", "前面. 後面")
        assertEqualsSpacingText("前面..後面", "前面.. 後面")
        assertEqualsSpacingText("前面...後面", "前面... 後面")
        assertEqualsSpacingText("前面 . 後面", "前面 . 後面")
        assertEqualsSpacingText("前面. 後面", "前面. 後面")
        assertEqualsSpacingText("前面 .後面", "前面 .後面")
        assertEqualsSpacingText("前面vs.後面", "前面 vs. 後面")
        assertEqualsSpacingText("前面U.S.A.後面", "前面 U.S.A. 後面")
        assertEqualsSpacingText(
            "Mr.龍島主道：「Let's Party!各位高明博雅君子！",
            "Mr. 龍島主道：「Let's Party! 各位高明博雅君子！",
        )
        assertEqualsSpacingText(
            "Mr.龍島主道:「Let's Party!各位高明博雅君子!",
            "Mr. 龍島主道:「Let's Party! 各位高明博雅君子!",
        )
        assertEqualsSpacingText("世.界.，草.班.与千.早.爱.音.", "世. 界.，草. 班. 与千. 早. 爱. 音.")
    }

    @Test
    fun periodAsFileExtensionAndVersion() {
        assertEqualsSpacingText("使用Python.py檔案", "使用 Python.py 檔案")
        assertEqualsSpacingText("設定檔.env很重要", "設定檔.env 很重要")
        assertEqualsSpacingText("編輯器.vscode目錄", "編輯器.vscode 目錄")
        assertEqualsSpacingText("黑人問號.jpg後面", "黑人問號.jpg 後面")
        assertEqualsSpacingText("黑人問號.jpg 後面", "黑人問號.jpg 後面")
        assertEqualsSpacingText("檔案package.lock.json存在", "檔案 package.lock.json 存在")
        assertEqualsSpacingText("環境.env", "環境.env")
        assertEqualsSpacingText("測試.test.js", "測試.test.js")
        assertEqualsSpacingText("專案.gitignore", "專案.gitignore")
        assertEqualsSpacingText("使用環境.env配置", "使用環境.env 配置")
        assertEqualsSpacingText("專案.prettierrc和.eslintrc", "專案.prettierrc 和.eslintrc")
        assertEqualsSpacingText("版本v1.2.3發布了", "版本 v1.2.3 發布了")
        assertEqualsSpacingText("pangu.js v1.2.3橫空出世", "pangu.js v1.2.3 橫空出世")
        assertEqualsSpacingText("pangu.js 1.2.3橫空出世", "pangu.js 1.2.3 橫空出世")
    }

    @Test
    fun questionMark() {
        assertEqualsSpacingText("前面?", "前面?")
        assertEqualsSpacingText("前面??", "前面??")
        assertEqualsSpacingText("前面???", "前面???")
        assertEqualsSpacingText("前面?後面", "前面? 後面")
        assertEqualsSpacingText("前面??後面", "前面?? 後面")
        assertEqualsSpacingText("前面???後面", "前面??? 後面")
        assertEqualsSpacingText("前面?abc", "前面? abc")
        assertEqualsSpacingText("前面?123", "前面? 123")
        assertEqualsSpacingText("所以,請問Jackey的鼻子有幾個?3.14個", "所以, 請問 Jackey 的鼻子有幾個? 3.14 個")
        assertEqualsSpacingText("前面 ? 後面", "前面 ? 後面")
        assertEqualsSpacingText("前面? 後面", "前面? 後面")
    }

    @Test
    fun httpUrls() {
        assertEqualsSpacingText("第三條的內容為http://se.360.cn/", "第三條的內容為 http://se.360.cn/")
        assertEqualsSpacingText("你https://%E5%A6%82", "你 https://%E5%A6%82")
        assertEqualsSpacingText("https://xxxxx/自动加空格.html", "https://xxxxx/自动加空格.html")
        assertEqualsSpacingText(
            "打開此連結，https://www.google.com/search?q=%E5%9B%BD%E5%AF%86SM2%2F3%2F4%E7%AE%97%E6%B3%95+360",
            "打開此連結，https://www.google.com/search?q=%E5%9B%BD%E5%AF%86SM2%2F3%2F4%E7%AE%97%E6%B3%95+360",
        )
        assertEqualsSpacingText(
            "https://www.google.com/search?q=中文&hl=zh-TW",
            "https://www.google.com/search?q=中文&hl=zh-TW",
        )
        assertEqualsSpacingText(
            "https://zh.wikipedia.org/w/index.php?title=中文&action=history",
            "https://zh.wikipedia.org/w/index.php?title=中文&action=history",
        )
        assertEqualsSpacingText(
            "網址是https://zh.wikipedia.org/wiki/%E4%B8%AD%E6%96%87",
            "網址是 https://zh.wikipedia.org/wiki/%E4%B8%AD%E6%96%87",
        )
        assertEqualsSpacingText(
            "https://zh.wikipedia.org/wiki/中文#歷史",
            "https://zh.wikipedia.org/wiki/中文#歷史",
        )
        assertEqualsSpacingText(
            "參考https://zh.wikipedia.org/wiki/中文#歷史的說明",
            "參考 https://zh.wikipedia.org/wiki/中文#歷史的說明",
        )
        assertEqualsSpacingText("https://zh.wikipedia.org/wiki/盤古", "https://zh.wikipedia.org/wiki/盤古")
        assertEqualsSpacingText("請看https://vinta.ws/code/。", "請看 https://vinta.ws/code/。")
        assertEqualsSpacingText("請看https://vinta.ws/code/，謝謝", "請看 https://vinta.ws/code/，謝謝")
        assertEqualsSpacingText("（https://vinta.ws/code/）", "（https://vinta.ws/code/）")
        assertEqualsSpacingText("(https://vinta.ws/code/)", "(https://vinta.ws/code/)")
        assertEqualsSpacingText("「https://vinta.ws/code/」", "「https://vinta.ws/code/」")
        assertEqualsSpacingText("詳見https://vinta.ws/code/.", "詳見 https://vinta.ws/code/.")
        assertEqualsSpacingText(
            "看完這篇#pangu 的介紹 https://vinta.ws/code/",
            "看完這篇 #pangu 的介紹 https://vinta.ws/code/",
        )
        assertEqualsSpacingText(
            "see https://vinta.ws/code/ and 中文",
            "see https://vinta.ws/code/ and 中文",
        )
    }

    @Test
    fun slashAsSeparatorJoinerAndList() {
        assertEqualsSpacingText("前面/後面", "前面/後面")
        assertEqualsSpacingText("Vinta/貓咪", "Vinta/貓咪")
        assertEqualsSpacingText("貓咪/Vinta", "貓咪/Vinta")
        assertEqualsSpacingText("速度是60公里/小時", "速度是 60 公里/小時")
        assertEqualsSpacingText("價格是$100/每小時", "價格是 $100/每小時")
        assertEqualsSpacingText("我/你\n他/她", "我/你\n他/她")
        assertEqualsSpacingText(
            "歡迎光臨/再見\n參考 https://example.com/docs",
            "歡迎光臨/再見\n參考 https://example.com/docs",
        )
        assertEqualsSpacingText("前面 / 後面", "前面 / 後面")
        assertEqualsSpacingText("Vinta / Abc123", "Vinta / Abc123")
        assertEqualsSpacingText("Abc123 / 陳上進", "Abc123 / 陳上進")
        assertEqualsSpacingText("陳上進 / Abc123", "陳上進 / Abc123")
        assertEqualsSpacingText("得到一個 A / B 的結果", "得到一個 A / B 的結果")
        assertEqualsSpacingText("好人 / bad guy", "好人 / bad guy")
        assertEqualsSpacingText("吃apple / banana", "吃 apple / banana")
        assertEqualsSpacingText("Vinta/Abc123", "Vinta/Abc123")
        assertEqualsSpacingText("得到一個A/B的結果", "得到一個 A/B 的結果")
        assertEqualsSpacingText("他要做A/B測試", "他要做 A/B 測試")
        assertEqualsSpacingText("打東東26/30", "打東東 26/30")
        assertEqualsSpacingText("打東東1/denominator", "打東東 1/denominator")
        assertEqualsSpacingText("吃apple/banana", "吃 apple/banana")
        assertEqualsSpacingText("選A/B其中一個", "選 A/B 其中一個")
        assertEqualsSpacingText("答案是6/2的商數", "答案是 6/2 的商數")
        assertEqualsSpacingText("安装指令：npx skills add vinta/hal-9000", "安装指令：npx skills add vinta/hal-9000")
        assertEqualsSpacingText("陳上進/貓咪/Abc123", "陳上進/貓咪/Abc123")
        assertEqualsSpacingText("陳上進/Abc123/貓咪", "陳上進/Abc123/貓咪")
        assertEqualsSpacingText("Abc123/Vinta/貓咪", "Abc123/Vinta/貓咪")
        assertEqualsSpacingText("Abc123/陳上進/貓咪", "Abc123/陳上進/貓咪")
        assertEqualsSpacingText("日期是2024/01/22的早上", "日期是 2024/01/22 的早上")
        assertEqualsSpacingText(
            "after 80'/气象工作者/不苟同/关注abc天气变化/向往123自由/热爱科学、互联网、编程Node.js Web C++ Julia Python",
            "after 80'/气象工作者/不苟同/关注 abc 天气变化/向往 123 自由/热爱科学、互联网、编程 Node.js Web C++ Julia Python",
        )
        assertEqualsSpacingText("陳上進 / 貓咪 / Abc123", "陳上進 / 貓咪 / Abc123")
        assertEqualsSpacingText("陳上進 / Abc123 / 貓咪", "陳上進 / Abc123 / 貓咪")
        assertEqualsSpacingText("Abc123 / Vinta / 貓咪", "Abc123 / Vinta / 貓咪")
        assertEqualsSpacingText("Abc123 / 陳上進 / 貓咪", "Abc123 / 陳上進 / 貓咪")
        assertEqualsSpacingText(
            "2016-12-26(奇幻电影节) / 2017-01-20(美国) / 詹姆斯麦卡沃伊",
            "2016-12-26 (奇幻电影节) / 2017-01-20 (美国) / 詹姆斯麦卡沃伊",
        )
    }

    @Test
    fun unixFilePathsAndGlobs() {
        assertEqualsSpacingText("/home和/root是Linux中的頂級目錄", "/home 和 /root 是 Linux 中的頂級目錄")
        assertEqualsSpacingText("/home/與/root是Linux中的頂級目錄", "/home/ 與 /root 是 Linux 中的頂級目錄")
        assertEqualsSpacingText("\"/home/\"和\"/root\"是Linux中的頂級目錄", "\"/home/\" 和 \"/root\" 是 Linux 中的頂級目錄")
        assertEqualsSpacingText(
            "當你用cat和od指令查看/dev/random和/dev/urandom的內容時",
            "當你用 cat 和 od 指令查看 /dev/random 和 /dev/urandom 的內容時",
        )
        assertEqualsSpacingText(
            "當你用cat和od指令查看\"/dev/random\"和\"/dev/urandom\"的內容時",
            "當你用 cat 和 od 指令查看 \"/dev/random\" 和 \"/dev/urandom\" 的內容時",
        )
        assertEqualsSpacingText("在/home目錄", "在 /home 目錄")
        assertEqualsSpacingText("查看/etc/passwd文件", "查看 /etc/passwd 文件")
        assertEqualsSpacingText("進入/usr/local/bin目錄", "進入 /usr/local/bin 目錄")
        assertEqualsSpacingText("配置檔在/etc/nginx/nginx.conf", "配置檔在 /etc/nginx/nginx.conf")
        assertEqualsSpacingText("隱藏檔案/.bashrc很重要", "隱藏檔案 /.bashrc 很重要")
        assertEqualsSpacingText("查看/home/.config/settings", "查看 /home/.config/settings")
        assertEqualsSpacingText("安裝到/usr/lib/python3.9/", "安裝到 /usr/lib/python3.9/")
        assertEqualsSpacingText("位於/opt/node-v16.14.0/bin", "位於 /opt/node-v16.14.0/bin")
        assertEqualsSpacingText("備份到/mnt/backup.2024-01-01/", "備份到 /mnt/backup.2024-01-01/")
        assertEqualsSpacingText("日誌在/var/log/app-name.log", "日誌在 /var/log/app-name.log")
        assertEqualsSpacingText("模組在/node_modules/@babel/core", "模組在 /node_modules/@babel/core")
        assertEqualsSpacingText("套件在/node_modules/@types/node", "套件在 /node_modules/@types/node")
        assertEqualsSpacingText("編譯器在/usr/bin/g++", "編譯器在 /usr/bin/g++")
        assertEqualsSpacingText(
            "套件在/usr/lib/gcc/x86_64-linux-gnu/11++",
            "套件在 /usr/lib/gcc/x86_64-linux-gnu/11++",
        )
        assertEqualsSpacingText("目錄/usr/bin/包含執行檔", "目錄 /usr/bin/ 包含執行檔")
        assertEqualsSpacingText("資料夾/etc/nginx/存放設定", "資料夾 /etc/nginx/ 存放設定")
        assertEqualsSpacingText("檢查src/main.py文件", "檢查 src/main.py 文件")
        assertEqualsSpacingText("構建dist/index.js完成", "構建 dist/index.js 完成")
        assertEqualsSpacingText("運行test/spec.js測試", "運行 test/spec.js 測試")
        assertEqualsSpacingText("編輯docs/README.md文檔", "編輯 docs/README.md 文檔")
        assertEqualsSpacingText("查看templates/base.html模板", "查看 templates/base.html 模板")
        assertEqualsSpacingText("複製assets/images/logo.png圖片", "複製 assets/images/logo.png 圖片")
        assertEqualsSpacingText("配置config/database.yml設定", "配置 config/database.yml 設定")
        assertEqualsSpacingText("執行scripts/deploy.sh腳本", "執行 scripts/deploy.sh 腳本")
        assertEqualsSpacingText("清理build/temp/目錄", "清理 build/temp/ 目錄")
        assertEqualsSpacingText("輸出到target/release/資料夾", "輸出到 target/release/ 資料夾")
        assertEqualsSpacingText("發布到public/static/路徑", "發布到 public/static/ 路徑")
        assertEqualsSpacingText("安裝node_modules/@babel/core套件", "安裝 node_modules/@babel/core 套件")
        assertEqualsSpacingText("設定.git/hooks/pre-commit鉤子", "設定 .git/hooks/pre-commit 鉤子")
        assertEqualsSpacingText("編輯.vscode/settings.json配置", "編輯 .vscode/settings.json 配置")
        assertEqualsSpacingText("參考./docs/API.md文件", "參考 ./docs/API.md 文件")
        assertEqualsSpacingText("執行./scripts/test.sh腳本", "執行 ./scripts/test.sh 腳本")
        assertEqualsSpacingText("查看./.claude/CLAUDE.md說明", "查看 ./.claude/CLAUDE.md 說明")
        assertEqualsSpacingText("位於src/components/Button/index.tsx", "位於 src/components/Button/index.tsx")
        assertEqualsSpacingText(
            "存放在assets/fonts/Inter/Regular.woff2",
            "存放在 assets/fonts/Inter/Regular.woff2",
        )
        assertEqualsSpacingText("從src/utils.js複製到dist/utils.js", "從 src/utils.js 複製到 dist/utils.js")
        assertEqualsSpacingText(
            "比較test/fixtures/input.txt和test/fixtures/output.txt",
            "比較 test/fixtures/input.txt 和 test/fixtures/output.txt",
        )
        assertEqualsSpacingText("聽說桐島rm -rf /*了", "聽說桐島 rm -rf /* 了")
        assertEqualsSpacingText("模板在templates/*.html裡", "模板在 templates/*.html 裡")
        assertEqualsSpacingText("測試所有test/**/*.js檔案", "測試所有 test/**/*.js 檔案")
    }
}
