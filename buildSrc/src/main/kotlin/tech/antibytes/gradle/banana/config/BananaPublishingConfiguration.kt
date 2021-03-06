/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.config

import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.api.VersioningConfiguration

open class BananaPublishingConfiguration {
    private val username = System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME")?.toString() ?: ""
    private val password = System.getenv("PACKAGE_REGISTRY_UPLOAD_TOKEN")?.toString() ?: ""
    private val githubOwner = "bitPogo"
    private val githubRepository = "banana-i18n-kmp"

    private val host = "github.com"
    private val path = "$githubOwner/$githubRepository"

    protected val gitHubRepositoryPath = "$host/$path"
    private val gitHubOwnerPath = "$host/$githubOwner"

    protected val license = LicenseConfiguration(
        name = "GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1",
        url = "https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt",
        distribution = "repo"
    )

    protected val developer = DeveloperConfiguration(
        id = githubOwner,
        name = githubOwner,
        url = "https://$host/$githubOwner",
        email = "solascriptura001+antibytes@gmail.com"
    )

    protected val sourceControl = SourceControlConfiguration(
        url = "git://$gitHubRepositoryPath.git",
        connection = "scm:git://$gitHubRepositoryPath.git",
        developerConnection = "scm:git://$gitHubRepositoryPath.git",
    )

    val repositories = setOf(
        MavenRepositoryConfiguration(
            name = "GitHubPackageRegistry",
            url = "https://maven.pkg.github.com/$path",
            username = username,
            password = password
        ),
        GitRepositoryConfiguration(
            name = "Development",
            gitWorkDirectory = "dev",
            url = "https://$gitHubOwnerPath/maven-dev",
            username = username,
            password = password
        ),
        GitRepositoryConfiguration(
            name = "Snapshot",
            gitWorkDirectory = "snapshots",
            url = "https://$gitHubOwnerPath/maven-snapshots",
            username = username,
            password = password
        ),
        GitRepositoryConfiguration(
            name = "Release",
            gitWorkDirectory = "releases",
            url = "https://$gitHubOwnerPath/maven-releases",
            username = username,
            password = password
        )
    )

    val versioning = VersioningConfiguration(
        featurePrefixes = listOf("core", "plugin")
    )

    companion object {
        private val configuration = BananaPublishingConfiguration()

        val repositories = configuration.repositories
        val versioning = configuration.versioning
    }
}
