package parser.test;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;

public class AdaptToCVisitor extends AAdaptToLangVisitor {
	@Override
	String adaptNegate(String val) {
		return "-" + TextUtils.parenthesize(val);
	}

	@Override
	String adaptBinOp(Integer opType, String left, String right) {
		// java is ludicrous
		String tok = LEMSExpressionParser.tokenNames[opType];
		String op = tok.substring(1, tok.length() - 1);
		switch (opType) {
		case LEMSExpressionParser.POW:
			return TextUtils.funcCall("pow", left, right);
		default:
			return left + op + right;
		}
	}

	@Override
	String adaptFuncCall(Integer funcToken, String arg) {
		switch (funcToken) {
		case LEMSExpressionParser.SIN:
			return TextUtils.funcCall("sin", arg);
		case LEMSExpressionParser.COS:
			return TextUtils.funcCall("cos", arg);
		case LEMSExpressionParser.TAN:
			return TextUtils.funcCall("tan", arg);
		case LEMSExpressionParser.SINH:
			return TextUtils.funcCall("tanh", arg);
		case LEMSExpressionParser.COSH:
			return TextUtils.funcCall("cosh", arg);
		case LEMSExpressionParser.TANH:
			return TextUtils.funcCall("tanh", arg);
		case LEMSExpressionParser.EXP:
			return TextUtils.funcCall("exp", arg);
		case LEMSExpressionParser.LOG:
			return TextUtils.funcCall("log", arg);
		case LEMSExpressionParser.LN:
			return TextUtils.funcCall("log", arg);
		case LEMSExpressionParser.FLOOR:
			return TextUtils.funcCall("floor", arg);
		case LEMSExpressionParser.CEIL:
			return TextUtils.funcCall("ceil", arg);
		case LEMSExpressionParser.ABS:
			return TextUtils.funcCall("fabs", arg);
		case LEMSExpressionParser.RAND:
			return TextUtils.funcCall("rand", arg);
		default:
			throw new ParseCancellationException("Unknow function "
					+ LEMSExpressionLexer.tokenNames[funcToken]);
		}
	}
}
