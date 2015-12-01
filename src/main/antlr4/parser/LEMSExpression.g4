grammar LEMSExpression;
import  CommonLexerRules;

expression:
	(logic | arithmetic | ternary)?
;


arithmetic:
	'(' arithmetic ')' 				   			# Parenthesized
	| arithmetic op = POW arithmetic   			# Pow
	| builtin  '(' arithmetic ')'               # FunctionCall
	| '-' arithmetic                   			# Negate
	| arithmetic op = (MUL | DIV) arithmetic    # MulDiv
	| arithmetic op = (ADD | SUB) arithmetic    # AddSub
	| FLOAT ID									# DimensionalLiteral
	| FLOAT 									# FloatLiteral
	| ID 										# Identifier
;

logic:
	logic AND logic                                               # And
	| logic OR logic 									          # Or
	| arithmetic op = (LEQ | LT | GEQ | GT | EQ | NEQ) arithmetic # Comparison
;


builtin:
func = (
		SIN | COS | TAN
		| SINH | COSH | TANH
		| EXP | LOG | LN
		| SQRT
		| ABS
		| CEIL | FLOOR
		| RAND
)
;

ternary: logic '?' expression ':' expression;

// Path parsing
path:
	select?
;

select:
	select '/' select       # Step
	| '..'                  # Up
	| predicate             # Pred
	| ID                    # Node
;

predicate:
	ID '[' filter ']'
;

filter:
	'*'
	| ID '=' '\'' ID '\''
;
