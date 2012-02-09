package wwek3.discussions.indecisive;

import java.util.Arrays;
import java.util.Comparator;

public class SuspiciousSort {

	public static void main(String args[]) {

		Integer big = new Integer(2000000000);
		Integer small = new Integer(-2000000000);
		Integer zero = new Integer(0);
		Integer[] arr = new Integer[] { big, small, zero };
		
		sortOne(arr);
		System.out.println(Arrays.asList(arr));

		sortTwo(arr);
		System.out.println(Arrays.asList(arr));

		sortThree(arr);
		System.out.println(Arrays.asList(arr));
}
	
	private static void sortOne(Integer[] arr) {
		Arrays.sort(arr, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Integer) o2).intValue() - ((Integer) o1).intValue();
			}
		});
		return;
	}
	
	private static Integer[] sortTwo(Integer[] arr) {
			Arrays.sort(arr, new Comparator() {
				public int compare(Object o1, Object o2) {
				return ((Integer) o1).compareTo(((Integer) o2));
			}
		});
		return arr;
	}
	
	private static Integer[] sortThree(Integer[] arr) {
		Arrays.sort(arr, new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				// TODO Auto-generated method stub
				return i1.compareTo(i2);
			}
		});
		return arr;
	}
}