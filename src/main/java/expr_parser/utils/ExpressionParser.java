package expr_parser.utils;

import java.util.Map;
import java.util.Set;

import expr_parser.visitors.AntlrExpressionParser;
import expr_parser.visitors.ContextEval;
import expr_parser.visitors.ListVariablesInExpr;

public class ExpressionParser {

	public static Double evaluateInContext(String expression,
			Map<String, Double> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ContextEval eval = new ContextEval(context);
		return p.parseAndVisitWith(eval).asDouble();
	}

	public static Set<String> findDeps(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ListVariablesInExpr listVars = new ListVariablesInExpr();
		p.parseAndVisitWith(listVars);
		return listVars.getVariablesInExpr();
	}

}
