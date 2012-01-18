package Week1.Discussions.A;

/*
class Horse extends Animal {
	Horse() {
		super();
	}
}
*/

/*
 * Problem:
 *  There is no default constructor for Horse's superclass Animal.  A string parameter must be provided.
 * 
 * Solution:
 *  Provide the required string parameter.
 */

class Horse extends Animal {
	private static String NAME = "Horse";
	Horse() {
		super(NAME);
	}
}