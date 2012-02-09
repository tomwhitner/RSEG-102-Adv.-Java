package tom.interfaces;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SalesEmployee represents a permanent sales employee who is compensated based on commission.
 * @author tom
 *
 */
public class SalesEmployee extends PermanentEmployee implements Sales {


	/**
	 * Create a new sales employee
	 * @param name The sales employee's full name
	 * @param hireDate The sales employee's date of hire
	 * @param wage The sales employee's base salary
	 * @param commission The sales employee's commission percentage
	 */
	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission, BigDecimal sales) {
		this(name, hireDate, wage, commission, sales, BigDecimal.ZERO);
	}

	/**
	 * Create a new sales employee
	 * @param name The sales employee's full name
	 * @param hireDate The sales employee's date of hire
	 * @param wage The sales employee's base salary
	 * @param commission The sales employee's commission percentage
	 * @param sales The sales employee's sales for this pay period
	 */
	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission, BigDecimal sales, BigDecimal vacation) {
		super(name, hireDate, wage, vacation);
		this.commission = commission;
		this.sales = sales;
	}

	/**
	 * @return the sales employee's sales for this pay period
	 */
	public BigDecimal getSales() {
		return sales;
	}

	/**
	 * @return the sales employee's commission percentage
	 */
	public BigDecimal getCommission() {
		return commission;
	}
	
	
	/**
	 * Performs earnings and vacation calculations and prints paycheck.
	 * @return the employee's pay for the period
	 */
	@Override
	public BigDecimal generatePayCheck() {
		BigDecimal salary = super.generatePayCheck(); // handles vacation
		
		BigDecimal commissionPay = calculateCommmission();
		
		BigDecimal earnings = salary.add(commissionPay);
		
		return earnings;
	}

	/**
	 * Calculates the employee's commission for this pay period
	 * @return the employee's commission for this pay period
	 */
	public BigDecimal calculateCommmission() {
		BigDecimal sales = getSales();
		BigDecimal commission = getCommission();
		BigDecimal commissionPay = sales.multiply(commission);
		return commissionPay;		
	}

	private BigDecimal sales;
	private BigDecimal commission;
}
