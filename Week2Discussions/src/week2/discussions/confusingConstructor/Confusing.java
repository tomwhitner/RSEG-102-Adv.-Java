package week2.discussions.confusingConstructor;

import java.util.*;

/*
   What Does It Print? Why?
		(a) Object
		(b) double array
		(c) None of the above 
 */

/* 
 * This prints "double array" since there is a relationship between 
 * double[] and Object and the compiler will choose the most specific 
 * type possible.  The relationship between double[] and Object is 
 * implicit.  All classes implicitly derive from Object.  Had another 
 * overload been defined with another child class of object, it would 
 * not compile since it would be ambiguous.
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
		new Confusing(null);
	}
}
