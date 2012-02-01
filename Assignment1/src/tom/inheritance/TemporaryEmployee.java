package tom.inheritance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TemporaryEmployee represents and hourly based employee
 * 
 * @author tom
 * 
 */
public class TemporaryEmployee extends Employee {

	/**
	 * Create a new temporary employee
	 * 
	 * @param name
	 *            The temporary employee's full name
	 * @param hireDate
	 *            The temporary employee's date of hire
	 * @param wage
	 *            The temporary employee's hourly rate
	 */
	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage) {
		this(name, hireDate, wage, BigDecimal.ZERO);
	}

	/**
	 * Create a new temporary employee
	 * 
	 * @param name
	 *            The temporary employee's full name
	 * @param hireDate
	 *            The temporary employee's date of hire
	 * @param wage
	 *            The temporary employee's hourly rate
	 * @param numberOfHoursWorked
	 *            The number of hours worked this pay period
	 */
	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage,
			BigDecimal numberOfHoursWorked) {
		super(name, hireDate, wage);
		this.numberOfHoursWorked = numberOfHoursWorked;
	}

	/**
	 * @return the number of hours worked this pay period
	 */
	public BigDecimal getNumberOfHoursWorked() {
		return numberOfHoursWorked;
	}

	/**
	 * @param numberOfHoursWorked
	 *            the number of hours worked this pay period to set
	 */
	public void setNumberOfHoursWorked(BigDecimal numberOfHoursWorked) {
		this.numberOfHoursWorked = numberOfHoursWorked;
	}

	/**
	 * Performs earnings and vacation calculations and prints paycheck.
	 * 
	 * @return the employee's pay for the period
	 */
	@Override
	public BigDecimal generatePayCheck() {

		BigDecimal wage = getWage();
		BigDecimal hours = getNumberOfHoursWorked();

		BigDecimal regularPay = null;
		BigDecimal overtimePay = null;

		// if hours > 40, then overtime was worked
		if (hours.compareTo(FULL_WEEK) > 0) {
			regularPay = wage.multiply(FULL_WEEK);
			overtimePay = wage.multiply(OVER_TIME_RATE).multiply(
					hours.subtract(FULL_WEEK));
		} else { // no overtime was worked
			regularPay = wage.multiply(hours);
			overtimePay = BigDecimal.ZERO;
		}

		BigDecimal earnings = regularPay.add(overtimePay);

		return earnings;
	}


	// constant values to support paycheck calculations
	private static final BigDecimal FULL_WEEK = new BigDecimal("40");
	private static final BigDecimal OVER_TIME_RATE = new BigDecimal("1.5");

	private BigDecimal numberOfHoursWorked;
}
