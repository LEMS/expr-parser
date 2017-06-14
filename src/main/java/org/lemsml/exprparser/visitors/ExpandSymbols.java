package org.lemsml.exprparser.visitors;

import java.util.Map;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;
import org.lemsml.exprparser.utils.TextUtils;

public class ExpandSymbols extends LEMSExpressionBaseVisitor<String> {

	private Map<String, String> context;

	public ExpandSymbols(Map<String, String> context) {
		this.context = context;
	}

	/** expr */
	@Override
	public String visitExpression(LEMSExpressionParser.ExpressionContext ctx) {
		String result;
		if (ctx.arithmetic() != null)
			result = new String(visit(ctx.arithmetic()));
		else if(ctx.logic() != null)
			result = new String(visit(ctx.logic()));
		else
			result = new String(visit(ctx.ternary()));
		return result;
	}

	/** '-' expr */
	@Override
	public String visitNegate(LEMSExpressionParser.NegateContext ctx) {
		String val = visit(ctx.arithmetic());
		return "-" + TextUtils.parenthesize(val);
	}

	/** expr op=POW expr */
	@Override
	public String visitPow(LEMSExpressionParser.PowContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op.getText(), left, TextUtils.parenthesize(right));
	}

	private String adaptBinOp(String op, String left, String right) {
		return left + op + right;
	}

	/** FLOAT */
	@Override
	public String visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx) {
		return ctx.FLOAT().getText();
	}

	/** ID */
	@Override
	public String visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		return context.get(id);
	}

	/** expr op=('*'|'/') expr */
	@Override
	public String visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op.getText(), left, right);
	}

	/** expr op=('+'|'-') expr */
	@Override
	public String visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op.getText(), left, right);
	}

	/** '(' expr ')' */
	@Override
	public String visitParenthesized(
			LEMSExpressionParser.ParenthesizedContext ctx) {
		return ("(" + visit(ctx.arithmetic()) + ")");
	}


	@Override
	public String visitTernary(LEMSExpressionParser.TernaryContext ctx) {
		 return visit(ctx.logic()) + '?' + visit(ctx.expression(0)) + ':' + visit(ctx.expression(1));
	}

	@Override
	public String visitAnd(LEMSExpressionParser.AndContext ctx) {
		String left = visit(ctx.logic(0));
		String right = visit(ctx.logic(1));
		return adaptBinOp("and", left, right);

	}

	@Override
	public String visitOr(LEMSExpressionParser.OrContext ctx) {
		String left = visit(ctx.logic(0));
		String right = visit(ctx.logic(1));
		return adaptBinOp("or", left, right);
	}

	@Override
	public String visitComparison(LEMSExpressionParser.ComparisonContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));
		return adaptBinOp(ctx.op.getText(), left, right);
	}

	@Override
	public String visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx) {
		return TextUtils.funcCall(ctx.builtin().func.getText(), visit(ctx.arithmetic()));
	}


}
