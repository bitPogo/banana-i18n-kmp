/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.banana.dependency.Dependency as LocalDependency
import tech.antibytes.gradle.banana.config.BananaCoreConfiguration
import tech.antibytes.gradle.grammar.jflex.JFlexTask
import tech.antibytes.gradle.grammar.PostConverterTask
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")
    id("tech.antibytes.gradle.publishing")
    id("tech.antibytes.gradle.coverage")
    id("tech.antibytes.gradle.grammar")
}

group = BananaCoreConfiguration.group

antiBytesCoverage {
    val generatedCommonSources = File(
        "${projectDir.absolutePath}/src-gen"
    ).walkBottomUp().toSet()

    val jvmConfig = JvmJacocoConfiguration.createJvmKmpConfiguration(project)
    configurations["jvm"] = jvmConfig.copy(
        additionalSources = generatedCommonSources
    )
}

antiBytesPublishing{
    packageConfiguration = BananaCoreConfiguration.publishing.packageConfiguration
    repositoryConfiguration = BananaCoreConfiguration.publishing.repositories
    versioning = BananaCoreConfiguration.publishing.versioning
}

kotlin {
    android()

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                kotlin.srcDirs("${projectDir.absolutePath.trimEnd('/')}/src-gen/commonMain/kotlin")

                implementation(Dependency.multiplatform.kotlin.common)
                implementation(Dependency.multiplatform.koin.core) {
                    exclude(
                        "org.jetbrains.kotlin",
                        "kotlin-stdlib-jdk8"
                    )
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)

                implementation(LocalDependency.antibytes.fixture)
                implementation(LocalDependency.antibytes.test)
            }
        }

        val androidMain by getting {
            dependencies {
               implementation(Dependency.multiplatform.kotlin.android)
            }
        }
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest)

                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
                implementation(Dependency.android.test.robolectric)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.jdk8)
                implementation(LocalDependency.jvm.icu)
            }
        }
        val jvmTest by getting {
            dependencies {
                dependsOn(commonTest)

                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }
    }
}

val tokenizerPath = "${projectDir.absolutePath}/src-gen/commonMain/kotlin/tech/antibytes/banana/tokenizer"

val jflex by tasks.creating(JFlexTask::class.java) {
    group = "Code Generation"
    description = "Creates a Java file from the given configuration for the Tokenizer"

    flexFile.set(
        File("${projectDir.absolutePath}/flex/BananaTokenizer.flex")
    )
    outputDirectory.set(
        File(tokenizerPath)
    )
    customSkeletonFile.set(
        File("${projectDir.absolutePath}/flex/KotlinCompatipleSkeleton.skel")
    )
}

val postProcessJFlex by tasks.creating(PostConverterTask::class.java) {
    group = "Code Generation"
    description = "Cleans up the generated and converted Tokenizer"

    targetFile.set(File("$tokenizerPath/BananaFlexTokenizer.kt"))

    replaceWithString.set(
        listOf(
            "import tech.antibytes.banana.BananaContract\n" +
                "import java.io.IOException\n" +
                "import java.io.Reader\n" +
                "import java.lang.Character\n" +
                "import java.lang.Error"
            to "import tech.antibytes.banana.BananaContract\n" +
                "import tech.antibytes.banana.tokenizer.TokenizerError.UnknownState\n" +
                "import tech.antibytes.banana.BananaRuntimeError",
            "IOException" to "BananaRuntimeError",
            "ArrayIndexOutOfBoundsException" to "BananaRuntimeError",
            "java.io.Reader" to "TokenizerContract.Reader",
            "Yytoken" to "BananaContract.Token",
            "private var zzReader: Reader" to "private var zzReader: TokenizerContract.Reader",
            "fun yyreset(reader: Reader?) {" to "fun yyreset(reader: TokenizerContract.Reader) {",
            "].toInt()" to "].code",
            "when (zzCh) {" to "when (zzCh.toChar()) {",
            "packed.length()" to "packed.length",
            "packed.charAt(i++)" to "packed[i++].code",
            "return String(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead)" to
                "return zzBuffer.concatToString(zzStartRead, zzMarkedPos)",
            "Character.isHighSurrogate(zzBuffer[zzEndRead - 1])" to "zzBuffer[zzEndRead - 1].isHighSurrogate()",
            "System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.size)" to "zzBuffer.copyInto(destination = newBuffer)",
            "    fun" to "    protected fun",
            "String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead)" to "zzBuffer.concatToString(zzStartRead, zzMarkedPos)",
            "zzReader!!" to "zzReader",
            "TokenizerContract.Reader?" to "TokenizerContract.Reader",
            "throw Error(message)" to "throw UnknownState(message)",
            "catch (e: BananaRuntimeError)" to "catch (e: Throwable)"
        )
    )

    replaceWithRegEx.set(
        listOf(
            "val message: String[ \t\n]+message =".toRegex() to "val message =",
            "var offset = 0([ \t\n]+)offset = ".toRegex() to "val offset = 0$1",
            "System.arraycopy\\(([ \t\n]+)zzBuffer, zzStartRead,[ \t\n]+zzBuffer, 0,[ \t\n]+zzEndRead - zzStartRead\n([ \t\n]+)\\)".toRegex()
            to "zzBuffer.copyInto($1destination = zzBuffer,$1destinationOffset = 0,$1startIndex = zzStartRead,$1endIndex = zzEndRead$2)",
            "return[ \t\n]+if \\(tokenValue.length > 2\\) \\{".toRegex() to "return if (tokenValue.length > 2) {",
            "internal abstract class BananaFlexTokenizer([\n\t ]+[^\n]+){6}".toRegex() to "internal abstract class BananaFlexTokenizer(",
            "if \\(zzReader != null\\) \\{[ \t\n]+zzReader.close\\(\\)[ \t\n]+\\}".toRegex() to "zzReader.close()",
        )
    )

    deleteWithString.set(
        listOf(
            "java.io."
        )
    )

    deleteWithRegEx.set(
        listOf(
            "// source:[a-zA-Z0-9/ \\-.]+\n".toRegex(),
            "[ \t\n]+run ".toRegex()
        )
    )
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}
