grammar MermaidClass;

// ========== PARSER RULES ==========

classDiagram
    : 'classDiagram' statement* EOF
    ;

// Design Pattern: Tagged Alternatives
statement
    : classDeclaration          # ClassDeclStmt
    | relationship              # RelationshipStmt
    | note                      # NoteStmt
    ;

// Design Pattern: Optional Components
classDeclaration
    : 'class' className stereotype? classBody?     # FullClass
    | 'class' className stereotype?                # SimpleClass
    ;

stereotype
    : '<<' stereotypeType '>>'
    ;

stereotypeType
    : 'abstract' | 'interface' | 'enumeration' | 'service' 
    | 'repository' | 'controller' | 'entity' | IDENTIFIER
    ;

className
    : IDENTIFIER
    ;

classBody
    : '{' classMember* '}'
    ;

// Design Pattern: Member Dispatch
classMember
    : attribute         # AttributeMember
    | method            # MethodMember
    | enumValue         # EnumValueMember
    ;

// Enhanced attribute parsing with annotations
attribute
    : annotation* visibility? name=IDENTIFIER ':' type multiplicity? constraint*
    | annotation* visibility? type name=IDENTIFIER constraint*
    ;

annotation
    : '@' annotationType ('(' annotationParams ')')?
    ;

annotationType
    : 'NotNull' | 'Unique' | 'Size' | 'Email' | 'JsonIgnore' 
    | 'Transient' | 'Temporal' | 'Lob' | 'Column' | 'Id' 
    | 'GeneratedValue' | 'OneToMany' | 'ManyToOne' | IDENTIFIER
    ;

annotationParams
    : annotationParam (',' annotationParam)*
    ;

annotationParam
    : IDENTIFIER '=' (STRING | NUMBER | BOOLEAN)
    ;

constraint
    : '[' constraintType ('=' constraintValue)? ']'
    ;

constraintType
    : 'min' | 'max' | 'length' | 'pattern' | 'nullable' | 'unique'
    ;

constraintValue
    : NUMBER | STRING | BOOLEAN
    ;

visibility
    : '+' | '-' | '#' | '~'
    ;

type
    : primitiveType
    | collectionType
    | customType
    ;

primitiveType
    : 'String' | 'Integer' | 'Long' | 'Float' | 'Double' 
    | 'Boolean' | 'UUID' | 'Date' | 'LocalDateTime' 
    | 'BigDecimal' | 'Instant' | 'LocalDate' | 'LocalTime'
    | 'Byte' | 'Short' | 'Character' | 'byte[]' | 'JSON'
    ;

collectionType
    : 'List' '<' type '>'
    | 'Set' '<' type '>'
    | 'Map' '<' type ',' type '>'
    | 'Collection' '<' type '>'
    ;

customType
    : IDENTIFIER ('<' typeArgs '>')? ('[' ']')*
    ;

typeArgs
    : type (',' type)*
    ;

multiplicity
    : '[' min=NUMBER '..' max=(NUMBER | '*') ']'
    | '[' exact=NUMBER ']'
    ;

// Enhanced method parsing with annotations
method
    : annotation* visibility? name=IDENTIFIER '(' parameterList? ')' returnType? methodModifier*
    ;

methodModifier
    : 'abstract' | 'static' | 'final' | 'synchronized' | 'native'
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : paramName=IDENTIFIER ':' type
    | type paramName=IDENTIFIER
    ;

returnType
    : ':' type
    | type
    ;

enumValue
    : IDENTIFIER
    ;

// Simplified relationships
relationship
    : className relType className label?
    ;

relType
    : '<|--'                    # InheritanceRel
    | '-->'                     # AssociationRel
    | '"' cardinality '"' '-->' '"' cardinality '"'  # CardinalityRel
    | '*--'                     # CompositionLeft
    | '--*'                     # CompositionRight
    | 'o--'                     # AggregationLeft
    | '--o'                     # AggregationRight
    ;

cardinality
    : NUMBER | '*' | NUMBER '..' NUMBER | NUMBER '..' '*'
    ;

label
    : ':' IDENTIFIER
    | STRING
    ;

note
    : 'note' 'for' className STRING     # NoteFor
    | 'note' STRING                      # SimpleNote
    ;

// ========== LEXER RULES ==========

// Keywords (highest priority)
CLASSDIAGRAM    : 'classDiagram';
CLASS           : 'class';
NOTE            : 'note';
FOR             : 'for';

// Relations (order matters - longest first!)
INHERIT         : '<|--';
ASSOC_RIGHT     : '-->';
COMPO_LEFT      : '*--';
COMPO_RIGHT     : '--*';
AGGREG_LEFT     : 'o--';
AGGREG_RIGHT    : '--o';

// Visibility
PLUS            : '+';
MINUS           : '-';
HASH            : '#';
TILDE           : '~';

// Delimiters
LSTEREO         : '<<';
RSTEREO         : '>>';
LBRACE          : '{';
RBRACE          : '}';
LPAREN          : '(';
RPAREN          : ')';
LBRACKET        : '[';
RBRACKET        : ']';
LANGLE          : '<';
RANGLE          : '>';
COLON           : ':';
COMMA           : ',';
DOTDOT          : '..';
STAR            : '*';
QUOTE           : '"';

// Literals
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER          : [0-9]+('.'[0-9]+)?;
STRING          : '"' (~["\r\n])* '"';
BOOLEAN         : 'true' | 'false';
AT              : '@';
EQUALS          : '=';

// Whitespace and Comments
WS              : [ \t\r\n]+ -> skip;
LINE_COMMENT    : '%%' ~[\r\n]* -> skip;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip;