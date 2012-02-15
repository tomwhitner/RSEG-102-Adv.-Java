package week4.vectors;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class TennisPro {

	public TennisPro(String name, int titles) {
		this.name = name;
		this.titles = titles;
	}

	public String getName() {
		return name;
	}

	public int getTitles() {
		return titles;
	}

	private String name;
	private int titles;

	public static void main(String[] args) {

		String names[] = { "Jimmy Connors", "Ivan Lendl", "John McEnroe",
				"Pete Sampras", "Bjorn Borg", "Guillermo Vilas",
				"Ilie Nastase", "Boris Becker", "Rod Laver" };

		int titles[] = { 109, 94, 77, 63, 62, 62, 57, 49, 47 };

		Vector<TennisPro> v = new Vector<TennisPro>(5);

		for (int i = 0; i < names.length; i++) {
			v.addElement(new TennisPro(names[i], titles[i]));
		}
		
		Collections.sort(v, new Comparator<TennisPro>() {

			@Override
			public int compare(TennisPro p1, TennisPro p2) {
				return ((Integer)p1.titles).compareTo((Integer)(p2.titles));
			}
			});
		
		
		System.out.println("Ascending:");
		for (TennisPro p : v) {
			System.out.println(p.name + " " + p.titles);
		}

		Collections.reverse(v);
		
		System.out.println("\nDescending:");
		for (TennisPro p : v) {
			System.out.println(p.name + " " + p.titles);
		}
	}
}
