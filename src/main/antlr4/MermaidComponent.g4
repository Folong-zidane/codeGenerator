grammar MermaidComponent;

// Grammaire pour les diagrammes de composants UML

componentDiagram
    : 'componentDiagram' statement* EOF
    ;

statement
    : componentDecl
    | interfaceDecl
    | dependencyDecl
    | packageDecl
    | noteDecl
    ;

componentDecl
    : 'component' componentName=IDENTIFIER ('as' alias=STRING)? componentBody?
    ;

componentBody
    : '{' statement* '}'
    ;

interfaceDecl
    : 'interface' interfaceName=IDENTIFIER ('as' alias=STRING)?
    ;

packageDecl
    : 'package' packageName=IDENTIFIER ('as' alias=STRING)? packageBody?
    ;

packageBody
    : '{' statement* '}'
    ;

dependencyDecl
    : source=IDENTIFIER arrow target=IDENTIFIER (':' label=STRING)?
    ;

arrow
    : '-->'     // Uses dependency
    | '<--'     // Provides dependency
    | '<-->'    // Bidirectional
    | '..'      // Realizes/Implements
    | '--'      // Association
    ;

noteDecl
    : 'note' position? 'of' componentName=IDENTIFIER ':' noteText=STRING
    | 'note' position? ':' noteText=STRING
    ;

position
    : 'left' | 'right' | 'top' | 'bottom'
    ;

// Tokens
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING          : '"' (~["\r\n])* '"' | '\'' (~['\r\n])* '\'' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;