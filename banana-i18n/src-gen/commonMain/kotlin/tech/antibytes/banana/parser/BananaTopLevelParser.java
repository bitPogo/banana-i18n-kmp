/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Skeleton implementation for Bison LALR(1) parsers in Kotlin

   Copyright (C) 2007-2015, 2018-2021 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */




import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * A Bison parser, automatically generated from <tt>/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/bison/BananaTopLevelParser.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
abstract class YYParser
{
      /** Version number for the Bison executable that generated this parser.  */
  public static final String bisonVersion = "3.8.2";
  /** Name of the skeleton that generated this parser.  */
  public static final String bisonSkeleton = "/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/bison/skeleton/kotlin-skel.m4";




    

  public enum SymbolKind
  {
    S_YYEOF(0),                    /* "end of file"  */
    S_YYerror(1),                  /* error  */
    S_YYUNDEF(2),                  /* "invalid token"  */
    S_RULE_OPENING(3),             /* "{{"  */
    S_RULE_CLOSURE(4),             /* "}}"  */
    S_DOUBLE(5),                   /* DOUBLE  */
    S_INTEGER(6),                  /* INTEGER  */
    S_ESCAPED(7),                  /* ESCAPED  */
    S_DELIMITER(8),                /* "|"  */
    S_ASCII_STRING(9),             /* ASCII_STRING  */
    S_NON_ASCII_STRING(10),        /* NON_ASCII_STRING  */
    S_LITERAL(11),                 /* LITERAL  */
    S_WHITESPACE(12),              /* WHITESPACE  */
    S_VARIABLE(13),                /* VARIABLE  */
    S_YYACCEPT(14),                /* $accept  */
    S_message(15),                 /* message  */
    S_text(16);                    /* text  */

    private final int yycode_;
    SymbolKind (int n) {
      this.yycode_ = n;
    }
    private static final SymbolKind[] values_ = {
      SymbolKind.S_YYEOF,
      SymbolKind.S_YYerror,
      SymbolKind.S_YYUNDEF,
      SymbolKind.S_RULE_OPENING,
      SymbolKind.S_RULE_CLOSURE,
      SymbolKind.S_DOUBLE,
      SymbolKind.S_INTEGER,
      SymbolKind.S_ESCAPED,
      SymbolKind.S_DELIMITER,
      SymbolKind.S_ASCII_STRING,
      SymbolKind.S_NON_ASCII_STRING,
      SymbolKind.S_LITERAL,
      SymbolKind.S_WHITESPACE,
      SymbolKind.S_VARIABLE,
      SymbolKind.S_YYACCEPT,
      SymbolKind.S_message,
      SymbolKind.S_text
    };
    static final SymbolKind get(int code) {
      return values_[code];
    }
    public final int getCode() {
      return this.yycode_;
    }
    /* Return YYSTR after stripping away unnecessary quotes and
       backslashes, so that it's suitable for yyerror.  The heuristic is
       that double-quoting is unnecessary unless the string contains an
       apostrophe, a comma, or backslash (other than backslash-backslash).
       YYSTR is taken from yytname.  */
    private static String yytnamerr_(String yystr)
    {
      if (yystr.charAt (0) == '"')
        {
          StringBuffer yyr = new StringBuffer();
          strip_quotes: for (int i = 1; i < yystr.length(); i++)
          {
            switch (yystr.charAt(i))
            {
              case '\'':
              case ',':
                break strip_quotes;
              case '\\':
                if (yystr.charAt(++i) != '\\')
                  break strip_quotes;
                /* Fall through.  */
              default:
                yyr.append(yystr.charAt(i));
                break;
              case '"':
                return yyr.toString();
            }
          }
        }
      return yystr;
    }
    /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
       First, the terminals, then, starting at \a YYNTOKENS_, nonterminals.  */
    private static final String[] yytname_ = yytname_init();
  private static final String[] yytname_init()
  {
    return new String[]
    {
  "\"end of file\"", "error", "\"invalid token\"", "\"{{\"", "\"}}\"",
  "DOUBLE", "INTEGER", "ESCAPED", "\"|\"", "ASCII_STRING",
  "NON_ASCII_STRING", "LITERAL", "WHITESPACE", "VARIABLE", "$accept",
  "message", "text", null
    };
  }
    /* The user-facing name of this symbol.  */
    public final String getName() {
      return yytnamerr_(yytname_[yycode_]);
    }

  };


