package org.lemsml.expr_parser.visitors;

import parser.LEMSExpressionParser;

public class ComparisonEvalVisitor extends EvalVisitor {

	private boolean isTrue;

	public boolean isTrue() {
		return isTrue;
	}

	@Override
	public Value visitTernary(LEMSExpressionParser.TernaryContext ctx) {
		 if(visit(ctx.logic()).asBoolean())
			 return new Value(visit(ctx.expression(0)).asDouble());
		 else
			 return new Value(visit(ctx.expression(1)).asDouble());
	}

	@Override
	public Value visitAnd(LEMSExpressionParser.AndContext ctx) {
		Value left = visit(ctx.logic(0));
		Value right = visit(ctx.logic(1));
		return new Value(left.asBoolean() && right.asBoolean());

	}

	@Override
	public Value visitOr(LEMSExpressionParser.OrContext ctx) {
		Value left = visit(ctx.logic(0));
		Value right = visit(ctx.logic(1));
		return new Value(left.asBoolean() || right.asBoolean());
	}

	@Override
	public Value visitComparison(LEMSExpressionParser.ComparisonContext ctx) {
		Value left = visit(ctx.arithmetic(0));

		Value right = visit(ctx.arithmetic(1));

		Double l = left.asDouble();
		Double r = right.asDouble();
		switch (ctx.op.getType()) {
		case LEMSExpressionParser.GEQ:
			return new Value(l >= right.asDouble());
		case LEMSExpressionParser.LEQ:
			return new Value(l <= r);
		case LEMSExpressionParser.GT:
			return new Value(l > r);
		case LEMSExpressionParser.LT:
			return new Value(l < r);
		case LEMSExpressionParser.EQ:
			System.out.println("WARNING: Float comparison is evil.");
			return new Value(compareWithinUlp(l, r, 4));
		case LEMSExpressionParser.NEQ:
			System.out.println("WARNING: Float comparison is evil.");
			return new Value(!compareWithinUlp(l, r, 4));
		default:
			throw new RuntimeException("Unknown operator: "
					+ LEMSExpressionParser.tokenNames[ctx.op.getType()]);
		}
	}

	public static boolean compareWithinUlp(double expected, double actual,
			long maxUlps) {
		long expectedBits = Double.doubleToLongBits(expected) < 0 ? 0x8000000000000000L - Double
				.doubleToLongBits(expected) : Double.doubleToLongBits(expected);
		long actualBits = Double.doubleToLongBits(actual) < 0 ? 0x8000000000000000L - Double
				.doubleToLongBits(actual) : Double.doubleToLongBits(actual);
		long difference = expectedBits > actualBits ? expectedBits - actualBits
				: actualBits - expectedBits;

		return !Double.isNaN(expected) && !Double.isNaN(actual)
				&& difference <= maxUlps;
	}
}
