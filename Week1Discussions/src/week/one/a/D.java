package week.one.a;

// Non-child class in same package referencing class members
public class D {

	public void accessTest() {

		A a = new A();
		int pub = a.pub;
		int prot = a.prot;
		int pack = a.pack;
		// int priv = a.priv; // The field A.priv is not visible
	}
}