    /**
     * Communication interface between the scanner and the Bison-generated
     * parser <tt>YYParser</tt>.
     */
    public interface Lexer {
        /* Token kinds.  */
    /** Token "end of file", to be returned by the scanner.  */
    static final int YYEOF = 0;
    /** Token error, to be returned by the scanner.  */
    static final int YYerror = 256;
    /** Token "invalid token", to be returned by the scanner.  */
    static final int YYUNDEF = 257;
    /** Token "{{", to be returned by the scanner.  */
    static final int RULE_OPENING = 258;
    /** Token "}}", to be returned by the scanner.  */
    static final int RULE_CLOSURE = 259;
    /** Token DOUBLE, to be returned by the scanner.  */
    static final int DOUBLE = 260;
    /** Token INTEGER, to be returned by the scanner.  */
    static final int INTEGER = 261;
    /** Token ESCAPED, to be returned by the scanner.  */
    static final int ESCAPED = 262;
    /** Token "|", to be returned by the scanner.  */
    static final int DELIMITER = 263;
    /** Token ASCII_STRING, to be returned by the scanner.  */
    static final int ASCII_STRING = 264;
    /** Token NON_ASCII_STRING, to be returned by the scanner.  */
    static final int NON_ASCII_STRING = 265;
    /** Token LITERAL, to be returned by the scanner.  */
    static final int LITERAL = 266;
    /** Token WHITESPACE, to be returned by the scanner.  */
    static final int WHITESPACE = 267;
    /** Token VARIABLE, to be returned by the scanner.  */
    static final int VARIABLE = 268;

    /** Deprecated, use YYEOF instead.  */
    public static final int EOF = YYEOF;
    

    /**
     * Method to retrieve the semantic value of the last scanned token.
     * @return the semantic value of the last scanned token.
     */
    Object getLVal();

    /**
     * Entry point for the scanner.  Returns the token identifier corresponding
     * to the next token and prepares to return the semantic value
     * of the token.
     * @return the token identifier corresponding to the next token.
     */
    int yylex() throws java.io.IOException;
    
    /**
     * Emit an errorin a user-defined way.
     *
     *
     * @param msg The string for the error message.
     */
    void yyerror(String msg);

    
}

    
    /**
     * The object doing lexical analysis for us.
     */
    private Lexer yylexer;

    



    /**
     * Instantiates the Bison-generated parser.
     * @param yylexer The scanner that will supply tokens to the parser.
     */
    public YYParser(Lexer yylexer)
    {
        
        this.yylacStack = new ArrayList<Integer>();
        this.yylacEstablished = false;
        this.yylexer = yylexer;
        
    }

    
    private java.io.PrintStream yyDebugStream = System.err;

    /**
     * The <tt>PrintStream</tt> on which the debugging output is printed.
     */
    public final java.io.PrintStream getDebugStream() { return yyDebugStream; }

    /**
     * Set the <tt>PrintStream</tt> on which the debug output is printed.
     * @param s The stream that is used for debugging output.
     */
    public final void setDebugStream(java.io.PrintStream s) { yyDebugStream = s; }

    private int yydebug = 0;

    /**
     * Answer the verbosity of the debugging output; 0 means that all kinds of
     * output from the parser are suppressed.
     */
    public final int getDebugLevel() { return yydebug; }

    /**
     * Set the verbosity of the debugging output; 0 means that all kinds of
     * output from the parser are suppressed.
     * @param level The verbosity level for debugging output.
     */
    public final void setDebugLevel(int level) { yydebug = level; }
    

    private int yynerrs = 0;

    /**
     * The number of syntax errors so far.
     */
    public final int getNumberOfErrors() { return yynerrs; }

    /**
     * Print an error message via the lexer.
     *
     * @param msg The error message.
     */
    public final void yyerror(String msg) {
        yylexer.yyerror(msg);
    }
    
    
    protected final void yycdebugNnl(String s) {
        if (0 < yydebug) {
            yyDebugStream.print(s);
        }
    }

