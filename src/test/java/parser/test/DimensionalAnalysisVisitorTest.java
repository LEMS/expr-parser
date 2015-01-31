package parser.test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.util.SI.AMPERE;
import static tec.units.ri.util.SI.METRE;
import static tec.units.ri.util.SIPrefix.CENTI;
import static tec.units.ri.util.SIPrefix.MILLI;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;

import parser.AntlrExpressionParser;
import parser.DimensionalAnalysisVisitor;

public class DimensionalAnalysisVisitorTest {

	@Test
	public void testAddSub() {

		Unit<?> expected = METRE;
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x + x";
		assertTrue(verifyCompatibility(expected, context, expr));

		context.put("y", MILLI(METRE));
		expr = "x + y";
		assertTrue(verifyCompatibility(expected, context, expr));

		expr = "x + 1";
		assertFalse(verifyCompatibility(expected, context, expr));

	}

	@Test
	public void testMulDiv() {
		Unit<?> expected = METRE.multiply(MILLI(METRE));
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x*x*x/x";
		assertTrue(verifyCompatibility(expected, context, expr));

		context.put("y", MILLI(AMPERE));
		expr = "(y*x/y)*x";
		assertTrue(verifyCompatibility(expected, context, expr));

		expr = "y/x";
		assertFalse(verifyCompatibility(expected, context, expr));

	}

	@Test
	public void testFuncCall() {

		Unit<?> expected = MILLI(METRE);
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "exp(x)";
		assertFalse(verifyCompatibility(expected, context, expr));

		expr = "x*(sin(x/x)^2 + cos(2*x*x/(x^2))^2) + x";
		assertTrue(verifyCompatibility(expected, context, expr));

		expr = "1/(1+exp((x - x)/(10*x)))";
		assertTrue(verifyCompatibility(ONE, context, expr));

	}

	@Test
	public void testExponentiation() {

		Unit<?> expected = MILLI(METRE).multiply(METRE);
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x^2";
		assertTrue(verifyCompatibility(expected, context, expr));

		expr = "x^x";
		assertFalse(verifyCompatibility(expected, context, expr));

		// TODO: think about this case...
		// It seems we'll need a full-fledged dimensional expression evaluator
		expr = "x^(1+1)";
		assertFalse(verifyCompatibility(ONE, context, expr));

	}

	private boolean verifyCompatibility(Unit<?> expected,
			Map<String, Unit<?>> context, String expr) {
		try {
			return expected.getDimension().equals(
					dimensionalAnalysis(expr, context).getDimension());
		} catch (ParseCancellationException ex) {
			System.out.println(ex.getMessage());
			return false;
		} catch (NumberFormatException ex) {
			System.out.print("TODO: handle exponentiation correctly ");
			System.out.println(ex.getMessage());
			return false;
		}
	}

	private Unit<?> dimensionalAnalysis(String expression,
			Map<String, Unit<?>> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		DimensionalAnalysisVisitor eval = new DimensionalAnalysisVisitor(
				context);
		return p.parseAndVisitWith(eval);
	}

}
