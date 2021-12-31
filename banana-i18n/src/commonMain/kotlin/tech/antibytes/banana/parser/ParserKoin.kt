/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi

internal fun resolveParserKoin(): Module {
    return module {
        single<PublicApi.TokenStore> {
            TokenStore(get())
        }

        single<Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>(named(BananaContract.KoinLabel.DEFAULT_ARGUMENT_PARSER)) {
            Pair(
                DefaultArgumentsParser,
                get(named(BananaContract.KoinLabel.COMPOUND_FACTORY))
            )
        }

        single<PublicApi.ParserPluginController> {
            ParserPluginController(
                get(),
                get(named(BananaContract.KoinLabel.DEFAULT_ARGUMENT_PARSER)),
                get(named(BananaContract.KoinLabel.PARSER_PLUGINS)),
            )
        }

        single<BananaContract.TopLevelParser> {
            BananaParser(get(), get())
        }
    }
}
