package tom.inheritance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * SalesEmployee represents a permanent sales employee who is compensated based on commission.
 * @author tom
 *
 */
public class SalesEmployee extends PermanentEmployee {

	/**
	 * Create a new sales employee
	 * @param name The sales employee's full name
	 * @param hireDate The sales employee's date of hire
	 * @param wage The sales employee's base salary
	 * @param commission The sales employee's commission percentage
	 */
	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission) {
		this(name, hireDate, wage, commission, BigDecimal.ZERO);
	}

	/**
	 * Create a new sales employee
	 * @param name The sales employee's full name
	 * @param hireDate The sales employee's date of hire
	 * @param wage The sales employee's base salary
	 * @param commission The sales employee's commission percentage
	 * @param sales The sales employee's sales for this pay period
	 */
	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission, BigDecimal sales) {
		super(name, hireDate, wage);
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
	 * @param sales sales employee's sales for this pay period to set
	 */
	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	/**
	 * @return the sales employee's commission percentage
	 */
	public BigDecimal getCommission() {
		return commission;
	}
	
	/**
	 * Calculates earnings (base and commission) and prints paycheck
	 */
	@Override
	public void generatePayCheck() {
		
		BigDecimal sales = getSales();
		BigDecimal commission = getCommission();
		
		BigDecimal salary = getWage().divide(NUMBER_OF_PAY_PERIODS, RoundingMode.HALF_UP);
		BigDecimal commissionPay = sales.multiply(commission);
		BigDecimal earnings = salary.add(commissionPay);
		
		// Every pay period, employee accrues 5 hours of vacation
		BigDecimal vacationBalance = getVacationBalance().add(BIWEEKLY_VACATION_ACCRUAL);
		setVacationBalance(vacationBalance);
		
		StringBuilder s = new StringBuilder();
		s.append("Annual Salary: ").append(CURRENCY_FORMAT.format(getWage())).append(RETURN);
		s.append("Commission %: ").append(PERCENT_FORMAT.format(commission)).append(RETURN);
		s.append("Sales this period: ").append(CURRENCY_FORMAT.format(sales)).append(RETURN);
		s.append("Pay: ").append(CURRENCY_FORMAT.format(earnings)).append(RETURN);
		s.append("Vacation (hours): ").append(vacationBalance).append(RETURN);
		
		printPaycheck(s.toString());

	}

	private BigDecimal sales;
	private BigDecimal commission;
}
