grammar MermaidActivity;

// Grammaire complète pour les diagrammes d'activité UML

activityDiagram
    : 'activityDiagram' statement* EOF
    ;

statement
    : initialNode
    | finalNode
    | activityNode
    | decisionNode
    | mergeNode
    | forkNode
    | joinNode
    | transition
    | swimlane
    | note
    ;

initialNode
    : '[*]'
    ;

finalNode
    : '[*]' | 'end'
    ;

activityNode
    : IDENTIFIER ('(' STRING ')')?
    | 'activity' IDENTIFIER ('as' STRING)?
    ;

decisionNode
    : 'decision' IDENTIFIER ('as' STRING)?
    | 'if' '(' condition=STRING ')' 'then'
    ;

mergeNode
    : 'merge' IDENTIFIER
    | 'endif'
    ;

forkNode
    : 'fork' IDENTIFIER
    | 'parallel'
    ;

joinNode
    : 'join' IDENTIFIER
    | 'endparallel'
    ;

swimlane
    : 'partition' swimlaneName=STRING '{' statement* '}'
    | 'swimlane' swimlaneName=IDENTIFIER '{' statement* '}'
    ;

transition
    : source=nodeRef arrow target=nodeRef (':' guard=STRING)?
    ;

nodeRef
    : IDENTIFIER | '[*]' | 'end'
    ;

arrow
    : '-->' | '->' | '==>' | '=>'
    ;

note
    : 'note' position? 'of' nodeRef ':' noteText=STRING
    | 'note' position? ':' noteText=STRING
    ;

position
    : 'left' | 'right' | 'top' | 'bottom'
    ;

// Control flow constructs
loopConstruct
    : 'while' '(' condition=STRING ')' statement* 'endwhile'
    | 'repeat' statement* 'until' '(' condition=STRING ')'
    ;

conditionalConstruct
    : 'if' '(' condition=STRING ')' 'then' statement* 
      ('else' statement*)? 'endif'
    ;

parallelConstruct
    : 'parallel' statement* ('and' statement*)* 'endparallel'
    ;

// Tokens
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING          : '"' (~["\r\n])* '"' | '\'' (~['\r\n])* '\'' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;