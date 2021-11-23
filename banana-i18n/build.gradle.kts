import tech.antibytes.gradle.dependency.Dependency
import tech.antibytes.gradle.banana.config.BananaCore
import tech.antibytes.gradle.publishing.api.PackageConfiguration

plugins {
    id("org.jetbrains.kotlin.multiplatform")

    // Android
    id("com.android.library")

    id("tech.antibytes.gradle.publishing")
    id("tech.antibytes.gradle.coverage")
}

group = BananaCore.group

antiBytesPublishing{
    packageConfiguration = PackageConfiguration(
        pom = BananaCore.publishing.pom,
        developers = listOf(BananaCore.publishing.developer),
        license = BananaCore.publishing.license,
        scm = BananaCore.publishing.sourceControl
    )
    versioning = BananaCore.publishing.versioning
    registryConfiguration = BananaCore.publishing.registries
}

kotlin {
    android {
        publishLibraryVariants("release")
    }

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
            }
        }

        val androidMain by getting {
            dependencies {
               implementation(Dependency.multiplatform.kotlin.android)
            }
        }
        val androidTest by getting {
            dependencies {
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
                implementation(Dependency.multiplatform.test.jvm)
                implementation(Dependency.multiplatform.test.junit)
            }
        }
    }
}

android {
    compileSdk = BananaCore.android.compileSdkVersion

    defaultConfig {
        minSdk = BananaCore.android.minSdkVersion
        targetSdk = BananaCore.android.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf("clearPackageData" to "true")
        )
    }

    buildTypes {
        getByName("debug") {
            matchingFallbacks.add("release")
        }
    }

    resourcePrefix(BananaCore.android.resourcePrefix)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.setSrcDirs(setOf("src/androidMain/kotlin"))
            res.setSrcDirs(setOf("src/androidMain/res"))
        }

        getByName("test") {
            java.setSrcDirs(setOf("src/androidTest/kotlin"))
            res.setSrcDirs(setOf("src/androidTest/res"))
        }
    }
}
