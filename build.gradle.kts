@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.maven.publish)
}

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    android {
        namespace = "dev.darkokoa.pangu"
        compileSdk = 36
        minSdk = 24

        withJava()
        withHostTestBuilder {}.configure {}

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    js {
        nodejs {
            testTask {
                useMocha {
                    timeout = "0"
                }
            }
        }
    }

    wasmJs {
        nodejs()
    }

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()

    tvosArm64()
    tvosSimulatorArm64()

    macosArm64()

    linuxX64()
    linuxArm64()

    mingwX64()

    @OptIn(ExperimentalAbiValidation::class)
    abiValidation()

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)

    if (providers.gradleProperty("signingInMemoryKey").isPresent) {
        signAllPublications()
    }

    coordinates(project.group.toString(), "pangu", project.version.toString())

    pom {
        name.set(rootProject.name)
        description.set(providers.gradleProperty("pomDescription"))
        inceptionYear.set("2024")
        url.set(providers.gradleProperty("pomUrl"))

        licenses {
            license {
                name.set(providers.gradleProperty("pomLicenseName"))
                url.set(providers.gradleProperty("pomLicenseUrl"))
                distribution.set("repo")
            }
        }

        scm {
            url.set(providers.gradleProperty("pomScmUrl"))
            connection.set(providers.gradleProperty("pomScmConnection"))
            developerConnection.set(providers.gradleProperty("pomScmDeveloperConnection"))
        }

        developers {
            developer {
                id.set(providers.gradleProperty("pomDeveloperId"))
                name.set(providers.gradleProperty("pomDeveloperName"))
                url.set(providers.gradleProperty("pomDeveloperUrl"))
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    if (name.startsWith("compileJvm")) {
        options.release.set(8)
    }
}

val hasXcodeBuild = providers.exec {
    commandLine("xcrun", "xcodebuild", "-version")
    isIgnoreExitValue = true
}.result.map { it.exitValue == 0 }

tasks.configureEach {
    val appleTaskNameFragments = listOf("Ios", "ios", "Macos", "macos", "Tvos", "tvos", "Watchos", "watchos")
    val isAppleNativeTestTask =
        appleTaskNameFragments.any { name.contains(it) } &&
            (name.startsWith("linkDebugTest") || name.endsWith("Test") || name.endsWith("TestBinaries"))

    if (isAppleNativeTestTask) {
        onlyIf("requires full Xcode installation") {
            hasXcodeBuild.get()
        }
    }
}
