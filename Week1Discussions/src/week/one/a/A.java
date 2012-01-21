package week.one.a;

public class A {

	public int pub;
	protected int prot;
	int pack;
	private int priv;

	// Inner class accessing outer class members
	class B {

		public void accessTest() {

			int pub = A.this.pub;
			int prot = A.this.prot;
			int pack = A.this.pack;
			int priv = A.this.priv;
		}
	}
}
