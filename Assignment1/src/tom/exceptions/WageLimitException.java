package tom.exceptions;

import java.math.BigDecimal;

public class WageLimitException extends WageException {

	public WageLimitException (BigDecimal wage, BigDecimal wageLimit) {
		super(wage);
		this.wageLimit = wageLimit;
	}
	
	public BigDecimal getWageDiff() {
		return  getWage().subtract(wageLimit);  // double check sign of this operation.
	}
	
	public BigDecimal getWageLimit() {
		return wageLimit;
	}
	
	private BigDecimal wageLimit;

}
