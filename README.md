# pangu.kt

[![badge-version]](https://search.maven.org/search?q=g:io.github.darkokoa%20a:pangu*)
![badge-jvm][badge-jvm]
![badge-android][badge-android]
![badge-js][badge-js]
![badge-wasm][badge-wasm]
![badge-ios][badge-ios]
![badge-watchos][badge-watchos]
![badge-tvos][badge-tvos]
![badge-macos][badge-macos]
![badge-windows][badge-windows]
![badge-linux][badge-linux]

Kotlin Multiplatform implementation of [pangu.js](https://github.com/vinta/pangu.js).

`pangu.kt` inserts spacing between CJK characters and Latin letters, numbers, or symbols.

## Install

For Kotlin Multiplatform projects:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("io.github.darkokoa:pangu:<version>")
            }
        }
    }
}
```

For JVM-only projects:

```kotlin
dependencies {
    implementation("io.github.darkokoa:pangu-jvm:<version>")
}
```

For Android-only projects:

```kotlin
dependencies {
    implementation("io.github.darkokoa:pangu-android:<version>")
}
```

Android KMP consumers can also depend on the root artifact from common source sets.

## Usage

Kotlin:

```kotlin
import dev.darkokoa.pangu.Pangu
import dev.darkokoa.pangu.spacingText

val pendingText = "中文abc"
val completedText = Pangu.spacingText(pendingText)

val completedWithExtension = pendingText.spacingText()
```

Java:

```java
import dev.darkokoa.pangu.Pangu;
import dev.darkokoa.pangu.PanguKt;

String pendingText = "中文abc";
String completedText = Pangu.INSTANCE.spacingText(pendingText);

String completedWithStaticFunction = PanguKt.spacingText(pendingText);
```

## Targets

The modern KMP build publishes:

- `jvm`
- `android`
- `js`
- `wasmJs`
- `iosArm64`, `iosSimulatorArm64`, `iosX64`
- `watchosArm32`, `watchosArm64`, `watchosDeviceArm64`, `watchosSimulatorArm64`
- `tvosArm64`, `tvosSimulatorArm64`
- `macosArm64`
- `linuxX64`, `linuxArm64`
- `mingwX64`

Deprecated Kotlin/Native targets `macosX64`, `watchosX64`, and `tvosX64` are no longer published.

## Notes

Processing time grows with input size. Very large text should be processed off the UI thread.

Java support requires Java 8 bytecode compatibility or newer.

## License

Released under the [MIT License](LICENSE).

[badge-version]: https://img.shields.io/maven-central/v/io.github.darkokoa/pangu?style=flat
[badge-ios]: https://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-js]: https://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat
[badge-jvm]: https://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat
[badge-android]: https://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/platform-wasm-624FE8.svg?style=flat
[badge-linux]: https://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat
[badge-windows]: https://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat
[badge-macos]: https://img.shields.io/badge/platform-macos-111111.svg?style=flat
[badge-watchos]: https://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat
[badge-tvos]: https://img.shields.io/badge/platform-tvos-808080.svg?style=flat
