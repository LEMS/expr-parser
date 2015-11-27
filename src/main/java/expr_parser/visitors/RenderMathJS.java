package expr_parser.visitors;

import java.util.Map;

import javax.measure.IncommensurableException;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.UnitConverter;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import parser.LEMSExpressionLexer;
import parser.LEMSExpressionParser;
import expr_parser.utils.TextUtils;

public class RenderMathJS extends ARenderAs {

	private Map<String, Unit<?>> unitContext;

	public RenderMathJS(Map<String, Unit<?>> units) {
		this.unitContext = units;
	}

	@Override
	public String adaptNegate(String val) {
		return "-" + TextUtils.parenthesize(val);
	}

	@Override
	public String adaptBinOp(Token op, String left, String right) {
		switch (op.getType()) {
		default:
			// We use Java notation in LEMS...
			return left + op.getText() + right;
		}
	}

	@Override
	public String adaptFuncCall(Integer funcToken, String arg) {
		switch (funcToken) {
		case LEMSExpressionParser.SIN:
			return TextUtils.funcCall("sin", arg);
		case LEMSExpressionParser.COS:
			return TextUtils.funcCall("cos", arg);
		case LEMSExpressionParser.TAN:
			return TextUtils.funcCall("tan", arg);
		case LEMSExpressionParser.SINH:
			return TextUtils.funcCall("sinh", arg);
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
			return TextUtils.funcCall("abs", arg);
		case LEMSExpressionParser.RAND:
			return TextUtils.funcCall("random", arg);
		default:
			throw new ParseCancellationException("Unknow function "
					+ LEMSExpressionLexer.tokenNames[funcToken]);
		}
	}

	@Override
	public String visitDimensionalLiteral(
			LEMSExpressionParser.DimensionalLiteralContext ctx) {
		Unit<?> unit = unitContext.get(ctx.ID().getText());
		UnitConverter converterToAny = null;
		try {
			converterToAny = unit.getConverterToAny(unit.getSystemUnit());
		} catch (UnconvertibleException | IncommensurableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Number val = converterToAny.convert(Double.valueOf(ctx.FLOAT()
				.getText()));
		return val.toString();

	}

	@Override
	public String visitAnd(LEMSExpressionParser.AndContext ctx) {
		return visit(ctx.logic(0)) + " and " + visit(ctx.logic(1));

	}

	@Override
	public String visitOr(LEMSExpressionParser.OrContext ctx) {
		return visit(ctx.logic(0)) + " or " + visit(ctx.logic(1));
	}

	public String visitComparison(LEMSExpressionParser.ComparisonContext ctx) {
		String left = visit(ctx.arithmetic(0));
		String right = visit(ctx.arithmetic(1));

		switch (ctx.op.getType()) {
		case LEMSExpressionParser.GEQ:
			return left + " >= " + right;
		case LEMSExpressionParser.LEQ:
			return left + " <= " + right;
		case LEMSExpressionParser.GT:
			return left + " > " + right;
		case LEMSExpressionParser.LT:
			return left + " < " + right;
		case LEMSExpressionParser.EQ:
			return left + " == " + right;
		case LEMSExpressionParser.NEQ:
			return left + " != " + right;
		default:
			throw new RuntimeException("Unknown operator: "
					+ LEMSExpressionParser.tokenNames[ctx.op.getType()]);
		}
	}

}
