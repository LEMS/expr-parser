import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ComparisonVisitorTest {

	public void testComparisons() {

		Map<String, Double> context = new HashMap<String, Double>();
		context.put("x", 0.5);
		assertTrue(evaluateInContext("x .lt. 1", context));
		assertTrue(evaluateInContext("x .geq. 1-0.5", context));
		assertTrue(evaluateInContext("2*x .geq. x/x", context));
		assertFalse(evaluateInContext("2*x .gt. 1.0", context));
	}

	@Test
	public void testAndOr() {

		Map<String, Double> context = new HashMap<String, Double>();
		context.put("x", 0.5);
		assertTrue(evaluateInContext("x .lt. 1 .and. x .geq. 0.5", context));
		assertFalse(evaluateInContext("x .lt. 1 .and. x .gt. 0.05e1", context));

		assertTrue(evaluateInContext("x .lt. 1 .or. x .geq. 0.05e1", context));
		assertFalse(evaluateInContext("x .gt. 1.1 .or. x .gt. 0.5", context));
	}



	private Boolean evaluateInContext(String expression, Map<String, Double> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ComparisonEvalVisitor eval = new ComparisonEvalVisitor(context);
		return p.parseAndVisitWith(eval).asBoolean();
	}

}
