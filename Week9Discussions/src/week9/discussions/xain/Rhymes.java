package week9.discussions.xain;

import java.util.Random;

public class Rhymes {
	private static Random rnd = new Random();

	public static void main(String[] args) {
		StringBuffer word = null;

		switch (rnd.nextInt(3)) {           // increase to 3 to allow value 2 to be produced.
		case 1:
			word = new StringBuffer("P");   // changed to strings
			break;							// add break statements
		case 2:
			word = new StringBuffer("G");
			break;
		default:
			word = new StringBuffer("M");
			break;
		}
		word.append('a');
		word.append('i');
		word.append('n');
		System.out.println(word);
	}
}
