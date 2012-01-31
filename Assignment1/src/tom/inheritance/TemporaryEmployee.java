package tom.inheritance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author tom
 *
 */
public class TemporaryEmployee extends Employee {

	/**
	 * Create a new temporary employee
	 * @param name The temporary employee's full name
	 * @param hireDate The temporary employee's date of hire
	 * @param wage The temporary employee's hourly rate
	 */
	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage) {
		this(name, hireDate, wage, BigDecimal.ZERO);
	}

	/**
	 * Create a new temporary employee
	 * @param name The temporary employee's full name
	 * @param hireDate The temporary employee's date of hire
	 * @param wage The temporary employee's hourly rate
	 * @param numberOfHoursWorked The number of hours worked this pay period
	 */
	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal numberOfHoursWorked) {
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
	 * @param numberOfHoursWorked the number of hours worked this pay period to set
	 */
	public void setNumberOfHoursWorked(BigDecimal numberOfHoursWorked) {
		this.numberOfHoursWorked = numberOfHoursWorked;
	}
	
	/**
	 * Calculates earnings (regular and overtime) and prints paycheck
	 */
	@Override
	public void generatePayCheck() {
		
		BigDecimal wage = getWage();
		BigDecimal hours = getNumberOfHoursWorked();

		BigDecimal regularPay = null;
		BigDecimal overtimePay = null;

		// if hours > 40, then overtime was worked
		if (hours.compareTo(FULL_WEEK) > 0) {
			regularPay = wage.multiply(FULL_WEEK);
			overtimePay = wage.multiply(OVER_TIME_RATE).multiply(hours.subtract(FULL_WEEK));
		} else {  // no overtime was worked
			regularPay = wage.multiply(hours);
			overtimePay = BigDecimal.ZERO;
		}
			
		BigDecimal earnings = regularPay.add(overtimePay);

		StringBuilder s = new StringBuilder();
		s.append("Hourly Rate: ").append(CURRENCY_FORMAT.format(wage)).append(RETURN);
		s.append("Hours Worked: ").append(hours).append(RETURN);
		s.append("Pay: ").append(CURRENCY_FORMAT.format(earnings)).append(RETURN);
		
		printPaycheck(s.toString());

	}
	
	// constant values to support paycheck calculations
	private static final BigDecimal FULL_WEEK = new BigDecimal("40");
	private static final BigDecimal OVER_TIME_RATE = new BigDecimal("1.5");

	private BigDecimal numberOfHoursWorked;
}
