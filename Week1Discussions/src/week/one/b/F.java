package week.one.b;

import week.one.a.A;

// Non-child class in different package referencing class members
public class F {

	public void accessTest() {

		A a = new A();
		int pub = a.pub;
		// int prot = a.prot; // The field A.prot is not visible
		// int pack = a.pack; // The field A.pack is not visible
		// int priv = a.priv; // The field A.priv is not visible
	}

}
