/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.banana.config.BananaCoreConfiguration
import tech.antibytes.gradle.jflex.JFlexTask
import tech.antibytes.gradle.jflex.PostConverterTask

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")
    id("tech.antibytes.gradle.publishing")
    id("tech.antibytes.gradle.coverage")
    id("tech.antibytes.gradle.jflex")
}

group = BananaCoreConfiguration.group

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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)
                implementation(Dependency.multiplatform.test.fixture)
                implementation(project(":test-utils"))
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
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.jdk8)
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

tasks.withType(JFlexTask::class.java) {
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

tasks.named("postProcessJFlex", PostConverterTask::class.java) {
    targetFile.set(File("$tokenizerPath/BananaFlexTokenizer.kt"))

    replaceWithString.set(
        listOf(
            "import tech.antibytes.banana.BananaContract\n" +
                "import tech.antibytes.banana.tokenizer.BananaFlexTokenizer\n" +
                "import tech.antibytes.banana.BananaRuntimeError\n" +
                "import java.io.IOException\n" +
                "import java.io.Reader\n" +
                "import java.lang.Character\n" +
                "import java.lang.Error"
            to "import tech.antibytes.banana.BananaRuntimeError\n" +
                "import tech.antibytes.banana.BananaContract",
            "IOException" to "Exception",
            "ArrayIndexOutOfBoundsException" to "Exception",
            "java.io.Reader" to "TokenizerContract.Reader",
            "Yytoken" to "BananaContract.Token",
            "private var zzReader: Reader" to "private var zzReader: TokenizerContract.Reader",
            "fun yyreset(reader: Reader?) {" to "fun yyreset(reader: TokenizerContract.Reader) {",
            "].toInt()" to "].code",
            "when (zzCh) {" to "when (zzCh.toChar()) {",
            "packed.length()" to "packed.length",
            "packed.charAt(i++)" to "packed[i++].code",
            "return String(zzBuffer, zzStartRead, zzMarkedPos-zzStartRead)" to
                "return zzBuffer.concatToString(zzStartRead, zzStartRead + (zzMarkedPos - zzStartRead))",
            "Character.isHighSurrogate(zzBuffer[zzEndRead - 1])" to "zzBuffer[zzEndRead - 1].isHighSurrogate()",
            "System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.size)" to "zzBuffer.copyInto(destination = newBuffer)",
            "    fun" to "    protected fun",
            "String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead)" to "zzBuffer.concatToString(zzStartRead, zzMarkedPos)",
            "zzReader!!" to "zzReader",
            "TokenizerContract.Reader?" to "TokenizerContract.Reader"
        )
    )

    replaceWithRegEx.set(
        listOf(
            "val message: String[ \t\n]+message =".toRegex() to "val message =",
            "var offset = 0([ \t\n]+)offset = ".toRegex() to "val offset = 0$1",
            "System.arraycopy\\(([ \t\n]+)zzBuffer, zzStartRead,[ \t\n]+zzBuffer, 0,[ \t\n]+zzEndRead - zzStartRead\n([ \t\n]+)\\)".toRegex()
            to "zzBuffer.copyInto($1destination = zzBuffer,$1destinationOffset = 0,$1startIndex = zzStartRead,$1endIndex = zzEndRead$2)",
            "\\).also[ \t\n]+run \\{ pushBackOffset = 0 \\}".toRegex() to ").also { pushBackOffset = 0 }",
            "return[ \t\n]+if \\(tokenValue.length == 3\\) \\{".toRegex() to "return if (tokenValue.length == 3) {",
            "internal abstract class BananaFlexTokenizer([\n\t ]+[^\n]+){6}".toRegex() to "internal abstract class BananaFlexTokenizer("
        )
    )

    deleteWithString.set(
        listOf(
            "java.io."
        )
    )

    deleteWithRegEx.set(
        listOf(
            "// source:[a-zA-Z0-9/ \\-.]+\n".toRegex()
        )
    )
}

