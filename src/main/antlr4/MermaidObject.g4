grammar MermaidObject;

// Grammaire pour les diagrammes d'objet UML

objectDiagram
    : 'objectDiagram' statement* EOF
    ;

statement
    : objectDecl
    | linkDecl
    | noteDecl
    ;

objectDecl
    : 'object' objectName=IDENTIFIER ('as' alias=STRING)? objectBody?
    ;

objectBody
    : '{' attributeInstance* '}'
    ;

attributeInstance
    : attributeName=IDENTIFIER '=' attributeValue=(STRING | NUMBER | BOOLEAN)
    ;

linkDecl
    : source=IDENTIFIER '--' target=IDENTIFIER (':' label=STRING)?
    | source=IDENTIFIER '-->' target=IDENTIFIER (':' label=STRING)?
    | source=IDENTIFIER '<--' target=IDENTIFIER (':' label=STRING)?
    | source=IDENTIFIER '<-->' target=IDENTIFIER (':' label=STRING)?
    ;

noteDecl
    : 'note' position? 'of' objectName=IDENTIFIER ':' noteText=STRING
    | 'note' position? ':' noteText=STRING
    ;

position
    : 'left' | 'right' | 'top' | 'bottom'
    ;

// Tokens
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING          : '"' (~["\r\n])* '"' | '\'' (~['\r\n])* '\'' ;
NUMBER          : [0-9]+ ('.' [0-9]+)? ;
BOOLEAN         : 'true' | 'false' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;