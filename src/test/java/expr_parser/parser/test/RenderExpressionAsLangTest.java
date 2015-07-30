package expr_parser.parser.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import expr_parser.visitors.ARenderAs;
import expr_parser.visitors.RenderC;
import expr_parser.visitors.RenderJava;
import expr_parser.visitors.RenderLatex;
import expr_parser.visitors.AntlrExpressionParser;

public class RenderExpressionAsLangTest {

	@Test
	public void testJava() {
		List<String> testStrings = new ArrayList<String>();
		testStrings.add("1.5+0.5");
		testStrings.add("1/3*(9*1e1/10.)/(300e-2)");
		testStrings.add("1/(1+10^(0-0e0))");
		testStrings.add("sin(0.1)^2+cos(0.1)^2");
		for (String tst : testStrings) {
			assertEquals(tst, adaptTo(tst, new RenderJava()));
		}
	}

	@Test
	public void testC() {
		List<String> testStrings = new ArrayList<String>();
		RenderC adaptor = new RenderC();
		testStrings.add("1.5+0.5");
		testStrings.add("1/3*(9*1e1/10.)/(300e-2)");
		for (String tst : testStrings) {
			assertEquals(tst, adaptTo(tst, adaptor));
		}
		assertEquals("1/(1+pow(10,(0-0e0)))",
				adaptTo("1/(1+10^(0-0e0))", adaptor));
		assertEquals("pow(sin(0.1),2)+pow(cos(0.1),2)",
				adaptTo("sin(0.1)^2+cos(0.1)^2", adaptor));
	}

	@Test
	public void testLatex() {
		RenderLatex adaptor = new RenderLatex();
		assertEquals("1.1\\times 10^{-1}", adaptTo("1.1e-1", adaptor));
		assertEquals("\\frac{1}{\\left (1+10^{\\left (0-0\\times 10^{0}\\right )}\\right )}",
				adaptTo("1/(1+10^(0-0e0))", adaptor));
		assertEquals("\\sin^{2}{\\left (0.1\\right )}+\\cos^{2}{\\left (0.1\\right )}",
				adaptTo("sin(0.1)^2+cos(0.1)^2", adaptor));
		assertEquals("a\\ \\cosh^{\\left (\\exp{\\left (b\\right )}\\cdot 2\\right )}{\\left (0.1\\right )}",
				adaptTo("a*cosh(0.1)^(exp(b)*2)", adaptor));
	}

	private String adaptTo(String expression, ARenderAs adaptor) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		return p.parseAndVisitWith(adaptor);
	}
}
