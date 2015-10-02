package expr_parser.utils;

import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import expr_parser.visitors.AntlrExpressionParser;
import expr_parser.visitors.ContextEvalVisitor;
import expr_parser.visitors.DimensionalAnalysisVisitor;
import expr_parser.visitors.ListVariablesInExprVisitor;
import expr_parser.visitors.QuantityConditionalEvalVisitor;
import expr_parser.visitors.QuantityEvalVisitor;

public class ExpressionParser {

	public static Double evaluateInContext(String expression, Map<String, Double> context) throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(expression);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + expression + "'");
		}
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ContextEvalVisitor eval = new ContextEvalVisitor(context);
		return p.parseAndVisitWith(eval).asDouble();
	}

	public static Boolean evaluateConditionInContext(String expression, Map<String, Quantity<?>> context, Map<String, Unit<?>> unitContext) throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(expression);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + expression + "'");
		}
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		QuantityConditionalEvalVisitor eval = new QuantityConditionalEvalVisitor(context, unitContext);
		return p.parseAndVisitWith(eval).asBoolean();
	}

	public static Quantity<?> evaluateQuantityInContext(String expression, Map<String, Quantity<?>> context, Map<String, Unit<?>> unitContext)
			throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(expression);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + expression + "'");
		}
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		QuantityEvalVisitor eval = new QuantityEvalVisitor(context, unitContext);
		return p.parseAndVisitWith(eval);
	}

	public static Quantity<?> evaluateQuantityInContext(AntlrExpressionParser p, Map<String, Quantity<?>> context, Map<String, Unit<?>> unitContext)
			throws UndefinedSymbolException {
		Set<String> depVars = listSymbolsInExpression(p);
		depVars.removeAll(context.keySet());
		if(depVars.size() > 0){
			throw new UndefinedSymbolException("Symbol(s) '" + depVars  + "' undefined in expression '" + p.getExpression() + "'");
		}
		QuantityEvalVisitor eval = new QuantityEvalVisitor(context, unitContext);
		return p.parseAndVisitWith(eval);
	}

	public static Set<String> listSymbolsInExpression(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ListVariablesInExprVisitor listVars = new ListVariablesInExprVisitor();
		p.parseAndVisitWith(listVars);
		return listVars.getVariablesInExpr();
	}

	public static Set<String> listSymbolsInExpression(AntlrExpressionParser p) {
		ListVariablesInExprVisitor listVars = new ListVariablesInExprVisitor();
		p.parseAndVisitWith(listVars);
		return listVars.getVariablesInExpr();
	}

	public static Unit<?> dimensionalAnalysis(String expression,
			Map<String, Unit<?>> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		DimensionalAnalysisVisitor eval = new DimensionalAnalysisVisitor(context);
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
