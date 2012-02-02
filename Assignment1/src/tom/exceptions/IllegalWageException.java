package tom.exceptions;

import java.math.BigDecimal;

public class IllegalWageException extends WageException {

	public IllegalWageException (BigDecimal wage) {
		super(wage);
	}
}
