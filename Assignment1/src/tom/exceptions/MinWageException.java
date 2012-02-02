package tom.exceptions;

import java.math.BigDecimal;

public class MinWageException extends WageLimitException {

	public MinWageException (BigDecimal wage, BigDecimal wageLimit) {
		super(wage, wageLimit);
	}
}
