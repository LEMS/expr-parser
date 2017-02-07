package org.lemsml.expr_parser.visitors;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;
import org.lemsml.expr_parser.utils.TextUtils;

public class RenderJava extends ARenderAs {

	@Override
	String adaptNegate(String val) {
		return "-" + TextUtils.parenthesize(val);
	}

	@Override
	String adaptBinOp(Token op, String left, String right) {
		switch (op.getType()) {
		default:
			// We use Java notation in LEMS...
			return left + op.getText() + right;
		}
	}

	@Override
	String adaptFuncCall(Integer funcToken, String arg) {
		switch (funcToken) {
		case LEMSExpressionParser.SIN:
			return TextUtils.funcCall("Math.sin", arg);
		case LEMSExpressionParser.COS:
			return TextUtils.funcCall("Math.cos", arg);
		case LEMSExpressionParser.TAN:
			return TextUtils.funcCall("Math.tan", arg);
		case LEMSExpressionParser.SINH:
			return TextUtils.funcCall("Math.sinh", arg);
		case LEMSExpressionParser.COSH:
			return TextUtils.funcCall("Math.cosh", arg);
		case LEMSExpressionParser.TANH:
			return TextUtils.funcCall("Math.tanh", arg);
		case LEMSExpressionParser.EXP:
			return TextUtils.funcCall("Math.exp", arg);
		case LEMSExpressionParser.LOG:
			return TextUtils.funcCall("Math.log", arg);
		case LEMSExpressionParser.LN:
			return TextUtils.funcCall("Math.log", arg);
		case LEMSExpressionParser.FLOOR:
			return TextUtils.funcCall("Math.floor", arg);
		case LEMSExpressionParser.CEIL:
			return TextUtils.funcCall("Math.ceil", arg);
		case LEMSExpressionParser.ABS:
			return TextUtils.funcCall("Math.abs", arg);
		case LEMSExpressionParser.RAND:
			return TextUtils.funcCall("Math.random", arg);
		default:
			throw new ParseCancellationException("Unknow function "
					+ LEMSExpressionLexer.tokenNames[funcToken]);
		}
	}

}
