import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class ListVariablesInExprVisitorTest {

	@Test
	public void testFindVariables() {
		List<String> vars = getResult("ab+(c/exp(d^2 + ef/sin(g)))");
		Set<String> result = new HashSet<>(vars);
		// java is hilarious
		Set<String> expected = new HashSet<String>(new ArrayList<String>(
				Arrays.asList("ab", "c", "d", "ef", "g")));
		expected.removeAll(result);
		assertTrue(expected.isEmpty());
	}

	private List<String> getResult(String expression) {
		ParseTree tree = new AntlrExpressionParser().generateTree(expression);

		ListVariablesInExprVisitor listVars = new ListVariablesInExprVisitor();
		listVars.visit(tree);
		return listVars.getVariablesInExpr();
	}
}
