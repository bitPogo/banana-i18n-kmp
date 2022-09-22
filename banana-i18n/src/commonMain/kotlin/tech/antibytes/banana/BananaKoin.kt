/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import tech.antibytes.banana.ast.resolveAstModule
import tech.antibytes.banana.interpreter.resolveInterpreterModule
import tech.antibytes.banana.parser.resolveParserModule
import tech.antibytes.banana.tokenizer.resolveTokenizerModule

internal fun initKoin(
    logger: PublicApi.Logger,
    parsersPlugins: ParserPluginMap,
    textInterceptor: PublicApi.TextInterceptor,
    linkFormatter: PublicApi.LinkFormatter,
    interpreterPlugins: RegisteredInterpreterPlugins,
): KoinApplication {
    return koinApplication {
        modules(
            resolveTokenizerModule(),
            resolveAstModule(),
            resolveParserModule(),
            resolveInterpreterModule(),
            resolveBananaParameterModule(
                logger,
                parsersPlugins,
                textInterceptor,
                linkFormatter,
                interpreterPlugins,
            ),
        )
    }
}
