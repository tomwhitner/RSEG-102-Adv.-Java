package week2.discussions.confusingConstructor;

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

	public static void main(String args[]) {
		new Confusing(null);
	}
}
