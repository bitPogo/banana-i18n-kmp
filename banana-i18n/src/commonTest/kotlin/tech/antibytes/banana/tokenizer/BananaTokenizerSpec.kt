/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
package tech.antibytes.banana.tokenizer

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.BananaContract.Token
import tech.antibytes.banana.BananaContract.TokenTypes
import tech.antibytes.mock.tokenizer.MockReader
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BananaTokenizerSpec {
    @Test
    fun `It fulfils TokenizerFactory`() {
        BananaTokenizer fulfils BananaContract.TokenizerFactory::class
    }

    @Test
    fun `Given getInstance is called with a Reader it returns a Tokenizer`() {
        // Given
        val reader = MockReader()

        // When
        val result = BananaTokenizer.getInstance(reader)

        // Then
        result fulfils BananaContract.Tokenizer::class
    }

    @Test
    fun `Given next is called, it returns EOF, if the input was empty`() {
        // Given
        val input = StringReader("")
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.EOF, "", -1, -1)
    }

    @Test
    fun `Given next is called, it returns EOF, if the input had been consumed`() {
        // Given
        val input = StringReader("a")
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        tokenizer.next()
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.EOF, "", -1, -1)
    }

    @Test
    fun `Given next is called, it fails for illegal chars`() {
        // Given
        val input = StringReader(0x1.toChar().toString())
        val tokenizer = BananaTokenizer.getInstance(input)

        // Then
        val error = assertFailsWith<TokenizerError.IllegalCharacter> {
            // When
            tokenizer.next()
        }

        error.message.startsWith("Illegal token ") mustBe true
    }

    @Test
    fun `Given next is called, it returns Literal Tokens`() {
        // Given
        val value = "!xxx"
        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.LITERAL, "!", 0, 1)
    }

    @Test
    fun `Given next is called, it returns Whitespace Tokens`() {
        val value = " \n\r\t"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.WHITESPACE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns AsciiString Tokens`() {
        val value = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        result mustBe Token(TokenTypes.ASCII_STRING, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns NonAsciiString Tokens`() {
        val value = "ηὕρηκα"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        result mustBe Token(TokenTypes.NON_ASCII_STRING, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Escaped Tokens`() {
        // Given
        val unescaped = "!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"
        val valueBuilder = StringBuilder(unescaped.length * 2)

        unescaped.forEach { char -> valueBuilder.append("\\$char") }

        val input = StringReader(valueBuilder.toString())
        val tokenizer = BananaTokenizer.getInstance(input)

        var idx = 0
        unescaped.forEach { char ->
            // When
            val result = tokenizer.next()

            // Then
            result mustBe Token(TokenTypes.ESCAPED, "\\$char", idx, idx + 2)
            idx += 2
        }
    }

    @Test
    fun `Given next is called, it returns Integer Tokens`() {
        // Given
        val value = "123456789"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.INTEGER, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Integer Tokens for non ascii digitis`() {
        // Given
        val value = "߀߁߂߃߄߅߆߇߈߉"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.INTEGER, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for floating point notation with leading Integer`() {
        val value = "1234.56789"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for floating point notation without leading Integer`() {
        val value = "1234.56789"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for exponetial notation`() {
        val value = "1234e23"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for positive signed exponetial notation for non ascii digitis`() {
        val value = "߀߁߂߃߄߅߆߇߈߉e+߀߁߂߃߄߅߆߇߈߉"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for positive signed exponetial notation`() {
        val value = "1234e+23"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for positive negative exponetial notation`() {
        val value = "1234e-23"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for positive negative exponetial notation with leading Float`() {
        val value = "1234.234e-23"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Double Tokens for positive negative exponetial notation with leading Float, which has no leading Integer`() {
        val value = ".234e-23"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.DOUBLE, value, 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Variable Tokens for numeric variables`() {
        val value = "\$1234567890"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.VARIABLE, value.drop(1), 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Variable Tokens for named variables with short names`() {
        val value = "\$abc"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.VARIABLE, value.drop(1), 0, value.length)
    }

    @Test
    fun `Given next is called, it returns Variable Tokens for named variables with long names`() {
        val value = "\$abc_def"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val result = tokenizer.next()

        // Then
        result mustBe Token(TokenTypes.VARIABLE, value.drop(1), 0, value.length)
    }

    @Test
    fun `Given next is called it returns variable only without trailing _`() {
        val value = "\$abc_"

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        // When
        val token1 = tokenizer.next()
        val token2 = tokenizer.next()

        // Then
        token1 mustBe Token(TokenTypes.VARIABLE, value.drop(1).trimEnd('_'), 0, value.lastIndex)
        token2 mustBe Token(TokenTypes.LITERAL, value.last().toString(), value.lastIndex, value.length)
    }

    @Test
    fun `Given the Tokenizer gets an complex input stream it tokenizes it`() {
        // Given
        val value = "ηὕρηκα! {{ measurement: +١٢٣٤٥٦٧٨٩٠١٢٣٤٥٦٧٨٩ | kilometer }} to walk until becoming a developer."

        val input = StringReader(value)
        val tokenizer = BananaTokenizer.getInstance(input)

        val tokens = mutableListOf<Token>()

        // When
        do {
            tokens.add(tokenizer.next())
        } while (tokens.last() != EOF)

        // Then
        tokens[0] mustBe Token(type = TokenTypes.NON_ASCII_STRING, value = "ηὕρηκα", start = 0, end = 6)
        tokens[1] mustBe Token(type = TokenTypes.LITERAL, "!", start = 6, end = 7)
        tokens[2] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 7, end = 8)
        tokens[3] mustBe Token(type = TokenTypes.LITERAL, "{", start = 8, end = 9)
        tokens[4] mustBe Token(type = TokenTypes.LITERAL, "{", start = 9, end = 10)
        tokens[5] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 10, end = 11)
        tokens[6] mustBe Token(type = TokenTypes.ASCII_STRING, value = "measurement", start = 11, end = 22)
        tokens[7] mustBe Token(type = TokenTypes.LITERAL, value = ":", start = 22, end = 23)
        tokens[8] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 23, end = 24)
        tokens[9] mustBe Token(type = TokenTypes.LITERAL, value = "+", start = 24, end = 25)
        tokens[10] mustBe Token(type = TokenTypes.INTEGER, value = "١٢٣٤٥٦٧٨٩٠١٢٣٤٥٦٧٨٩", start = 25, end = 44)
        tokens[11] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 44, end = 45)
        tokens[12] mustBe Token(type = TokenTypes.LITERAL, value = "|", start = 45, end = 46)
        tokens[13] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 46, end = 47)
        tokens[14] mustBe Token(type = TokenTypes.ASCII_STRING, value = "kilometer", start = 47, end = 56)
        tokens[15] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 56, end = 57)
        tokens[16] mustBe Token(type = TokenTypes.LITERAL, value = "}", start = 57, end = 58)
        tokens[17] mustBe Token(type = TokenTypes.LITERAL, value = "}", start = 58, end = 59)
        tokens[18] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 59, end = 60)
        tokens[19] mustBe Token(type = TokenTypes.ASCII_STRING, value = "to", start = 60, end = 62)
        tokens[20] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 62, end = 63)
        tokens[21] mustBe Token(type = TokenTypes.ASCII_STRING, value = "walk", start = 63, end = 67)
        tokens[22] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 67, end = 68)
        tokens[23] mustBe Token(type = TokenTypes.ASCII_STRING, value = "until", start = 68, end = 73)
        tokens[24] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 73, end = 74)
        tokens[25] mustBe Token(type = TokenTypes.ASCII_STRING, value = "becoming", start = 74, end = 82)
        tokens[26] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 82, end = 83)
        tokens[27] mustBe Token(type = TokenTypes.ASCII_STRING, value = "a", start = 83, end = 84)
        tokens[28] mustBe Token(type = TokenTypes.WHITESPACE, value = " ", start = 84, end = 85)
        tokens[29] mustBe Token(type = TokenTypes.ASCII_STRING, value = "developer", start = 85, end = 94)
        tokens[30] mustBe Token(type = TokenTypes.LITERAL, value = ".", start = 94, end = 95)
        tokens[31] mustBe EOF
    }

    @Test
    fun `Given setReader is called with a Reader, it replaces the current Reader`() {
        // Given
        val reader = MockReader()

        val tokenizer = BananaTokenizer.getInstance(reader)

        // When
        tokenizer.setReader(StringReader(""))

        // Then
        tokenizer.next() mustBe EOF
    }
}
