package expr_parser.visitors;

import java.util.Map;

import parser.LEMSExpressionParser;

public class ComparisonEval extends ContextEval
{

	private boolean isTrue;

	public boolean isTrue()
	{
		return isTrue;
	}

	public ComparisonEval(Map<String, Double> context)
	{
		super(context);
	}

	@Override
	public Value visitComparison(LEMSExpressionParser.ComparisonContext ctx)
	{
		Value left = visit(ctx.arithmetic(0)); // get value of left
												// subexpression
		Value right = visit(ctx.arithmetic(1)); // get value of right
												// subexpression
		Double l = left.asDouble();
		Double r = right.asDouble();
		switch(ctx.op.getType())
		{
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
				throw new RuntimeException("Unknown operator: " + LEMSExpressionParser.tokenNames[ctx.op.getType()]);
		}
	}

	public static boolean compareWithinUlp(double expected, double actual, long maxUlps)
	{
		long expectedBits = Double.doubleToLongBits(expected) < 0 ? 0x8000000000000000L - Double.doubleToLongBits(expected) : Double.doubleToLongBits(expected);
		long actualBits = Double.doubleToLongBits(actual) < 0 ? 0x8000000000000000L - Double.doubleToLongBits(actual) : Double.doubleToLongBits(actual);
		long difference = expectedBits > actualBits ? expectedBits - actualBits : actualBits - expectedBits;

		return !Double.isNaN(expected) && !Double.isNaN(actual) && difference <= maxUlps;
	}
}
