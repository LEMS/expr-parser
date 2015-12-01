package expr_parser.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Queue;

import expr_parser.visitors.AntlrExpressionParser;
import expr_parser.visitors.ExpandSymbols;

public class SymbolExpander {
	public static final LinkedHashMap<String, String> expandSymbols(
			LinkedHashMap<String, String> context) {
		// we expect a toposorted context, ergo linkedHM
		LinkedList<String> queue = new LinkedList<String>();
		queue.addAll(context.keySet());
		return expandSymbols(context, queue);
	}

	private static final LinkedHashMap<String, String> expandSymbols(
			LinkedHashMap<String, String> context, Queue<String> queue) {
		while (!queue.isEmpty()) {
			String s = queue.remove();
			AntlrExpressionParser p = new AntlrExpressionParser(context.get(s));
			ExpandSymbols expander = new ExpandSymbols(context);
			context.put(s, p.parseAndVisitWith(expander));
			expandSymbols(context, queue);
		}
		return context;
	}

}
