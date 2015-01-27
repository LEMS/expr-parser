grammar LEMSExpression;
import CommonLexerRules;

base:   expr  
		;

expr:   '(' expr ')'                # Parenthesized
    |   BuiltinFuncs '(' expr? ')'  # FunctionCall
    |   expr op=POW expr            # Pow
    |   '-' expr                    # Negate
    |   expr op=(MUL | DIV) expr    # MulDiv
    |   expr op=(ADD | SUB) expr    # AddSub
    |   FLOAT                       # FloatLiteral
    |   ID                          # Identifier
    ;

BuiltinFuncs:   'sin'   
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

