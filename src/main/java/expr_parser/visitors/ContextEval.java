package expr_parser.visitors;

import java.util.HashMap;
import java.util.Map;

import parser.LEMSExpressionParser;

public class ContextEval extends Eval
{
	Map<String, Double> context = new HashMap<String, Double>();

	public ContextEval(Map<String, Double> context)
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
