package parser.test;

import java.util.Arrays;

import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class StringTemplateTest {


	private TestModel model = new TestModel();


	@Test
	public void testST() {
        ST hello = new ST("Hello, <name>");
        hello.add("name", "World");
        System.out.println(hello.render());
        
	}


	@Test
	public void testGroup() {
		STGroup group = new STGroupFile("/Users/boris/git/lems-expr-parser/src/test/java/parser/test/test.stg");
		ST st = group.getInstanceOf("test");

		model.name = "model";
		model.value = 1d;
		st.add("parameters", Arrays.asList("p1", "p2", "p3"));
		st.add("model", model);
        System.out.println(st.render());

	}

}
