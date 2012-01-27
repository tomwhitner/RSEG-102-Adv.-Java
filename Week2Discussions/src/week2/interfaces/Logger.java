package week2.interfaces;

import java.util.GregorianCalendar;

/*
 * Logs any Loggable instance to the console
 */
public class Logger {
	public void Log(Loggable loggable) {
		String text = loggable.getTextToLog();
		GregorianCalendar cal = new GregorianCalendar();
		System.out.println(cal.getTime() + " : " + text);
	}
}
