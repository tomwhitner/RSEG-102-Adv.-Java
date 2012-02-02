package tom.exceptions;

import java.math.BigDecimal;

public class WageException extends Exception {

	public WageException (BigDecimal wage) {
		this.wage = wage;
	}
	
	public BigDecimal getWage() {
		return wage;
	}
	
	private BigDecimal wage;
}
