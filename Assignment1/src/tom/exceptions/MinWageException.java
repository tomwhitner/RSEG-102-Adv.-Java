package tom.exceptions;

import java.math.BigDecimal;

public class MinWageException extends WageLimitException {

	public MinWageException(BigDecimal wage, BigDecimal wageLimit) {
		super(wage, "Wage " + CURRENCY_FORMAT.format(wage)
				+ " is out of range.  Must be greater than or equal to "
				+ CURRENCY_FORMAT.format(wageLimit), wageLimit);
	}
}
