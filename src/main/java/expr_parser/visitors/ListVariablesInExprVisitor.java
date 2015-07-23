package expr_parser.visitors;

import java.util.HashSet;
import java.util.Set;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;

public class ListVariablesInExprVisitor extends LEMSExpressionBaseVisitor<String> {
	Set<String> variablesInExpr = new HashSet<String>();

	public Set<String> getVariablesInExpr() {
		return variablesInExpr;
	}

	// Technically, the "right" way of achieving this functionality would
	// be using a listener instead of a visitor (as listeners recursively
	// walk the whole tree by default).

	/** expr */
	@Override
	public String visitExpression(LEMSExpressionParser.ExpressionContext ctx) {
		if (ctx.arithmetic().isEmpty()) {
			visit(ctx.logic());
		} else {
			visit(ctx.arithmetic());
		}
		//System.out.println("variables in expr:" + variablesInExpr);
		return variablesInExpr.toString();
	}

	/** ID */
	@Override
	public String visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		variablesInExpr.add(id);
		return id;
	}

}
