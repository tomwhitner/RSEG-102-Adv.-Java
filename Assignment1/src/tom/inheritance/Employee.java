package tom.inheritance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Employee is an abstract class that provides general capabilities for concrete subclasses.
 * @author tom
 *
 */
public abstract class Employee {
	
	/**
	 * 
	 * @param name The employee's full name
	 * @param hireDate The employee's date of hire
	 */
	public Employee(String name, Date hireDate) {
		this(name, hireDate, BigDecimal.ZERO);
	}
	
	/**
	 * 
	 * @param name The employee's full name
	 * @param hireDate The employee's date of hire
	 * @param wage The employee's wage, specific usage is dictated by each subclass
	 */
	public Employee(String name, Date hireDate, BigDecimal wage) {
		this.name = name;
		this.hireDate = hireDate;
		this.wage = wage;
	}

	/**
	 * @return the employee's date of hire
	 */
	public Date getHireDate() {
		return hireDate;
	}

	/**
	 * @return the employee's full name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the employee's wage
	 */
	public BigDecimal getWage() {
		return wage;
	}
	
	/**
	 * The number of pay periods per year
	 */
	protected static final BigDecimal NUMBER_OF_PAY_PERIODS = new BigDecimal("26");

	/**
	 * Performs earnings and vacation calculations and prints paycheck.
	 * @return the employee's pay for the period
	 */
	public abstract BigDecimal generatePayCheck();

	private Date hireDate;
	private String name;
	private BigDecimal wage;

}
