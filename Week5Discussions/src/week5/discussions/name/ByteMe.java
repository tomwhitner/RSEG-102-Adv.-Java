package week5.discussions.name;

class ByteMe {

	public static void main(String[] args) {

		for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {

			if (b == 0x30)

				System.out.print("Boo ");

		}

		System.out.println("Byte.MIN_VALUE = " + Byte.MIN_VALUE);
		System.out.println("Byte.MAX_VALUE = " + Byte.MAX_VALUE);
		System.out.println("0x90 = " + 0x90);
	}

}
