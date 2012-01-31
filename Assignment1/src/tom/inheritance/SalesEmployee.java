package tom.inheritance;

import java.math.BigDecimal;
import java.util.Date;

public class SalesEmployee extends PermanentEmployee {

	
	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission) {
		this(name, hireDate, wage, commission, BigDecimal.ZERO);
	}

	public SalesEmployee(String name, Date hireDate, BigDecimal wage, BigDecimal commission, BigDecimal sales) {
		super(name, hireDate, wage);
		this.commission = commission;
		this.sales = sales;
	}

	/**
	 * @return the sales
	 */
	public BigDecimal getSales() {
		return sales;
	}

	/**
	 * @param sales the sales to set
	 */
	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	/**
	 * @return the commission
	 */
	public BigDecimal getCommission() {
		return commission;
	}
	
	@Override
	public void generatePayCheck() {
		// TODO Auto-generated method stub

	}

	private BigDecimal sales;
	private BigDecimal commission;
	
}
