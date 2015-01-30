package parser.test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ExprWriterVisitorTest {

	@Test
	public void testJava() {
		List<String> testStrings = new ArrayList<String>();
		testStrings.add("1.5+0.5");
		testStrings.add("1/3*(9*1e1/10.)/(300e-2)");
		testStrings.add("1/(1+10^(0-0e0))");
		testStrings.add("sin(0.1)^2+cos(0.1)^2");
		for (String tst : testStrings){
			assertEquals(tst, adaptTo(tst, new AdaptToJavaVisitor()));
		}
	} 
	@Test
	public void testC() {
		List<String> testStrings = new ArrayList<String>();
		AdaptToCVisitor adaptor = new AdaptToCVisitor();
		testStrings.add("1.5+0.5");
		testStrings.add("1/3*(9*1e1/10.)/(300e-2)");
		for (String tst : testStrings){
			assertEquals(tst, adaptTo(tst, adaptor));
		}
		assertEquals("1/(1+pow(10,(0-0e0)))", adaptTo("1/(1+10^(0-0e0))", adaptor));
		assertEquals("pow(sin(0.1),2)+pow(cos(0.1),2)", adaptTo("sin(0.1)^2+cos(0.1)^2", adaptor));
	} 

	private String adaptTo(String expression, AAdaptToLangVisitor adaptor) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		return p.parseAndVisitWith(adaptor);
	}
}
