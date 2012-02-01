package tom.inheritance;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.GregorianCalendar;

/**
 * Test the employee classes
 * @author tom
 *
 */
public class TestDriver {

	/**
	 * Test the employee classes
	 * @param args Command line arguments; not used.
	 */
	public static void main(String[] args) {
		
		GregorianCalendar cal = new GregorianCalendar();
		
		Employee[] employees = new Employee[6];
		
		cal.set(2001, GregorianCalendar.JANUARY, 1);
		PermanentEmployee peter = new PermanentEmployee("Peter Pratt", cal.getTime(), new BigDecimal(100000), new BigDecimal(40));
		peter.recordVacation(new BigDecimal(8)); // took one day vacation.
		employees[0] = peter;
		
		cal.set(2002, GregorianCalendar.FEBRUARY, 2);
		PermanentEmployee pam = new PermanentEmployee("Pam Parks", cal.getTime(), new BigDecimal(125000));
		employees[1] = pam;
		
		cal.set(2003, GregorianCalendar.MARCH, 3);
		TemporaryEmployee tammy = new TemporaryEmployee("Tamsyn Taylor", cal.getTime(), new BigDecimal(8.25));
		tammy.setNumberOfHoursWorked(new BigDecimal(25)); 
		employees[2] = tammy;
		
		cal.set(2004, GregorianCalendar.APRIL, 4);
		TemporaryEmployee tim = new TemporaryEmployee("Tim Tredeau", cal.getTime(), new BigDecimal(10.00), new BigDecimal(43));
		employees[3] = tim;
		
		cal.set(2005, GregorianCalendar.MAY, 5);
		SalesEmployee seth = new SalesEmployee("Seth Sullivan", cal.getTime(), new BigDecimal(25000), new BigDecimal(0.10), new BigDecimal(10000));
		employees[4] = seth;
				 
		cal.set(2006, GregorianCalendar.JUNE, 6);
		SalesEmployee sarah = new SalesEmployee("Sarah Stamp", cal.getTime(), new BigDecimal(32000), new BigDecimal(0.12), new BigDecimal(50000), new BigDecimal(6));
		employees[5] = sarah;
				 
		NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
		for (Employee e : employees) {
			System.out.println(e.getName() + ": " + CURRENCY_FORMAT.format(e.generatePayCheck()) + " (" + e.getClass().getSimpleName() + ")");
		}
	}
}
