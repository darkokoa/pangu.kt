plugins {
    kotlin("multiplatform") version "1.7.10"
}

//group = "dev.ryz3n"
//version = "1.0-SNAPSHOT"

plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        nodeVersion = "16.13.1"
    }
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChrome()
                    useFirefox()
                }
            }
        }
        nodejs {
            testTask {
                useMocha {
                    // Disable test case timeout, bringing parity with other platforms.
                    timeout = "0"
                }
            }
        }
    }

//    ios()
//    watchos()
//    tvos()
//
//    mingwX64()
//    macosX64()
//    macosArm64()
//    linuxX64()
//    linuxArm64()

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(npm("karma-detect-browsers", "^2.0"))
            }
        }
    }
}
