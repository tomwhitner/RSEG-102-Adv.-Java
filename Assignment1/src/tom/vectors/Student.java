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

	private String name;
	private double score;

}
