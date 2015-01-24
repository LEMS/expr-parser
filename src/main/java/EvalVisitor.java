public class EvalVisitor extends LEMSExpressionBaseVisitor<Double> {

	/** expr */
	@Override
	public Double visitBase(LEMSExpressionParser.BaseContext ctx) {
		Double result = visit(ctx.expr());
		System.out.println(ctx.expr().getText() + " = " + result);
		return result; 
	}

	/** '-' expr */
	@Override
	public Double visitNegate(LEMSExpressionParser.NegateContext ctx) {
		return -visit(ctx.expr());
	}

	/** expr op=POW expr */
	@Override
	public Double visitPow(LEMSExpressionParser.PowContext ctx) {
		Double left = visit(ctx.expr(0)); // get value of left subexpression
		Double right = visit(ctx.expr(1)); // get value of right subexpression
		return new Double(Math.pow(left, right));
	}

	/** FLOAT */
	@Override
	public Double visitFloat(LEMSExpressionParser.FloatContext ctx) {
		return Double.valueOf(ctx.FLOAT().getText());
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Double visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Double left = visit(ctx.expr(0)); // get value of left subexpression
		Double right = visit(ctx.expr(1)); // get value of right subexpression
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return left * right;
		return left / right; // must be DIV
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Double visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Double left = visit(ctx.expr(0)); // get value of left subexpression
		Double right = visit(ctx.expr(1)); // get value of right subexpression
		if (ctx.op.getType() == LEMSExpressionParser.ADD)
			return left + right;
		return left - right; // must be SUB
	}

	/** '(' expr ')' */
	@Override
	public Double visitParens(LEMSExpressionParser.ParensContext ctx) {
		return visit(ctx.expr()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Double visitFuncCall(LEMSExpressionParser.FuncCallContext ctx) {
		Double ret = null;
		switch (ctx.BuiltinFuncs().getText()) {
		case "sin":
			ret = new Double(Math.sin(visit(ctx.expr())));
			break;
		case "cos":
			ret = new Double(Math.cos(visit(ctx.expr())));
			break;
		case "tan":
			ret = new Double(Math.tan(visit(ctx.expr())));
			break;
		case "sinh":
			ret = new Double(Math.sinh(visit(ctx.expr())));
			break;
		case "cosh":
			ret = new Double(Math.cosh(visit(ctx.expr())));
			break;
		case "tanh":
			ret = new Double(Math.tanh(visit(ctx.expr())));
			break;
		case "exp":
			ret = new Double(Math.exp(visit(ctx.expr())));
			break;
		case "log":
			ret = new Double(Math.log(visit(ctx.expr())));
			break;
		case "ln":
			ret = new Double(Math.log(visit(ctx.expr())));
			break;
		case "floor":
			ret = new Double(Math.floor(visit(ctx.expr())));
			break;
		case "ceil":
			ret = new Double(Math.ceil(visit(ctx.expr())));
			break;
		case "abs":
			ret = new Double(Math.abs(visit(ctx.expr())));
			break;
		case "random":
			ret = new Double(Math.random());
			break;
		}

		return ret;
	}

}