        protected final void yycdebug(String s) {
        if (0 < yydebug) {
            yyDebugStream.println(s);
        }
    }

    private final class YYStack {
        private int[] stateStack = new int[16];
        private Object[] valueStack = new Object[16];

        public int size = 16;
        public int height = -1;

        public final void push(int state, Object value) {
            height++;
            if (size == height) {
                int[] newStateStack = new int[size * 2];
                System.arraycopy(stateStack, 0, newStateStack, 0, height);
                stateStack = newStateStack;

                Object[] newValueStack = new Object[size * 2];
                System.arraycopy(valueStack, 0, newValueStack, 0, height);
                valueStack = newValueStack;

                size *= 2;
            }

            stateStack[height] = state;
            valueStack[height] = value;
        }

        public final void pop() {
            pop(1);
        }

        public final void pop(int num) {
            // TODO Replace with valueStack.fill
            // Avoid memory leaks... garbage collection is a white lie!
            if (num > 0) {
                java.util.Arrays.fill(valueStack, height - num + 1, height + 1, null);
            }
            height -= num;
        }

        public final int stateAt(int i) {
            return stateStack[height - i];
        }
        
        public final Object valueAt(int i) {
            return valueStack[height - i];
        }

        // TODO Replace with a PrintStream interface
        // Print the state stack on the debug stream.
        public void print(java.io.PrintStream out) {
            out.print ("Stack now");

            for (int i = 0; i <= height; i++) {
                out.print(' ');
                out.print(stateStack[i]);
            }
            out.println();
        }
    }

        /**
         * Returned by a Bison action in order to stop the parsing process and
         * return success (<tt>true</tt>).
         */
        public static final int YYACCEPT = 0;

    /**
     * Returned by a Bison action in order to stop the parsing process and
     * return failure (<tt>false</tt>).
     */
    public static final int YYABORT = 1;

    

    /**
     * Returned by a Bison action in order to start error recovery without
     * printing an error message.
     */
    public static final int YYERROR = 2;

    /**
     * Internal return codes that are not supported for user semantic
     * actions.
     */
    private static final int YYERRLAB = 3;
    private static final int YYNEWSTATE = 4;
    private static final int YYDEFAULT = 5;
    private static final int YYREDUCE = 6;
    private static final int YYERRLAB1 = 7;
    private static final int YYRETURN = 8;
    

    private int yyerrstatus_ = 0;

    
    /**
     * Whether error recovery is being done.  In this state, the parser
     * reads token until it reaches a known state, and then restarts normal
     * operation.
     */
    public final boolean recovering ()
    {
        return yyerrstatus_ == 0;
    }

        /** Compute post-reduction state.
         * @param yystate   the current state
         * @param yysym     the nonterminal to push on the stack
         */
        private int yyLRGotoState(int yystate, int yysym) {
    int yyr = yypgoto_[yysym - YYNTOKENS_] + yystate;
    if (0 <= yyr && yyr <= YYLAST_ && yycheck_[yyr] == yystate) {
        return yytable_[yyr];
    } else {
        return yydefgoto_[yysym - YYNTOKENS_];
    }
}

    private int yyaction(int yyn, YYStack yystack, int yylen)
    {
        /* If YYLEN is nonzero, implement the default value of the action:
           '$$ = $1'.  Otherwise, use the top of the stack.

           Otherwise, the following line sets YYVAL to garbage.
           This behavior is undocumented and Bison
           users should not rely upon it.  */
        Object yyval;
        if (0 < yylen) {
            yyval = yystack.valueAt(yylen - 1);
        } else {
            yyval = yystack.valueAt(0);
        }

        yyReducePrint(yyn, yystack);

        // TODO CHECK if it can be removed right away if only default condition is present
        switch (yyn)
        {
            
/* "/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/src-gen/commonMain/kotlin/tech/antibytes/banana/parser/BananaTopLevelParser.java":446  */

            default: break;
        }

        yySymbolPrint("-> $$ =", SymbolKind.get(yyr1_[yyn]), yyval);

        yystack.pop(yylen);
        yylen = 0;
        /* Shift the result of the reduction.  */
        int yystate = yyLRGotoState(yystack.stateAt(0), yyr1_[yyn]);
        yystack.push(yystate, yyval);
        return YYNEWSTATE;
    }

    
    /*--------------------------------.
    | Print this symbol on YYOUTPUT.  |
    `--------------------------------*/

