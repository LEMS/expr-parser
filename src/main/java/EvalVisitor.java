
public class EvalVisitor extends LEMSExpressionBaseVisitor<Value> {

	/** expr */
	@Override
	public Value visitBase(LEMSExpressionParser.BaseContext ctx) {
		Value result = visit(ctx.expr());
		System.out.println(ctx.expr().getText() + " = " + result);
		return result; 
	}

	/** '-' expr */
	@Override
	public Value visitNegate(LEMSExpressionParser.NegateContext ctx) {
		Value val = visit(ctx.expr());
		return new Value(-val.asDouble());
	}

	/** expr op=POW expr */
	@Override
	public Value visitPow(LEMSExpressionParser.PowContext ctx) {
		Value left = visit(ctx.expr(0)); // get value of left subexpression
		Value right = visit(ctx.expr(1)); // get value of right subexpression
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
		Value left = visit(ctx.expr(0)); 
		Value right = visit(ctx.expr(1)); 
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return new Value(left.asDouble() * right.asDouble());
		return new Value(left.asDouble() / right.asDouble());
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Value visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Value left = visit(ctx.expr(0)); 
		Value right = visit(ctx.expr(1)); 
		if (ctx.op.getType() == LEMSExpressionParser.ADD)
			return new Value(left.asDouble() + right.asDouble());
        return new Value(left.asDouble() - right.asDouble());
	}

	/** '(' expr ')' */
	@Override
	public Value visitParenthesized(LEMSExpressionParser.ParenthesizedContext ctx) {
		return visit(ctx.expr()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Value visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx) {
		Value ret = null;
		//TODO: check nargs (rand takes 0)
		switch (ctx.BuiltinFuncs().getText()) {
		case "sin":
			ret = new Value(Math.sin(visit(ctx.expr()).asDouble()));
			break;
		case "cos":
			ret = new Value(Math.cos(visit(ctx.expr()).asDouble()));
			break;
		case "tan":
			ret = new Value(Math.tan(visit(ctx.expr()).asDouble()));
			break;
		case "sinh":
			ret = new Value(Math.sinh(visit(ctx.expr()).asDouble()));
			break;
		case "cosh":
			ret = new Value(Math.cosh(visit(ctx.expr()).asDouble()));
			break;
		case "tanh":
			ret = new Value(Math.tanh(visit(ctx.expr()).asDouble()));
			break;
		case "exp":
			ret = new Value(Math.exp(visit(ctx.expr()).asDouble()));
			break;
		case "log":
			ret = new Value(Math.log(visit(ctx.expr()).asDouble()));
			break;
		case "ln":
			ret = new Value(Math.log(visit(ctx.expr()).asDouble()));
			break;
		case "floor":
			ret = new Value(Math.floor(visit(ctx.expr()).asDouble()));
			break;
		case "ceil":
			ret = new Value(Math.ceil(visit(ctx.expr()).asDouble()));
			break;
		case "abs":
			ret = new Value(Math.abs(visit(ctx.expr()).asDouble()));
			break;
		case "random":
			ret = new Value(Math.random());
			break;
		}

		return ret;
	}

}
