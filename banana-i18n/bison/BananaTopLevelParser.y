// Prolog
%language "Java"
%require "3.8.2"
%define parse.trace
// %define api.push-pull push
%define parse.lac "full"
%define api.parser.abstract

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
|   text message
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


// Epilog
