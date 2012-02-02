/**
 * 
 */
package tom.interfaces;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Employee is an interface that describes the common behavior for all employees.
 * @author tom
 *
 */
public interface Employee {

	/**
	 * @return the employee's date of hire
	 */
	Date getHireDate();

	/**
	 * @return the employee's full name
	 */
	String getName();

	/**
	 * @return the employee's wage
	 */
	BigDecimal getWage();

	/**
	 * Performs earnings and vacation calculations and prints paycheck.
	 */
	BigDecimal generatePayCheck();
	
	/**
	 * The number of pay periods per year
	 */
	static final BigDecimal NUMBER_OF_PAY_PERIODS = new BigDecimal("26");
}
