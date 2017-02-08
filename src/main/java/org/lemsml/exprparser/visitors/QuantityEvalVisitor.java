package org.lemsml.exprparser.visitors;

import static tec.units.ri.AbstractUnit.ONE;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.measure.IncommensurableException;
import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.UnitConverter;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.lemsml.exprparser.utils.UndefinedSymbolException;
import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;
import tec.units.ri.quantity.Quantities;

public class QuantityEvalVisitor extends LEMSExpressionBaseVisitor<Quantity<?>> {
	Map<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
	Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	public QuantityEvalVisitor(Map<String, Quantity<?>> quantities,
			Map<String, Unit<?>> units) {
		this.context = quantities;
		this.unitContext = units;
	}

	/** expr */
	@Override
	public Quantity<?> visitExpression(
			LEMSExpressionParser.ExpressionContext ctx) {
		Quantity<?> result = visit(ctx.arithmetic());
		// System.out.println("[" + ctx.arithmetic().getText() + "] = " +
		// result);
		return result;
	}

	/** ID */
	@Override
	public Quantity<?> visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		Quantity<?> quantity = context.get(id);
		if(null == quantity){
			String msg = MessageFormat
				.format("Symbol {0} undefined.", ctx.ID().getText());
			throw new ParseCancellationException(msg, new UndefinedSymbolException(msg));
		}
		return quantity;
	}

	/** '-' expr */
	@Override
	public Quantity<?> visitNegate(LEMSExpressionParser.NegateContext ctx) {
		Quantity<?> visited = visit(ctx.arithmetic());
		return Quantities.getQuantity(-visited.getValue().doubleValue(), visited.getUnit());
	}

	/** expr op=POW expr */
	@Override
	public Quantity<?> visitPow(LEMSExpressionParser.PowContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0));
		Quantity<?> right = visit(ctx.arithmetic(1));
		if (!right.getUnit().isCompatible(ONE)) {
			String msg = MessageFormat
					.format("Expected adimensional exponent in '{0}{1}{2}', but found '{3}'",
							ctx.arithmetic(0).getText(),
							ctx.op.getText(),
							ctx.arithmetic(1).getText(),
							right.getUnit().getDimension());
			throw new ParseCancellationException(msg);
		}

		Integer exponent = right.getValue().intValue();
		return Quantities.getQuantity(Math.pow(left.getValue().doubleValue(),
				exponent), left.getUnit().pow(exponent));
	}

	/** FLOAT */
	@Override
	public Quantity<?> visitFloatLiteral(
			LEMSExpressionParser.FloatLiteralContext ctx) {
		return Quantities.getQuantity(Double.valueOf(ctx.getText()), ONE);
	}

	/** FLOAT ID */
	@Override
	public Quantity<?> visitDimensionalLiteral(
			LEMSExpressionParser.DimensionalLiteralContext ctx) {
		return Quantities.getQuantity(Double.valueOf(ctx.FLOAT().getText()),
				unitContext.get(ctx.ID().getText()));
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Quantity<?> visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0));
		Quantity<?> right = visit(ctx.arithmetic(1));
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return left.multiply(right);
		return left.divide(right);
	}

	/**
	 * expr op=('+'|'-') expr
	 *
	 */
	@Override
	public Quantity<?> visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0));
		Quantity<?> right = visit(ctx.arithmetic(1));
		UnitConverter converterToAny;
		try {
			converterToAny = right.getUnit().getConverterToAny(left.getUnit());
		} catch (UnconvertibleException | IncommensurableException e) {
			StringBuilder msgSb = new StringBuilder();
			msgSb.append("Incompatible units ");
			msgSb.append("'" + left.getUnit().getDimension() + "'");
			msgSb.append(" and ");
			msgSb.append("'" + right.getUnit().getDimension() + "'");
			msgSb.append(" in expression ");
			msgSb.append("'" + ctx.arithmetic(0).getText() + ctx.op.getText()
					+ ctx.arithmetic(1).getText() + "'");
			throw new ParseCancellationException(msgSb.toString());
		}
		Double rightVal = converterToAny.convert(right.getValue())
				.doubleValue();
		Double leftVal = left.getValue().doubleValue();
		if (ctx.op.getType() == LEMSExpressionParser.ADD)
			return Quantities.getQuantity(leftVal + rightVal, left.getUnit());
		return Quantities.getQuantity(leftVal - rightVal, left.getUnit());
	}

	/** '(' expr ')' */
	@Override
	public Quantity<?> visitParenthesized(
			LEMSExpressionParser.ParenthesizedContext ctx) {
		return visit(ctx.arithmetic()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Quantity<?> visitFunctionCall(
			LEMSExpressionParser.FunctionCallContext ctx) {
		Quantity<?> ret = null;
		ret = visit(ctx.arithmetic());
		if (!ret.getUnit().isCompatible(ONE)) {
			StringBuilder msgSb = new StringBuilder();
			msgSb.append("Expected adimensional argument in ");
			msgSb.append("'" + ctx.builtin().func.getText());
			msgSb.append("(" + ctx.arithmetic().getText() + ")'");
			msgSb.append(", but found ");
			msgSb.append("'" + ret.getUnit().getDimension() + "'");
			throw new ParseCancellationException(msgSb.toString());
		}
		Double val = ret.getValue().doubleValue();

		switch (ctx.builtin().func.getType()) {
		case LEMSExpressionParser.SIN:
			ret = Quantities.getQuantity(Math.sin(val), ONE);
			break;
		case LEMSExpressionParser.COS:
			ret = Quantities.getQuantity(Math.cos(val), ONE);
			break;
		case LEMSExpressionParser.TAN:
			ret = Quantities.getQuantity(Math.tan(val), ONE);
			break;
		case LEMSExpressionParser.SINH:
			ret = Quantities.getQuantity(Math.sinh(val), ONE);
			break;
		case LEMSExpressionParser.COSH:
			ret = Quantities.getQuantity(Math.cosh(val), ONE);
			break;
		case LEMSExpressionParser.TANH:
			ret = Quantities.getQuantity(Math.tanh(val), ONE);
			break;
		case LEMSExpressionParser.EXP:
			ret = Quantities.getQuantity(Math.exp(val), ONE);
			break;
		case LEMSExpressionParser.LOG:
			ret = Quantities.getQuantity(Math.log(val), ONE);
			break;
		case LEMSExpressionParser.LN:
			ret = Quantities.getQuantity(Math.log(val), ONE);
			break;
		case LEMSExpressionParser.FLOOR:
			ret = Quantities.getQuantity(Math.floor(val), ONE);
			break;
		case LEMSExpressionParser.CEIL:
			ret = Quantities.getQuantity(Math.ceil(val), ONE);
			break;
		case LEMSExpressionParser.ABS:
			ret = Quantities.getQuantity(Math.abs(val), ONE);
			break;
		case LEMSExpressionParser.RAND:
			ret = Quantities.getQuantity(Math.random(), ONE);
			break;
		default:

			break;
		}

		return ret;
	}
}
