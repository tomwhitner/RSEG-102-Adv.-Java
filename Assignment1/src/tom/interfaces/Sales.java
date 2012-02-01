package tom.interfaces;

import java.math.BigDecimal;

public interface Sales extends Employee {

	/**
	 * @return the sales employee's sales for this pay period
	 */
	BigDecimal getSales();

	/**
	 * @return the sales employee's commission percentage
	 */
	BigDecimal getCommission();

	/**
	 * Calculates the employee's commission for this pay period
	 * @return the employee's commission for this pay period
	 */
	BigDecimal calculateCommmission();
}
