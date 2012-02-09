package tom.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Test {

		public static void main (String[] args) {
			// Create a collection as an instance of ArrayList class. 
			ArrayList<String> collection = new ArrayList<String>();
			
			// Tokenize the following string by delimiter “ , .” (blank space, comma and a dot):
			String source = 
				"In solving a problem of this sort, the grand thing is to be able to reason backwards. " +
				"That is a very useful accomplishment, and a very easy one, but people do not " +
				"practice it much. In the every-day affairs of life it is more useful to reason forwards, " +
				"and so the other comes to be neglected.";
			
			String[] tokens = source.split(" |,|\\.");  // This yields several empty strings 
			
			// Create the list with the tokens populated from the above string. 
			collection.addAll(Arrays.asList(tokens));
			
			// Verify that the collection is not empty. 
			assert !collection.isEmpty() : "Collection is empty.";
			System.out.println("Collection isEmpty = " + collection.isEmpty());
			
			// Find the size of the collection.
			System.out.println("Collection size = " + collection.size());
			System.out.println();
			
			// Iterate over this unsorted list. 
			printCollection(collection);
			
			// Do reverse iteration over this unsorted list. 
			Collections.reverse(collection);
			printCollection(collection);
			
			// Make the list iteration in natural sorting order.
			Collections.sort(collection);
			printCollection(collection);
			
			// Make the list iteration in case insensitive sorting order.
			Collections.sort(collection, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}
			});
			printCollection(collection);
			
			// Remove couple of tokens from the collection and iterate again. 
			collection.remove(5);
			collection.remove(10);
			printCollection(collection);
			
			// Clear the collection.
			collection.clear();
			
			// Check if it is empty and its size.
			System.out.println("Collection isEmpty = " + collection.isEmpty());
			System.out.println("Collection size = " + collection.size());			
		}
		
		private static int maxLineLength = 80;
	
		// This method enumerates the collection and outputs its contents to the console.
		// It will line break and indent at approximately maxLineLength character intervals.
		private static void printCollection(Collection<String> c) {
			StringBuilder sb = new StringBuilder(maxLineLength + 10);
			for (String s : c) {
				sb.append(s + ",");
				if (sb.length() > maxLineLength) {
					System.out.println(sb.toString());
					sb = new StringBuilder(maxLineLength + 10);
					sb.append("  ");
				}
			}
			System.out.println(sb.toString());
			System.out.println();
		}		
}
