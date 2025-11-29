grammar MermaidSequence;

// ========== PARSER RULES ==========

sequenceDiagram
    : frontMatter? directive* 'sequenceDiagram' statement* EOF
    ;

frontMatter
    : '---' yamlContent '---'
    ;

yamlContent
    : (~('---'))*
    ;

directive
    : 'autonumber'
    | '%%{' directiveContent '}%%'
    ;

directiveContent
    : (~('}%%'))*
    ;

statement
    : participantDecl           # ParticipantStmt
    | actorDecl                 # ActorStmt
    | message                   # MessageStmt
    | combinedFragment          # CombinedFragmentStmt
    | note                      # NoteStmt
    | boxBlock                  # BoxStmt
    | activation                # ActivationStmt
    | linkDecl                  # LinkStmt
    | createDestroy             # CreateDestroyStmt
    | backgroundRect            # BackgroundStmt
    ;

// Participants and Actors
participantDecl
    : 'participant' participantId participantConfig? alias?
    ;

actorDecl
    : 'actor' participantId alias?
    ;

participantConfig
    : '@{' configParams '}'
    ;

configParams
    : configParam (',' configParam)*
    ;

configParam
    : STRING ':' STRING
    ;

alias
    : 'as' STRING
    ;

participantId
    : IDENTIFIER
    ;

// Messages
message
    : sender=participantId arrow receiver=participantId ':' messageText
    | sender=participantId arrow activation? receiver=participantId ':' messageText
    ;

arrow
    : '->'          # SolidArrow
    | '-->'         # DottedArrow
    | '->>'         # SolidArrowHead
    | '-->>'        # DottedArrowHead
    | '<<->>'       # BiDirectionalSolid
    | '<<-->>'      # BiDirectionalDotted
    | '-x'          # SolidCross
    | '--x'         # DottedCross
    | '-)'          # SolidAsync
    | '--)'         # DottedAsync
    ;

messageText
    : STRING
    | IDENTIFIER (IDENTIFIER)*
    ;

// Activations
activation
    : 'activate' participantId      # ActivateLong
    | 'deactivate' participantId    # DeactivateLong
    | '+'                           # ActivateShorthand
    | '-'                           # DeactivateShorthand
    ;

// Combined Fragments
combinedFragment
    : altBlock
    | optBlock
    | loopBlock
    | parBlock
    | criticalBlock
    | breakBlock
    ;

altBlock
    : 'alt' condition statement* ('else' condition statement*)* 'end'
    ;

optBlock
    : 'opt' condition statement* 'end'
    ;

loopBlock
    : 'loop' condition statement* 'end'
    ;

parBlock
    : 'par' condition statement* ('and' condition statement*)* 'end'
    ;

criticalBlock
    : 'critical' condition statement* ('option' condition statement*)* 'end'
    ;

breakBlock
    : 'break' condition statement* 'end'
    ;

condition
    : STRING
    | IDENTIFIER (IDENTIFIER)*
    ;

// Notes
note
    : 'note' notePosition participantId ':' noteText
    | 'note' 'over' participantList ':' noteText
    ;

notePosition
    : 'left' 'of'
    | 'right' 'of'
    | 'over'
    ;

participantList
    : participantId (',' participantId)*
    ;

noteText
    : STRING
    | IDENTIFIER (IDENTIFIER)*
    ;

// Box grouping
boxBlock
    : 'box' boxColor? boxLabel? statement* 'end'
    ;

boxColor
    : COLOR
    | 'transparent'
    | 'rgb(' NUMBER ',' NUMBER ',' NUMBER ')'
    | 'rgba(' NUMBER ',' NUMBER ',' NUMBER ',' NUMBER ')'
    ;

boxLabel
    : STRING
    ;

// Links
linkDecl
    : 'link' participantId ':' linkLabel '@' linkUrl
    | 'links' participantId ':' jsonLinks
    ;

linkLabel
    : STRING
    ;

linkUrl
    : STRING
    ;

jsonLinks
    : '{' jsonLinkPair (',' jsonLinkPair)* '}'
    ;

jsonLinkPair
    : STRING ':' STRING
    ;

// Create/Destroy
createDestroy
    : 'create' participantType participantId alias?
    | 'destroy' participantId
    ;

participantType
    : 'participant'
    | 'actor'
    ;

// Background rectangles
backgroundRect
    : 'rect' rectColor statement* 'end'
    ;

rectColor
    : 'rgb(' NUMBER ',' NUMBER ',' NUMBER ')'
    | 'rgba(' NUMBER ',' NUMBER ',' NUMBER ',' NUMBER ')'
    ;

// ========== LEXER RULES ==========

// Keywords
SEQUENCEDIAGRAM : 'sequenceDiagram';
PARTICIPANT     : 'participant';
ACTOR           : 'actor';
AS              : 'as';
NOTE            : 'note';
OVER            : 'over';
LEFT            : 'left';
RIGHT           : 'right';
OF              : 'of';
BOX             : 'box';
RECT            : 'rect';
LINK            : 'link';
LINKS           : 'links';
CREATE          : 'create';
DESTROY         : 'destroy';
ACTIVATE        : 'activate';
DEACTIVATE      : 'deactivate';
AUTONUMBER      : 'autonumber';

// Combined fragments
ALT             : 'alt';
ELSE            : 'else';
OPT             : 'opt';
LOOP            : 'loop';
PAR             : 'par';
AND             : 'and';
CRITICAL        : 'critical';
OPTION          : 'option';
BREAK           : 'break';
END             : 'end';

// Colors
TRANSPARENT     : 'transparent';

// Arrows (order matters - longest first!)
BIDIRECTIONAL_DOTTED : '<<-->>';
BIDIRECTIONAL_SOLID  : '<<->>';
DOTTED_ARROWHEAD     : '-->>';
SOLID_ARROWHEAD      : '->>';
DOTTED_ASYNC         : '-->';
SOLID_ASYNC          : '->';
DOTTED_CROSS         : '--x';
SOLID_CROSS          : '-x';
DOTTED_OPEN          : '--)';
SOLID_OPEN           : '-)';

// Delimiters
LBRACE          : '{';
RBRACE          : '}';
LPAREN          : '(';
RPAREN          : ')';
LBRACKET        : '[';
RBRACKET        : ']';
COLON           : ':';
COMMA           : ',';
AT              : '@';
PLUS            : '+';
MINUS           : '-';
YAML_DELIM      : '---';
DIRECTIVE_START : '%%{';
DIRECTIVE_END   : '}%%';

// Literals
IDENTIFIER      : [a-zA-ZÀ-ſ_] [a-zA-Z0-9À-ſ_]*;
NUMBER          : [0-9]+ ('.' [0-9]+)?;
STRING          : '"' (~["\r\n] | '\\"')* '"';
COLOR           : '#' [0-9a-fA-F]+;

// Whitespace and Comments
WS              : [ \t\r\n]+ -> skip;
LINE_COMMENT    : '%%' ~[\r\n]* -> skip;

// Error handling
UNKNOWN         : .;