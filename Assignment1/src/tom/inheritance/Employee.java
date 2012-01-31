package tom.inheritance;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
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
	 * @param wage the employee's wage to set
	 */
	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}
	
	/**
	 * Performs earnings and vacation calculations and prints paycheck.
	 */
	public abstract void generatePayCheck();
	
	/**
	 * Formats and prints paycheck
	 * @param details The details provided by subclass
	 */
	protected void printPaycheck(String details) {
		StringBuilder payCheck = new StringBuilder();
		payCheck.append("- - - -").append(RETURN);
		payCheck.append("Name: ").append(getName()).append(RETURN);
		payCheck.append("Hire date: ").append(DATE_FORMAT.format(getHireDate())).append(RETURN);
		
		payCheck.append("Pay date: ").append(DATE_FORMAT.format(new Date())).append(RETURN);
		payCheck.append("PayCheck #: ").append(getNextPayCheckId()).append(RETURN);
		
		payCheck.append(RETURN);
		
		payCheck.append(details);
		payCheck.append("- - - -").append(RETURN);
		
		System.out.println(payCheck);
	}

	// constants used to format paycheck output; may be used by subclasses
	
	/**
	 * Symbol for printing line separator (carriage return)
	 */
	protected static final String RETURN = System.getProperty("line.separator");

	/**
	 * Format for printing currency values
	 */
	protected static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();

	/**
	 * Format for printing percent values
	 */
	protected static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

	/**
	 * Format for printing date values
	 */
	protected static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();

	/**
	 * @return the next paycheck Id
	 */
	private static int getNextPayCheckId() {
		return payCheckId++;
	}

	private Date hireDate;
	private String name;
	private BigDecimal wage;
	private static int payCheckId = 1;
	
}
