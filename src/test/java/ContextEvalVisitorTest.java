import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ContextEvalVisitorTest {

	@Test
	public void testContextEval() {

		Map<String, Double> context = new HashMap<String, Double>();
		context.put("x", 0.5);
		String expr = "x*(sin(x)^2 + cos(x)^2)";

		assertEquals(getResult(expr, context), 0.5, 1e-10);
	}

	@Test
	public void testDependencyResolution() {

		// make it a linkedHM to enforce unordered dependencies
		Map<String, String> expressions = new LinkedHashMap<String, String>();
		expressions.put("foo", "(sin(x)^2 + cos(x)^2)");
		expressions.put("bar", "(sin(y)^2 + cos(y)^2)");
		expressions.put("x", "0.0");
		expressions.put("IDependOnOthers", "foo/bar + 1");
		expressions.put("y", "3.14");
		expressions.put("w", "0.0");

		// Build expression dependency graph
		// DirectedGraph<String> dependencies = new DirectedGraph<String>();
		// for (Entry<String, String> entry : expressions.entrySet()) {
		//
		// dependencies.addEdge(entry.getKey(), );
		// }
		//
		// // topological sort over dependencies
		// List<String> sorted = TopologicalSort.sort(dependencies);
		// Collections.reverse(sorted);
		//
		// // evaluates all expressions according to dependencies
		// Map<String, Double> evaluated = new HashMap<String, Double>();
		// for (String exp : sorted) {
		// Expression e = parsedExprs.get(exp).setVariables(evaluated);
		// evaluated.put(exp, e.evaluate());
		// }

		// assertEquals(evaluated.get("IDependOnOthers"), 2.0, 1e-10);
	}

	private Double getResult(String expression, Map<String, Double> context) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ContextEvalVisitor eval = new ContextEvalVisitor(context);
		return p.parseAndVisitWith(eval);
	}
}
