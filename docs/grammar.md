# Grammar and syntax

The following description of the grammar is unofficial and users should not rely on it, since it is owned by the Wikimedia Foundation.
generic grammar:

```ebnf
<message>               := (<text> | <variable> | <function> | <link> | <freeLink>)*
<text>                  := double | escaped | integer | ascii | nonascii | literal | link_start | link_end | function_start | function_end | delimiter | whitespace
<variable>              := variable
<link>                  := link_start <space> <linkTarget> <space> (delimiter <space> <linkDisplay>)? link_end
<linkTarget>            := (whitespace? <linkText> | <function> | <variable>)+
<linkDisplay>           := (whitespace? <displayText> | <function> | <variable>)+
<linkText>              := double | escaped | integer | ascii | nonascii | <linkLiteral>
<linkDisplayText>       := double | escaped | integer | ascii | nonascii | url | function_start | function_end | delimiter | link_start
<freeLink>              := "[" <space> url (whitespace <freeLinkDisplayText>)? <space> "]"
<freeLinkUrl>           := url | <variable>
<freeLinkDisplayText>   := <linkDisplayText> | link_end
<function>              := function_start <space> <identifier> <space> <arguments>? function_end
<arguments>             := ":" <space> <argument> <space> (delimiter <space> <argument> <space>)*
<argument>              := (whitespace? <nestedText> | <variable> | <function>))+
<nestedText>            := double | escaped | integer | ascii | nonascii | literal | link_start | link_end | function_start
<space>                 := whitespace*
<identifier>            := ascii ( "_" ascii )*
<linkLiteral>           := ^"{" | ^"[" | ^"}" | ^"]" | ^"<" | ^">"
<displayLiteral>        := ^"]"

// Token
float               := {integer}? "." {integer}
ascii_letter        := [A-Za-z]
non_ascii_letter    := {::LETTER::--{ascii_letter}}
url_letter          := {ascii_letter} | digit | "-" | "_" | "." | "~" | "!" | "*" | "'" | "(" | ")" | ";" | ":" | "@" | "&" | "=" | "+" | "$" | "," | "/" | "?" | "%" | "#" | "[" | "]"

illegal             := [\u0000-\u0008 \u000B \u000C \u000E-\u001F \u007F-\u009F]
integer             := {::NUMBER::}+
double              := {float} ( [eE] [-+]? {integer} )? | {integer} [eE] [-+]? {integer}
ascii               := {ascii_letter}+
non_ascii           := {non_ascii_letter}+
escaped             := "\" [\u0021-\u002F \u003A-\u0040 \u005B-\u0060 \u007B-\u007E]
variable            := "$" ( {integer} | {ascii} ( "_" {ascii} )* )
url                 := {ascii} "://" {url_letter}+ | "//" {url_letter}+
function_start      := "{" "{"
function_end        := "}" "}"
link_start          := "[" "["
link_end            := "]" "]"
delimiter           := "|"
space               := [\s\t\n\r]+
```

Please note, `{::SOME_NAME::}` refers to the unicode definition of the Name.See [here](https://www.unicode.org/reports/tr18/#General_Category_Property) for more.
Please note, that `<argument>` can be replaced by the invoked rules, if the specific function name matches.

## Error Handling
If the parser faces an unexpected Token (for example [[ A illegal character follows me }]]), it should recover by assuming the expected Token was beforehand or producing a ErrorNode and log an Error.
The reason is simply to avoid any unexpected behaviour in a consuming application (like app crashes) which could lead to bad user experience.

## Up to discussion
The following points are up to discussion and part of the parser core.

### Links
* label extensions nor the redirect prefixes are not supported
* free links without braces are not supported
* links are not nestable in Functions by the default, since it can be error prone. However plugins can decide to unlock that.

## Shared rules
`<argument>`, `<rule>`, `<simpleRule>`, `<option>`, `<nestedRuleOrText>`, `<nestedText>`, `<exclusiveText>`, `<space>` and `<mandatorySpace>` are shared rules. That means each of those rules can be used in a parser component. (see PluralRule for example)
Also note, that rule ids will be normalized, which means, that rules names are case insensitive.

## Build-Ins
### Common Plugin Rules
CommonPluginRules are rules shared upon plugins and buildins, but not by the core rules. The following syntax describes those common rules:

```ebnf
<number>            := <sign> double | <sign> integer
<sign>              := '+' | '-' | ε
<specialNumber>     := <sign> nonascii <specialExponent> | <sign> '.' nonascii <specialExponent> | <sign> nonascii '.' nonascii <specialExponent>
<specialSign>       := + | - | ε
<specialExponent>   := [Ee] <specialSign> nonascii | ε
<repeatArgument>    := '=' '=' <space>
```

`<number>`, `<specialNumber>` and `<repeatArgument>` are exposed for usage.

### Bidi
Bidi will triggered, if the parser reads `bidi` as `<ruleId>`. It also requires no special syntax and falls back to `<generic>`.

### Choice
Syntax

