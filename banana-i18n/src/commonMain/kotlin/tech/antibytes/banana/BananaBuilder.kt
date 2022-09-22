/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.interpreter.DefaultLinkFormatter
import tech.antibytes.banana.interpreter.DefaultTextInterceptor

class BananaBuilder(
    private var locale: Locale,
) : PublicApi.BananaBuilder {
    private var logger: PublicApi.Logger = DefaultLogger()
    private var textInterceptor: PublicApi.TextInterceptor = DefaultTextInterceptor()
    private var linkFormatter: PublicApi.LinkFormatter = DefaultLinkFormatter()
    private val plugins: MutableSet<PublicApi.Plugin> = mutableSetOf()

    override fun setLanguage(locale: Locale): PublicApi.BananaBuilder {
        return this.also {
            this.locale = locale
        }
    }

    override fun setTextInterceptor(interceptor: PublicApi.TextInterceptor): PublicApi.BananaBuilder {
        return this.also {
            textInterceptor = interceptor
        }
    }

    override fun setLinkFormatter(formatter: PublicApi.LinkFormatter): PublicApi.BananaBuilder {
        return this.also {
            linkFormatter = formatter
        }
    }

    override fun setLogger(logger: PublicApi.Logger): PublicApi.BananaBuilder {
        return this.also {
            this.logger = logger
        }
    }

    override fun registerPlugin(plugin: PublicApi.Plugin): PublicApi.BananaBuilder {
        return this.also {
            plugins.add(plugin)
        }
    }

    private fun initializeInterpreterPlugins(): RegisteredInterpreterPlugins {
        val interpreterPlugins = mutableMapOf<String, PublicApi.CustomInterpreter>()

        plugins.forEach { plugin ->
            interpreterPlugins[plugin.name.uppercase()] = plugin.interpreter.getInstance(logger, locale)
        }

        return interpreterPlugins
    }

    private fun collectParserPlugins(): ParserPluginMap {
        val parserPlugins = mutableMapOf<String, Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>()

        plugins.forEach { plugin ->
            if (plugin.parser != null) {
                parserPlugins[plugin.name.uppercase()] = plugin.parser
            }
        }

        return parserPlugins
    }

    override fun build(): PublicApi.BananaI18n {
        val koin = initKoin(
            logger,
            collectParserPlugins(),
            textInterceptor,
            linkFormatter,
            initializeInterpreterPlugins(),
        )

        return BananaI18n(koin)
    }
}
