# pangu.kt

[![badge-version]](https://search.maven.org/search?q=g:io.github.darkokoa%20a:pangu*)
![badge-jvm][badge-jvm]
![badge-js][badge-js]
![badge-nodejs][badge-nodejs]
![badge-android][badge-android]
![badge-ios][badge-ios]
![badge-watchos][badge-watchos]
![badge-tvos][badge-tvos]
![badge-macos][badge-macos]
![badge-windows][badge-windows]
![badge-linux][badge-linux]

Kotlin Multiplatform (Kotlin MPP aka KMP) implementation of [pangu.js](https://github.com/vinta/pangu.js).

## Usage

If your project is a Kotlin multiplatform project, add the dependency in `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.darkokoa:pangu:<version>")
            }
        }
    }
}
```

If your project is a(an) JVM or Android project: 

```groovy
dependencies {
    implementation("io.github.darkokoa:pangu-jvm:<version>")
}
```

## Example

In Kotlin

```kotlin
// use Pangu singleton
val pendingText = "..."
val completedText = Pangu.spacingText(pendingText)

// use kotlin extension function
val pendingText = "..."
val completedText = pendingText.spacingText(pendingText)
```

In Java

```groovy
// use Pangu singleton
String pendingText = "...";
String completedText = Pangu.INSTANCE.spacingText(pendingText);

// use PanguKt static class
String pendingText = "...";
String completedText = PanguKt.spacingText(pendingText);
```

## Matters Needing Attention
1. The processing time also becomes longer when you need to process longer text. Doing so will block the current thread for a long time. If the UI thread is blocked it means that the program may crash.
2. Java support requires Java 8 or above.

## License

Released under the [MIT License](https://opensource.org/licenses/MIT).

[badge-version]: https://img.shields.io/maven-central/v/io.github.darkokoa/pangu?style=flat
[badge-ios]: https://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
[badge-js]: https://img.shields.io/badge/platform-js-F8DB5D.svg?style=flat
[badge-nodejs]: https://img.shields.io/badge/platform-nodejs-68a063.svg?style=flat
[badge-jvm]: https://img.shields.io/badge/platform-jvm-DB413D.svg?style=flat
[badge-android]: https://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-linux]: https://img.shields.io/badge/platform-linux-2D3F6C.svg?style=flat
[badge-windows]: https://img.shields.io/badge/platform-windows-4D76CD.svg?style=flat
[badge-macos]: https://img.shields.io/badge/platform-macos-111111.svg?style=flat
[badge-watchos]: https://img.shields.io/badge/platform-watchos-C0C0C0.svg?style=flat
[badge-tvos]: https://img.shields.io/badge/platform-tvos-808080.svg?style=flat
[badge-wasm]: httpss://img.shields.io/badge/platform-wasm-624FE8.svg?style=flat
