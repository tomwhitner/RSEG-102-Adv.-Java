package week2.interfaces;

/*
 * Demonstrate interface implementation
 */
public class Main {
	
	public static void main(String[] args) {
		
		Dog fido = new Dog("Fido");
		Dog sparky = new Dog("Sparky");
		
		Logger logger = new Logger();
		
		logger.Log(fido);
		logger.Log(sparky);
		
	}
	
}
