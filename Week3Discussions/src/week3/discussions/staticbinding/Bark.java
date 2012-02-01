package week3.discussions.staticbinding;

public class Bark {
	public static void main(String args[]) {
		Dog woofer = new Dog();
		Dog nipper = new Basenji();
		woofer.bark();
		nipper.bark();
	}
}
