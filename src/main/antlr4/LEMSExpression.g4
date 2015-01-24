grammar LEMSExpression;
import CommonLexerRules;

base:   expr  
		;

expr:   '(' expr ')'                # parens
    |   BuiltinFuncs '(' expr? ')'  # funcCall
    |   '-' expr                    # Negate
    |   expr op=POW expr            # Pow
    |   expr op=(MUL | DIV) expr    # MulDiv
    |   expr op=(ADD | SUB) expr    # AddSub
    |   FLOAT                       # float
    |   ID                          # id
    ;

BuiltinFuncs:   'sin'   
              | 'cos'  
              | 'tan' 
              ;
