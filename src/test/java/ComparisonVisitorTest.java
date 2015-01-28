import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ComparisonVisitorTest {

	@Test
	public void testContextEval() {

		Map<String, Double> context = new HashMap<String, Double>();
		context.put("x", 0.5);
		assertTrue(evaluateInContext("x .lt. 1", context));
		assertTrue(evaluateInContext("x .geq. 1-0.5", context));
		assertTrue(evaluateInContext("2*x .geq. x/x", context));
		assertFalse(evaluateInContext("2*x .gt. 1.0", context));
	}


	private Boolean evaluateInContext(String expression, Map<String, Double> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ComparisonEvalVisitor eval = new ComparisonEvalVisitor(context);
		return p.parseAndVisitWith(eval).asBoolean();
	}

}
