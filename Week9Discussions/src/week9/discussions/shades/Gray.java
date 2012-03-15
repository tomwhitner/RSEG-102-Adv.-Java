package week9.discussions.shades;

public class Gray {
	public static void main(String[] args) {
		System.out.println(X.Y.Z);
	}
}

class X {
	static class Y {
		static String Z = "Black";
	}

	static Foo Y = new Foo();
}

class Foo {
	String Z = "White";
}
