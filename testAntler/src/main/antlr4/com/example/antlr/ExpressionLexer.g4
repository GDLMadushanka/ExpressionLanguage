lexer grammar ExpressionLexer;

JSONPATH_FUNCTIONS: 'contains ' | 'in' | 'nin' | 'subsetof' | 'size' | 'empty' | 'empty true' | 'empty false' | '=~';

// Tokens for identifiers, operators, and keywords
VAR: 'var';
PAYLOAD: 'payload' | '$';
REGISTRY: 'registry';
SECRET: 'secret';
BASE64ENCODE: 'base64encode';
BASE64DECODE: 'base64decode';
URLENCODE: 'urlEncode';
URLDECODE: 'urlDecode';
NOW: 'now';
TODAY: 'today';
FORMATDATE: 'formatDate';
ISNUMBER: 'isNumber';
ISSTRING: 'isString';
ISARRAY: 'isArray';
ISOBJECT: 'isObject';
INTEGER: 'integer';
FLOAT: 'float';
STRING: 'string';
BOOLEAN: 'boolean';
ABS: 'abs';
FLOOR: 'floor';
CEIL: 'ceil';
SQRT: 'sqrt';
LOG: 'log';
POW: 'pow';
LENGTH: 'length';
TOUPPER: 'toUpper';
TOLOWER: 'toLower';
SUBSTRING: 'subString';
STARTSWITH: 'startsWith';
ENDSWITH: 'endsWith';
CONTAINS: 'contains';
TRIM: 'trim';
REPLACE: 'replace';
SPLIT: 'split';
AND: 'and' | '&&';
OR: 'or' | '||';
NOT: 'not' | '!';

DOUBLE_DOT : '..';
ASTERISK : '*';

// Operators
PLUS: '+';
MINUS: '-';
DIV: '/';
MODULO: '%';
EQ: '==';
NEQ: '!=';
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';

// Delimiters
LPAREN: '(';
RPAREN: ')';
LBRACKET: '[';
RBRACKET: ']';
DOT: '.';
COMMA: ',';
COLON: ':';
QUOTE: '"' | '\'';

// Literals
BOOLEAN_LITERAL: 'true' | 'false';
NUMBER: '-'? [0-9]+ ('.' [0-9]+)?;


STRING_LITERAL : ('"' (ESC | ~["\\])* '"' | '\'' (ESC | ~['\\])* '\'');


fragment ESC
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

// Identifiers
ID: [a-zA-Z_][a-zA-Z_0-9]*;
GETPROPERTY: 'getProperty';
// Special symbols for JSONPath filter expressions
QUESTION: '?';
AT: '@';

// Whitespace
WS: [ \t\n\r]+ -> skip;
