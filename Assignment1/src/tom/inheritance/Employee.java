package tom.inheritance;

import java.math.BigDecimal;
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
	
	protected void printPaycheck(String details) {
		System.out.println("Name: " + getName());
		System.out.println("Hire date: " + getHireDate());
		Date date = new Date();
		System.out.println("Pay date: " + date);
		System.out.println(" - - - ");
		System.out.println(details);
	}

	private Date hireDate;
	private String name;
	private BigDecimal wage;
	
}
