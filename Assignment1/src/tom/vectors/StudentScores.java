package tom.vectors;

import java.util.Vector;

public class StudentScores {

	public static void main(String[] args) {
		
		// Define arrays consisting of names and scores (pick your own choice of values) for about 5 students.
		String[] names = { "Alice", "Bill", "Charlie", "Dawn", "Eric" };
		double[] scores = { 99.3, 81.4, 70.4, 90.0, 88.6 };

		assert names.length == scores.length : "Array lengths are not equal.";

		// Create a vector containing 5 objects of type Student using the names and scores stored in the arrays. 
		Vector<Student> students = new Vector<Student>(names.length);

		for (int i = 0; i < names.length; i++) {
			students.add(new Student(names[i], scores[i]));
		}

		// Verify that this vector is not empty. 
		assert !students.isEmpty() : "Vector is empty.";
		
		// Output the first element and the last element of the vector. 
		System.out.println("First = " + students.firstElement());
		System.out.println("Last = " + students.lastElement());

		// Write code to verify (using name) if vector consists of one Student object. (Search for one student in the vector using name). 
		// ??
		String searchFrom = "Charlie";
		for (Student s : students) {
			if (s.getName() == searchFrom) {
				System.out.println("Found " + searchFrom);
			}
		}		
		
		
		// Also, write code to show removal of one Student object and inserting a new Student object at that position.
		int loc = 3;
		Student charlie = students.remove(loc);
		Student chelsea = new Student("Chelsea", 100.0);
		students.insertElementAt(chelsea, loc);
		
		// Enumerate all elements in the Vector.
		for (Student s : students) {
			System.out.println(s);
		}

	}
}
