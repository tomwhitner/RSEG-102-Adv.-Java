package tom.inheritance;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

public class TestDriver {

	public static void main(String[] args) {
		
		GregorianCalendar cal = new GregorianCalendar();
		
		Employee[] employees = new Employee[6];
		
		cal.set(2001, GregorianCalendar.JANUARY, 1);
		employees[0] = new PermanentEmployee("Peter ", cal.getTime(), new BigDecimal(100000), new BigDecimal(40));
		
		cal.set(2002, GregorianCalendar.FEBRUARY, 2);
		employees[1] = new PermanentEmployee("Pam ", cal.getTime(), new BigDecimal(125000));
		
		cal.set(2003, GregorianCalendar.MARCH, 3);
		employees[2] = new TemporaryEmployee("Tammy ", cal.getTime(), new BigDecimal(8.25));
		
		cal.set(2004, GregorianCalendar.APRIL, 4);
		employees[3] = new TemporaryEmployee("Tim", cal.getTime(), new BigDecimal(10.00), new BigDecimal(43));
		
		cal.set(2005, GregorianCalendar.MAY, 5);
		employees[4] = new SalesEmployee("Seth", cal.getTime(), new BigDecimal(25000), new BigDecimal(10));
		
		cal.set(2006, GregorianCalendar.JUNE, 6);
		employees[5] = new SalesEmployee("Sarah", cal.getTime(), new BigDecimal(32000), new BigDecimal(12), new BigDecimal(50000));
		
		for (Employee e : employees) {
			e.generatePayCheck();
		}
	}
}
