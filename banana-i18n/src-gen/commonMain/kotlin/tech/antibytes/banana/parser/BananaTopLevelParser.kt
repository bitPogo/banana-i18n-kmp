import java.io.IOException
import java.io.PrintStream
import java.util.ArrayList
import java.util.Arrays

/**
 * A Bison parser, automatically generated from <tt>/Users/d4l000126/projects/own/banana-i18n-kmp/banana-i18n/bison/BananaTopLevelParser.y</tt>.
 *
 * @author LALR (1) parser skeleton written by Paolo Bonzini.
 */
internal class YYParser(yylexer: Lexer) {
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
                                if (yystr[++i] != '\\') break@strip_quotes
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
        val lVal: Any?

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
        fun yyerror(msg: String?)

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
     * The object doing lexical analysis for us.
     */
    private val yylexer: Lexer
    /**
     * The <tt>PrintStream</tt> on which the debugging output is printed.
     */
    /**
     * Set the <tt>PrintStream</tt> on which the debug output is printed.
     * @param s The stream that is used for debugging output.
     */
    var debugStream = System.err
    /**
     * Answer the verbosity of the debugging output; 0 means that all kinds of
     * output from the parser are suppressed.
     */
    /**
     * Set the verbosity of the debugging output; 0 means that all kinds of
     * output from the parser are suppressed.
     * @param level The verbosity level for debugging output.
     */
    var debugLevel = 0

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
    fun yyerror(msg: String?) {
        yylexer.yyerror(msg)
    }

    protected fun yycdebugNnl(s: String?) {
        if (0 < debugLevel) {
            debugStream.print(s)
        }
    }

