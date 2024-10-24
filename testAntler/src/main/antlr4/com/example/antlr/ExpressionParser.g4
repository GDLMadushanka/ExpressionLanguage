parser grammar ExpressionParser;

options {
    tokenVocab = ExpressionLexer;
}

expression
    : comparisonExpression
    | EOF
    ;

comparisonExpression
    : logicalExpression ( (GT | LT | GTE | LTE | EQ | NEQ) logicalExpression )*
    ;

logicalExpression
    : arithmeticExpression (AND logicalExpression | OR logicalExpression)?
    ;

arithmeticExpression
    : term ( (PLUS | MINUS) term )*
    ;

term
    : factor ( (ASTERISK | DIV | MODULO) factor )*
    ;

factor
    : literal
    | functionCall
    | variableAccess
    | payloadAccess
    | LPAREN expression RPAREN
    ;

literal
    : arrayLiteral
    | BOOLEAN_LITERAL
    | NUMBER
    | STRING_LITERAL
    ;

variableAccess
    : VAR ( DOT ID  // Dot notation: var.variableName
          | (DOT)? LBRACKET STRING_LITERAL RBRACKET  // Bracket notation: var["variableName"]
          )
      ( (DOUBLE_DOT ASTERISK    // Handles recursive descent and wildcard, like var.variableName..*
        | DOT ASTERISK          // Handles wildcard after dot notation, like var.variableName.*
        | DOT ID
        | LBRACKET arrayIndex RBRACKET   // Handles array access, like var.variableName[...]
        | DOT LBRACKET arrayIndex RBRACKET   // Handles dot followed by bracket notation, like var.variableName.["property"]
        )*
      )
    ;

arrayLiteral
    : LBRACKET (expression (COMMA expression)*)? RBRACKET // Array with zero or more literals, separated by commas
    ;

payloadAccess
    : PAYLOAD ( (DOUBLE_DOT ASTERISK
    | DOUBLE_DOT ID
    | DOT ID
    | LBRACKET arrayIndex RBRACKET
    | DOT LBRACKET arrayIndex RBRACKET
    | DOT ASTERISK)*
    | DOUBLE_DOT ID (LBRACKET arrayIndex RBRACKET)? )
    ;

arrayIndex
    : NUMBER
    | STRING_LITERAL
    | expression
    | multipleArrayIndices
    | sliceArrayIndex
    | expression ( (PLUS | MINUS | MULT | DIV ) expression)*
    | ASTERISK
    | QUESTION filterExpression
    ;

multipleArrayIndices
    : expression (COMMA expression)+
    ;

sliceArrayIndex
    : signedExpressions? COLON signedExpressions? (COLON signedExpressions?)?
    ;

signedExpressions
    : MINUS? expression
    ;

filterExpression
    : (filterComponent)+
    ;

filterComponent
    : variableAccess
    | payloadAccess
    | stringOrOperator
    ;

stringOrOperator
    : QUESTION | AT | JSONPATH_FUNCTIONS| STRING_LITERAL |NUMBER | BOOLEAN_LITERAL | ID | GT | LT | GTE | LTE | EQ | NEQ
    | PLUS | MINUS | MULT | DIV | LPAREN | RPAREN | DOT | COMMA | COLON | WS | AND | OR | NOT | ASTERISK
    ;

functionCall
    : LENGTH LPAREN expression RPAREN
    | TOUPPER LPAREN expression RPAREN
    | TOLOWER LPAREN expression RPAREN
    | SUBSTRING LPAREN expression COMMA expression (COMMA expression)? RPAREN
    | STARTSWITH LPAREN expression COMMA expression RPAREN
    | ENDSWITH LPAREN expression COMMA expression RPAREN
    | CONTAINS LPAREN expression COMMA expression RPAREN
    | TRIM LPAREN expression RPAREN
    | REPLACE LPAREN expression COMMA expression COMMA expression RPAREN
    | SPLIT LPAREN expression COMMA expression RPAREN
    | ABS LPAREN expression RPAREN
    | FLOOR LPAREN expression RPAREN
    | CEIL LPAREN expression RPAREN
    | SQRT LPAREN expression RPAREN
    | LOG LPAREN expression RPAREN
    | POW LPAREN expression COMMA expression RPAREN
    | REGISTRY LPAREN expression RPAREN (DOT GETPROPERTY LPAREN expression RPAREN)?
    | SECRET LPAREN expression RPAREN
    | BASE64ENCODE LPAREN expression (COMMA expression)? RPAREN
    | BASE64DECODE LPAREN expression RPAREN
    | URLENCODE LPAREN expression (COMMA expression)? RPAREN
    | URLDECODE LPAREN expression RPAREN
    | ISNUMBER LPAREN expression RPAREN
    | ISSTRING LPAREN expression RPAREN
    | ISARRAY LPAREN expression RPAREN
    | ISOBJECT LPAREN expression RPAREN
    | NOW LPAREN RPAREN
    | TODAY LPAREN STRING_LITERAL RPAREN
    | FORMATDATE LPAREN expression COMMA STRING_LITERAL RPAREN
    | INTEGER LPAREN expression RPAREN
    | FLOAT LPAREN expression RPAREN
    | STRING LPAREN expression RPAREN
    | BOOLEAN LPAREN expression RPAREN
    ;
