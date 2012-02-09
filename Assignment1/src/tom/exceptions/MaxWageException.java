package tom.exceptions;

import java.math.BigDecimal;

public class MaxWageException extends WageLimitException {

	public MaxWageException(BigDecimal wage, BigDecimal wageLimit) {
		super(wage, "Wage " + CURRENCY_FORMAT.format(wage)
				+ " is out of range.  Must be less than or equal to "
				+ CURRENCY_FORMAT.format(wageLimit), wageLimit);
	}
}
