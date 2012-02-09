package week4.discussions.indecisive;

class Indecisive {

	public static void main(String[] args) {
		System.out.println(decision());
	}

	static boolean decision() {

		try {
			return true;
		} finally {
			return false;
		}
	}
}
