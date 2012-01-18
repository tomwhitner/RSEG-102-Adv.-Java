package Week1.Discussions.B;

/*
class Foo {
	int size;
	String name;

	Foo(String name, int size) {
		this.name = name;
		this.size = size;
	}
}

public static void main(String[] args) {
	Foo f = new Foo();
} 
*/

/*
 * Problem:
 *  The main method is not defined within a class.
 *  There is no default constructor for class Foo.
 * 
 * Solution:
 *  Move the main method into Foo class definition.
 *  Add the necessary parameters to Foo constructor.
 */

class Foo {
	int size;
	String name;

	Foo(String name, int size) {
		this.name = name;
		this.size = size;
	}
	
	public static void main(String[] args) {
		Foo f = new Foo("The Foo", 1);
	}
}

