package week2.discussions.change;
import java.math.BigDecimal;
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
		
		BigDecimal a = new BigDecimal("2.00");
		BigDecimal b = new BigDecimal("1.10");
		System.out.println(a.subtract(b));  // -> 0.90
		
		
		System.out.println(2.00f - 1.10f);  // -> 0.9
		System.out.println(2.00f - 1.18f);  // -> 0.82000005
	}
}