    private void yySymbolPrint(String s, SymbolKind yykind,
Object yyvalue) {
    // TODO Replace with builder
    if (yydebug > 0) {
        yycdebug(s
            + (yykind.getCode() < YYNTOKENS_ ? " token " : " nterm ")
        + yykind.getName() + " ("
        + (yyvalue == null ? "(null)" : yyvalue.toString()) + ")");
    }
}

    
    /**
     * Parse input from the scanner that was specified at object construction
     * time.  Return whether the end of the input was reached successfully.
     *
     * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
     *          imply that there were no syntax errors.
     */
    public boolean parse() throws java.io.IOException

    {
        
        
/* Lookahead token kind.  */
int yychar = YYEMPTY_;
/* Lookahead symbol kind.  */
SymbolKind yytoken = null;

/* State.  */
int yyn = 0;
int yylen = 0;
int yystate = 0;
YYStack yystack = new YYStack ();
int label = YYNEWSTATE;



/* Semantic value of the lookahead.  */
Object yylval = null;

        
            // Discard the LAC context in case there still is one left from a
            // previous invocation.
            yylacDiscard("init");
        
            yycdebug ("Starting parse");
        yyerrstatus_ = 0;
        yynerrs = 0;

        /* Initialize the stack.  */
        yystack.push (yystate, yylval);
        
        
        
        for (;;)
            labelSwitch: switch (label)
            {
                /* New state.  Unlike in the C/C++ skeletons, the state is already
                   pushed when we come here.  */
                case YYNEWSTATE:
                yycdebug ("Entering state " + yystate);
                if (0 < yydebug) {
                    yystack.print(yyDebugStream);
                }

                /* Accept?  */
                if (yystate == YYFINAL_) {
                    return true;
                }

                /* Take a decision.  First try without lookahead.  */
                yyn = yypact_[yystate];
                if (yyPactValueIsDefault (yyn))
                {
                    label = YYDEFAULT;
                    break labelSwitch;
                }
                
                /* Read a lookahead token.  */
                if (yychar == YYEMPTY_)
                {
                    
                    yycdebug ("Reading a token");
                    yychar = yylexer.yylex ();
                    yylval = yylexer.getLVal();
                    
                }

                    /* Convert token to internal form.  */
                    yytoken = yytranslate_ (yychar);
                yySymbolPrint("Next token is", yytoken,
                    yylval);

                if (yytoken == SymbolKind.S_YYerror)
                {
                    // The scanner already issued an error message, process directly
                    // to error recovery.  But do not keep the error token as
                    // lookahead, it is too special and may lead us to an endless
                    // loop in error recovery. */
                    yychar = Lexer.YYUNDEF;
                    yytoken = SymbolKind.S_YYUNDEF;
                    label = YYERRLAB1;
                }
                else
                {
                    /* If the proper action on seeing token YYTOKEN is to reduce or to
                       detect an error, take that action.  */
                    yyn += yytoken.getCode();
                    if (yyn < 0 || YYLAST_ < yyn || yycheck_[yyn] != yytoken.getCode()) {
                        if (!yylacEstablish(yystack, yytoken)) {
                            label = YYERRLAB;
                        } else
                            label = YYDEFAULT;
                    } else if (yytable_[yyn] <= 0) { /* <= 0 means reduce or error.  */
                        yyn = yytable_[yyn];

                        if (yyTableValueIsError(yyn)) {
                            label = YYERRLAB;
                        } else if (!yylacEstablish(yystack, yytoken)) {
                            label = YYERRLAB;
                        } else {
                            yyn = -yyn;
                            label = YYREDUCE;
                        }
                    } else {
                        /* Shift the lookahead token.  */
                        yySymbolPrint("Shifting", yytoken,
                            yylval);
                        
                        /* Discard the token being shifted.  */
                        yychar = YYEMPTY_;

                        /* Count tokens shifted since error; after three, turn off error
                           status.  */
                        if (yyerrstatus_ > 0) {
                            --yyerrstatus_;
                        }

                        yystate = yyn;
                        yystack.push(yystate, yylval);
                        yylacDiscard("shift");
                        label = YYNEWSTATE;
                    }
                }
                break labelSwitch;

                /*-----------------------------------------------------------.
                | yydefault -- do the default action for the current state.  |
                `-----------------------------------------------------------*/
                case YYDEFAULT:
                yyn = yydefact_[yystate];
                if (yyn == 0) {
                    label = YYERRLAB;
                } else {
                    label = YYREDUCE;
                }
                break labelSwitch;

                /*-----------------------------.
                | yyreduce -- Do a reduction.  |
                `-----------------------------*/
                case YYREDUCE:
                yylen = yyr2_[yyn];
                label = yyaction(yyn, yystack, yylen);
                yystate = yystack.stateAt(0);
                break labelSwitch;

                /*------------------------------------.
                | yyerrlab -- here on detecting error |
                `------------------------------------*/
                case YYERRLAB:
                /* If not already recovering from an error, report this error.  */
                if (yyerrstatus_ == 0)
                {
                    ++yynerrs;
                    if (yychar == YYEMPTY_) {
                        yytoken = null;
                    }
                    yyreportSyntaxError(new Context(this, yystack, yytoken));
                }
                
                if (yyerrstatus_ == 3)
                {
                    /* If just tried and failed to reuse lookahead token after an
                       error, discard it.  */

                    if (yychar <= Lexer.YYEOF) {
                        /* Return failure if at end of input.  */
                        if (yychar == Lexer.YYEOF) {
                            return false;
                        }
                    } else {
                        yychar = YYEMPTY_;
                    }
                }

                /* Else will try to reuse lookahead token after shifting the error
                   token.  */
                label = YYERRLAB1;
                break labelSwitch;

                /*-------------------------------------------------.
                | errorlab -- error raised explicitly by YYERROR.  |
                `-------------------------------------------------*/
                case YYERROR:
                /* Do not reclaim the symbols of the rule which action triggered
                   this YYERROR.  */
                yystack.pop (yylen);
                yylen = 0;
                yystate = yystack.stateAt(0);
                label = YYERRLAB1;
                break labelSwitch;

                /*-------------------------------------------------------------.
                | yyerrlab1 -- common code for both syntax error and YYERROR.  |
                `-------------------------------------------------------------*/
                case YYERRLAB1:
                yyerrstatus_ = 3;       /* Each real token shifted decrements this.  */

                // Pop stack until we find a state that shifts the error token.
                for (;;)
                {
                    yyn = yypact_[yystate];
                    if (!yyPactValueIsDefault (yyn))
                    {
                        yyn += SymbolKind.S_YYerror.getCode();
                        if (yyn >= 0 &&
                            yyn <= YYLAST_ &&
                            yycheck_[yyn] == SymbolKind.S_YYerror.getCode()
                        ) {
                            yyn = yytable_[yyn];
                            if (0 < yyn) {
                                break;
                            }
                        }
                    }

                    /* Pop the current state because it cannot handle the
                     * error token.  */
                    if (yystack.height == 0) {
                        return false;
                    }

                    
                    yystack.pop ();
                    yystate = yystack.stateAt(0);
                    if (0 < yydebug) {
                        yystack.print(yyDebugStream);
                    }
                }

                if (label == YYABORT) {
                    /* Leave the switch.  */
                    break labelSwitch;
                }

                

                /* Shift the error token.  */
                yylacDiscard("error recovery");
                yySymbolPrint("Shifting", SymbolKind.get(yystos_[yyn]),
                    yylval);

                yystate = yyn;
                yystack.push (yyn, yylval);
                label = YYNEWSTATE;
                break labelSwitch;

                /* Accept.  */
                case YYACCEPT:
                return true;

                /* Abort.  */
                case YYABORT:
                return false;
            }
    }


    

