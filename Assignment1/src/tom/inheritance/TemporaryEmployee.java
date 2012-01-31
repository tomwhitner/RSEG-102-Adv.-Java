package tom.inheritance;

import java.math.BigDecimal;
import java.util.Date;

public class TemporaryEmployee extends Employee {

	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage) {
		this(name, hireDate, wage, BigDecimal.ZERO);
	}

	public TemporaryEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal numberOfHoursWorked) {
		super(name, hireDate, wage);
		this.numberOfHoursWorked = numberOfHoursWorked;
	}

	/**
	 * @return the numberOfHoursWorked
	 */
	public BigDecimal getNumberOfHoursWorked() {
		return numberOfHoursWorked;
	}

	/**
	 * @param numberOfHoursWorked the numberOfHoursWorked to set
	 */
	public void setNumberOfHoursWorked(BigDecimal numberOfHoursWorked) {
		this.numberOfHoursWorked = numberOfHoursWorked;
	}
	
	private static final BigDecimal FULL_WEEK = new BigDecimal("40");
	private static final BigDecimal OVER_TIME = new BigDecimal("1.5");
	
	@Override
	public void generatePayCheck() {
		
		BigDecimal earnings = null;
		BigDecimal hours = getNumberOfHoursWorked();
		BigDecimal wage = getWage();
		
		if (hours.compareTo(FULL_WEEK) > 0) {
			BigDecimal overTime = hours.subtract(FULL_WEEK).multiply(OVER_TIME).multiply(wage);
			earnings = wage.multiply(FULL_WEEK).add(overTime);
		} else {
			earnings = wage.multiply(hours);
		}
				
		StringBuilder s = new StringBuilder();
		s.append("Hourly Rate: ").append(CURRENCY_FORMAT.format(wage)).append(RETURN);
		s.append("Hours Worked: ").append(hours).append(RETURN);
		s.append("Pay: ").append(CURRENCY_FORMAT.format(earnings)).append(RETURN);
		
		printPaycheck(s.toString());

	}
	
	private BigDecimal numberOfHoursWorked;


}
