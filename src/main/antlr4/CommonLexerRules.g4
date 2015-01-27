lexer grammar CommonLexerRules; 

NEWLINE:  '\r' ? '\n'    ; // return newlines to parser (end-statement signal)
ID  :     [a-zA-Z]+      ; // match identifiers
WS  :     [ \t]+ -> skip ; // toss out whitespace

MUL :   '*' ;
DIV :   '/' ;
ADD :   '+' ;
SUB :   '-' ;
POW :   '^' ;

LEQ : '.leq.' ;
GEQ	: '.geq'  ;
LT	: '.lt.'  ;
GT	: '.gt.'  ;
EQ	: '.eq.'  ;
NEQ	: '.neq.' ;
AND: '.and.';
OR:	 '.or'  ; 

FLOAT:  DIGIT+ '.' DIGIT* EXP? [Ll]?
    |   DIGIT+ EXP? [Ll]?
    |   '.' DIGIT+ EXP? [Ll]?
    ;
fragment
DIGIT:  '0'..'9' ; 
fragment
EXP :   ('E' | 'e') ('+' | '-')? DIGIT+ ;

