public class EvalVisitor extends LEMSExpressionBaseVisitor<Float> {

	/** expr */
	@Override
	public Float visitBase(LEMSExpressionParser.BaseContext ctx) {
		Float result = visit(ctx.expr());
		System.out.println(ctx.expr().getText() + " = " + result);
		return result; 
	}

	/** '-' expr */
	@Override
	public Float visitNegate(LEMSExpressionParser.NegateContext ctx) {
		return -visit(ctx.expr());
	}

	/** expr op=POW expr */
	@Override
	public Float visitPow(LEMSExpressionParser.PowContext ctx) {
		Float left = visit(ctx.expr(0)); // get value of left subexpression
		Float right = visit(ctx.expr(1)); // get value of right subexpression
		return new Float(Math.pow(left, right));
	}

	/** FLOAT */
	@Override
	public Float visitFloat(LEMSExpressionParser.FloatContext ctx) {
		return Float.valueOf(ctx.FLOAT().getText());
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Float visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Float left = visit(ctx.expr(0)); // get value of left subexpression
		Float right = visit(ctx.expr(1)); // get value of right subexpression
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return left * right;
		return left / right; // must be DIV
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Float visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Float left = visit(ctx.expr(0)); // get value of left subexpression
		Float right = visit(ctx.expr(1)); // get value of right subexpression
		if (ctx.op.getType() == LEMSExpressionParser.ADD)
			return left + right;
		return left - right; // must be SUB
	}

	/** '(' expr ')' */
	@Override
	public Float visitParens(LEMSExpressionParser.ParensContext ctx) {
		return visit(ctx.expr()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Float visitFuncCall(LEMSExpressionParser.FuncCallContext ctx) {
		Float ret = null;
		switch (ctx.BuiltinFuncs().getText()) {
		case "sin":
			ret = new Float(Math.sin(visit(ctx.expr())));
			break;
		case "cos":
			ret = new Float(Math.cos(visit(ctx.expr())));
			break;
		case "tan":
			ret = new Float(Math.tan(visit(ctx.expr())));
			break;
		}

		return ret;
	}

}
