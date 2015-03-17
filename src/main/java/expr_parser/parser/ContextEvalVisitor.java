package expr_parser.parser;

import java.util.HashMap;
import java.util.Map;

import parser.LEMSExpressionParser;

public class ContextEvalVisitor extends EvalVisitor
{
	Map<String, Double> context = new HashMap<String, Double>();

	public ContextEvalVisitor(Map<String, Double> context)
	{
		this.context = context;
	}

	/** ID */
	@Override
	public Value visitIdentifier(LEMSExpressionParser.IdentifierContext ctx)
	{
		String id = ctx.ID().getText();
		// TODO: check for undefined ids
		return new Value(context.get(id));
	}

}
