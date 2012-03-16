package tom.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class TransferUtility {

	public static void transferText(Reader reader, Writer writer) throws IOException {
		
        BufferedReader in = new BufferedReader(reader);
        BufferedWriter out = new BufferedWriter(writer);
        
		int c;

        while ((c = in.read()) != -1) {
            out.write(c);
        }
	
        out.flush();
        
        in.close();
        out.close();
	
	}
	
	public static void transferBinary(InputStream inStream, OutputStream outStream) throws IOException {
		
		BufferedInputStream in = new BufferedInputStream(inStream);
		BufferedOutputStream out = new BufferedOutputStream(outStream);

		int c;
		int count = 0;
        while ((c = in.read()) != -1) {
        	count++;
            out.write(c);
        }
		
        out.flush();
    	
        in.close();
        out.close();
	}
}
