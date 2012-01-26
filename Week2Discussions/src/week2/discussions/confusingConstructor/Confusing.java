package week2.discussions.confusingConstructor;

import java.util.*;

/*
   What Does It Print? Why?
		(a) Object
		(b) double array
		(c) None of the above 
 */

public class Confusing {
	
	public Confusing(Object o) {
		System.out.println("Object");
	}

	public Confusing(double[] dArray) {
		System.out.println("double array");
	}
	
	public Confusing(String s) {
		System.out.println("string");
	}
	public static void main(String args[]) {
		//new Confusing(null);  // The constructor Confusing(Object) is ambiguous
	}
}