```ebnf
<choiceRule>            := ':' <space> <choiceArgument> <space> <lowerBoundChoice> <space> <choices>
<choiceArgument>        := <number> | <rule> | <specialNumbers>
<lowerBoundChoice>      := '|' <space> <lowerBound> <space> <nestedRuleOrText>
<choices>               := '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText> <space> <choices>
                        | '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText>
<selector>              := <lowerBound> | <boundary>
<lowerBound>            := <number> <space> '~' | <rule> <space> '~'
<boundary>              := <number> <space> '<' | <rule> <space> '<'
```

The syntax will apply, if the parser reads `choice` as `<ruleId>`.

### Plural
Syntax

```ebnf
<pluralrule>        := ':' <space> <pluralArgument> <space> <pluralSelectors>
<pluralArgument>    := <number> | <rule> | <specialNumbers>
<pluralSelectors>   := '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText> <space> <pluralSelectors>
                    | '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText>
<selector>          := <number> <space> '=' | '%' <keyword> <space> '=' | ε
<keyword>           := [Zz][Ee][Rr][Oo] | [Oo][Nn][Ee] | [Tt][Ww][Oo] | [Ff][Ee][Ww] | [Mm][Aa][Nn][Yy] | [Oo][Tt][Hh][Ee][Rr]
```

