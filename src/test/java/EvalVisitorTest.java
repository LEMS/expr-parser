import static org.junit.Assert.assertEquals;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

public class EvalVisitorTest {
  private static final double EPSILON = 0.0000000001d;

  @Test
  public void testAddSub() {
    assertEquals(2.0d, getResult("1.5 + 0.5"), EPSILON);
    assertEquals(1.0d, getResult("1.5 - 0.5"), EPSILON);
  }

  @Test
  public void testMultDiv() {
    assertEquals(-6.0d, getResult("-0.2e1 * 3"), EPSILON);
    assertEquals(3.0d, getResult("9 / 3"), EPSILON);
  }

  @Test
  public void testParenthesis() {
    assertEquals(4.5d, getResult("3 * (1 + .5)"), EPSILON);
  }

  @Test
  public void testNegate() {
    assertEquals(1.5d, getResult("3 * (1 + -.5)"), EPSILON);
  }

  @Test
  public void testTwoParenthesis() {
    assertEquals(4.5d, getResult("3 * ((9.0 - 7) / 2 + .5)"), EPSILON);
  }

  @Test
  public void testTrig() {
    assertEquals(0.0d, getResult("sin(0)"), EPSILON);
    assertEquals(1.0d, getResult("sin(0.5*3.14159265359)"), EPSILON);
    assertEquals(1.0d, getResult("sin(1/2)^2 + cos(1/2)^2"), EPSILON);
  }


  private Float getResult(String expression) {
    ANTLRInputStream input = new ANTLRInputStream(expression);
    LEMSExpressionLexer lexer = new LEMSExpressionLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    LEMSExpressionParser parser = new LEMSExpressionParser(tokens);
    ParseTree tree = parser.base();

    EvalVisitor eval = new EvalVisitor();
    return eval.visit(tree);
  }
}
