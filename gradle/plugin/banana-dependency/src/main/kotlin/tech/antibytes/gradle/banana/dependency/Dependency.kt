package tech.antibytes.gradle.banana.dependency

object Dependency {

    val multiplatform = Multiplatform

    object Multiplatform {
        val kotlin = Kotlin

        object Kotlin {
            const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlin.stdlib}"
            const val jdk = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin.stdlib}"
            const val js = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val native = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
            const val android = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
        }

        val coroutines = Coroutines

        object Coroutines {
            const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.kotlin.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.kotlin.coroutines}"
            const val js = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Version.kotlin.coroutines}"
        }

        val test = Test

        object Test {
            const val common = "org.jetbrains.kotlin:kotlin-test-common:${Version.kotlin.stdlib}"
            const val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Version.kotlin.stdlib}"
            const val jvm = "org.jetbrains.kotlin:kotlin-test:${Version.kotlin.stdlib}"
            const val junit = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin.stdlib}"

            val mockk = Mockk

            object Mockk {
                const val junit = "io.mockk:mockk:${Version.kotlinTest.mockk}"
            }

            val kotest = Kotest

            object Kotest {
                const val common = "io.kotest:kotest-assertions-core:${Version.kotlinTest.kotest}"
                const val commonProperties = "io.kotest:kotest-assertions-core:${Version.kotlinTest.kotest}"
            }
        }
    }

    val jvmTest = JvmTest

    object JvmTest {
        const val junit = "junit:junit:${Version.jvmTest.junit}"
    }

    val android = Android

    object Android {
        // Android
        const val desugar = "com.android.tools:desugar_jdk_libs:${Version.android.desugar}"

        // AndroidX
        const val ktx = "androidx.core:core-ktx:${Version.android.ktx}"
        const val appCompat = "androidx.appcompat:appcompat:${Version.android.appCompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.android.constraintLayout}"

        // Material
        const val material = "com.google.android.material:material:${Version.android.material}"
    }

    val androidTest = AndroidTest

    object AndroidTest {
        const val core = "androidx.test:core:${Version.androidTest.test}"
        const val runner = "androidx.test:runner:${Version.androidTest.test}"
        const val rules = "androidx.test:rules:${Version.androidTest.test}"

        const val junit = "androidx.test.ext:junit:${Version.androidTest.test}"

        const val espressoCore = "androidx.test.espresso:espresso-core:${Version.androidTest.espresso}"
        const val espressoIntents = "androidx.test.espresso:espresso-intents:${Version.androidTest.espresso}"
        const val espressoWeb = "androidx.test.espresso:espresso-web:${Version.androidTest.espresso}"

        const val uiAutomator = "androidx.test.uiautomator:uiautomator:${Version.androidTest.uiAutomator}"

        const val robolectric = "org.robolectric:robolectric:${Version.androidTest.robolectric}"
    }
}
