package tom.exceptions;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class WageException extends Exception {

	public WageException(BigDecimal wage, String message) {
		super(message);
		this.wage = wage;
	}

	protected static final NumberFormat CURRENCY_FORMAT = NumberFormat
			.getCurrencyInstance();

	public BigDecimal getWage() {
		return wage;
	}

	private BigDecimal wage;
}
