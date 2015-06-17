package expr_parser.visitors;

import org.antlr.v4.runtime.Token;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;

public abstract class ARenderAs extends LEMSExpressionBaseVisitor<String> {

	abstract String adaptNegate(String val);

	abstract String adaptBinOp(Token op, String left, String right);

	abstract String adaptFuncCall(Integer funcName, String arg);

	/** expr */
	@Override
	public String visitExpression(LEMSExpressionParser.ExpressionContext ctx) {
		String result;
		if (ctx.arithmetic() != null) {
			result = new String(visit(ctx.arithmetic()));
			System.out.println(ctx.arithmetic().getText() + " = " + result);
		} else {
			result = new String(visit(ctx.logic()));
			System.out.println(ctx.logic().getText() + " = " + result);
		}
		return result;
	}

	/** '-' expr */
	@Override
	public String visitNegate(LEMSExpressionParser.NegateContext ctx) {
		String val = visit(ctx.arithmetic());
		return adaptNegate(val);
	}

	/** expr op=POW expr */
	@Override
	public String visitPow(LEMSExpressionParser.PowContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op, left, right);
	}

	/** FLOAT */
	@Override
	public String visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx) {
		return ctx.FLOAT().getText();
	}

	/** ID */
	@Override
	public String visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		return ctx.ID().getText();
	}

	/** expr op=('*'|'/') expr */
	@Override
	public String visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op, left, right);
	}

	/** expr op=('+'|'-') expr */
	@Override
	public String visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op, left, right);
	}

	/** '(' expr ')' */
	@Override
	public String visitParenthesized(
			LEMSExpressionParser.ParenthesizedContext ctx) {
		return ("(" + visit(ctx.arithmetic()) + ")");
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public String visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx) {
		return adaptFuncCall(ctx.builtin().func.getType(),
				visit(ctx.arithmetic()));
	}

}
