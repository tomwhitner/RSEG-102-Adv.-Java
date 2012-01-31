package tom.inheritance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class PermanentEmployee extends Employee {
	
	public PermanentEmployee(String name, Date hireDate, BigDecimal wage) {
		this(name, hireDate, wage, BigDecimal.ZERO);
	}

	public PermanentEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal vacationBalance) {
		super(name, hireDate, wage);
		this.vacationBalance = vacationBalance;
	}

	/**
	 * @return the vacationBalance
	 */
	public BigDecimal getVacationBalance() {
		return vacationBalance;
	}

	/**
	 * @param vacationBalance the vacationBalance to set
	 */
	private void setVacationBalance(BigDecimal vacationBalance) {
		this.vacationBalance = vacationBalance;
	}
	
	/**
	 * 
	 */
	@Override
	public void generatePayCheck() {
		
		BigDecimal earnings = getWage().divide(NUMBER_OF_PAY_PERIODS, RoundingMode.HALF_UP);
		
		// Every pay period, employee accrues 5 hours of vacation
		BigDecimal vacationBalance = getVacationBalance().add(BIWEEKLY_VACATION_ACCRUAL);
		setVacationBalance(vacationBalance);
		
		StringBuilder s = new StringBuilder();
		s.append("Salary: " + earnings + "\n");
		s.append("Vacation (hours): " + vacationBalance);
		
		printPaycheck(s.toString());
	}
	
	private static BigDecimal BIWEEKLY_VACATION_ACCRUAL = new BigDecimal("5");
	private static BigDecimal NUMBER_OF_PAY_PERIODS = new BigDecimal("26");

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

	private BigDecimal vacationBalance;

}
