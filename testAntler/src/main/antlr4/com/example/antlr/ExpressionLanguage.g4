grammar ExpressionLanguage;

// Parser Rules
expression
    : conditionalExpression
    | logicalOrExpression
    ;

conditionalExpression
    : IF expression THEN expression ELSE expression
    ;

logicalOrExpression
    : logicalAndExpression (OR logicalAndExpression)*
    ;

logicalAndExpression
    : equalityExpression (AND equalityExpression)*
    ;

equalityExpression
    : relationalExpression ((EQ | NEQ) relationalExpression)*
    ;

relationalExpression
    : additiveExpression ((GT | GTE | LT | LTE) additiveExpression)*
    ;

additiveExpression
    : multiplicativeExpression ((PLUS | MINUS) multiplicativeExpression)*
    ;

multiplicativeExpression
    : unaryExpression ((MULT | DIV | MOD) unaryExpression)*
    ;

unaryExpression
    : NOT unaryExpression
    | memberExpression
    ;

memberExpression
    : primary (memberAccess)*
    ;

memberAccess
    : DOT IDENTIFIER
    | DOT methodCall
    | LBRACKET expression RBRACKET
    ;

methodCall
    : predefinedMethodCall
    | IDENTIFIER LPAREN argumentList? RPAREN
    ;

predefinedMethodCall
    : stringMethodCall
    | arrayMethodCall
    | objectMethodCall
    ;

stringMethodCall
    : (TO_UPPER | TO_LOWER | LENGTH | SUBSTRING | STARTS_WITH | ENDS_WITH | CONTAINS | TRIM | REPLACE | SPLIT)
      LPAREN argumentList? RPAREN
    ;

arrayMethodCall
    : (SORT | ORDER_BY | SLICE | ADD | REMOVE_AT | REMOVE_ELEMENT | SET)
      LPAREN argumentList? RPAREN
    ;

objectMethodCall
    : (PUT | REMOVE_KEY | RENAME_KEY | MERGE)
      LPAREN argumentList? RPAREN
    ;

mathFunctionCall
    : MATH_DOT (ABS | FLOOR | CEIL | SQRT | LOG | POW | SUM | AVERAGE | MIN | MAX) LPAREN argumentList? RPAREN
    ;

mathConstant
    : MATH_DOT (PI | E)
    ;

dateFunctionCall
    : (NOW | TODAY | FORMAT_DATE) LPAREN argumentList? RPAREN
    ;

typeCheckFunctionCall
    : (IS_NUMBER | IS_STRING | IS_ARRAY | IS_OBJECT) LPAREN argumentList? RPAREN
    ;

conversionFunctionCall
    : (TO_INT | TO_FLOAT | TO_STRING | TO_BOOLEAN) LPAREN argumentList? RPAREN
    ;

registryFunctionCall
    : REGISTRY LPAREN argumentList? RPAREN (DOT GET_PROPERTY LPAREN argumentList? RPAREN)?
    ;

secretFunctionCall
    : SECRET LPAREN argumentList? RPAREN
    ;

encodingFunctionCall
    : (B64_ENCODE | B64_DECODE | URL_ENCODE | URL_DECODE) LPAREN argumentList? RPAREN
    ;

jsonFunctionCall
    : JSON_DOT (PARSE | STRINGIFY) LPAREN argumentList? RPAREN
    ;

predefinedFunctionCall
    : mathFunctionCall
    | dateFunctionCall
    | typeCheckFunctionCall
    | conversionFunctionCall
    | registryFunctionCall
    | secretFunctionCall
    | encodingFunctionCall
    | jsonFunctionCall
    ;

primary
    : functionCall
    | variableReference
    | literal
    | mathConstant
    | LPAREN expression RPAREN
    | arrayFilterExpression
    ;

arrayFilterExpression
    : LPAREN expression WHERE expression (SORT BY expression)? (LIMIT expression)? (SELECT expression)? RPAREN
    ;

functionCall
    : predefinedFunctionCall
    | IDENTIFIER LPAREN argumentList? RPAREN
    ;

variableReference
    : baseVariable (memberAccess)*
    ;

baseVariable
    : VAR
    | PAYLOAD
    | ENV
    | IDENTIFIER
    ;