    /**
     * Information needed to get the list of expected tokens and to forge
     * a syntax error diagnostic.
     */
    public static final class Context {
    Context(YYParser parser, YYStack stack, SymbolKind token) {
        yyparser = parser;
        yystack = stack;
        yytoken = token;
    }

    private YYParser yyparser;
    private YYStack yystack;


    /**
     * The symbol kind of the lookahead token.
     */
    public final SymbolKind getToken() {
        return yytoken;
    }

    private SymbolKind yytoken;
    static final int NTOKENS = YYParser.YYNTOKENS_;

    /**
     * Put in YYARG at most YYARGN of the expected tokens given the
     * current YYCTX, and return the number of tokens stored in YYARG.  If
     * YYARG is null, return the number of expected tokens (guaranteed to
     * be less than YYNTOKENS).
     */
    int getExpectedTokens(SymbolKind yyarg[], int yyargn) {
        return getExpectedTokens (yyarg, 0, yyargn);
    }

    int getExpectedTokens(SymbolKind yyarg[], int yyoffset, int yyargn) {
        int yycount = yyoffset;
            // Execute LAC once. We don't care if it is successful, we
            // only do it for the sake of debugging output.
            if (!yyparser.yylacEstablished)
                yyparser.yylacCheck(yystack, yytoken);
        
        for (int yyx = 0; yyx < YYNTOKENS_; ++yyx)
        {
            SymbolKind yysym = SymbolKind.get(yyx);
            if (yysym != SymbolKind.S_YYerror
            && yysym != SymbolKind.S_YYUNDEF
            && yyparser.yylacCheck(yystack, yysym))
            {
                if (yyarg == null) {
                    yycount += 1;
                } else if (yycount == yyargn) {
                    return 0;
                } else {
                    yyarg[yycount++] = yysym;
                }
            }
        }
        if (yyarg != null && yycount == yyoffset && yyoffset < yyargn) {
            yyarg[yycount] = null;
        }

        return yycount - yyoffset;
    }
}

    
    /** Check the lookahead yytoken.
     * \returns  true iff the token will be eventually shifted.
     */
    boolean yylacCheck(YYStack yystack, SymbolKind yytoken)
{
    // Logically, the yylacStack's lifetime is confined to this function.
    // Clear it, to get rid of potential left-overs from previous call.
    yylacStack.clear();
    // Reduce until we encounter a shift and thereby accept the token.
    yycdebugNnl("LAC: checking lookahead " + yytoken.getName() + ":");
    int lacTop = 0;
    while (true)
    {
        int topState = (yylacStack.isEmpty()
            ? yystack.stateAt(lacTop)
        : yylacStack.get(yylacStack.size() - 1));
        int yyrule = yypact_[topState];
        if (yyPactValueIsDefault(yyrule) ||
            (yyrule += yytoken.getCode()) < 0 ||
            YYLAST_ < yyrule || yycheck_[yyrule] != yytoken.getCode()
        ) {
            // Use the default action.
            yyrule = yydefact_[+topState];
            if (yyrule == 0) {
                yycdebug(" Err");
                return false;
            }
        } else {
            // Use the action from yytable.
            yyrule = yytable_[yyrule];
            if (yyTableValueIsError(yyrule)) {
                yycdebug(" Err");
                return false;
            }
            if (0 < yyrule) {
                yycdebug(" S" + yyrule);
                return true;
            }
            yyrule = -yyrule;
        }
        // By now we know we have to simulate a reduce.
        yycdebugNnl(" R" + (yyrule - 1));
        // Pop the corresponding number of values from the stack.
        {
            int yylen = yyr2_[yyrule];
            // First pop from the LAC stack as many tokens as possible.
            int lacSize = yylacStack.size();
            if (yylen < lacSize) {
                // yylacStack.setSize(lacSize - yylen);
                for (/* Nothing */; 0 < yylen; yylen -= 1) {
                    yylacStack.remove(yylacStack.size() - 1);
                }
                yylen = 0;
            } else if (lacSize != 0) {
                yylacStack.clear();
                yylen -= lacSize;
            }
            // Only afterwards look at the main stack.
            // We simulate popping elements by incrementing lacTop.
            lacTop += yylen;
        }
        // Keep topState in sync with the updated stack.
        topState = (yylacStack.isEmpty()
        ? yystack.stateAt(lacTop)
        : yylacStack.get(yylacStack.size() - 1));
        // Push the resulting state of the reduction.
        int state = yyLRGotoState(topState, yyr1_[yyrule]);
        yycdebugNnl(" G" + state);
        yylacStack.add(state);
    }
}

