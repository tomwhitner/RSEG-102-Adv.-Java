package week10.discussions.count;

/*
 
 This prints (a)0.  The reason is that the float f loses precision due to the magnitude of int START.  Adding 50 to START results
 in the same float value as can be seen by running the code below.  To fix this, either reduce the magnitude of START to a range
 that doesn't cause precision loss, or change f from float to int.
 */

public class Count {
	
	public static void main(String[] args) {
		
		final int START = 2000000000;
		
		float f1 = START;
		float f2 = f1 + 50;
		
		System.out.println("f1 " + (f1 == f2 ? "==" : "!=") + " f2");
	}
}

// output: f1 == f2
