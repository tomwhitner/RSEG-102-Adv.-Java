package week7.discussions.loopy;

public class Loopy {

	public static void main(String[] args) {

		final int start = Integer.MAX_VALUE - 2;

		final int end = Integer.MAX_VALUE;

		int count = 0;

		System.out.println("test");
		
		try {

		for (int i = start; i <= end; i++)
		{
			count++;
			System.out.println(i + " - " + count);
		}
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println(count);
		System.out.println("test");

	}

}
