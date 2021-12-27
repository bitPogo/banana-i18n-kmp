/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi

internal fun PublicApi.Token.isEOF(): Boolean {
    return this == BananaContract.EOF
}

internal fun PublicApi.Token.isVariable(): Boolean {
    return type == PublicApi.TokenTypes.VARIABLE
}

internal fun PublicApi.Token.isAscii(): Boolean {
    return type == PublicApi.TokenTypes.ASCII_STRING
}

internal fun PublicApi.Token.isSpace(): Boolean {
    return type == PublicApi.TokenTypes.WHITESPACE
}

internal fun PublicApi.Token.isFunctionStart(): Boolean {
    return type == PublicApi.TokenTypes.FUNCTION_START
}

internal fun PublicApi.Token.isFunctionEnd(): Boolean {
    return type == PublicApi.TokenTypes.FUNCTION_END
}

internal fun PublicApi.Token.isFunctionArgumentIndicator(): Boolean {
    return value == ":" &&
        type == PublicApi.TokenTypes.LITERAL
}

internal fun PublicApi.Token.isDelimiter(): Boolean {
    return type == PublicApi.TokenTypes.DELIMITER
}

private val INVALID_LINK_LITERAL = listOf("{", "[", "}", "]")

internal fun PublicApi.Token.isLegalLinkLiteral(): Boolean {
    return type == PublicApi.TokenTypes.LITERAL && !INVALID_LINK_LITERAL.contains(value)
}

internal fun PublicApi.Token.isLinkText(): Boolean {
    return type == PublicApi.TokenTypes.DOUBLE ||
        type == PublicApi.TokenTypes.INTEGER ||
        type == PublicApi.TokenTypes.ASCII_STRING ||
        type == PublicApi.TokenTypes.NON_ASCII_STRING ||
        isLegalLinkLiteral() ||
        type == PublicApi.TokenTypes.ESCAPED
}

internal fun PublicApi.Token.isLinkStart(): Boolean {
    return type == PublicApi.TokenTypes.LINK_START
}

internal fun PublicApi.Token.isLinkEnd(): Boolean {
    return type == PublicApi.TokenTypes.LINK_END
}

internal fun PublicApi.Token.isFreeLinkStart(): Boolean {
    return value == "[" &&
        type == PublicApi.TokenTypes.LITERAL
}

internal fun PublicApi.Token.isFreeLinkEnd(): Boolean {
    return value == "]" &&
        type == PublicApi.TokenTypes.LITERAL
}

internal fun PublicApi.Token.isUrl(): Boolean {
    return type == PublicApi.TokenTypes.URL
}
