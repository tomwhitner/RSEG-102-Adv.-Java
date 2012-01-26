package week2.discussions.confusingConstructor;

import java.util.ArrayList;
import java.util.List;

public class MoreConfusing {
	
	public MoreConfusing(Object o) {
		System.out.println("Object");
	}

	public MoreConfusing(List<String> l) {
		System.out.println("List");
	}

	public MoreConfusing(ArrayList<String> al) {
		System.out.println("ArrayList");
	}

	public static void main(String args[]) {
		new MoreConfusing(null);  // Prints "ArrayList"
	}
}
