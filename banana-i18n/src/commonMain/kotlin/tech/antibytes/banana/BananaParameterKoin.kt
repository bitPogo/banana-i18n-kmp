/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun resolveBananaParameterModule(
    logger: PublicApi.Logger,
    parsersPlugins: ParserPluginMap,
    textInterceptor: PublicApi.TextInterceptor,
    linkFormatter: PublicApi.LinkFormatter,
    interpreterPlugins: RegisteredInterpreterPlugins,
): Module {
    return module {
        single { logger }
        single(named(BananaContract.KoinLabels.PARSER_PLUGINS)) { parsersPlugins }
        single { textInterceptor }
        single { linkFormatter }
        single(named(BananaContract.KoinLabels.INTERPRETER_PLUGINS)) { interpreterPlugins }
    }
}
