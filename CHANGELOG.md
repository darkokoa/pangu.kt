# Changelog

## 1.0.0

Breaking plain-text spacing changes since 0.2.0. `spacingText` now follows pangu.js 10.4.1 for the rules below. Bump callers that depended on 0.2.0 output before upgrading.

### Half-width punctuation, URLs, and slashes

- `~ ! ; : , . ?` are no longer converted to full-width. A trailing space is inserted only when CJK, a letter, or a digit follows. `前面!後面` becomes `前面! 後面`, and a trailing `前面?` stays `前面?`.
- The one remaining full-width conversion is a colon glued to a parenthesis: `前面:)後面` becomes `前面：) 後面`. `~=` stays one token.
- `http://` and `https://` URLs are masked before spacing, so percent-encoding, slashes, and CJK inside the URL are preserved. A space is added between CJK and the URL.
- `/` is no longer an operator. `前面/後面` stays `前面/後面`. The old rule that deleted the space after a slash is gone. `./`, `.git`, and `.vscode` paths are spaced as one unit.

### Affixes, quotes, and symbols

- `+`, `-`, and `|` are read per line. Direct CJK contact makes undecided `+` and `|` into separators (`陳上進 + Vinta + Abc123`, `Abc123 | Vinta | 貓咪`). A line with no such contact keeps joiners (`A+B`, `x|y`).
- Affixes stay attached: `+886`, `18+`, `3.5+`, single-letter grades `A+` and `D-`, and a single-letter flag `-m`. `C++` stays intact. A hyphen between brackets is a separator only on a line that already has a hyphen touching CJK.
- A lone tight interpunct (`·`, `•`, `‧`) becomes `・`. Spaced interpuncts and runs such as `••••` stay as written.
- Dingbats use the real block U+2700–U+27BF. Letterlike symbols (U+2100–U+214F, including ℃) are spaced. The old character class used an em dash (`—`, U+2014) in place of a hyphen, so U+2014 was treated as a symbol; it no longer is, and `他說——不對` stays tight.
- Superscripts, `™`, `℠`, and `®` stay attached on the left and take a space before following CJK. `©` takes a space before a year.
- Backtick contents are left untouched. Quote pairs can span a newline. A straight single quote around only CJK stays tight. `object.method(` does not gain a space before `(`.

### API

- `Pangu.hasProperSpacing` and `String.hasProperSpacing()` report whether `spacingText` would change the text. Both are callable from Java (`Pangu.INSTANCE.hasProperSpacing`, `PanguKt.hasProperSpacing`).
- Dotted calls (`addEventListener(`) and misused `”...”` pairs are fixed-length lookbehinds. Patterns stay in the regex subset shared by Java 8, Android ICU, JS, and Kotlin/Native.

### HTML tags and hashtags

- A tag with an ASCII name and a closing `>` is masked before spacing and restored afterwards. Text between tags is spaced. Double-quoted attribute values are spaced (`<input value="測試123">` becomes `<input value="測試 123">`). The tag name and the attribute syntax stay as written. This is a text scan, not a DOM walk.
- A void tag (`<br>`, `<hr>`, `<img>`, and the rest of the void set) stays tight against neighboring text, including `<br />`. A bare non-void tag with no matching closer (`<div>`, `<String>`, `<Spinner />`) is a mention: `在這裡插入一個<div>標籤` becomes `在這裡插入一個 <div> 標籤`. `<br/>` with no space before the slash is not a tag.
- `<` and `>` that are not a tag stay brackets, so `前面<中文123漢字>後面` is still `前面 <中文 123 漢字> 後面` and an HTML comment keeps its brackets.
- `#` treats a non-breaking space as a gap, so `台北\u00a0#中文` and `中文#\u00a0abc` stay as written. `/#tag` in a slash list stays tight. `C#` is unchanged.

### Not in this release

Product-name and rating suffix lists (`Disney+`, `公視+`, `AB+`), DOM traversal, and the CLI are unchanged from 0.2.0: they are still absent.
