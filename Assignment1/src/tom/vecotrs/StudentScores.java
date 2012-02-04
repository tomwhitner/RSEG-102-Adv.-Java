package tom.vecotrs;

import java.util.Vector;

public class StudentScores {

	public static void main(String[] args) {

		String[] names = { "Alice", "Bill", "Charlie", "Dawn", "Eric" };
		double[] scores = { 99.3, 81.4, 70.4, 90.0, 88.6 };

		assert names.length == scores.length : "Array lengths are not equal.";

		Vector<Student> students = new Vector<Student>(names.length);

		for (int i = 0; i < names.length; i++) {
			students.add(new Student(names[i], scores[i]));
		}

		// verify vector is not empty
		assert !students.isEmpty() : "Vector is empty.";

		// output first student
		System.out.println("First = " + students.firstElement());

		// output last student
		System.out.println("Last = " + students.lastElement());

		// ?? Write code to verify (using name) if vector consists of one Student object. ??
		
		// search for a student
		String searchFrom = "Charlie";
		for (Student s : students) {
			if (s.getName() == searchFrom) {
				System.out.println("Found " + searchFrom);
			}
		}

		// remove one student
		int loc = 3;
		Student charlie = students.remove(loc);

		// add another
		Student chelsea = new Student("Chelsea", 100.0);
		students.insertElementAt(chelsea, loc);

		for (Student s : students) {
			System.out.println(s);
		}

	}
}
