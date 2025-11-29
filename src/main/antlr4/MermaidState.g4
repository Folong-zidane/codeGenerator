grammar MermaidState;

// Grammaire complète pour les diagrammes d'état-transition Mermaid

stateDiagram
    : 'stateDiagram' ('-v2')? statement* EOF
    ;

statement
    : stateDeclaration
    | transition
    | startTransition
    | endTransition
    | compositeState
    | note
    | direction
    | choice
    | fork
    | join
    | history
    | concurrency
    ;

stateDeclaration
    : stateId ':' stateLabel
    | stateId
    ;

transition
    : sourceState arrow targetState (':' transitionLabel)?
    | sourceState arrow targetState ':' transitionLabel ('/' action)?
    | sourceState arrow targetState '[' guard ']' ('/' action)?
    | sourceState arrow targetState ':' transitionLabel '[' guard ']' ('/' action)?
    ;

startTransition
    : '[*]' arrow stateId (':' transitionLabel)?
    ;

endTransition
    : stateId arrow '[*]' (':' transitionLabel)?
    ;

compositeState
    : 'state' stateId '{' statement* '}'
    ;

note
    : 'note' position 'of' stateId ':' noteText
    | 'note' position 'of' stateId NEWLINE noteText
    ;

direction
    : 'direction' directionValue
    ;

choice
    : 'state' choiceId '<<choice>>'
    ;

fork
    : 'state' forkId '<<fork>>'
    ;

join
    : 'state' joinId '<<join>>'
    ;

history
    : 'state' historyId '<<history>>'
    | 'state' historyId '<<history,type=deep>>'
    | 'state' historyId '<<history,type=shallow>>'
    ;

concurrency
    : '--'
    ;

// Éléments de base
sourceState     : stateId | '[*]' ;
targetState     : stateId | '[*]' ;
stateId         : IDENTIFIER | STRING ;
stateLabel      : STRING | MULTILINE_STRING ;
transitionLabel : STRING | MULTILINE_STRING | IDENTIFIER ;
noteText        : STRING | MULTILINE_STRING ;
choiceId        : IDENTIFIER ;
forkId          : IDENTIFIER ;
joinId          : IDENTIFIER ;
historyId       : IDENTIFIER ;
guard           : STRING | IDENTIFIER | expression ;
action          : STRING | IDENTIFIER | methodCall ;
position        : 'left' | 'right' | 'top' | 'bottom' ;
directionValue  : 'TB' | 'BT' | 'RL' | 'LR' ;
arrow           : '-->' | '->' ;

// Expressions et appels de méthodes
expression
    : IDENTIFIER operator IDENTIFIER
    | IDENTIFIER operator STRING
    | IDENTIFIER operator NUMBER
    | '(' expression ')'
    | expression logicalOperator expression
    ;

methodCall
    : IDENTIFIER '(' parameterList? ')'
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : IDENTIFIER | STRING | NUMBER
    ;

operator
    : '==' | '!=' | '<' | '>' | '<=' | '>=' | '=' | '+' | '-' | '*' | '/'
    ;

logicalOperator
    : '&&' | '||' | 'and' | 'or' | '&' | '|'
    ;

// Tokens
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING          : '"' (~["\r\n])* '"' | '\'' (~['\r\n])* '\'' ;
MULTILINE_STRING: '"""' .*? '"""' | '\'\'\'' .*? '\'\'\'' ;
NUMBER          : [0-9]+ ('.' [0-9]+)? ;
NEWLINE         : '\r'? '\n' ;
WS              : [ \t]+ -> skip ;
LINE_COMMENT    : '%%' ~[\r\n]* -> skip ;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;

// Caractères spéciaux
LBRACE          : '{' ;
RBRACE          : '}' ;
LBRACKET        : '[' ;
RBRACKET        : ']' ;
LPAREN          : '(' ;
RPAREN          : ')' ;
COLON           : ':' ;
SEMICOLON       : ';' ;
COMMA           : ',' ;
DOT             : '.' ;
SLASH           : '/' ;
DASH            : '-' ;
STAR            : '*' ;
PLUS            : '+' ;
EQUALS          : '=' ;