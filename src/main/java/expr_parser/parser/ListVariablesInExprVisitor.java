package expr_parser.parser;

import java.util.ArrayList;
import java.util.List;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;

public class ListVariablesInExprVisitor extends LEMSExpressionBaseVisitor<String>
{
	List<String> variablesInExpr = new ArrayList<String>();

	public List<String> getVariablesInExpr()
	{
		return variablesInExpr;
	}

	// Technically, the "right" way of achieving this functionality would
	// be using a listener instead of a visitor (as listeners recursively
	// walk the whole tree by default).

	/** expr */
	@Override
	public String visitExpression(LEMSExpressionParser.ExpressionContext ctx)
	{
		if(ctx.arithmetic().isEmpty())
		{
			visit(ctx.logic());
		}
		else
		{
			visit(ctx.arithmetic());
		}
		System.out.println("variables in expr:" + variablesInExpr);
		return variablesInExpr.toString();
	}

	/** ID */
	@Override
	public String visitIdentifier(LEMSExpressionParser.IdentifierContext ctx)
	{
		String id = ctx.ID().getText();
		variablesInExpr.add(id);
		return id;
	}

}
