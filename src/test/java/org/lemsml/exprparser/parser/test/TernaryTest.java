package org.lemsml.exprparser.parser.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.lemsml.exprparser.visitors.AntlrExpressionParser;
import org.lemsml.exprparser.visitors.ComparisonEvalVisitor;

public class TernaryTest {

	@Test
	public void testComparisons() {

		assertEquals(1., evaluate("1<2 ? 1 : 0"), 1e-10);
		assertEquals(0., evaluate("1>2 ? 1 : 0"), 1e-10);
		assertEquals(1., evaluate("1<2 and 2<3 ? 1 : 0"), 1e-10);
		assertEquals(1., evaluate("1<2 or 2>3 ? 1 : 0"), 1e-10);
		assertEquals(0., evaluate("1>2 or 2>3 ? 1 : 0"), 1e-10);
		assertEquals(0., evaluate("1>2 and 2>3 ? 1 : 0"), 1e-10);

	}

	private Double evaluate(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ComparisonEvalVisitor eval = new ComparisonEvalVisitor();
		return p.parseAndVisitWith(eval).asDouble();
	}

}
