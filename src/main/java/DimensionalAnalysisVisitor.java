
import static tec.units.ri.AbstractUnit.ONE;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class DimensionalAnalysisVisitor extends
		LEMSExpressionBaseVisitor<Unit<?>> {
	Map<String, Unit<?>> context = new HashMap<String, Unit<?>>();

	public DimensionalAnalysisVisitor(Map<String, Unit<?>> dimensions) {
		this.context = dimensions;
	}

	/** expr */
	@Override
	public Unit<?> visitBase(LEMSExpressionParser.BaseContext ctx) {
		Unit<?> result = visit(ctx.expr());
		System.out.println("[" + ctx.expr().getText() + "] = " + result);
		return result;
	}

	/** ID */
	@Override
	public Unit<?> visitIdentifier(LEMSExpressionParser.IdentifierContext ctx) {
		String id = ctx.ID().getText();
		// TODO: check for undefined ids
		return context.get(id);
	}

	/** '-' expr */
	@Override
	public Unit<?> visitNegate(LEMSExpressionParser.NegateContext ctx) {
		return visit(ctx.expr());
	}

	/** expr op=POW expr */
	@Override
	public Unit<?> visitPow(LEMSExpressionParser.PowContext ctx) {
		Unit<?> left = visit(ctx.expr(0));
		Unit<?> right = visit(ctx.expr(1));
		if (!right.getDimension().equals(ONE.getDimension())) {
			StringBuilder msgSb = new StringBuilder();
			msgSb.append("Expected adimensional exponent in ");
			msgSb.append("'" + ctx.expr(0).getText() + ctx.op.getText()
					+ ctx.expr(1).getText() + "'");
			msgSb.append(", but found ");
			msgSb.append("'" + right.getDimension() + "'");
			throw new ParseCancellationException(msgSb.toString());
		}
		// TODO: there be dragons. Need to eval expr to get the exp...
		// hoping to find only integers in exponents, need to
		// catch exception here
		return left.pow(Integer.parseInt(ctx.expr(1).getText()));
	}

	/** FLOAT */
	@Override
	public Unit<?> visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx) {
		return ONE;
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Unit<?> visitMulDiv(LEMSExpressionParser.MulDivContext ctx) {
		Unit<?> left = visit(ctx.expr(0));
		Unit<?> right = visit(ctx.expr(1));
		if (ctx.op.getType() == LEMSExpressionParser.MUL)
			return left.multiply(right);
		return left.divide(right);
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Unit<?> visitAddSub(LEMSExpressionParser.AddSubContext ctx) {
		Unit<?> left = visit(ctx.expr(0));
		Unit<?> right = visit(ctx.expr(1));
		if (!left.getDimension().equals(right.getDimension())) {
			StringBuilder msgSb = new StringBuilder();
			msgSb.append("Incompatible units ");
			msgSb.append("'" + left.getDimension() + "'");
			msgSb.append(" and ");
			msgSb.append("'" + right.getDimension() + "'");
			msgSb.append(" in expression ");
			msgSb.append("'" + ctx.expr(0).getText() + ctx.op.getText()
					+ ctx.expr(1).getText() + "'");
			throw new ParseCancellationException(msgSb.toString());
		}
		return right;
	}

	/** '(' expr ')' */
	@Override
	public Unit<?> visitParenthesized(LEMSExpressionParser.ParenthesizedContext ctx) {
		return visit(ctx.expr()); // return child expr's value
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Unit<?> visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx) {
		Unit<?> ret = null;
		switch (ctx.BuiltinFuncs().getText()) {
		case "random":
			return ONE;
		default:
			ret = visit(ctx.expr());
			if (!ret.getDimension().equals(ONE.getDimension())) {
				StringBuilder msgSb = new StringBuilder();
				msgSb.append("Expected adimensional argument in ");
				msgSb.append("'" + ctx.BuiltinFuncs().getText());
				msgSb.append("(" + ctx.expr().getText() + ")'");
				msgSb.append(", but found ");
				msgSb.append("'" + ret.getDimension() + "'");
				throw new ParseCancellationException(msgSb.toString());
			}
			break;
		}

		return ret;
	}

}
