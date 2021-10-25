package tech.antibytes.gradle.banana.config

object LibraryConfig {
    val publishConfig = PublishConfig
    val android = AndroidLibraryConfig

    const val group = "tech.antibytes"
    const val name = "banana-i18n-kmp"

    const val githubOwner = "bitPogo"
    const val githubRepository = "banana-i18n-kmp"

    object PublishConfig {
        const val groupId = "tech.antibytes.banana-i18n-kmp"
        const val description = "A implementation of MW's banana for Kotlin Multiplatform."

        const val year = "2021"

        // URL
        const val host = "github.com"
        const val path = "$githubOwner/$githubRepository"

        const val url = "https://$host/$path"

        // DEVELOPER
        const val developerId = "bitPogo"
        const val developerName = "bitPogo"
        const val developerEmail = ""

        // LICENSE
        const val licenseName = "LGPL2.1"
        const val licenseUrl = "$url/blob/main/LICENSE"
        const val licenseDistribution = "repo"

        // SCM
        const val scmUrl = "git://$host/$path.git"
        const val scmConnection = "scm:$scmUrl"
        const val scmDeveloperConnection = scmConnection
    }

    object AndroidLibraryConfig {
        const val minSdkVersion = 24
        const val compileSdkVersion = 30
        const val targetSdkVersion = 30

        const val resourcePrefix = "banana_i18n_"
    }
}