    protected fun yycdebug(s: String?) {
        if (0 < debugLevel) {
            debugStream.println(s)
        }
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
            // TODO Replace with valueStack.fill
            // Avoid memory leaks... garbage collection is a white lie!
            if (num > 0) {
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

        // TODO Replace with a PrintStream interface
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
        return if (0 <= yyr && yyr <= YYLAST_ && yycheck_[yyr] == yystate) {
            yytable_[yyr].toInt()
        } else {
            yydefgoto_[yysym - YYNTOKENS_].toInt()
        }
    }

    private fun yyaction(yyn: Int, yystack: YYParser.YYStack, yylen: Int): Int {
        /* If YYLEN is nonzero, implement the default value of the action:
           '$$ = $1'.  Otherwise, use the top of the stack.

           Otherwise, the following line sets YYVAL to garbage.
           This behavior is undocumented and Bison
           users should not rely upon it.  */
        var yylen = yylen
        val yyval = if (0 < yylen) yystack.valueAt(yylen - 1) else yystack.valueAt(0)
        yyReducePrint(yyn, yystack)
        when (yyn) {
            else -> {}
        }
        yySymbolPrint("-> $$ =", SymbolKind[yyr1_[yyn].toInt()], yyval)
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

    /*--------------------------------.
    | Print this symbol on YYOUTPUT.  |
    `--------------------------------*/
    private fun yySymbolPrint(
        s: String, yykind: SymbolKind,
        yyvalue: Any?
    ) {
        // TODO Replace with builder
        if (debugLevel > 0) {
            yycdebug(
                s
                    + (if (yykind.getCode() < YYNTOKENS_) " token " else " nterm ")
                    + yykind.getName() + " ("
                    + (yyvalue?.toString() ?: "(null)") + ")"
            )
        }
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

        // Discard the LAC context in case there still is one left from a
        // previous invocation.
        yylacDiscard("init")
        yycdebug("Starting parse")
        yyerrstatus_ = 0
        numberOfErrors = 0

        /* Initialize the stack.  */yystack.push(yystate, yylval)
        while (true) {
            labelSwitch@ when (label) {
                YYNEWSTATE -> {
                    yycdebug("Entering state $yystate")
                    if (0 < debugLevel) {
                        yystack.print(debugStream)
                    }

                    /* Accept?  */if (yystate == YYFINAL_) {
                        return true
                    }

                    /* Take a decision.  First try without lookahead.  */yyn = yypact_[yystate].toInt()
                    if (yyPactValueIsDefault(yyn)) {
                        label = YYDEFAULT
                        break@labelSwitch
                    }

                    /* Read a lookahead token.  */if (yychar == YYEMPTY_) {
                        yycdebug("Reading a token")
                        yychar = yylexer.yylex()
                        yylval = yylexer.getLVal()
                    }

                    /* Convert token to internal form.  */yytoken = yytranslate_(yychar)
                    yySymbolPrint(
                        "Next token is", yytoken,
                        yylval
                    )
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
                        yyn += yytoken.getCode()
                        if (yyn < 0 || YYLAST_ < yyn || yycheck_[yyn] != yytoken.getCode()) {
                            label = if (!yylacEstablish(yystack, yytoken)) {
                                YYERRLAB
                            } else YYDEFAULT
                        } else if (yytable_[yyn] <= 0) { /* <= 0 means reduce or error.  */
                            yyn = yytable_[yyn].toInt()
                            if (yyTableValueIsError(yyn)) {
                                label = YYERRLAB
                            } else if (!yylacEstablish(yystack, yytoken)) {
                                label = YYERRLAB
                            } else {
                                yyn = -yyn
                                label = YYREDUCE
                            }
                        } else {
                            /* Shift the lookahead token.  */
                            yySymbolPrint(
                                "Shifting", yytoken,
                                yylval
                            )

                            /* Discard the token being shifted.  */yychar = YYEMPTY_

                            /* Count tokens shifted since error; after three, turn off error
                           status.  */if (yyerrstatus_ > 0) {
                                --yyerrstatus_
                            }
                            yystate = yyn
                            yystack.push(yystate, yylval)
                            yylacDiscard("shift")
                            label = YYNEWSTATE
                        }
                    }
                    break@labelSwitch
                }
                YYDEFAULT -> {
                    yyn = yydefact_[yystate].toInt()
                    label = if (yyn == 0) {
                        YYERRLAB
                    } else {
                        YYREDUCE
                    }
                    break@labelSwitch
                }
                YYREDUCE -> {
                    yylen = yyr2_[yyn].toInt()
                    label = yyaction(yyn, yystack, yylen)
                    yystate = yystack.stateAt(0)
                    break@labelSwitch
                }
                YYERRLAB -> {
                    /* If not already recovering from an error, report this error.  */if (yyerrstatus_ == 0) {
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
                   token.  */label = YYERRLAB1
                    break@labelSwitch
                }
                YYERROR -> {
                    /* Do not reclaim the symbols of the rule which action triggered
                   this YYERROR.  */yystack.pop(yylen)
                    yylen = 0
                    yystate = yystack.stateAt(0)
                    label = YYERRLAB1
                    break@labelSwitch
                }
                YYERRLAB1 -> {
                    yyerrstatus_ = 3 /* Each real token shifted decrements this.  */

                    // Pop stack until we find a state that shifts the error token.
                    while (true) {
                        yyn = yypact_[yystate].toInt()
                        if (!yyPactValueIsDefault(yyn)) {
                            yyn += SymbolKind.S_YYerror.getCode()
                            if (yyn >= 0 && yyn <= YYLAST_ && yycheck_[yyn] == SymbolKind.S_YYerror.getCode()) {
                                yyn = yytable_[yyn].toInt()
                                if (0 < yyn) {
                                    break
                                }
                            }
                        }

                        /* Pop the current state because it cannot handle the
                     * error token.  */if (yystack.height == 0) {
                            return false
                        }
                        yystack.pop()
                        yystate = yystack.stateAt(0)
                        if (0 < debugLevel) {
                            yystack.print(debugStream)
                        }
                    }
                    if (label == YYABORT) {
                        /* Leave the switch.  */
                        break@labelSwitch
                    }

                    /* Shift the error token.  */yylacDiscard("error recovery")
                    yySymbolPrint(
                        "Shifting", SymbolKind[yystos_[yyn].toInt()],
                        yylval
                    )
                    yystate = yyn
                    yystack.push(yyn, yylval)
                    label = YYNEWSTATE
                    break@labelSwitch
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
            // Execute LAC once. We don't care if it is successful, we
            // only do it for the sake of debugging output.
            if (!yyparser.yylacEstablished) yyparser.yylacCheck(yystack, token)
            for (yyx in 0 until YYParser.YYNTOKENS_) {
                val yysym = SymbolKind[yyx]
                if (yysym != SymbolKind.S_YYerror && yysym != SymbolKind.S_YYUNDEF && yyparser.yylacCheck(
                        yystack,
                        yysym
                    )
                ) {
                    if (yyarg == null) {
                        yycount += 1
                    } else if (yycount == yyargn) {
                        return 0
                    } else {
                        yyarg[yycount++] = yysym
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

    /** Check the lookahead yytoken.
     * \returns  true iff the token will be eventually shifted.
     */
    fun yylacCheck(yystack: YYParser.YYStack, yytoken: SymbolKind): Boolean {
        // Logically, the yylacStack's lifetime is confined to this function.
        // Clear it, to get rid of potential left-overs from previous call.
        yylacStack.clear()
        // Reduce until we encounter a shift and thereby accept the token.
        yycdebugNnl("LAC: checking lookahead " + yytoken.getName() + ":")
        var lacTop = 0
        while (true) {
            var topState = if (yylacStack.isEmpty()) yystack.stateAt(lacTop) else yylacStack[yylacStack.size - 1]
            var yyrule = yypact_[topState].toInt()
            if (yyPactValueIsDefault(yyrule) || yytoken.getCode()
                    .let { yyrule += it; yyrule } < 0 || YYLAST_ < yyrule || yycheck_[yyrule] != yytoken.getCode()
            ) {
                // Use the default action.
                yyrule = yydefact_[+topState].toInt()
                if (yyrule == 0) {
                    yycdebug(" Err")
                    return false
                }
            } else {
                // Use the action from yytable.
                yyrule = yytable_[yyrule].toInt()
                if (yyTableValueIsError(yyrule)) {
                    yycdebug(" Err")
                    return false
                }
                if (0 < yyrule) {
                    yycdebug(" S$yyrule")
                    return true
                }
                yyrule = -yyrule
            }
            // By now we know we have to simulate a reduce.
            yycdebugNnl(" R" + (yyrule - 1))
            // Pop the corresponding number of values from the stack.
            run {
                var yylen = yyr2_[yyrule].toInt()
                // First pop from the LAC stack as many tokens as possible.
                val lacSize = yylacStack.size
                if (yylen < lacSize) {
                    // yylacStack.setSize(lacSize - yylen);
                    while ( /* Nothing */0 < yylen) {
                        yylacStack.removeAt(yylacStack.size - 1)
                        yylen -= 1
                    }
                    yylen = 0
                } else if (lacSize != 0) {
                    yylacStack.clear()
                    yylen -= lacSize
                }
                // Only afterwards look at the main stack.
                // We simulate popping elements by incrementing lacTop.
                lacTop += yylen
            }
            // Keep topState in sync with the updated stack.
            topState = if (yylacStack.isEmpty()) yystack.stateAt(lacTop) else yylacStack[yylacStack.size - 1]
            // Push the resulting state of the reduction.
            val state = yyLRGotoState(topState, yyr1_[yyrule].toInt())
            yycdebugNnl(" G$state")
            yylacStack.add(state)
        }
    }

    /** Establish the initial context if no initial context currently exists.
     * \returns  true iff the token will be eventually shifted.
     */
    fun yylacEstablish(yystack: YYParser.YYStack, yytoken: SymbolKind): Boolean {
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
        return if (yylacEstablished) {
            true
        } else {
            yycdebug("LAC: initial context established for " + yytoken.getName())
            yylacEstablished = true
            yylacCheck(yystack, yytoken)
        }
    }

    /** Discard any previous initial lookahead context because of event.
     * \param event  the event which caused the lookahead to be discarded.
     * Only used for debbuging output.   */
    fun yylacDiscard(event: String) {
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
            yycdebug("LAC: initial context discarded due to $event")
            yylacEstablished = false
        }
    }

    /** The stack for LAC.
     * Logically, the yylacStack's lifetime is confined to the function
     * yylacCheck. We just store it as a member of this class to hold
     * on to the memory and to avoid frequent reallocations.
     */
    var yylacStack: ArrayList<Int>

    /**  Whether an initial LAC context was established.  */
    var yylacEstablished: Boolean

    /**
     * Build and emit a "syntax error" message in a user-defined way.
     *
     * @param ctx  The context of the error.
     */
    private fun yyreportSyntaxError(yyctx: Context) {
        yyerror("syntax error")
    }

    // Report on the debug stream that the rule yyrule is going to be reduced.
    private fun yyReducePrint(yyrule: Int, yystack: YYParser.YYStack) {
        if (debugLevel == 0) return
        val yylno: Int = yyrline_.get(yyrule)
        val yynrhs = yyr2_[yyrule].toInt()
        /* Print the symbols being reduced, and their result.  */yycdebug(
            "Reducing stack by rule " + (yyrule - 1)
                + " (line " + yylno + "):"
        )

        /* The symbols being reduced.  */for (yyi in 0 until yynrhs) yySymbolPrint(
            "   $" + (yyi + 1) + " =",
            SymbolKind[yystos_[yystack.stateAt(yynrhs - (yyi + 1))].toInt()],
            yystack.valueAt(yynrhs - (yyi + 1))
        )
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

        /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
        private val yyrline_: ByteArray = yyrline_init()
        private fun yyrline_init(): ByteArray {
            return byteArrayOf(
                0, 24, 24, 25, 29, 30, 31, 32, 33, 34,
                35, 36, 37
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

    /**
     * Instantiates the Bison-generated parser.
     * @param yylexer The scanner that will supply tokens to the parser.
     */
    init {
        yylacStack = ArrayList()
        yylacEstablished = false
        this.yylexer = yylexer
    }
}
