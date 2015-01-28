
public class EvalVisitor extends LEMSExpressionBaseVisitor<Value> {

	/** expr */
	@Override
	public Value visitExpression(LEMSExpressionParser.ExpressionContext ctx) {
		Value result;
		if(ctx.arithmetic() != null){
			result = new Value(visit(ctx.arithmetic()).asDouble());
			System.out.println(ctx.arithmetic().getText() + " = " + result);
		}
		else{
			result = new Value(visit(ctx.logic()).asBoolean());
			System.out.println(ctx.logic().getText() + " = " + result);
		}
		return result; 
	}

	/** '-' expr */
	@Override
	public Value visitNegate(LEMSExpressionParser.NegateContext ctx) {
		Value val = visit(ctx.arithmetic());
		return new Value(-val.asDouble());
	}

	/** expr op=POW expr */
	@Override
	public Value visitPow(LEMSExpressionParser.PowContext ctx) {
		Value left = visit(ctx.arithmetic(0)); // get value of left subexpression
		Value right = visit(ctx.arithmetic(1)); // get value of right subexpression
		return new Value(Math.pow(left.asDouble(), right.asDouble()));
	}

	/** FLOAT */
	@Override
	public Value visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx) {
		return new Value(Double.valueOf(ctx.FLOAT().getText()));
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Value visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Value left = visit(ctx.arithmetic(0)); 
		Value right = visit(ctx.arithmetic(1)); 
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return new Value(left.asDouble() * right.asDouble());
		return new Value(left.asDouble() / right.asDouble());
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Value visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Value left = visit(ctx.arithmetic(0)); 
		Value right = visit(ctx.arithmetic(1)); 
		if (ctx.op.getType() == LEMSExpressionParser.ADD)
			return new Value(left.asDouble() + right.asDouble());
        return new Value(left.asDouble() - right.asDouble());
	}

	/** '(' expr ')' */
	@Override
	public Value visitParenthesized(LEMSExpressionParser.ParenthesizedContext ctx) {
		return visit(ctx.arithmetic()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Value visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx) {
		Value ret = null;
		//TODO: check nargs (rand takes 0)
		switch (ctx.BuiltinFuncs().getText()) {
		case "sin":
			ret = new Value(Math.sin(visit(ctx.arithmetic()).asDouble()));
			break;
		case "cos":
			ret = new Value(Math.cos(visit(ctx.arithmetic()).asDouble()));
			break;
		case "tan":
			ret = new Value(Math.tan(visit(ctx.arithmetic()).asDouble()));
			break;
		case "sinh":
			ret = new Value(Math.sinh(visit(ctx.arithmetic()).asDouble()));
			break;
		case "cosh":
			ret = new Value(Math.cosh(visit(ctx.arithmetic()).asDouble()));
			break;
		case "tanh":
			ret = new Value(Math.tanh(visit(ctx.arithmetic()).asDouble()));
			break;
		case "exp":
			ret = new Value(Math.exp(visit(ctx.arithmetic()).asDouble()));
			break;
		case "log":
			ret = new Value(Math.log(visit(ctx.arithmetic()).asDouble()));
			break;
		case "ln":
			ret = new Value(Math.log(visit(ctx.arithmetic()).asDouble()));
			break;
		case "floor":
			ret = new Value(Math.floor(visit(ctx.arithmetic()).asDouble()));
			break;
		case "ceil":
			ret = new Value(Math.ceil(visit(ctx.arithmetic()).asDouble()));
			break;
		case "abs":
			ret = new Value(Math.abs(visit(ctx.arithmetic()).asDouble()));
			break;
		case "random":
			ret = new Value(Math.random());
			break;
		}

		return ret;
	}

}
