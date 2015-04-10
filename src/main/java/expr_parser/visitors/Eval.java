package expr_parser.visitors;

import parser.LEMSExpressionBaseVisitor;
import parser.LEMSExpressionParser;

public class Eval extends LEMSExpressionBaseVisitor<Value>
{

	/** expr */
	@Override
	public Value visitExpression(LEMSExpressionParser.ExpressionContext ctx)
	{
		Value result;
		if(ctx.arithmetic() != null)
		{
			result = new Value(visit(ctx.arithmetic()).asDouble());
			System.out.println(ctx.arithmetic().getText() + " = " + result);
		}
		else
		{
			result = new Value(visit(ctx.logic()).asBoolean());
			System.out.println(ctx.logic().getText() + " = " + result);
		}
		return result;
	}

	/** '-' expr */
	@Override
	public Value visitNegate(LEMSExpressionParser.NegateContext ctx)
	{
		Value val = visit(ctx.arithmetic());
		return new Value(-val.asDouble());
	}

	/** expr op=POW expr */
	@Override
	public Value visitPow(LEMSExpressionParser.PowContext ctx)
	{
		Value left = visit(ctx.arithmetic(0));
		Value right = visit(ctx.arithmetic(1));
		return new Value(Math.pow(left.asDouble(), right.asDouble()));
	}

	/** FLOAT */
	@Override
	public Value visitFloatLiteral(LEMSExpressionParser.FloatLiteralContext ctx)
	{
		return new Value(Double.valueOf(ctx.FLOAT().getText()));
	}

	/** expr op=('*'|'/') expr */
	@Override
	public Value visitMulDiv(LEMSExpressionParser.MulDivContext ctx)
	{
		Value left = visit(ctx.arithmetic(0));
		Value right = visit(ctx.arithmetic(1));
		if(ctx.op.getType() == LEMSExpressionParser.MUL) return new Value(left.asDouble() * right.asDouble());
		return new Value(left.asDouble() / right.asDouble());
	}

	/** expr op=('+'|'-') expr */
	@Override
	public Value visitAddSub(LEMSExpressionParser.AddSubContext ctx)
	{
		Value left = visit(ctx.arithmetic(0));
		Value right = visit(ctx.arithmetic(1));
		if(ctx.op.getType() == LEMSExpressionParser.ADD) return new Value(left.asDouble() + right.asDouble());
		return new Value(left.asDouble() - right.asDouble());
	}

	/** '(' expr ')' */
	@Override
	public Value visitParenthesized(LEMSExpressionParser.ParenthesizedContext ctx)
	{
		return visit(ctx.arithmetic());
	}

	/** BuiltinFuncs '(' expr ')' */
	@Override
	public Value visitFunctionCall(LEMSExpressionParser.FunctionCallContext ctx)
	{
		Value ret = null;
		// TODO: check nargs (rand takes 0)
		switch(ctx.builtin().func.getType())
		{
			case LEMSExpressionParser.SIN:
				ret = new Value(Math.sin(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.COS:
				ret = new Value(Math.cos(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.TAN:
				ret = new Value(Math.tan(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.SINH:
				ret = new Value(Math.sinh(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.COSH:
				ret = new Value(Math.cosh(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.TANH:
				ret = new Value(Math.tanh(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.EXP:
				ret = new Value(Math.exp(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.LOG:
				ret = new Value(Math.log(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.LN:
				ret = new Value(Math.log(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.FLOOR:
				ret = new Value(Math.floor(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.CEIL:
				ret = new Value(Math.ceil(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.ABS:
				ret = new Value(Math.abs(visit(ctx.arithmetic()).asDouble()));
				break;
			case LEMSExpressionParser.RAND:
				ret = new Value(Math.random());
				break;
		}

		return ret;
	}

}
