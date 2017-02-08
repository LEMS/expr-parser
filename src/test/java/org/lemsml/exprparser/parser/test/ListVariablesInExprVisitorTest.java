package org.lemsml.exprparser.parser.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import org.lemsml.exprparser.visitors.AntlrExpressionParser;
import org.lemsml.exprparser.visitors.ListVariablesInExprVisitor;

public class ListVariablesInExprVisitorTest {

	@Test
	public void testFindVariables() {
		Set<String> vars = getResult("ab+(c/exp(d^2 + ef/sin(g)))");
		Set<String> result = new HashSet<>(vars);
		// java is hilarious
		Set<String> expected = new HashSet<String>(new ArrayList<String>(
				Arrays.asList("ab", "c", "d", "ef", "g")));
		expected.removeAll(result);
		assertTrue(expected.isEmpty());
	}

	private Set<String> getResult(String expression) {
		AntlrExpressionParser p = new AntlrExpressionParser(expression);
		ListVariablesInExprVisitor listVars = new ListVariablesInExprVisitor();
		p.parseAndVisitWith(listVars);
		return listVars.getVariablesInExpr();
	}
}
