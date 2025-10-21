grammar MermaidClass;

// Entry point
diagram
    : 'classDiagram' (classDecl | relationship)* EOF
    ;

classDecl
    : 'class' className '{' member* '}'
    ;

member
    : field
    | method
    ;

field
    : visibility? type identifier
    ;

method
    : visibility? returnType identifier '(' paramList? ')'
    ;

relationship
    : className relType multiplicity? className multiplicity?
    ;

paramList
    : parameter (',' parameter)*
    ;

parameter
    : type identifier
    ;

// Lexer rules
className : IDENTIFIER ;
type : IDENTIFIER ;
identifier : IDENTIFIER ;
returnType : IDENTIFIER | 'void' ;

visibility
    : '+' | '-' | '#' | '~'
    ;

relType
    : '-->' | '<|--' | '*--' | 'o--' | '"1"' '-->' '"*"' | '"*"' '-->' '"1"'
    ;

multiplicity
    : '"' NUMBER '..' (NUMBER | '*') '"'
    ;

IDENTIFIER : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER : [0-9]+ ;
WS : [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;