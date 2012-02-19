package week5.discussions.name;

import java.util.HashSet;
import java.util.Set;

public class Name2 {
	
	private String first, last;

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Name2(String first, String last) {
		this.first = first;
		this.last = last;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Name))
			return false;
		Name2 n = (Name2) o;
		return n.first.equals(first) && n.last.equals(last);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		return result;
	}
	
	public static void main(String[] args) {
		Set s = new HashSet();
		Name2 n2 = new Name2("Mickey", "Mouse");
		s.add(n2);
		n2.setLast("Dog");
		System.out.println(s.contains(n2));
	}

}
