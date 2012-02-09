package tom.vectors;

public class Student {


	public Student(String name, double score) {
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public double getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", score=" + score + "]";
	}

	@Override
	public boolean equals(Object otherObject) {
		  // a quick test to see if the objects are identical
	      if (this == otherObject) return true;

	      // must return false if the explicit parameter is null
	      if (otherObject == null) return false;

	      // if the classes don't match, they can't be equal
	      if (getClass() != otherObject.getClass())
	         return false;

	      // now we know otherObject is a non-null Employee
	      Student other = (Student) otherObject;

	      // test whether the fields have identical values
	      return name.equals(other.name);	
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	private String name;
	private double score;

}
