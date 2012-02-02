package week3.discussions.catchingexceptions;

public class TestExceptions {
	
	public static void main(String[] args) {
		
		try {
			for (int i = 0; true; i++) {
				System.out.println("args[" + i + "]=" + args[i]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Exception caught:");
			System.out.println(e);
		} finally {
			System.out.println("Quiting...");
		}
		
	}
	
}