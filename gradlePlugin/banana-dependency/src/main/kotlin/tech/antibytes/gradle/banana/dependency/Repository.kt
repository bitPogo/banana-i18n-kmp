/*
 * Copyright (c) 2021 D4L data4life gGmbH / All rights reserved.
 *
 * D4L owns all legal rights, title and interest in and to the Software Development Kit ("SDK"),
 * including any intellectual property rights that subsist in the SDK.
 *
 * The SDK and its documentation may be accessed and used for viewing/review purposes only.
 * Any usage of the SDK for other purposes, including usage for the development of
 * applications/third-party applications shall require the conclusion of a license agreement
 * between you and D4L.
 *
 * If you are interested in licensing the SDK for your own applications/third-party
 * applications and/or if you’d like to contribute to the development of the SDK, please
 * contact D4L by email to help@data4life.care.
 */
package tech.antibytes.gradle.banana.dependency

import org.gradle.api.artifacts.dsl.RepositoryHandler

data class Credentials(
    val username: String,
    val password: String
)

data class CustomRepository(
    val url: String,
    val groupIds: List<String>,
    val credentials: Credentials? = null
)

val githubGroups = listOf(
    "tech.antibytes.gradle-plugins"
)

val repositories = listOf(
    CustomRepository(
        "https://raw.github.com/bitPogo/maven-dev/main/dev",
        githubGroups
    ),
    CustomRepository(
        "https://raw.github.com/bitPogo/maven-snapshots/main/snapshots",
        githubGroups
    ),
    CustomRepository(
        "https://raw.github.com/bitPogo/maven-releases/main/releases",
        githubGroups
    ),
)

fun RepositoryHandler.addCustomRepositories() {
    repositories.forEach { (url, groups, credentials) ->
        maven {
            setUrl(url)
            if (credentials is Credentials) {
                credentials {
                    username = credentials.username
                    password = credentials.password
                }
            }
            content {
                groups.forEach { group -> includeGroup(group) }
            }
        }
    }
}
