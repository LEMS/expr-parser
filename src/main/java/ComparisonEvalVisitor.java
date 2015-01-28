import java.util.Map;

public class ComparisonEvalVisitor extends ContextEvalVisitor {

	private boolean isTrue;

	public boolean isTrue() {
		return isTrue;
	}

	public ComparisonEvalVisitor(Map<String, Double> context) {
		super(context);
	}

	@Override
	public Value visitComparison(LEMSExpressionParser.ComparisonContext ctx) {
		Value left = visit(ctx.arithmetic(0)); // get value of left subexpression
		Value right = visit(ctx.arithmetic(1)); // get value of right subexpression
		switch (ctx.op.getType()) {
		case LEMSExpressionParser.GEQ:
			return new Value(left.asDouble() >= right.asDouble());
		case LEMSExpressionParser.LEQ:
			return new Value(left.asDouble() <= right.asDouble());
		case LEMSExpressionParser.GT:
			return new Value(left.asDouble() > right.asDouble());
		case LEMSExpressionParser.LT:
			return new Value(left.asDouble() < right.asDouble());
		default:
			throw new RuntimeException("unknown operator: "
					+ LEMSExpressionParser.tokenNames[ctx.op.getType()]);
			// case LEMSExpressionParser.EQ:
			// this.isTrue = Float.compare(left, right). ;
			// break;
			// case LEMSExpressionParser.NEQ:
			// this.isTrue = (left != right);
			// break;
		}
	}

}
