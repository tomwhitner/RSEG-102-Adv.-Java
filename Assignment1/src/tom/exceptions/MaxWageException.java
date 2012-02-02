package tom.exceptions;

import java.math.BigDecimal;

public class MaxWageException extends WageLimitException {

	public MaxWageException (BigDecimal wage, BigDecimal wageLimit) {
		super(wage, wageLimit);
	}
}
