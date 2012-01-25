package week2.discussions.change;

/*
 If you pay $2.00 for a spark plug that costs $1.10, how much change do you get?
 What Does It Print? Why?

 (a) 0.9
 (b) 0.90
 (c) It varies
 (d) None of the above
 */

public class Change {
	public static void main(String args[]) {
		System.out.println(2.00 - 1.10); // -> 0.8999999999999999
		System.out.println(2.00f - 1.10f); // -> 0.9
	}
}
