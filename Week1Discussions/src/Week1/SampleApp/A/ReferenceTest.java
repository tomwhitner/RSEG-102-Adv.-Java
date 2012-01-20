package Week1.SampleApp.A;

// Copying Objects

import java.awt.Dimension;

class ReferenceTest {
	public static void main(String[] args) {
		Dimension a = new Dimension(5, 10);
		System.out.println("a.height = " + a.height);
		Dimension b = a;
		b.height = 30;
		System.out.println("a.height = " + a.height + " after change to b");
	}
}

/*

Output:
	a.height = 10
	a.height = 30 after change to b
	
Explanation:
	The ReferenceTest class is a reference type.  As such, both a and b are references to instances of the ReferenceTest class.  In Java, assigning references such as a=b copies only
	the reference not the whole object.  So, both a and b, in fact, refer to the same object.  Therefore changes made through one reference will appear in both.
	
*/