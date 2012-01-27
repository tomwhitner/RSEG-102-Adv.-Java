package week2.interfaces;

/*
 * Represent a Dog, but also need to be Loggable
 */
public class Dog extends Animal implements Loggable {

	private static String TYPE = Dog.class.getName();
	
	public Dog(String name) {
		super(TYPE, name);
	}

	public String getTextToLog() {
		return "My name is " + getName();
	}

	@Override
	public String speak() {
		return "Woof";
	}

}
