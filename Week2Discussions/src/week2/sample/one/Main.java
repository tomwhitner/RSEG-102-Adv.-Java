package week2.sample.one;

public class Main {

	public static void main(String... args) {
		
		// expected to work
		Size s1 = Size.valueOf("LARGE"); 
		//  tom's edit
		
		// does case matter?
		Size s2 = Size.valueOf("Large");   
		// YES!  Exception in thread "main" java.lang.IllegalArgumentException: No enum const class week2.sample.one.Size.Large

		// what happens for invalid values?
		Size s3 = Size.valueOf("INVALID");   
		// Same! Exception in thread "main" java.lang.IllegalArgumentException: No enum const class week2.sample.one.Size.INVALID
	}
}
