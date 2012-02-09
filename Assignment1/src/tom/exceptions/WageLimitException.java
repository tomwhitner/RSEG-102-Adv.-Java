package tom.exceptions;

import java.math.BigDecimal;

public class WageLimitException extends WageException {

	public WageLimitException (BigDecimal wage, String message, BigDecimal wageLimit) {
		super(wage, message);
		this.wageLimit = wageLimit;
	}
	
	public BigDecimal getWageDiff() {
		return  getWage().subtract(wageLimit);  
	}
	
	public BigDecimal getWageLimit() {
		return wageLimit;
	}
	
	private BigDecimal wageLimit;

}
