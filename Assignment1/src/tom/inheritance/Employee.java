package tom.inheritance;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

public abstract class Employee {
	
	public Employee(String name, Date hireDate) {
		this(name, hireDate, BigDecimal.ZERO);
	}
	
	public Employee(String name, Date hireDate, BigDecimal wage) {
		this.name = name;
		this.hireDate = hireDate;
		this.wage = wage;
	}

	/**
	 * @return the hireDate
	 */
	public Date getHireDate() {
		return hireDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the wage
	 */
	public BigDecimal getWage() {
		return wage;
	}

	/**
	 * @param wage the wage to set
	 */
	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}
	
	/**
	 * Paychecks are generated once every two weeks (26 pay periods every year).
	 */
	public abstract void generatePayCheck();
	
	protected static final String RETURN = "\n";
	private static int payCheckId = 1;
	protected static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
	protected static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();
	
	protected void printPaycheck(String details) {
		StringBuilder payCheck = new StringBuilder();
		payCheck.append("- - - -").append(RETURN);
		payCheck.append("Name: ").append(getName()).append(RETURN);
		payCheck.append("Hire date: ").append(DATE_FORMAT.format(getHireDate())).append(RETURN);
		
		payCheck.append("Pay date: ").append(DATE_FORMAT.format(new Date())).append(RETURN);
		payCheck.append("PayCheck #: ").append(getNextPayCheckId()).append(RETURN);
		
		payCheck.append(" - - - ").append(RETURN);
		
		payCheck.append(details);
		payCheck.append("- - - -").append(RETURN);
		
		System.out.println(payCheck);
	}

	/**
	 * @return the payCheckId
	 */
	private static int getNextPayCheckId() {
		return payCheckId++;
	}



	private Date hireDate;
	private String name;
	private BigDecimal wage;
	
}
