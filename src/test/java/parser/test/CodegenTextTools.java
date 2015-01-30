package parser.test;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class CodegenTextTools {
	static public String funcCall(String ... args) {
		String pars = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ',');
		return args[0] + parenthesize(pars);
	}

	static public String parenthesize(String arg) {
		return "(" + arg + ")";
	}
}
