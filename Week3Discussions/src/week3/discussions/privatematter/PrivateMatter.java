package week3.discussions.privatematter;

class Base {
	public String name = "Base";
	public String getName() { return name + " (m)"; }
}

class Derived extends Base {
	String name = "Derived";
	public String getName() { return name + " (m)"; }
}

public class PrivateMatter {
	public static void main(String[] args) {
		System.out.println("Derived: " + new Derived().name);
		
		Base b = new Derived();
		System.out.println("Base:    " + b.name);
		System.out.println("Base:    " + b.getName());
		
		Derived d = new Derived();
		System.out.println("Derived: " + d.name);
		System.out.println("Derived: " + d.getName());
	}
}