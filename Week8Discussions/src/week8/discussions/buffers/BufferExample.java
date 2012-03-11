package week8.discussions.buffers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BufferExample {

	public static void main(String args []) { 
		
		try {
			// create the file object
			File file = new File("byte.bin");
			// create an input stream for the file (previously created)
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			
			while (in.available() > 0) {
				// Because this read is buffered, it does not do a physical disk read fo reach loop iteration
				System.out.print(in.read());
				if (in.available() > 0) System.out.print(", ");
			}
			
			// close the stream
			in.close();
		
			// required catch block just print stack trace for now
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
