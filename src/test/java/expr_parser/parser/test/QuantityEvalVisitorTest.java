package expr_parser.parser.test;

import static org.junit.Assert.assertEquals;
import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.unit.MetricPrefix.CENTI;
import static tec.units.ri.unit.SI.METRE;
import static tec.units.ri.unit.SI.SECOND;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tec.units.ri.quantity.Quantities;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class QuantityEvalVisitorTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	Map<String, Quantity<?>> ctxt = new HashMap<String, Quantity<?>>();
	Map<String, Unit<?>> uctxt = new HashMap<String, Unit<?>>();
	Map<String, Quantity<?>> exprExpected = new HashMap<String, Quantity<?>>();

	@SuppressWarnings("deprecation")
	@Before
	public void setUp() {
		ctxt.put("x", Quantities.getQuantity(1.0, CENTI(METRE)));
		ctxt.put("y", Quantities.getQuantity(1.0, SECOND));
		uctxt.put("cm", CENTI(METRE));
		uctxt.put("m", METRE);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void shouldPass() throws UndefinedSymbolException {

		verify("x + x", cm(2.));
		verify("x - x", cm(0.));
		verify("1/(1+exp((x - x)/(10*x)))", Quantities.getQuantity(0.5, ONE));
		verify("1.2cm", cm(1.2));
		verify("x^2", cm(1.).multiply(cm(1.)));
		verify("x^(1+1)", cm(1.).multiply(cm(1.)));
		// dodgy float comparision...
		verify("x * sin(3.14159265358979 cm/x/2.)", cm(1.));
		verify("x * y / y / x * (x-x)", cm(0.));
		verify("cos(x * y / y / x * (x-x)/2/x)", Quantities.getQuantity(1., ONE));
		verify("-0.1cm", cm(-0.1));

		//non commutativity!!!
		// am I shooting myself in the foot?
		verify("1cm + 1m", cm(101.));
		verify("1m + 1cm", Quantities.getQuantity(1.01, METRE));
	}


	@Test
	public void shouldFail() throws UndefinedSymbolException {
		exception.expect(ParseCancellationException.class);

		verify("x + y", null);
		verify("x * y",  null);
		verify("sin(x)", null);

	}

	public void verify(String expr, Quantity<?> expected)
			throws UndefinedSymbolException {
		System.out.println(MessageFormat.format("Verifying [{0} ?= {1}]", expr, expected));
		Quantity<?> r = ExpressionParser.evaluateQuantitiesContext(expr, ctxt,
				uctxt);
		assertEquals(expected, r);
	}

	@SuppressWarnings("deprecation")
	public Quantity<Length> cm(Double x) {
		return Quantities.getQuantity(x, CENTI(METRE));
	}

	@SuppressWarnings("deprecation")
	public Quantity<Time> s(Double x) {
		return Quantities.getQuantity(x, SECOND);
	}

}
