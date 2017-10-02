package org.lemsml.exprparser.parser.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tec.units.ri.unit.MetricPrefix.CENTI;
import static tec.units.ri.unit.Units.METRE;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.junit.Before;
import org.junit.Test;

import tec.units.ri.quantity.Quantities;
import org.lemsml.exprparser.visitors.AntlrExpressionParser;
import org.lemsml.exprparser.visitors.QuantityConditionalEvalVisitor;

public class DimensionalComparisonVisitorTest {

	private Map<String, Unit<?>> uctxt = new HashMap<String, Unit<?>>();
	private Map<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		uctxt.put("cm", CENTI(METRE));
		uctxt.put("m", METRE);
		context.put("x", Quantities.getQuantity(0.5, METRE));
	}

	@Test
	public void testComparisons() {

		assertTrue(evaluateInContext("x .lt. 1m"));
		assertTrue(evaluateInContext("x .geq. 1m-0.5m"));
		//assertTrue(evaluateInContext("2*x .geq. x/x"));
		assertFalse(evaluateInContext("2*x .gt. 1.0m"));

		// Float comparison is tricky...
		assertTrue(evaluateInContext("2cm .eq. 2cm"));
		assertTrue(evaluateInContext("2*x .eq. 1m"));
		assertTrue(evaluateInContext("x^2 .eq. 0.5m*0.5m"));
		assertTrue(evaluateInContext("x^2 .eq. 1m/2*0.5m"));
		assertFalse(evaluateInContext("2*x .neq. 1.0m"));
//		assertTrue(evaluateInContext("0e100cm .eq. -0e100cm"));
		assertTrue(evaluateInContext("sin(0) .eq. 0"));
		assertTrue(evaluateInContext("sin(0) .neq. 1e-99"));

	}

	@Test
	public void testAndOr() {

		assertTrue(evaluateInContext("x .lt. 1m .and. x .geq. 0.5m"));
		assertTrue(evaluateInContext("2*x .geq. 1m .and. x .geq. 0.5m"));
		assertFalse(evaluateInContext("x .lt. 1m .and. x .gt. 0.05e1m"));

		assertTrue(evaluateInContext("x .lt. 1m .or. x .geq. 0.05e1m"));
		assertFalse(evaluateInContext("x .gt. 1.1m .or. x .gt. 0.5m"));

	}

	private Boolean evaluateInContext(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		QuantityConditionalEvalVisitor eval = new QuantityConditionalEvalVisitor(context, uctxt);
		return p.parseAndVisitWith(eval).asBoolean();
	}

}