argumentList
    : expression (COMMA expression)*
    ;

literal
    : NUMBER
    | STRING
    | TRUE
    | FALSE
    | NULL
    | arrayLiteral
    ;

arrayLiteral
    : LBRACKET (expression (COMMA expression)*)? RBRACKET
    ;

// Lexer Rules
VAR         : 'var';
PAYLOAD     : 'payload';
ENV         : 'env';
IF          : 'if';
THEN        : 'then';
ELSE        : 'else';
WHERE       : 'where';
SORT        : 'sort';
BY          : 'by';
LIMIT       : 'limit';
SELECT      : 'select';
NOT         : 'not';
AND         : 'and';
OR          : 'or';
TRUE        : 'true';
FALSE       : 'false';
NULL        : 'null';

// Predefined Methods and Functions
TO_UPPER        : 'toUpper';
TO_LOWER        : 'toLower';
LENGTH          : 'length';
SUBSTRING       : 'substring';
STARTS_WITH     : 'startsWith';
ENDS_WITH       : 'endsWith';
CONTAINS        : 'contains';
TRIM            : 'trim';
REPLACE         : 'replace';
SPLIT           : 'split';

ADD             : 'add';
REMOVE_AT       : 'removeAt';
REMOVE_ELEMENT  : 'remove';
SET             : 'set';
SLICE           : 'slice';
ORDER_BY        : 'orderBy';

PUT             : 'put';
REMOVE_KEY      : 'remove';
RENAME_KEY      : 'renameKey';
MERGE           : 'merge';

MATH_DOT        : 'Math' DOT;

ABS             : 'abs';
FLOOR           : 'floor';
CEIL            : 'ceil';
SQRT            : 'sqrt';
LOG             : 'log';
POW             : 'pow';
SUM             : 'sum';
AVERAGE         : 'average';
MIN             : 'min';
MAX             : 'max';

// Math Constants
PI              : 'PI';
E               : 'E';

NOW             : 'now';
TODAY           : 'today';
FORMAT_DATE     : 'formatDate';

IS_NUMBER       : 'isNumber';
IS_STRING       : 'isString';
IS_ARRAY        : 'isArray';
IS_OBJECT       : 'isObject';

TO_INT          : 'toInt';
TO_FLOAT        : 'toFloat';
TO_STRING       : 'toString';
TO_BOOLEAN      : 'toBoolean';

REGISTRY        : 'registry';
GET_PROPERTY    : 'getProperty';

SECRET          : 'secret';

B64_ENCODE      : 'b64encode';
B64_DECODE      : 'b64decode';
URL_ENCODE      : 'urlEncode';
URL_DECODE      : 'urlDecode';

// JSON functions
JSON_DOT        : 'JSON' DOT;
PARSE           : 'parse';
STRINGIFY       : 'stringify';

PLUS        : '+';
MINUS       : '-';
MULT        : '*';
DIV         : '/';
MOD         : '%';
EQ          : '==';
NEQ         : '!=';
GT          : '>';
GTE         : '>=';
LT          : '<';
LTE         : '<=';

LPAREN      : '(';
RPAREN      : ')';
LBRACKET    : '[';
RBRACKET    : ']';
LBRACE      : '{';
RBRACE      : '}';
DOT         : '.';
COMMA       : ',';
SEMI        : ';';
COLON       : ':';
QUESTION    : '?';

STRING
    :   '"' ( ESC_SEQ | ~["\\\r\n] )* '"'
    |   '\'' ( ESC_SEQ | ~['\\\r\n] )* '\''
    ;

fragment ESC_SEQ
    :   '\\' [btnfr"'\\/]  // Basic escape sequences
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment OCTAL_ESC
    :   '\\' [0-3]? [0-7] [0-7]
    ;

fragment HEX_DIGIT
    :   [0-9a-fA-F]
    ;

NUMBER
    :   '-'? DIGIT+ ('.' DIGIT+)?  // Integer and floating-point numbers, optionally negative
    ;

IDENTIFIER
    :   LETTER (LETTER | DIGIT | '_')*
    ;

fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z];

// Whitespace and Comments
WS
    :   [ \t\r\n]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;
