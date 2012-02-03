package tom.exceptions;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class TestDriver {

	/**
	 * Test the exception classes
	 * 
	 * @param args Command line arguments; not used.
	 */
	public static void main(String[] args) {

		BigDecimal[] testWages = new BigDecimal[8];
		testWages[0] = new BigDecimal("-2");
		testWages[1] = new BigDecimal("0");
		testWages[2] = new BigDecimal("3");
		testWages[3] = new BigDecimal("6");
		testWages[4] = new BigDecimal("100");
		testWages[5] = new BigDecimal("199");
		testWages[6] = new BigDecimal("200"); // added to test upper limit
												// condition
		testWages[7] = new BigDecimal("250");

		for (BigDecimal wage : testWages) {
			try {
				System.out.println("Validating wage: "
						+ CURRENCY_FORMAT.format(wage));
				verifyEmployeeWage(wage);
				System.out.println(" Wage is valid.");
			} catch (IllegalWageException e) {
				System.out.println(" Wage "
						+ CURRENCY_FORMAT.format(e.getWage())
						+ " is illegal.  Cannot be negative or zero.");
			} catch (MinWageException e) {
				System.out
						.println(" Wage "
								+ CURRENCY_FORMAT.format(e.getWage())
								+ " is out of range.  Must be greater than or equal to "
								+ CURRENCY_FORMAT.format(e.getWageLimit())
								+ ".  Difference = "
								+ CURRENCY_FORMAT.format(e.getWageDiff()) + ".");
			} catch (MaxWageException e) {
				System.out.println(" Wage "
						+ CURRENCY_FORMAT.format(e.getWage())
						+ " is out of range.  Must be less than or equal to "
						+ CURRENCY_FORMAT.format(e.getWageLimit())
						+ ".  Difference = "
						+ CURRENCY_FORMAT.format(e.getWageDiff()) + ".");
			} finally {
				System.out.println(" Wage validation complete.");
				System.out.println("");
			}
		}
	}

	private static final NumberFormat CURRENCY_FORMAT = NumberFormat
			.getCurrencyInstance();
	private static final BigDecimal LOWER_WAGE_LIMIT = new BigDecimal("6.00");
	private static final BigDecimal UPPER_WAGE_LIMIT = new BigDecimal("200.00");

	public static void verifyEmployeeWage(BigDecimal wage)
			throws IllegalWageException, MinWageException, MaxWageException {

		// Wage cannot be negative or zero.
		if (wage.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalWageException(wage);
		}

		// wage must be $6 or greater
		if (wage.compareTo(LOWER_WAGE_LIMIT) < 0) {
			throw new MinWageException(wage, LOWER_WAGE_LIMIT);
		}

		// wage must be $200 or less
		if (wage.compareTo(UPPER_WAGE_LIMIT) > 0) {
			throw new MaxWageException(wage, UPPER_WAGE_LIMIT);
		}

	}
}
