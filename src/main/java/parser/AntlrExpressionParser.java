package parser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;

public class AntlrExpressionParser {

	ParseTree tree;

	public AntlrExpressionParser(String expression) {
		ANTLRInputStream input = new ANTLRInputStream(expression);
		LEMSExpressionLexer lexer = new LEMSExpressionLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LEMSExpressionParser parser = new LEMSExpressionParser(tokens);
		ParseTree tree = parser.expression();
		this.tree = tree;
	}

	public <T> T parseAndVisitWith(LEMSExpressionBaseVisitor<T> visitor) {
		return visitor.visit(tree);
	}

}
