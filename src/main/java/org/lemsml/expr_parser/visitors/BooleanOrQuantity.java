package org.lemsml.expr_parser.visitors;

import javax.measure.Quantity;

public class BooleanOrQuantity {
	public static BooleanOrQuantity VOID = new BooleanOrQuantity(new Object());

	final Object value;

	public BooleanOrQuantity(Object value) {
		this.value = value;
	}

	public Boolean asBoolean() {
		return (Boolean) value;
	}

	public Quantity<?> asQuantity() {
		return (Quantity<?>) value;
	}

	public boolean isQuantity() {
		return value instanceof Quantity<?>;
	}

	@Override
	public int hashCode() {

		if (value == null) {
			return 0;
		}

		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object o) {

		if (value == o) {
			return true;
		}

		if (value == null || o == null || o.getClass() != value.getClass()) {
			return false;
		}

		BooleanOrQuantity that = (BooleanOrQuantity) o;

		return this.value.equals(that.value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
