grammar LEMSExpression;

import CommonLexerRules;

expression
:
	(logic | arithmetic)?
;

arithmetic
:
	'(' arithmetic ')' 				   			# Parenthesized
	| BuiltinFuncs '(' arithmetic? ')' 			# FunctionCall
	| arithmetic op = POW arithmetic   			# Pow
	| '-' arithmetic                   			# Negate
	| arithmetic op = (MUL | DIV) arithmetic    # MulDiv
	| arithmetic op = (ADD | SUB) arithmetic    # AddSub
	| FLOAT 									# FloatLiteral
	| ID 										# Identifier
;

logic
:
	logic AND logic                                               # And
	| logic OR logic 									          # Or
	| arithmetic op = (LEQ | LT | GEQ | GT | EQ | NEQ) arithmetic # Comparison
;


BuiltinFuncs
:
	'sin'
	| 'cos'
	| 'tan'
	| 'sqrt'
	| 'sinh'
	| 'cosh'
	| 'tanh'
	| 'exp'
	| 'log'
	| 'ln'
	| 'random'
	| 'ceil'
	| 'floor'
	| 'abs'
;

