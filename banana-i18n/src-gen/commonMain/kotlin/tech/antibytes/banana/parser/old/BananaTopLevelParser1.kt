import java.io.IOException
import java.io.PrintStream
import java.util.Arrays

/**
 * A Bison parser, automatically generated from <tt>/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/bison/BananaTopLevelParser.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
internal class YYParser
/**
 * Instantiates the Bison-generated parser.
 * @param yylexer The scanner that will supply tokens to the parser.
 */(
    /**
     * The object doing lexical analysis for us.
     */
    private val yylexer: Lexer
) {
    enum class SymbolKind(  /* text  */
        val code: Int
    ) {
        S_YYEOF(0),  /* "end of file"  */
        S_YYerror(1),  /* error  */
        S_YYUNDEF(2),  /* "invalid token"  */
        S_RULE_OPENING(3),  /* "{{"  */
        S_RULE_CLOSURE(4),  /* "}}"  */
        S_DOUBLE(5),  /* DOUBLE  */
        S_INTEGER(6),  /* INTEGER  */
        S_ESCAPED(7),  /* ESCAPED  */
        S_DELIMITER(8),  /* "|"  */
        S_ASCII_STRING(9),  /* ASCII_STRING  */
        S_NON_ASCII_STRING(10),  /* NON_ASCII_STRING  */
        S_LITERAL(11),  /* LITERAL  */
        S_WHITESPACE(12),  /* WHITESPACE  */
        S_VARIABLE(13),  /* VARIABLE  */
        S_YYACCEPT(14),  /* $accept  */
        S_message(15),  /* message  */
        S_text(16);

        /* The user-facing name of this symbol.  */
        override val name: String?
            get() = yytnamerr_(yytname_[this.code])

        companion object {
            private val values_ = arrayOf(
                S_YYEOF,
                S_YYerror,
                S_YYUNDEF,
                S_RULE_OPENING,
                S_RULE_CLOSURE,
                S_DOUBLE,
                S_INTEGER,
                S_ESCAPED,
                S_DELIMITER,
                S_ASCII_STRING,
                S_NON_ASCII_STRING,
                S_LITERAL,
                S_WHITESPACE,
                S_VARIABLE,
                S_YYACCEPT,
                S_message,
                S_text
            )

            operator fun get(code: Int): SymbolKind {
                return values_[code]
            }

            /* Return YYSTR after stripping away unnecessary quotes and
       backslashes, so that it's suitable for yyerror.  The heuristic is
       that double-quoting is unnecessary unless the string contains an
       apostrophe, a comma, or backslash (other than backslash-backslash).
       YYSTR is taken from yytname.  */
            private fun yytnamerr_(yystr: String): String {
                if (yystr[0] == '"') {
                    val yyr = StringBuffer()
                    var i = 1
                    strip_quotes@ while (i < yystr.length) {
                        when (yystr[i]) {
                            '\'', ',' -> break@strip_quotes
                            '\\' -> {
                                if (yystr[++i] != '\\') {
                                    break@strip_quotes
                                }
                                yyr.append(yystr[i])
                            }
                            '"' -> return yyr.toString()
                            else -> yyr.append(yystr[i])
                        }
                        i++
                    }
                }
                return yystr
            }

            /* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
       First, the terminals, then, starting at \a YYNTOKENS_, nonterminals.  */
            private val yytname_ = yytname_init()
            private fun yytname_init(): Array<String?> {
                return arrayOf(
                    "\"end of file\"", "error", "\"invalid token\"", "\"{{\"", "\"}}\"",
                    "DOUBLE", "INTEGER", "ESCAPED", "\"|\"", "ASCII_STRING",
                    "NON_ASCII_STRING", "LITERAL", "WHITESPACE", "VARIABLE", "\$accept",
                    "message", "text", null
                )
            }
        }
    }

    /**
     * Communication interface between the scanner and the Bison-generated
     * parser <tt>YYParser</tt>.
     */
    interface Lexer {
        /**
         * Method to retrieve the semantic value of the last scanned token.
         * @return the semantic value of the last scanned token.
         */
        val lVal: Any

        /**
         * Entry point for the scanner.  Returns the token identifier corresponding
         * to the next token and prepares to return the semantic value
         * of the token.
         * @return the token identifier corresponding to the next token.
         */
        @Throws(IOException::class)
        fun yylex(): Int

        /**
         * Emit an errorin a user-defined way.
         *
         *
         * @param msg The string for the error message.
         */
        fun yyerror(msg: String)

        companion object {
            /* Token kinds.  */
            /** Token "end of file", to be returned by the scanner.   */
            const val YYEOF = 0

            /** Token error, to be returned by the scanner.   */
            const val YYerror = 256

            /** Token "invalid token", to be returned by the scanner.   */
            const val YYUNDEF = 257

            /** Token "{{", to be returned by the scanner.   */
            const val RULE_OPENING = 258

            /** Token "}}", to be returned by the scanner.   */
            const val RULE_CLOSURE = 259

            /** Token DOUBLE, to be returned by the scanner.   */
            const val DOUBLE = 260

            /** Token INTEGER, to be returned by the scanner.   */
            const val INTEGER = 261

            /** Token ESCAPED, to be returned by the scanner.   */
            const val ESCAPED = 262

            /** Token "|", to be returned by the scanner.   */
            const val DELIMITER = 263

            /** Token ASCII_STRING, to be returned by the scanner.   */
            const val ASCII_STRING = 264

            /** Token NON_ASCII_STRING, to be returned by the scanner.   */
            const val NON_ASCII_STRING = 265

            /** Token LITERAL, to be returned by the scanner.   */
            const val LITERAL = 266

            /** Token WHITESPACE, to be returned by the scanner.   */
            const val WHITESPACE = 267

            /** Token VARIABLE, to be returned by the scanner.   */
            const val VARIABLE = 268

            /** Deprecated, use YYEOF instead.   */
            const val EOF = YYEOF
        }
    }

    /**
     * The number of syntax errors so far.
     */
    var numberOfErrors = 0
        private set

    /**
     * Print an error message via the lexer.
     *
     * @param msg The error message.
     */
    fun yyerror(msg: String) {
        yylexer.yyerror(msg)
    }

    private inner class YYStack {
        private var stateStack = IntArray(16)
        private var valueStack = arrayOfNulls<Any>(16)
        var size = 16
        var height = -1
        fun push(state: Int, value: Any?) {
            height++
            if (size == height) {
                val newStateStack = IntArray(size * 2)
                System.arraycopy(stateStack, 0, newStateStack, 0, height)
                stateStack = newStateStack
                val newValueStack = arrayOfNulls<Any>(size * 2)
                System.arraycopy(valueStack, 0, newValueStack, 0, height)
                valueStack = newValueStack
                size *= 2
            }
            stateStack[height] = state
            valueStack[height] = value
        }

        @JvmOverloads
        fun pop(num: Int = 1) {
            // Avoid memory leaks... garbage collection is a white lie!
            if (0 < num) {
                Arrays.fill(valueStack, height - num + 1, height + 1, null)
            }
            height -= num
        }

        fun stateAt(i: Int): Int {
            return stateStack[height - i]
        }

        fun valueAt(i: Int): Any? {
            return valueStack[height - i]
        }

        // Print the state stack on the debug stream.
        fun print(out: PrintStream) {
            out.print("Stack now")
            for (i in 0..height) {
                out.print(' ')
                out.print(stateStack[i])
            }
            out.println()
        }
    }

    private var yyerrstatus_ = 0

    /**
     * Whether error recovery is being done.  In this state, the parser
     * reads token until it reaches a known state, and then restarts normal
     * operation.
     */
    fun recovering(): Boolean {
        return yyerrstatus_ == 0
    }

    /** Compute post-reduction state.
     * @param yystate   the current state
     * @param yysym     the nonterminal to push on the stack
     */
    private fun yyLRGotoState(yystate: Int, yysym: Int): Int {
        val yyr = yypgoto_[yysym - YYNTOKENS_] + yystate
        return if (yyr in 0..YYLAST_ && yycheck_[yyr] == yystate.toByte()) yytable_[yyr]
            .toInt() else yydefgoto_[yysym - YYNTOKENS_].toInt()
    }

    private fun yyaction(yyn: Int, yystack: YYParser.YYStack, yylen: Int): Int {
        /* If YYLEN is nonzero, implement the default value of the action:
           '$$ = $1'.  Otherwise, use the top of the stack.

           Otherwise, the following line sets YYVAL to garbage.
           This behavior is undocumented and Bison
           users should not rely upon it.  */
        var yylen = yylen
        val yyval = if (0 < yylen) {
            yystack.valueAt(yylen - 1)
        } else {
            yystack.valueAt(0)
        }

        when (yyn) {
            else -> {}
        }
        yystack.pop(yylen)
        yylen = 0
        /* Shift the result of the reduction.  */
        val yystate = yyLRGotoState(
            yystack.stateAt(0), yyr1_[yyn]
                .toInt()
        )
        yystack.push(yystate, yyval)
        return YYNEWSTATE
    }

    /**
     * Parse input from the scanner that was specified at object construction
     * time.  Return whether the end of the input was reached successfully.
     *
     * @return <tt>true</tt> if the parsing succeeds.  Note that this does not
     * imply that there were no syntax errors.
     */
    @Throws(IOException::class)
    fun parse(): Boolean {

/* Lookahead token kind.  */
        var yychar = YYEMPTY_
        /* Lookahead symbol kind.  */
        var yytoken: SymbolKind? = null

/* State.  */
        var yyn = 0
        var yylen = 0
        var yystate = 0
        val yystack = YYParser.YYStack()
        var label = YYNEWSTATE

/* Semantic value of the lookahead.  */
        var yylval: Any? = null
        yyerrstatus_ = 0
        numberOfErrors = 0

        /* Initialize the stack.  */yystack.push(yystate, yylval)
        while (true) {
            when (label) {
                YYNEWSTATE -> {
                    /* Accept?  */
                    if (yystate == YYFINAL_) {
                        return true
                    }

                    /* Take a decision.  First try without lookahead.  */yyn = yypact_[yystate].toInt()
                    if (yyPactValueIsDefault(yyn)) {
                        label = YYDEFAULT
                    }

                    /* Read a lookahead token.  */
                    if (yychar == YYEMPTY_) {
                        yychar = yylexer.yylex()
                        yylval = yylexer.lVal
                    }

                    /* Convert token to internal form.  */yytoken = yytranslate_(yychar)
                    if (yytoken == SymbolKind.S_YYerror) {
                        // The scanner already issued an error message, process directly
                        // to error recovery.  But do not keep the error token as
                        // lookahead, it is too special and may lead us to an endless
                        // loop in error recovery. */
                        yychar = Lexer.YYUNDEF
                        yytoken = SymbolKind.S_YYUNDEF
                        label = YYERRLAB1
                    } else {
                        /* If the proper action on seeing token YYTOKEN is to reduce or to
                       detect an error, take that action.  */
                        yyn += yytoken.code
                        if (yyn < 0 || YYLAST_ < yyn || yycheck_[yyn] != yytoken.code.toByte()) {
                            label = YYDEFAULT
                        } else if (yytable_[yyn] <= 0) {
                            yyn = yytable_[yyn].toInt()
                            if (yyTableValueIsError(yyn)) {
                                label = YYERRLAB
                            } else {
                                yyn = -yyn
                                label = YYREDUCE
                            }
                        } else {
                            /* Shift the lookahead token.  */
                            /* Discard the token being shifted.  */
                            yychar = YYEMPTY_

                            /* Count tokens shifted since error; after three, turn off error
                           status.  */
                            if (yyerrstatus_ > 0) {
                                --yyerrstatus_
                            }
                            yystate = yyn
                            yystack.push(yystate, yylval)
                            label = YYNEWSTATE
                        }
                    }
                }
                YYDEFAULT -> {
                    yyn = yydefact_[yystate].toInt()
                    label = if (yyn == 0) {
                        YYERRLAB
                    } else {
                        YYREDUCE
                    }
                }
                YYREDUCE -> {
                    yylen = yyr2_[yyn].toInt()
                    label = yyaction(yyn, yystack, yylen)
                    yystate = yystack.stateAt(0)
                }
                YYERRLAB -> {
                    /* If not already recovering from an error, report this error.  */
                    if (yyerrstatus_ == 0) {
                        ++numberOfErrors
                        if (yychar == YYEMPTY_) {
                            yytoken = null
                        }
                        yyreportSyntaxError(Context(this, yystack, yytoken!!))
                    }
                    if (yyerrstatus_ == 3) {
                        /* If just tried and failed to reuse lookahead token after an
                       error, discard it.  */
                        if (yychar <= Lexer.YYEOF) {
                            /* Return failure if at end of input.  */
                            if (yychar == Lexer.YYEOF) {
                                return false
                            }
                        } else {
                            yychar = YYEMPTY_
                        }
                    }

                    /* Else will try to reuse lookahead token after shifting the error
                   token.  */
                    label = YYERRLAB1
                }
                YYERROR -> {
                    /* Do not reclaim the symbols of the rule which action triggered
                   this YYERROR.  */
                    yystack.pop(yylen)
                    yylen = 0
                    yystate = yystack.stateAt(0)
                    label = YYERRLAB1
                }
                YYERRLAB1 -> {
                    yyerrstatus_ = 3 /* Each real token shifted decrements this.  */

                    // Pop stack until we find a state that shifts the error token.
                    while (true) {
                        yyn = yypact_[yystate].toInt()
                        if (!yyPactValueIsDefault(yyn)) {
                            yyn += SymbolKind.S_YYerror.code
                            if (0 <= yyn && yyn <= YYLAST_ && yycheck_[yyn] == SymbolKind.S_YYerror.code.toByte()) {
                                yyn = yytable_[yyn].toInt()
                                if (0 < yyn) break
                            }
                        }

                        /* Pop the current state because it cannot handle the
                     * error token.  */if (yystack.height == 0) return false
                        yystack.pop()
                        yystate = yystack.stateAt(0)
                    }
                    if (label == YYABORT) /* Leave the switch.  */ 

                    /* Shift the error token.  */yystate = yyn
                    yystack.push(yyn, yylval)
                    label = YYNEWSTATE
                }
                YYACCEPT -> return true
                YYABORT -> return false
            }
        }
    }

    /**
     * Information needed to get the list of expected tokens and to forge
     * a syntax error diagnostic.
     */
    class Context internal constructor(
        private val yyparser: YYParser, private val yystack: YYParser.YYStack,
        /**
         * The symbol kind of the lookahead token.
         */
        val token: SymbolKind
    ) {

        /**
         * Put in YYARG at most YYARGN of the expected tokens given the
         * current YYCTX, and return the number of tokens stored in YYARG.  If
         * YYARG is null, return the number of expected tokens (guaranteed to
         * be less than YYNTOKENS).
         */
        fun getExpectedTokens(yyarg: Array<SymbolKind?>?, yyargn: Int): Int {
            return getExpectedTokens(yyarg, 0, yyargn)
        }

        fun getExpectedTokens(yyarg: Array<SymbolKind?>?, yyoffset: Int, yyargn: Int): Int {
            var yycount = yyoffset
            val yyn = YYParser.yypact_[yystack.stateAt(0)].toInt()
            if (!YYParser.yyPactValueIsDefault(yyn)) {
                /* Start YYX at -YYN if negative to avoid negative
               indexes in YYCHECK.  In other words, skip the first
               -YYN actions for this state because they are default
               actions.  */
                val yyxbegin = if (yyn < 0) -yyn else 0
                /* Stay within bounds of both yycheck and yytname.  */
                val yychecklim = YYParser.YYLAST_ - yyn + 1
                val yyxend = if (yychecklim < NTOKENS) yychecklim else NTOKENS
                for (yyx in yyxbegin until yyxend) {
                    if (YYParser.yycheck_[yyx + yyn] == yyx.toByte() &&
                        yyx != SymbolKind.S_YYerror.code &&
                        !YYParser.yyTableValueIsError(YYParser.yytable_[yyx + yyn].toInt())
                    ) {
                        if (yyarg == null) yycount += 1
                        else if (yycount == yyargn) return 0 // FIXME: this is incorrect.
                        else yyarg[yycount++] = SymbolKind[yyx]
                    }
                }
            }
            if (yyarg != null && yycount == yyoffset && yyoffset < yyargn) {
                yyarg[yycount] = null
            }
            return yycount - yyoffset
        }

        companion object {
            const val NTOKENS = YYParser.YYNTOKENS_
        }
    }

    /**
     * Build and emit a "syntax error" message in a user-defined way.
     *
     * @param yyctx  The context of the error.
     */
    private fun yyreportSyntaxError(yyctx: Context) {
        yyerror("syntax error")
    }

    companion object {
        /** Version number for the Bison executable that generated this parser.   */
        const val bisonVersion = "3.8.2"

        /** Name of the skeleton that generated this parser.   */
        const val bisonSkeleton =
            "/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/bison/skeleton/kotlin-skel.m4"

        /**
         * Returned by a Bison action in order to stop the parsing process and
         * return success (<tt>true</tt>).
         */
        const val YYACCEPT = 0

        /**
         * Returned by a Bison action in order to stop the parsing process and
         * return failure (<tt>false</tt>).
         */
        const val YYABORT = 1

        /**
         * Returned by a Bison action in order to start error recovery without
         * printing an error message.
         */
        const val YYERROR = 2

        /**
         * Internal return codes that are not supported for user semantic
         * actions.
         */
        private const val YYERRLAB = 3
        private const val YYNEWSTATE = 4
        private const val YYDEFAULT = 5
        private const val YYREDUCE = 6
        private const val YYERRLAB1 = 7
        private const val YYRETURN = 8

        /**
         * Whether the given `yypact_` value indicates a defaulted state.
         * @param yyvalue   the value to check
         */
        private fun yyPactValueIsDefault(yyvalue: Int): Boolean {
            return yyvalue == yypact_ninf_.toInt()
        }

        /**
         * Whether the given `yytable_`
         * value indicates a syntax error.
         * @param yyvalue the value to check
         */
        private fun yyTableValueIsError(yyvalue: Int): Boolean {
            return yyvalue == yytable_ninf_.toInt()
        }

        private const val yypact_ninf_: Byte = -5
        private const val yytable_ninf_: Byte = -1

        /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
        private val yypact_ = yypact_init()
        private fun yypact_init(): ByteArray {
            return byteArrayOf(
                -4, -5, -5, -5, -5, -5, -5, -5, -5, -5,
                9, -4, -5, -5
            )
        }

        /* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE does not specify something else to do.  Zero
   means the default is an error.  */
        private val yydefact_ = yydefact_init()
        private fun yydefact_init(): ByteArray {
            return byteArrayOf(
                0, 12, 4, 5, 6, 7, 8, 9, 10, 11,
                0, 2, 1, 3
            )
        }

        /* YYPGOTO[NTERM-NUM].  */
        private val yypgoto_ = yypgoto_init()
        private fun yypgoto_init(): ByteArray {
            return byteArrayOf(
                -5, -1, -5
            )
        }

        /* YYDEFGOTO[NTERM-NUM].  */
        private val yydefgoto_ = yydefgoto_init()
        private fun yydefgoto_init(): ByteArray {
            return byteArrayOf(
                0, 10, 11
            )
        }

        /* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule whose
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
        private val yytable_ = yytable_init()
        private fun yytable_init(): ByteArray {
            return byteArrayOf(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 12,
                13
            )
        }

        private val yycheck_ = yycheck_init()
        private fun yycheck_init(): ByteArray {
            return byteArrayOf(
                4, 5, 6, 7, 8, 9, 10, 11, 12, 0,
                11
            )
        }

        /* YYSTOS[STATE-NUM] -- The symbol kind of the accessing symbol of
   state STATE-NUM.  */
        private val yystos_ = yystos_init()
        private fun yystos_init(): ByteArray {
            return byteArrayOf(
                0, 4, 5, 6, 7, 8, 9, 10, 11, 12,
                15, 16, 0, 15
            )
        }

        /* YYR1[RULE-NUM] -- Symbol kind of the left-hand side of rule RULE-NUM.  */
        private val yyr1_ = yyr1_init()
        private fun yyr1_init(): ByteArray {
            return byteArrayOf(
                0, 14, 15, 15, 16, 16, 16, 16, 16, 16,
                16, 16, 16
            )
        }

        /* YYR2[RULE-NUM] -- Number of symbols on the right-hand side of rule RULE-NUM.  */
        private val yyr2_ = yyr2_init()
        private fun yyr2_init(): ByteArray {
            return byteArrayOf(
                0, 2, 1, 2, 1, 1, 1, 1, 1, 1,
                1, 1, 1
            )
        }

        /* YYTRANSLATE_(TOKEN-NUM) -- Symbol number corresponding to TOKEN-NUM
       as returned by yylex, with out-of-bounds checking.  */
        private fun yytranslate_(t: Int): SymbolKind {
            // Last valid token kind.
            val code_max = 268
            return if (t <= 0) SymbolKind.S_YYEOF else if (t <= code_max) SymbolKind[yytranslate_table_[t].toInt()] else SymbolKind.S_YYUNDEF
        }

        private val yytranslate_table_ = yytranslate_table_init()
        private fun yytranslate_table_init(): ByteArray {
            return byteArrayOf(
                0, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 1, 2, 3, 4,
                5, 6, 7, 8, 9, 10, 11, 12, 13
            )
        }

        private const val YYLAST_ = 10
        private const val YYEMPTY_ = -2
        private const val YYFINAL_ = 12
        private const val YYNTOKENS_ = 14
    }
}
