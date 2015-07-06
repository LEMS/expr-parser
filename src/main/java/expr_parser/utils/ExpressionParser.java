package expr_parser.utils;

import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import expr_parser.visitors.AntlrExpressionParser;
import expr_parser.visitors.ContextEval;
import expr_parser.visitors.DimensionalAnalysis;
import expr_parser.visitors.ListVariablesInExpr;
import expr_parser.visitors.QuantityEval;

public class ExpressionParser {

	public static Double evaluateInContext(String expression, Map<String, Double> context) throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(expression);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + expression + "'");
		}
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ContextEval eval = new ContextEval(context);
		return p.parseAndVisitWith(eval).asDouble();
	}

	public static Quantity<?> evaluateQuantitiesContext(String expression, Map<String, Quantity<?>> context, Map<String, Unit<?>> unitContext)
			throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(expression);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + expression + "'");
		}
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		QuantityEval eval = new QuantityEval(context, unitContext);
		return p.parseAndVisitWith(eval);
	}

	public static Set<String> listSymbolsInExpression(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ListVariablesInExpr listVars = new ListVariablesInExpr();
		p.parseAndVisitWith(listVars);
		return listVars.getVariablesInExpr();
	}

	public static Unit<?> dimensionalAnalysis(String expression,
			Map<String, Unit<?>> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		DimensionalAnalysis eval = new DimensionalAnalysis(context);
		return p.parseAndVisitWith(eval);
	}

	public static boolean verifyUnitCompatibility(Unit<?> expected,
			Map<String, Unit<?>> context, String expr) {
		try {
			return expected.getDimension().equals(
					ExpressionParser.dimensionalAnalysis(expr, context).getDimension());
		} catch (ParseCancellationException ex) {
			System.out.println(ex.getMessage());
			return false;
		} catch (NumberFormatException ex) {
			System.out.print("TODO: handle exponentiation correctly ");
			System.out.println(ex.getMessage());
			return false;
		}
	}

}
