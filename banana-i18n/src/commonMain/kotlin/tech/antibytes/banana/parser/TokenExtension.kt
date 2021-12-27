/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract

internal fun BananaContract.Token.isEOF(): Boolean {
    return this == BananaContract.EOF
}

internal fun BananaContract.Token.isVariable(): Boolean {
    return type == BananaContract.TokenTypes.VARIABLE
}

internal fun BananaContract.Token.isAscii(): Boolean {
    return type == BananaContract.TokenTypes.ASCII_STRING
}

internal fun BananaContract.Token.isSpace(): Boolean {
    return type == BananaContract.TokenTypes.WHITESPACE
}

internal fun BananaContract.Token.isFunctionStart(): Boolean {
    return type == BananaContract.TokenTypes.FUNCTION_START
}

internal fun BananaContract.Token.isFunctionEnd(): Boolean {
    return type == BananaContract.TokenTypes.FUNCTION_END
}

internal fun BananaContract.Token.isFunctionArgumentIndicator(): Boolean {
    return value == ":" &&
        type == BananaContract.TokenTypes.LITERAL
}

internal fun BananaContract.Token.isDelimiter(): Boolean {
    return type == BananaContract.TokenTypes.DELIMITER
}

private val INVALID_LINK_LITERAL = listOf("{", "[", "}", "]")

internal fun BananaContract.Token.isLegalLinkLiteral(): Boolean {
    return type == BananaContract.TokenTypes.LITERAL && !INVALID_LINK_LITERAL.contains(value)
}

internal fun BananaContract.Token.isLinkText(): Boolean {
    return type == BananaContract.TokenTypes.DOUBLE ||
        type == BananaContract.TokenTypes.INTEGER ||
        type == BananaContract.TokenTypes.ASCII_STRING ||
        type == BananaContract.TokenTypes.NON_ASCII_STRING ||
        isLegalLinkLiteral() ||
        type == BananaContract.TokenTypes.ESCAPED
}

internal fun BananaContract.Token.isLinkStart(): Boolean {
    return type == BananaContract.TokenTypes.LINK_START
}

internal fun BananaContract.Token.isLinkEnd(): Boolean {
    return type == BananaContract.TokenTypes.LINK_END
}

internal fun BananaContract.Token.isFreeLinkStart(): Boolean {
    return value == "[" &&
        type == BananaContract.TokenTypes.LITERAL
}

internal fun BananaContract.Token.isFreeLinkEnd(): Boolean {
    return value == "]" &&
        type == BananaContract.TokenTypes.LITERAL
}

internal fun BananaContract.Token.isUrl(): Boolean {
    return type == BananaContract.TokenTypes.URL
}