    /** Establish the initial context if no initial context currently exists.
     * \returns  true iff the token will be eventually shifted.
     */
    boolean yylacEstablish(YYStack yystack, SymbolKind yytoken) {
    /* Establish the initial context for the current lookahead if no initial
       context is currently established.

       We define a context as a snapshot of the parser stacks.  We define
       the initial context for a lookahead as the context in which the
       parser initially examines that lookahead in order to select a
       syntactic action.  Thus, if the lookahead eventually proves
       syntactically unacceptable (possibly in a later context reached via a
       series of reductions), the initial context can be used to determine
       the exact set of tokens that would be syntactically acceptable in the
       lookahead's place.  Moreover, it is the context after which any
       further semantic actions would be erroneous because they would be
       determined by a syntactically unacceptable token.

       yylacEstablish should be invoked when a reduction is about to be
       performed in an inconsistent state (which, for the purposes of LAC,
       includes consistent states that don't know they're consistent because
       their default reductions have been disabled).

       For parse.lac=full, the implementation of yylacEstablish is as
       follows.  If no initial context is currently established for the
       current lookahead, then check if that lookahead can eventually be
       shifted if syntactic actions continue from the current context.  */
    if (yylacEstablished) {
        return true;
    } else {
        yycdebug("LAC: initial context established for " + yytoken.getName());
        yylacEstablished = true;
        return yylacCheck(yystack, yytoken);
    }
}

