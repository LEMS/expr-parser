package expr_parser.parser;

import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;
import expr_parser.utils.TextUtils;

public class AdaptToLatexVisitor extends AAdaptToLangVisitor
{
	@Override
	String adaptNegate(String val)
	{
		return "-" + TextUtils.parenthesize(val);
	}

	@Override
	public String visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx)
	{
		// q&d
		String[] flt = ctx.FLOAT().getText().toLowerCase().split("e");
		String ret;
		if(flt.length > 1)
		{
			ret = flt[0] + "\\times 10^" + TextUtils.bracketize(flt[1]);
		}
		else
		{
			ret = flt[0];
		}
		return ret;
	}

	@Override
	public String visitPow(LEMSExpressionParser.PowContext ctx)
	{
		List<Integer> needFlippedExponent = Arrays.asList(LEMSExpressionParser.SIN, LEMSExpressionParser.COS, LEMSExpressionParser.TAN, LEMSExpressionParser.SINH, LEMSExpressionParser.COSH,
				LEMSExpressionParser.TANH);

		Token nextLeftToken = ctx.arithmetic(0).getStart();

		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));

		if(needFlippedExponent.contains(nextLeftToken.getType()))
		{
			// q & very d
			String tokenText = nextLeftToken.getText();
			return left.replace(tokenText, tokenText + "^" + TextUtils.bracketize(right));
		}
		else
		{
			return left + '^' + TextUtils.bracketize(right);
		}
	}

	@Override
	String adaptBinOp(Token op, String left, String right)
	{
		switch(op.getType())
		{
			case LEMSExpressionParser.MUL:
				if(right.matches("^[0-9]"))
				{
					// need explicit operator e.g. a*2 -> a.2
					return left + "\\cdot " + right;
				}
				else
				{
					// just a space will do e.g. a*b -> a b
					return left + "\\ " + right;
				}
			case LEMSExpressionParser.DIV:
				return "\\frac" + TextUtils.bracketize(left) + TextUtils.bracketize(right);
			default:
				return left + op.getText() + right;
		}
	}

	@Override
	String adaptFuncCall(Integer funcToken, String arg)
	{
		switch(funcToken)
		{
			case LEMSExpressionParser.SIN:
				return TextUtils.latexFuncCall("\\sin", arg);
			case LEMSExpressionParser.COS:
				return TextUtils.latexFuncCall("\\cos", arg);
			case LEMSExpressionParser.TAN:
				return TextUtils.latexFuncCall("\\tan", arg);
			case LEMSExpressionParser.SINH:
				return TextUtils.latexFuncCall("\\sinh", arg);
			case LEMSExpressionParser.COSH:
				return TextUtils.latexFuncCall("\\cosh", arg);
			case LEMSExpressionParser.TANH:
				return TextUtils.latexFuncCall("\\tanh", arg);
			case LEMSExpressionParser.EXP:
				return TextUtils.latexFuncCall("\\exp", arg);
			case LEMSExpressionParser.LOG:
				return TextUtils.latexFuncCall("\\log", arg);
			case LEMSExpressionParser.LN:
				return TextUtils.latexFuncCall("\\log", arg);
			case LEMSExpressionParser.FLOOR:
				return "\\lfloor " + arg + "\\rfloor";
			case LEMSExpressionParser.CEIL:
				return "\\lceil " + arg + "\\rceil";
			case LEMSExpressionParser.ABS:
				return "\\lvert " + arg + "\\rvert";
			case LEMSExpressionParser.RAND:
				return "\\operatorname{rand}()";
			default:
				throw new ParseCancellationException("Unknow function " + LEMSExpressionLexer.tokenNames[funcToken]);
		}
	}
}
