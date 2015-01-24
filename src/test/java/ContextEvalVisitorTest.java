import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class ContextEvalVisitorTest {

	@Test
	public void testContextEval() {

		Map<String, Double> context = new HashMap<String, Double>();
		context.put("x", 0.5);
		String expr = "x*(sin(x)^2 + cos(x)^2)";

		assertEquals(getResult(expr, context), 0.5, 1e-10);
	}

	private Double getResult(String expression, Map<String, Double> context) {
		ParseTree tree = new AntlrExpressionParser().generateTree(expression);

		ContextEvalVisitor eval = new ContextEvalVisitor(context);
		return eval.visit(tree);
	}
}
