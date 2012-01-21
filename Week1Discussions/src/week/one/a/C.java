package week.one.a;

// Child class in same package referencing super class members
public class C extends A {

	public void accessTest() {
		int pub = this.pub;
		int prot = this.prot;
		int pack = this.pack;
		// int priv = this.priv; // The field A.priv is not visible
	}

}
