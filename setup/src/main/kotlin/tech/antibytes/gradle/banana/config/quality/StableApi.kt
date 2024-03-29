/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.banana.config.quality

import tech.antibytes.gradle.quality.api.StableApiConfiguration

object StableApi {
    val api = StableApiConfiguration(
        excludeProjects = excludes,
    )
}