    /** Discard any previous initial lookahead context because of event.
     * \param event  the event which caused the lookahead to be discarded.
     *               Only used for debbuging output.  */
    void yylacDiscard(String event) {
        /* Discard any previous initial lookahead context because of Event,
           which may be a lookahead change or an invalidation of the currently
           established initial context for the current lookahead.

           The most common example of a lookahead change is a shift.  An example
           of both cases is syntax error recovery.  That is, a syntax error
           occurs when the lookahead is syntactically erroneous for the
           currently established initial context, so error recovery manipulates
           the parser stacks to try to find a new initial context in which the
           current lookahead is syntactically acceptable.  If it fails to find
           such a context, it discards the lookahead.  */
        if (yylacEstablished) {
            yycdebug("LAC: initial context discarded due to " + event);
            yylacEstablished = false;
        }
    }

    /** The stack for LAC.
     * Logically, the yylacStack's lifetime is confined to the function
     * yylacCheck. We just store it as a member of this class to hold
     * on to the memory and to avoid frequent reallocations.
     */
    ArrayList<Integer> yylacStack;
    /**  Whether an initial LAC context was established. */
    boolean yylacEstablished;
    

    

    /**
     * Build and emit a "syntax error" message in a user-defined way.
     *
     * @param ctx  The context of the error.
     */
    private void yyreportSyntaxError(Context yyctx) {
        yyerror("syntax error");
    }

    /**
     * Whether the given <code>yypact_</code> value indicates a defaulted state.
     * @param yyvalue   the value to check
     */
    private static boolean yyPactValueIsDefault(int yyvalue) {
        return yyvalue == yypact_ninf_;
    }

    /**
     * Whether the given <code>yytable_</code>
     * value indicates a syntax error.
     * @param yyvalue the value to check
     */
    private static boolean yyTableValueIsError(int yyvalue) {
        return yyvalue == yytable_ninf_;
    }

    private static final byte yypact_ninf_ = -5;
    private static final byte yytable_ninf_ = -1;

