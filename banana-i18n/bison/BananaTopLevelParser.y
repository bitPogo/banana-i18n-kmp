// Prolog
%language "Java"
%require "3.8.2"
%define parse.trace
%define api.push-pull push
%define parse.lac "full"
%define api.parser.abstract

%code {
    fun test() {}
}

%token
    RULE_OPENING        "{{"
    RULE_CLOSURE        "}}"
    DOUBLE
    INTEGER
    ESCAPED
    DELIMITER           "|"
    ASCII_STRING
    NON_ASCII_STRING
    LITERAL
    WHITESPACE
    VARIABLE


// Rules
%%
message:
        text
    |   rule
    |   text message
    |   %empty
    ;

text:
        DOUBLE
    |   INTEGER
    |   ESCAPED
    |   DELIMITER
    |   ASCII_STRING
    |   NON_ASCII_STRING
    |   LITERAL
    |   WHITESPACE
    |   RULE_CLOSURE
    ;

rule:   simpleRule
    |   complexRule
    ;

complexRule: "{{" space ASCII_STRING space arguments space "}}" ;

arguments:  ":" space optionGroup
    |       %empty
    ;

optionGroup:    option space optionGroup
    |           option
    |           %empty
    ;

option: "|" space nestedMessage ;

nestedMessage:  nestedText
    |           rule
    |           nestedText nestedMessage
    ;

nestedText:
        DOUBLE
    |   INTEGER
    |   ESCAPED
    |   ASCII_STRING
    |   NON_ASCII_STRING
    |   LITERAL
    |   WHITESPACE
    ;

simpleRule: VARIABLE ;

space:
        WHITESPACE
    |   %empty
    ;



// Epilog
