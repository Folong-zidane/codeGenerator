grammar MermaidClass;

// ========== PARSER RULES ==========

// Support for YAML front matter and directives
classDiagram
    : frontMatter? directive* 'classDiagram' statement* EOF
    ;

frontMatter
    : '---' yamlContent '---'
    ;

yamlContent
    : (~('---'))* 
    ;

directive
    : '%%{' directiveContent '}%%'
    | 'direction' directionValue
    | 'title:' STRING
    ;

directiveContent
    : (~('}%%'))*
    ;

directionValue
    : 'TB' | 'TD' | 'BT' | 'RL' | 'LR'
    ;

// Design Pattern: Tagged Alternatives - Extended
statement
    : classDeclaration          # ClassDeclStmt
    | relationship              # RelationshipStmt
    | note                      # NoteStmt
    | styleStatement            # StyleStmt
    | interactionStatement      # InteractionStmt
    | namespaceStatement        # NamespaceStmt
    ;

// Style statements for Mermaid styling
styleStatement
    : 'style' className styleProperties
    | 'classDef' classDefName styleProperties
    | 'cssClass' STRING className
    ;

styleProperties
    : styleProperty (',' styleProperty)*
    ;

styleProperty
    : IDENTIFIER ':' styleValue
    ;

styleValue
    : STRING | IDENTIFIER | COLOR | NUMBER
    ;

classDefName
    : IDENTIFIER | 'default'
    ;

// Interaction statements
interactionStatement
    : 'click' className actionType STRING STRING?
    | 'link' className STRING STRING?
    | 'callback' className STRING STRING?
    ;

actionType
    : 'href' | 'call'
    ;

// Namespace support
namespaceStatement
    : 'namespace' IDENTIFIER '{' classDeclaration* '}'
    ;

// Design Pattern: Optional Components
classDeclaration
    : 'class' className stereotype? classBody?     # FullClass
    | 'class' className stereotype?                # SimpleClass
    ;

stereotype
    : '<<' stereotypeType '>>'
    ;

// More flexible stereotype types
stereotypeType
    : 'abstract' | 'interface' | 'enumeration' | 'service' 
    | 'repository' | 'controller' | 'entity' | 'enum'
    | IDENTIFIER                    # CustomStereotype
    ;

// Enhanced className with labels and special characters
className
    : IDENTIFIER                    # SimpleClassName
    | IDENTIFIER '[' STRING ']'     # ClassWithLabel
    | BACKTICK_STRING               # EscapedClassName
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
    : '[' min=NUMBER DOTDOT max=(NUMBER | '*') ']'
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

// Simplified parameter - single format to avoid ambiguity
parameter
    : type paramName=IDENTIFIER
    | paramName=IDENTIFIER ':' type
    ;

// Unambiguous return type - prioritize colon form
returnType
    : ':' type                  # ColonReturnType
    | type                      # DirectReturnType
    ;

// Enhanced enum values supporting constants
enumValue
    : ENUM_CONSTANT
    | IDENTIFIER
    ;

// Enhanced relationships with full cardinality support
relationship
    : className cardinality? relType cardinality? className label?  # CardinalityRelationship
    | className relType className label?                            # SimpleRelationship
    ;

relType
    : INHERIT                   # InheritanceRel
    | ASSOC_RIGHT               # AssociationRel
    | BIDIRECTIONAL             # BiDirectionalRel
    | COMPO_LEFT                # CompositionLeft
    | COMPO_RIGHT               # CompositionRight
    | AGGREG_LEFT               # AggregationLeft
    | AGGREG_RIGHT              # AggregationRight
    | LINK_SOLID                # LinkSolid
    | DEPENDENCY                # Dependency
    | REALIZATION               # Realization
    | LINK_DASHED               # LinkDashed
    | LOLLIPOP_LEFT             # LollipopLeft
    | LOLLIPOP_RIGHT            # LollipopRight
    ;

// Enhanced cardinality with all Mermaid formats
cardinality
    : STRING                    # QuotedCardinality
    | NUMBER                    # NumericCardinality
    | '*'                       # ManyCardinality
    | NUMBER DOTDOT NUMBER      # RangeCardinality
    | NUMBER DOTDOT '*'         # OpenRangeCardinality
    | '0' DOTDOT '1'           # ZeroToOneCardinality
    | 'many'                    # ManyTextCardinality
    ;

// Flexible labels supporting multi-word text
label
    : ':' labelText
    ;

labelText
    : IDENTIFIER (IDENTIFIER)*  # MultiWordLabel
    | STRING                    # StringLabel
    ;

// Enhanced notes with multi-line support
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
NAMESPACE       : 'namespace';
DIRECTION       : 'direction';
STYLE           : 'style';
CLASSDEF        : 'classDef';
CSSCLASS        : 'cssClass';
CLICK           : 'click';
LINK            : 'link';
CALLBACK        : 'callback';
HREF            : 'href';
CALL            : 'call';
TITLE           : 'title:';

// Direction values
TB              : 'TB';
TD              : 'TD';
BT              : 'BT';
RL              : 'RL';
LR              : 'LR';

// Stereotype keywords
ABSTRACT        : 'abstract';
INTERFACE       : 'interface';
ENUMERATION     : 'enumeration';
ENUM            : 'enum';
SERVICE         : 'service';
REPOSITORY      : 'repository';
CONTROLLER      : 'controller';
ENTITY          : 'entity';
DEFAULT         : 'default';
MANY            : 'many';

// Relations (order matters - longest first!)
BIDIRECTIONAL   : '<-->';
INHERIT         : '<|--';
REALIZATION     : '..|>';
LOLLIPOP_LEFT   : '()--';
LOLLIPOP_RIGHT  : '--()';
ASSOC_RIGHT     : '-->';
COMPO_LEFT      : '*--';
COMPO_RIGHT     : '--*';
AGGREG_LEFT     : 'o--';
AGGREG_RIGHT    : '--o';
DEPENDENCY      : '..>';
LINK_SOLID      : '--';
LINK_DASHED     : '..';
YAML_DELIM      : '---';
DIRECTIVE_START : '%%{';
DIRECTIVE_END   : '}%%';

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

// Enhanced literals with international support
ENUM_CONSTANT   : [A-Z] [A-Z0-9_ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞŸ]*;
IDENTIFIER      : [a-zA-ZÀ-ſ_] [a-zA-Z0-9À-ſ_]*;
NUMBER          : [0-9]+('.'[0-9]+)?;
STRING          : '"' (~["\r\n])* '"';
BACKTICK_STRING : '`' (~[`\r\n])* '`';
COLOR           : '#' [0-9a-fA-F]+;
BOOLEAN         : 'true' | 'false';
AT              : '@';
EQUALS          : '=';

// Enhanced whitespace and comments
WS              : [ \t\r\n]+ -> skip;
LINE_COMMENT    : '%%' ~[\r\n]* -> skip;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip;

// Error handling for unknown characters
UNKNOWN         : . ;