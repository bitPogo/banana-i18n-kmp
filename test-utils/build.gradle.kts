/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.banana.config.BananaCoreConfiguration
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.configuration")
    id("tech.antibytes.gradle.coverage")
}

group = BananaCoreConfiguration.group

kotlin {
    android()

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependency.multiplatform.kotlin.common)

                implementation(Dependency.multiplatform.test.common)
                implementation(Dependency.multiplatform.test.annotations)
            }
        }
        val commonTest by getting {
            kotlin.srcDir("${projectDir.absolutePath.trimEnd('/')}/src-gen/commonTest/kotlin")

            dependencies {
                implementation(Dependency.multiplatform.test.fixture)
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(Dependency.multiplatform.kotlin.android)

                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
                implementation(Dependency.androidTest.robolectric)
            }
        }
        val androidTest by getting {
            dependencies {
                dependsOn(commonTest)
            }
        }

        val jvmMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation(Dependency.multiplatform.kotlin.jdk8)

                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }
        val jvmTest by getting {
            dependencies {
                dependsOn(commonTest)
            }
        }
    }
}

val templatesPath = "${projectDir}/src/commonTest/resources/template"
val configPath = "${projectDir}/src-gen/commonTest/kotlin/tech/antibytes/util/test/config"

val provideTestConfig: Task by tasks.creating {
    doFirst {
        val templates = File(templatesPath)
        val configs = File(configPath)

        val config = File(templates, "TestConfig.tmpl")
            .readText()
            .replace("PROJECT_DIR", projectDir.toPath().toAbsolutePath().toString())

        if (!configs.exists()) {
            if(!configs.mkdir()) {
                System.err.println("Creation of the configuration directory failed!")
            }
        }
        File(configPath, "TestConfig.kt").writeText(config)
    }
}

tasks.withType(KotlinCompile::class.java) {
    if (this.name.contains("Test")) {
        this.dependsOn(provideTestConfig)
    }
}
