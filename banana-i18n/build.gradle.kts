/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.banana.config.BananaCoreConfiguration
import tech.antibytes.gradle.jflex.JFlexTask

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
                implementation(Dependency.multiplatform.kotlin.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)
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

val tokenizerPath = "${projectDir.absolutePath}/src/commonMain/kotlin/tech/antibytes/banana/tokenizer"

tasks.withType(JFlexTask::class.java) {
    flexFile.set(
        File("${projectDir.absolutePath}/flex/BananaTokenizer.flex")
    )
    outputDirectory.set(
        File(tokenizerPath)
    )
    customSkeletonFile.set(
        File("${projectDir.absolutePath}/flex/FlexSkeleton.skel")
    )
}

tasks.create("postProcessTokenizen") {
    val replacements = mapOf(
        "import java.io.IOException\n" +
            "import java.io.Reader\n" +
            "import java.lang.ArrayIndexOutOfBoundsException\n" +
            "import java.lang.Character\n" +
            "import java.lang.Error\n" +
            "import java.lang.System" to "package tech.antibytes.banana.tokenizer\n" +
            "\n" +
            "import tech.antibytes.banana.BananaRuntimeError\n" +
            "import tech.antibytes.banana.tokenizer.TokenizerContract.Reader",
        "IOException" to "Exception",
        "ArrayIndexOutOfBoundsException" to "Exception",
        "java.io.Reader" to "Reader",
        "private var zzReader: Reader" to "private var zzReader: Reader = `in`",
        "\n" +
            "    /* user code: */ /**\n" +
            "     * Creates a new scanner\n" +
            "     *\n" +
            "     * @param   in  the Reader to read input from.\n" +
            "     */\n" +
            "    init {\n" +
            "        zzReader = `in`\n" +
            "    }" to "",
        "val message: String\n" +
            "            message =" to "val message =",
        "var offset = 0\n" +
            "            offset = " to "val offset = 0\n" +
            "            ",
        "].toInt()" to "].code",
        "Illegal token \"\\\${yytext()}\" detected." to "Illegal token \"\${yytext()}\" detected.",
        "when (zzCh) {" to "when (zzCh.toChar()) {",
        "System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.size)" to "zzBuffer.copyInto(destination = newBuffer)",
        "System.arraycopy(\n" +
            "                zzBuffer, zzStartRead,\n" +
            "                zzBuffer, 0,\n" +
            "                zzEndRead - zzStartRead\n" +
            "            )" to "zzBuffer.copyInto(\n" +
            "                destination = zzBuffer,\n" +
            "                destinationOffset = 0,\n" +
            "                startIndex = zzStartRead,\n" +
            "                endIndex = zzEndRead - zzStartRead\n" +
            "            )"
    )

    doLast {
        val transpiledKotlinFile = File("$tokenizerPath/BananaTokenizer.kt")
        var fileContent = transpiledKotlinFile.readText()

        replacements.forEach { (old, new) ->
            fileContent = fileContent.replace(old, new)
        }

        transpiledKotlinFile.writeText(fileContent)
    }
}
