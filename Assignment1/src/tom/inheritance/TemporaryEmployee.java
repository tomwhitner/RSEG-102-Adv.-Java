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
	
	@Override
	public void generatePayCheck() {
		// TODO Auto-generated method stub

	}
	
	private BigDecimal numberOfHoursWorked;


}
