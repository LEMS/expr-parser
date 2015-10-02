package expr_parser.visitors;

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

import expr_parser.utils.UndefinedSymbolException;
import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;
import tec.units.ri.quantity.Quantities;

public class QuantityConditionalEvalVisitor extends LEMSExpressionBaseVisitor<BooleanOrQuantity> {

	Map<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
	Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	public QuantityConditionalEvalVisitor(Map<String, Quantity<?>> quantities,
			Map<String, Unit<?>> units) {
		this.context = quantities;
		this.unitContext = units;
	}

	/** expr */
	@Override
	public BooleanOrQuantity visitExpression(
			LEMSExpressionParser.ExpressionContext ctx) {
		BooleanOrQuantity result;
		if(ctx.arithmetic() == null || ctx.arithmetic().isEmpty()){
			result = new BooleanOrQuantity(visit(ctx.logic()).asBoolean());
		}
		else{
			result = new BooleanOrQuantity(visit(ctx.arithmetic()).asQuantity());
		}
		return result;
	}

	/** ID */
	@Override
	public BooleanOrQuantity visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		Quantity<?> quantity = context.get(id);
		if(null == quantity){
			String msg = MessageFormat
				.format("Symbol {0} undefined.", ctx.ID().getText());
			throw new ParseCancellationException(msg, new UndefinedSymbolException(msg));
		}
		return new BooleanOrQuantity(quantity);
	}

	/** '-' expr */
	@Override
	public BooleanOrQuantity visitNegate(LEMSExpressionParser.NegateContext ctx) {
		Quantity<?> visited = visit(ctx.arithmetic()).asQuantity();
		return new BooleanOrQuantity(Quantities.getQuantity(-visited.getValue().doubleValue(), visited.getUnit()));
	}

	/** expr op=POW expr */
	@Override
	public BooleanOrQuantity visitPow(LEMSExpressionParser.PowContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0)).asQuantity();
		Quantity<?> right = visit(ctx.arithmetic(1)).asQuantity();
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
		return new BooleanOrQuantity(Quantities.getQuantity(Math.pow(left.getValue().doubleValue(),
				exponent), left.getUnit().pow(exponent)));
	}

	/** FLOAT */
	@Override
	public BooleanOrQuantity visitFloatLiteral(
			LEMSExpressionParser.FloatLiteralContext ctx) {
		return new BooleanOrQuantity(Quantities.getQuantity(Double.valueOf(ctx.getText()), ONE));
	}

	/** FLOAT ID */
	@Override
	public BooleanOrQuantity visitDimensionalLiteral(
			LEMSExpressionParser.DimensionalLiteralContext ctx) {
		return new BooleanOrQuantity(Quantities.getQuantity(Double.valueOf(ctx.FLOAT().getText()),
				unitContext.get(ctx.ID().getText())));
	}

	/** expr op=('*'|'/') expr */
	@Override
	public BooleanOrQuantity visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0)).asQuantity();
		Quantity<?> right = visit(ctx.arithmetic(1)).asQuantity();
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return new BooleanOrQuantity(left.multiply(right));
		return new BooleanOrQuantity(left.divide(right));
	}

	/** expr op=('+'|'-') expr */
	@Override
	public BooleanOrQuantity visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0)).asQuantity();
		Quantity<?> right = visit(ctx.arithmetic(1)).asQuantity();
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
			return new BooleanOrQuantity(Quantities.getQuantity(leftVal + rightVal, left.getUnit()));
		return new BooleanOrQuantity(Quantities.getQuantity(leftVal - rightVal, left.getUnit()));
	}

	/** '(' expr ')' */
	@Override
	public BooleanOrQuantity visitParenthesized(
			LEMSExpressionParser.ParenthesizedContext ctx) {
		return visit(ctx.arithmetic()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public BooleanOrQuantity visitFunctionCall(
			LEMSExpressionParser.FunctionCallContext ctx) {
		Quantity<?> ret = null;
		ret = visit(ctx.arithmetic()).asQuantity();
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

		return new BooleanOrQuantity(ret);
	}

	public BooleanOrQuantity visitAnd(LEMSExpressionParser.AndContext ctx) {
		BooleanOrQuantity left = visit(ctx.logic(0));
		BooleanOrQuantity right = visit(ctx.logic(1));
		return new BooleanOrQuantity(left.asBoolean() && right.asBoolean());

	}

	public BooleanOrQuantity visitOr(LEMSExpressionParser.AndContext ctx) {
		BooleanOrQuantity left = visit(ctx.logic(0));
		BooleanOrQuantity right = visit(ctx.logic(1));
		return new BooleanOrQuantity(left.asBoolean() || right.asBoolean());
	}

	@SuppressWarnings("unchecked")
	@Override
	public BooleanOrQuantity visitComparison(LEMSExpressionParser.ComparisonContext ctx) {
		Quantity<?> left = visit(ctx.arithmetic(0)).asQuantity();
		Quantity<?> right = visit(ctx.arithmetic(1)).asQuantity();
		int compareTo = ((Comparable<Quantity<?>>) left).compareTo(right);
		switch (ctx.op.getType()) {
		case LEMSExpressionParser.GEQ:
			return new BooleanOrQuantity(compareTo > 0 || compareTo == 0);
		case LEMSExpressionParser.LEQ:
			return new BooleanOrQuantity(compareTo < 0 || compareTo == 0);
		case LEMSExpressionParser.GT:
			return new BooleanOrQuantity(compareTo > 0);
		case LEMSExpressionParser.LT:
			return new BooleanOrQuantity(compareTo < 0);
		case LEMSExpressionParser.EQ:
			System.out.println("WARNING: Float comparison is evil.");
			//return new BooleanOrQuantity(compareWithinUlp(l, r, 4));
			return new BooleanOrQuantity(compareTo == 0);

		case LEMSExpressionParser.NEQ:
			System.out.println("WARNING: Float comparison is evil.");
			return new BooleanOrQuantity(compareTo != 0);
			//return new BooleanOrQuantity(!compareWithinUlp(left, r, 4));
		default:
			throw new RuntimeException("Unknown operator: "
					+ LEMSExpressionParser.tokenNames[ctx.op.getType()]);
		}
	}

//	public static boolean compareWithinUlp(double expected, double actual, long maxUlps) {
//		long expectedBits = Double.doubleToLongBits(expected) < 0 ? 0x8000000000000000L - Double
//				.doubleToLongBits(expected) : Double.doubleToLongBits(expected);
//		long actualBits = Double.doubleToLongBits(actual) < 0 ? 0x8000000000000000L - Double
//				.doubleToLongBits(actual) : Double.doubleToLongBits(actual);
//		long difference = expectedBits > actualBits ? expectedBits - actualBits
//				: actualBits - expectedBits;
//
//		return !Double.isNaN(expected) && !Double.isNaN(actual)
//				&& difference <= maxUlps;
//	}
}
