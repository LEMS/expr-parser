package expr_parser.parser.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import expr_parser.visitors.AntlrExpressionParser;
import expr_parser.visitors.EvalVisitor;

public class EvalVisitorTest {
	private static final double EPSILON = 1e-10d;

	@Test
	public void testAddSub() {
		assertEquals(2.0d, getResult("1.5 + 0.5"), EPSILON);
		assertEquals(1.0d, getResult("1.5 - 5e-1"), EPSILON);
	}

	@Test
	public void testMultDiv() {
		assertEquals(-6.0d, getResult("-0.2e1 * 3"), EPSILON);
		assertEquals(1.0d, getResult("1/3*(9 * 1e1/10.) / (300e-2)"), EPSILON);
	}

	@Test
	public void testPow() {
		assertEquals(0.5d, getResult("1/(1+10^(0 - 0e0))"), EPSILON);
	}

	@Test
	public void testTrig() {
		assertEquals(0.0d, getResult("sin(0)"), EPSILON);
		assertEquals(1.0d, getResult("cos(0)"), EPSILON);
		assertEquals(1.0d, getResult("sin(0.1)^2 + cos(0.1)^2"), EPSILON);
		assertEquals(getResult("tan(1.23)"), getResult("sin(1.23)/cos(1.23)"),
				EPSILON);
	}

	@Test
	public void testHyp() {
		assertEquals(1.0d, getResult("cosh(0)"), EPSILON);
		assertEquals(0.0d, getResult("sinh(0.1)+sinh(-0.1)"), EPSILON);
		assertEquals(1.0d, getResult("cosh(1/2)^2 - sinh(1/2)^2 "), EPSILON);
		assertEquals(getResult("tanh(0.1)"), getResult("sinh(0.1)/cosh(0.1)"),
				EPSILON);
	}

	@Test
	public void testExpLog() {
		assertEquals(0.0d, getResult("log(1)"), EPSILON);
		assertEquals(0.0d, getResult("ln(1)"), EPSILON);
		assertEquals(1.0d, getResult("exp(0)"), EPSILON);
		assertEquals(1e-4d, getResult("exp(log(1e-4))"), EPSILON);
		assertEquals(0.5d, getResult("1./(1+exp(0))"), EPSILON);
	}

	@Test
	public void testFloorCeilAbs() {
		assertEquals(1.0d, getResult("floor(1.1)"), EPSILON);
		assertEquals(1.0d, getResult("ceil(0.9e0)"), EPSILON);
		assertEquals(1.0d, getResult("abs(-1)"), EPSILON);
		assertEquals(1.1d, getResult("abs(1.1e0)"), EPSILON);
	}

	@Test
	public void testDimensionalLiteral() {
		assertEquals(10.0d, getResult("10V"), EPSILON);
		assertEquals(5.0d, getResult("5 nA"), EPSILON);
		assertEquals(1.2d, getResult("1.2 mol_per_m_per_A_per_s"), EPSILON);
	}

	private Double getResult(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		EvalVisitor eval = new EvalVisitor();
		return p.parseAndVisitWith(eval).asDouble();
	}
}
