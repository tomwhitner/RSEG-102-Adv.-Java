package week3.discussions.catchingexceptions;

import java.util.logging.Logger;

public class TestExceptions {
	
	public static void main(String[] args) {
		
		Logger.getLogger("week3.discussions.catchingexceptions").info("start");		
		Logger.getLogger("week3.discussions.catchingexceptions").info("start");		
		try {
			for (int i = 0; true; i++) {
				System.out.println("args[" + i + "]=" + args[i]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Exception caught:");
			System.out.println(e);
			Logger.getLogger("week3.discussions.catchingexceptions").info(e.getMessage());
		} finally {
			System.out.println("Quiting...");
		}
		Logger.getLogger("week3.discussions.catchingexceptions").info("Tom");		
	}
	
}