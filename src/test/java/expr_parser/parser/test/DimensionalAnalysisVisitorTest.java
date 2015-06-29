package expr_parser.parser.test;

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

import org.junit.Test;

import expr_parser.utils.ExpressionParser;

public class DimensionalAnalysisVisitorTest {

	@Test
	public void testAddSub() {

		Unit<?> expected = METRE;
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x + x";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		context.put("y", MILLI(METRE));
		expr = "x + y";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		expr = "x + 1";
		assertFalse(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

	}

	@Test
	public void testMulDiv() {
		Unit<?> expected = METRE.multiply(MILLI(METRE));
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x*x*x/x";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		context.put("y", MILLI(AMPERE));
		expr = "(y*x/y)*x";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		expr = "y/x";
		assertFalse(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

	}

	@Test
	public void testFuncCall() {

		Unit<?> expected = MILLI(METRE);
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "exp(x)";
		assertFalse(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		expr = "x*(sin(x/x)^2 + cos(2*x*x/(x^2))^2) + x";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		expr = "1/(1+exp((x - x)/(10*x)))";
		assertTrue(ExpressionParser.verifyUnitCompatibility(ONE, context, expr));

	}

	@Test
	public void testExponentiation() {

		Unit<?> expected = MILLI(METRE).multiply(METRE);
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("x", CENTI(METRE));

		String expr = "x^2";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		expr = "x^x";
		assertFalse(ExpressionParser.verifyUnitCompatibility(expected, context,
				expr));

		// TODO: think about this case...
		// It seems we'll need a full-fledged dimensional expression evaluator
		expr = "x^(1+1)";
		assertFalse(ExpressionParser
				.verifyUnitCompatibility(ONE, context, expr));

	}

	@Test
	public void testLiteral() {

		Unit<?> expected = METRE;
		Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();
		context.put("cm", CENTI(METRE));

		String expr = "1.2 cm";
		assertTrue(ExpressionParser.verifyUnitCompatibility(expected, context, expr));

	}

}
