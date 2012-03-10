package week8.discussions.fileio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteExample {

	public static void main(String args []) { 
		
		try {
			// create the file object
			File file = new File("byte.bin");
			// create the physical file
			file.createNewFile();
			// create and initialize and array of ten bytes
			byte[] outBytes = {1,2,3,4,5,6,7,8,9,10};
			// create an output stream for the file
			OutputStream out = new FileOutputStream(file);
			// write the array to the file via the stream
			out.write(outBytes);
			// close the stream
			out.close();
			// create an input stream for the same file
			InputStream in = new FileInputStream(file);
			// allocate a byte array with space for the number of bytes in the stream
			byte[] inBytes = new byte[in.available()];
			// read the bytes
			int bytesRead = in.read(inBytes);
			// close the stream
			in.close();
			// verify that the bytes read is equal to length of both input and output arrays
			assert (bytesRead == outBytes.length);
			assert (bytesRead == inBytes.length);
			// verify array contents are identical and print input array contents to console
			for (int i = 0; i< outBytes.length; i++) {
				assert outBytes[i]==inBytes[i];
				if (i != 0) System.out.print(", ");
				System.out.print(inBytes[i]);
			}
			// required catch block just print stack trace for now
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