    /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
  private static final byte[] yypact_ = yypact_init();
  private static final byte[] yypact_init()
  {
    return new byte[]
    {
      -4,    -5,    -5,    -5,    -5,    -5,    -5,    -5,    -5,    -5,
       9,    -4,    -5,    -5
    };
  }

/* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE does not specify something else to do.  Zero
   means the default is an error.  */
  private static final byte[] yydefact_ = yydefact_init();
  private static final byte[] yydefact_init()
  {
    return new byte[]
    {
       0,    12,     4,     5,     6,     7,     8,     9,    10,    11,
       0,     2,     1,     3
    };
  }

/* YYPGOTO[NTERM-NUM].  */
  private static final byte[] yypgoto_ = yypgoto_init();
  private static final byte[] yypgoto_init()
  {
    return new byte[]
    {
      -5,    -1,    -5
    };
  }

/* YYDEFGOTO[NTERM-NUM].  */
  private static final byte[] yydefgoto_ = yydefgoto_init();
  private static final byte[] yydefgoto_init()
  {
    return new byte[]
    {
       0,    10,    11
    };
  }

/* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule whose
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
  private static final byte[] yytable_ = yytable_init();
  private static final byte[] yytable_init()
  {
    return new byte[]
    {
       1,     2,     3,     4,     5,     6,     7,     8,     9,    12,
      13
    };
  }

private static final byte[] yycheck_ = yycheck_init();
  private static final byte[] yycheck_init()
  {
    return new byte[]
    {
       4,     5,     6,     7,     8,     9,    10,    11,    12,     0,
      11
    };
  }

/* YYSTOS[STATE-NUM] -- The symbol kind of the accessing symbol of
   state STATE-NUM.  */
  private static final byte[] yystos_ = yystos_init();
  private static final byte[] yystos_init()
  {
    return new byte[]
    {
       0,     4,     5,     6,     7,     8,     9,    10,    11,    12,
      15,    16,     0,    15
    };
  }

/* YYR1[RULE-NUM] -- Symbol kind of the left-hand side of rule RULE-NUM.  */
  private static final byte[] yyr1_ = yyr1_init();
  private static final byte[] yyr1_init()
  {
    return new byte[]
    {
       0,    14,    15,    15,    16,    16,    16,    16,    16,    16,
      16,    16,    16
    };
  }

/* YYR2[RULE-NUM] -- Number of symbols on the right-hand side of rule RULE-NUM.  */
  private static final byte[] yyr2_ = yyr2_init();
  private static final byte[] yyr2_init()
  {
    return new byte[]
    {
       0,     2,     1,     2,     1,     1,     1,     1,     1,     1,
       1,     1,     1
    };
  }



/* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
  private static final byte[] yyrline_ = yyrline_init();
  private static final byte[] yyrline_init()
  {
    return new byte[]
    {
       0,    25,    25,    26,    30,    31,    32,    33,    34,    35,
      36,    37,    38
    };
  }


    // Report on the debug stream that the rule yyrule is going to be reduced.
    private void yyReducePrint (int yyrule, YYStack yystack)
    {
        if (yydebug == 0) {
            return;
        }
        int yylno = yyrline_[yyrule];
        int yynrhs = yyr2_[yyrule];
        /* Print the symbols being reduced, and their result.  */
        yycdebug ("Reducing stack by rule " + (yyrule - 1)
            + " (line " + yylno + "):");

        /* The symbols being reduced.  */
        // TODO: Use Stringbuilder
        for (int yyi = 0; yyi < yynrhs; yyi++) {
            yySymbolPrint("   $" + (yyi + 1) + " =",
                SymbolKind.get(yystos_[yystack.stateAt(yynrhs - (yyi + 1))]),
            yystack.valueAt ((yynrhs) - (yyi + 1)));
        }
    }

    /* YYTRANSLATE_(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
       as returned by yylex, with out-of-bounds checking.  */
    private static final SymbolKind yytranslate_(int t)
      {
        // Last valid token kind.
        int code_max = 268;
        if (t <= 0) {
            return SymbolKind.S_YYEOF;
        }
        else if (t <= code_max) {
            return SymbolKind.get(yytranslate_table_[t]);
        }
        else {
            return SymbolKind.S_YYUNDEF;
        }
    }
    private static final byte[] yytranslate_table_ = yytranslate_table_init();
  private static final byte[] yytranslate_table_init()
  {
    return new byte[]
    {
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13
    };
  }


    private static final int YYLAST_ = 10;
    private static final int YYEMPTY_ = -2;
    private static final int YYFINAL_ = 12;
    private static final int YYNTOKENS_ = 14;

    
}
