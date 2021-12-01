%%

%class BananaTokenizer
%unicode
%line
%column
%apiprivate

%{

%}

%state NO_ASCII
// Pseudo Token
illegal         = [\u0000-\u0008 \u000B \u000C \u000E-\u001F \u007F-\u009F]
integer         = [0-9]+
float           = integer? '.' integer
double          = float ( [eE] [-+]? integer )? | integer [eE] [-+]? integer
delimiter       = '|'
letter          = [A-Za-z]
ascii           = letter+
escaped         = '\\' [\u0021-\u002F \u003A-\u0040 \u005B-\u0060 \u007B-\u007E]
variable        = '\$' integer | '\$' letter+ ( '_' letter* )
ruleclosure     = "}" "}" [^}]
ruleopnening    = "{" "{"
whitespace      = [ \t\n\r]
%%
//
<YYINITIAL> {
    {illegal}   { throw new BananaRuntimeError("Illegal token \"${yytext()}\" detected."); }
    .           { /* Do nothing */ }
}


