/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.interpreter.DefaultLinkFormatter
import tech.antibytes.banana.interpreter.DefaultTextInterceptor

class BananaBuilder : PublicApi.BananaBuilder {
    private var locale: Locale? = null
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

    private fun initializeBanana(): PublicApi.BananaI18n {
        val koin = initKoin(
            logger,
            emptyMap(),
            textInterceptor,
            linkFormatter,
            emptyMap()
        )

        return BananaI18n(koin)
    }

    override fun build(): PublicApi.BananaI18n {
        return if (locale == null) {
            throw BananaRuntimeError("You forgot to set a Language!")
        } else {
            initializeBanana()
        }
    }
}
