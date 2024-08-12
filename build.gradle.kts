import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") version "1.7.10"

    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

plugins.withType<NodeJsRootPlugin> {
    configure<NodeJsRootExtension> {
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


    ios()
    watchos()
    tvos()

    macosX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    mingwX64()

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

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    // Configure all publications.
    publications.withType<MavenPublication> {
        // Publish docs with each artifact.
        artifact(javadocJar)

        // Provide information requited by Maven Central.
        pom {
            name.set(rootProject.name)
            description.set(findProperty("pomDescription") as String)
            url.set(findProperty("pomUrl") as String)

            licenses {
                license {
                    name.set(findProperty("pomLicenseName") as String)
                    url.set(findProperty("pomLicenseUrl") as String)
                }
            }

            scm {
                url.set(findProperty("pomScmUrl") as String)
                connection.set(findProperty("pomScmConnection") as String)
                developerConnection.set(findProperty("pomScmDeveloperConnection") as String)
            }

            developers {
                developer {
                    id.set(findProperty("pomDeveloperId") as String)
                    name.set(findProperty("pomDeveloperName") as String)
                }
            }
        }
    }
}

signing {
    val signFileContents = File("${rootProject.projectDir}/secret/sign").readLines()

    val signingKeyId = signFileContents.getOrNull(0)
    val signingPassword = signFileContents.getOrNull(1)
    val signingKeyBase64 = signFileContents.getOrNull(2)

    if (signingKeyId != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKeyId, signingKeyBase64, signingPassword)
    }

    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            val ossrhContents = File("${rootProject.projectDir}/secret/ossrh").readLines()
            val ossrhUsername = ossrhContents.getOrNull(0)
            val ossrhPassword = ossrhContents.getOrNull(1)
            val ossrhStagingProfileId = ossrhContents.getOrNull(2)

            username.set(ossrhUsername)
            password.set(ossrhPassword)
            stagingProfileId.set(ossrhStagingProfileId)
        }
    }
}
