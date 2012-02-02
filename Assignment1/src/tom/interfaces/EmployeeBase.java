package tom.interfaces;

import java.math.BigDecimal;
import java.util.Date;

/**
 * EmployeeBase is an abstract class that provides general capabilities for concrete subclasses,
 * e.g. it provides common implementation of the Employee interface where possible.
 * @author tom
 *
 */
abstract class EmployeeBase implements Employee {

	/**
	 * 
	 * @param name The employee's full name
	 * @param hireDate The employee's date of hire
	 */
	public EmployeeBase(String name, Date hireDate) {
		this(name, hireDate, BigDecimal.ZERO);
	}
	
	/**
	 * 
	 * @param name The employee's full name
	 * @param hireDate The employee's date of hire
	 * @param wage The employee's wage, specific usage is dictated by each subclass
	 */
	public EmployeeBase(String name, Date hireDate, BigDecimal wage) {
		this.name = name;
		this.hireDate = hireDate;
		this.wage = wage;
	}

	/**
	 * @return the employee's date of hire
	 */
	@Override
	public Date getHireDate() {
		return hireDate;
	}

	/**
	 * @return the employee's full name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the employee's wage
	 */
	@Override
	public BigDecimal getWage() {
		return wage;
	}

	private Date hireDate;
	private String name;
	private BigDecimal wage;
}
