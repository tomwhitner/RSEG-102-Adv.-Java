package Week1.SampleApp.B;

public class MyClass {
	public static int x = 7;
	public int y = 3;

	public static void main(String[] args) {
		MyClass c1 = new MyClass();
		MyClass c2 = new MyClass();

		// Two instances c1 and c2 of the MyClass are created. How many class
		// members and instance members are created and what are they?

		c1.y = 5;
		c2.y = 6;
		c1.x = 1;
		c2.x = 2;

		System.out.println("c1.y = " + c1.y);  // 5
		System.out.println("c2.y = " + c2.y);  // 6
		System.out.println("c1.x = " + c1.x);  // 2
		System.out.println("c2.x = " + c2.x);  // 2
	}
}

/*

Output:
 	
 	c1.y = 5
	c2.y = 6
	c1.x = 2
	c2.x = 2

Explanation:

	Since x is declared as static, it is a class member.  Only one of these is ever created for the class as a whole.  In this example, both instances of MyClass share the same x member.
	This explains why changing c2.x affects c1.x.  While the syntax c1.x works, it is better to reference this member as MyTest.x as it clarifies the fact that x is a class member.
	
	Since y is not declared static, it is an instance member.  One of these is created for each instance of the class that is created.  So, both instances of MyClass get their own y member.


*/