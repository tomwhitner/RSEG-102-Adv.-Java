package week.one.b;

import week.one.a.A;

// Child class in different package referencing super class members
public class E extends A {

	public void accessTest() {
		int pub = this.pub;
		int prot = this.prot;
		// int pack = this.pack; // The field A.pack is not visible
		// int priv = this.priv; // The field A.priv is not visible
	}
}
