package tom.inheritance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * PermanentEmployee represents a permanent salaried employee.
 * @author tom
 *
 */
public class PermanentEmployee extends Employee {
	
	/**
	 * Create a new permanent employee
	 * @param name The permanent employee's full name
	 * @param hireDate The permanent employee's date of hire
	 * @param wage The permanent employee's annual salary
	 */
	public PermanentEmployee(String name, Date hireDate, BigDecimal wage) {
		this(name, hireDate, wage, BigDecimal.ZERO);
	}

	/**
	 * 
	 * Create a new permanent employee
	 * @param name The permanent employee's full name
	 * @param hireDate The permanent employee's date of hire
	 * @param wage The permanent employee's annual salary
	 * @param vacationBalance The permanent employee's current vacation balance in hours
	 */
	public PermanentEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal vacationBalance) {
		super(name, hireDate, wage);
		this.vacationBalance = vacationBalance;
	}

	/**
	 * @return the permanent employee's current vacation balance in hours
	 */
	public BigDecimal getVacationBalance() {
		return vacationBalance;
	}

	/**
	 * @param vacationBalance the permanent employee's current vacation balance in hours to set
	 */
	protected void setVacationBalance(BigDecimal vacationBalance) {
		this.vacationBalance = vacationBalance;
	}
	
	/**
	 * Calculates earnings (salary and vacation) and prints paycheck
	 */
	@Override
	public void generatePayCheck() {
		
		BigDecimal earnings = getWage().divide(NUMBER_OF_PAY_PERIODS, RoundingMode.HALF_UP);
		
		// Every pay period, employee accrues 5 hours of vacation
		BigDecimal vacationBalance = getVacationBalance().add(BIWEEKLY_VACATION_ACCRUAL);
		setVacationBalance(vacationBalance);
		
		StringBuilder s = new StringBuilder();
		s.append("Annual Salary: ").append(CURRENCY_FORMAT.format(getWage())).append(RETURN);
		s.append("Pay: ").append(CURRENCY_FORMAT.format(earnings)).append(RETURN);
		s.append("Vacation (hours): ").append(vacationBalance).append(RETURN);
		
		printPaycheck(s.toString());
	}
	
	/**
	 * 
	 * @param hoursTaken The number of vacation hours used.
	 */
	public void recordVacation(BigDecimal hoursTaken) {
		
		if (hoursTaken.compareTo(hoursTaken) > 0) {
			throw new IllegalArgumentException("vaction hours taken exceeds hours accrued.");
		} else {
			setVacationBalance(getVacationBalance().subtract(hoursTaken));
		}
		
	}
	
	/**
	 * The amount of vacation accrued during a pay period
	 */
	protected static final BigDecimal BIWEEKLY_VACATION_ACCRUAL = new BigDecimal("5");
	
	/**
	 * The number of pay periods per year
	 */
	protected static final BigDecimal NUMBER_OF_PAY_PERIODS = new BigDecimal("26");


	private BigDecimal vacationBalance;

}