The syntax will apply, if the parser reads `plural` as `<ruleId>`. Possible keywords are `zero`, `one`, `two`, `few`, `many` and `other`, which can be written case insensitive.
Please see [PluralRules](https://developer.android.com/reference/android/icu/text/PluralRules.html) for more information.
Also note: it always uses the last given matching selector.

### Number
A family of plugins, which have a common base as well as their specific implementation.

#### Generic

```ebnf
<genericNumber>     := ':' <space> <numberArgument>
<numberArgument>    := <number> | <rule> | <specialNumbers>
```

#### Number
Syntax

```ebnf
<number>    := <genericNumber>
```

The syntax will apply, if the parser reads `number` as `<ruleId>`.

###= Fraction
Syntax:

```ebnf
<fraction>    := <genericNumber>
```

The syntax will apply, if the parser reads `fraction` as `<ruleId>`.

###= Integer
Syntax:

```ebnf
<integer>   := <genericNumber>
```

The syntax will apply, if the parser reads `integer` as `<ruleId>`.

### Gender
Gender will triggered, if the parser reads `gender` as `<ruleId>`. It also requires no special syntax and falls back to `<generic>`.

### Selection
Syntax

```ebnf
<selectionRule>         := ':' <space> <selectArgument> <space> <selectionOptions>
<selectionParameter>    := <argument>
<selectionOptions>      := '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText> <space> <selectionOptions> | '|' <space> <selector> <space> <repeatArgument> <nestedRuleOrText>
<selector>              := <rule> <space> '=' | <nestedText*> <space> '='
```

The syntax will apply, if the parser reads `select` as `<ruleId>`.
Please note `<nestedText*>` means, that the `<nestedText>` rule applies except, if the given token is a `=`. Also, it always uses the last given matching selector.

### Pick
Syntax

```ebnf
<pickRule>          := ':' <space> <pickArgument> <space> <pickOption>
<pickArgument>      := integer | <rule>
<pickOption>        := <option> <space> <pickOption> | <option>
```

The syntax will apply, if the parser reads `pick` as `<ruleId>`.

### Capitalize
Capitalize will triggered, if the parser reads `capitalize` as `<ruleId>`. It also requires no special syntax and falls back to `<generic>`.

## Plugins
### Currency

```ebnf
<currencyRule>      := ':' <space> <currencyArgument> <space> <target>
<currencyArgument>  := <number> | <rule> | <specialNumbers>
<target>            := '|' <space> <rule> | '|' <space> ascii
```

The syntax will apply, if the rule set is hooked up the parser reads `currency` as `<ruleId>`.

### Grammar

```ebnf
<grammarRule>       := ':' <space> <grammarArgument> <space> <option>
<grammarArgument>   :=  [::keyword::] | <rule>
```

The syntax will apply, if the rule set is hooked up the parser reads `grammar` as `<ruleId>`.
Possible keywords are: `nominative`, `genitive`, `dative`, `accusative`, `ablative`, `instrumental`, `locative`, `vocative`, `inessive`, `illative`, `partitive`, `elative`, `prefix`, `superessive`, `allative`, `equative`, `comitative`, `rol`, `ba`, `k`, `ainmlae`, `orodnik`, `lokatiw`, `lokativ`, `mestnik`, `prefixed` and `תחילית`.
`[::keyword::]` refers to those keywords and they can be written in a case insensitive manor.

### Measurement

```ebnf
<measurementRule>       := ':' <space> <measurementArgument> <space> <unit> <perUnit>
<measurementArgument>   := <number> | <rule> | <specialNumbers>
<unit>                  := '|' <space> [::keyword::] | '|' <space> <rule>
<perUnit>               := <space> '|' [::keyword::] | '|' <space> <rule> | ε
```

The syntax will apply, if the rule set is hooked up the parser reads `measurement` as `<ruleId>`.
Possible keywords are: `acre`, `acre_foot`, `ampere`, `arc_minute`, `arc_second`, `astronomical_unit`, `bit`, `bushel`, `byte`, `calorie`, `carat`, `celsius`, `centiliter`, `centimeter`, `cubic_centimeter`, `cubic_foot`, `cubic_inch`, `cubic_kilometer`, `cubic_meter`, `cubic_mile`, `cubic_yard`, `cup`, `day`, `deciliter`, `decimeter`, `degree`, `fahrenheit`, `fathom`, `fluid_ounce`, `foodcalorie`, `foot`, `furlong`, `gallon`, `gigabit`, `gigabyte`, `gigahertz`, `gigawatt`, `gram`, `g_force`, `hectare`, `hectoliter`, `hectopascal`, `hertz`, `horsepower`, `hour`, `inch`, `inch_hg`, `joule`, `karat`, `kelvin`, `kilobit`, `kilobyte`, `kilocalorie`, `kilogram`, `kilohertz`, `kilojoule`, `kilometer`, `kilometer_per_hour`, `kilowatt`, `kilowatt_hour`, `light_year`, `liter`, `liter_per_kilometer`, `lux`, `megabit`, `megabyte`, `megahertz`, `megaliter`, `megawatt`, `meter`, `meter_per_second`, `meter_per_second_squared`, `metric_ton`, `microgram`, `micrometer`, `microsecond`, `mile`, `mile_per_gallon`, `mile_per_hour`, `milliampere`, `millibar`, `milligram`, `milliliter`, `millimeter`, `millimeter_of_mercury`, `millisecond`, `milliwatt`, `minute`, `month`, `nanometer`, `nanosecond`, `nautical_mile`, `ohm`, `ounce`, `ounce_troy`, `parsec`, `picometer`, `pint`, `pound`, `pound_per_square_inch`, `quart`, `radian`, `second`, `square_centimeter`, `square_foot`, `square_inch`, `square_kilometer`, `square_meter`, `square_mile`, `square_yard`, `stone`, `tablespoon`, `teaspoon`, `terabit`, `terabyte`, `ton`, `volt`, `watt`, `week`, `yard` and `year`.
`[::keyword::]` refers to those keywords and they can be written in a case insensitive manor.

### Time
Time is a family of Plugins, which has common base as well as their specific Implementation

#### Generic
`<dateVariable>`, which is defined as:

```ebnf
<dateVariable>  := <rule> | <sign> integer
```

The difference between `<simpleRule>` and `<dateVariable>` is not in the syntax, it is in the semantic meaning.
The 2nd shared time rule is `<calendar>`, which is defined as:

```ebnf
<calendar>  := '|' <space> [::keywordCalendar::] | '|' <space> <rule>  | ε
```
Possible calendars are `buddhist`, `chinese`, `coptic`, `ethiopian`, `gregorian`, `hebrew`, `indian`, `islamic`, `japanese` and `taiwanese`.
`[::keywordCalendar::]` refers to those keywords and they can be written in a case insensitive manor.
The 3rd shared time rule is `<timezone>`, which is defined as:

```ebnf
<timezone>  := '|' '!' <space> <sign> integer | '!' <space> <rule> | ε
<sign>      := '+' | '-' | ε
```

At least `<genericTime>` rule:

```ebnf
<genericTime>       := ':' <space> <dateVariable> <space> <calendar> <space> <timezone>
```
#### Date

```ebnf
<dateRule>          := <genericTime>
```

The syntax will apply, if the rule set is hooked up the parser reads `date` as `<ruleId>`.

#### Time

```ebnf
<timeRule>          := <genericTime>
```

The syntax will apply, if the rule set is hooked up the parser reads `time` as `<ruleId>`.

#### FullTime

```ebnf
<dateTimeRule>      := <genericTime>
```

The syntax will apply, if the rule set is hooked up the parser reads `fulltime` as `<ruleId>`.

#### CustomTime

```ebnf
<customTimeRule>    := ':' <space> <dateVariable> <space> <option> <space> <calendar> <space> <timezone>
```

The syntax will apply, if the rule set is hooked up the parser reads `customtime` as `<ruleId>`.

### Relative Time

```ebnf
<relativeTime>      := ':' <space> <quantity> <space> <unit>
<quantity>          :=  <number> | <rule> | <specialNumbers> | [::keywordDirection::]
<unit>              := '|' <space> [::keywordRelativeUnit::] | '|' <space> <rule>
```

The syntax will apply, if the rule set is hooked up the parser reads `relativeTime` as `<ruleId>`.
Possible keywords for direction are: `last`, `next`, `none` and `this`.
Possible keywords for unit are: `monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`, `sunday`, `now`, `second`, `minute`, `hour`, `day`, `week`, `month`, `quarter` and `year`.

### And
And will triggered, if the parser reads `and` as `<ruleId>`. It also requires no special syntax and falls back to `<generic>`.
