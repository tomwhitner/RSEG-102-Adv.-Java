package Week1.Discussions.C;

/*
class Horse {
	Horse() {
	} // constructor

	void doStuff() {
		Horse();
	}
}
*/

/*
 * Problem:
 *  It is not possible to explicitly call a constructor method.
 * 
 * Solution
 *  Remove the call to the constructor.  Alternatively, if the constructor does something that the doStuff method also needs to do, one
 *  could refactor that common functionality into a separate method that can be called from both doStuff and the constructor.
 */

class Horse {
	public Horse() {
		functionalityThatIsCommonToConstructorAndDoStuff();
	} // constructor
	
	public void Horse() {
		System.out.println("horse was called.");
	}

	void doStuff() {
		functionalityThatIsCommonToConstructorAndDoStuff();
	}
	
	void functionalityThatIsCommonToConstructorAndDoStuff() {
	}
	
	public static void main(String[] args) {
		Horse h = new Horse();
		h.Horse();
	}
}