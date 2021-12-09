/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
package tech.antibytes.banana.tokenizer;

import tech.antibytes.banana.BananaRuntimeError;
import tech.antibytes.banana.BananaContract.Token;
import tech.antibytes.banana.BananaContract.TokenTypes;

%%

%class BananaFlexTokenizer
%unicode
%line
%column
%abstract

%{
    private int offset = 0;

    private Token createToken(TokenTypes tokenType) {
          return new Token(
              tokenType,
              yytext(),
              offset,
              offset + yylength()
          ).also { offset += yylength() };
    }

    private Token createVariableToken() {
          return new Token(
               TokenTypes.VARIABLE,
               yytext().drop(1),
               offset,
               offset + yylength()
          ).also { offset += yylength() };
    }
%}

%xstate RULE
// Pseudo Token
illegal             = [\u0000-\u0008 \u000B \u000C \u000E-\u001F \u007F-\u009F]
integer             = \p{number}+
float               = {integer}? "." {integer}
double              = {float} ( [eE] [-+]? {integer} )? | {integer} [eE] [-+]? {integer}
ascii_letter        = [A-Za-z]
ascii               = {ascii_letter}+
non_ascii_letter    = \p{letter} // Note: actually it should be {letter--a-zA-Z}, however this is not supported by jflex, but given by the order
non_ascii           = {non_ascii_letter}+
escaped             = "\\" [\u0021-\u002F \u003A-\u0040 \u005B-\u0060 \u007B-\u007E]
variable            = "\$" {integer} | "\$" {ascii} ( "_" {ascii} )*
whitespaces         = [\s\t\n\r]+

%%

<YYINITIAL> {
    {whitespaces}       { return createToken(TokenTypes.WHITESPACE); }

    {escaped}           { return createToken(TokenTypes.ESCAPED); }

    {double}            { return createToken(TokenTypes.DOUBLE); }
    {integer}           { return createToken(TokenTypes.INTEGER); }
    {ascii}             { return createToken(TokenTypes.ASCII_STRING); }
    {non_ascii}         { return createToken(TokenTypes.NON_ASCII_STRING); }

    {variable}          { return createVariableToken(); }

    {illegal}           { throw new BananaRuntimeError("Illegal token \"" + yytext() + "\" detected."); }
    .                   { return createToken(TokenTypes.LITERAL); }
}
