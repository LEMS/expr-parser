package parser.test;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;

public class AdaptToCVisitor extends AAdaptToLangVisitor {
	@Override
	String adaptNegate(String val) {
		return "-" + CodegenTextTools.parenthesize(val);
	}

	@Override
	String adaptBinOp(Integer opType, String left, String right) {
		// java is ludicrous
		String tok = LEMSExpressionParser.tokenNames[opType];
		String op = tok.substring(1, tok.length() - 1);
		switch (opType) {
		case LEMSExpressionParser.POW:
			return CodegenTextTools.funcCall("pow", left, right);
		default:
			return left + op + right;
		}
	}

	@Override
	String adaptFuncCall(Integer funcToken, String arg) {
		switch (funcToken) {
		case LEMSExpressionParser.SIN:
			return CodegenTextTools.funcCall("sin", arg);
		case LEMSExpressionParser.COS:
			return CodegenTextTools.funcCall("cos", arg);
		case LEMSExpressionParser.TAN:
			return CodegenTextTools.funcCall("tan", arg);
		case LEMSExpressionParser.SINH:
			return CodegenTextTools.funcCall("tanh", arg);
		case LEMSExpressionParser.COSH:
			return CodegenTextTools.funcCall("cosh", arg);
		case LEMSExpressionParser.TANH:
			return CodegenTextTools.funcCall("tanh", arg);
		case LEMSExpressionParser.EXP:
			return CodegenTextTools.funcCall("exp", arg);
		case LEMSExpressionParser.LOG:
			return CodegenTextTools.funcCall("log", arg);
		case LEMSExpressionParser.LN:
			return CodegenTextTools.funcCall("log", arg);
		case LEMSExpressionParser.FLOOR:
			return CodegenTextTools.funcCall("floor", arg);
		case LEMSExpressionParser.CEIL:
			return CodegenTextTools.funcCall("ceil", arg);
		case LEMSExpressionParser.ABS:
			return CodegenTextTools.funcCall("fabs", arg);
		case LEMSExpressionParser.RAND:
			return CodegenTextTools.funcCall("rand", arg);
		default:
			throw new ParseCancellationException("Unknow function "
					+ LEMSExpressionLexer.tokenNames[funcToken]);
		}
	}
}
