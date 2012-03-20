package tom.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/*
 * This interface defines the contract for transferring data from file to socket or socket to file
 */
public interface TransferStrategy {

	/*
	 * Transfers data from file to socket
	 */
	void transfer(File file, Socket socket) throws FileNotFoundException, IOException;

	/*
	 * Transfers data from socket to file
	 */
	void transfer(Socket socket, File file) throws FileNotFoundException, IOException;

	/*
	 * This class is a concrete implementation of the TransferStrategy interface
	 * for Ascii/Text data It implements the singleton design pattern
	 */
	public class AsciiTransfer implements TransferStrategy {

		private AsciiTransfer() {
		}

		private static AsciiTransfer theInstance = new AsciiTransfer();

		public static AsciiTransfer getInstance() {
			return theInstance;
		}

		/*
		 * Transfers text data from socket to file
		 */
		@Override
		public void transfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferText(new FileReader(file), new OutputStreamWriter(socket.getOutputStream()));
		}

		/*
		 * Transfers text data from socket to file
		 */
		@Override
		public void transfer(Socket socket, File file) throws FileNotFoundException, IOException {
			TransferUtility.transferText(new InputStreamReader(socket.getInputStream()), new FileWriter(file));
		}
	}

	/*
	 * This class is a concrete implementation of the TransferStrategy interface
	 * for Binary data It implements the singleton design pattern
	 */
	public class BinaryTransfer implements TransferStrategy {

		private BinaryTransfer() {
		}

		private static BinaryTransfer theInstance = new BinaryTransfer();

		public static BinaryTransfer getInstance() {
			return theInstance;
		}

		/*
		 * Transfers binary data from socket to file
		 */
		@Override
		public void transfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferBinary(new FileInputStream(file), socket.getOutputStream());
		}

		/*
		 * Transfers binary data from socket to file
		 */
		@Override
		public void transfer(Socket socket, File file) throws FileNotFoundException, IOException {
			TransferUtility.transferBinary(socket.getInputStream(), new FileOutputStream(file));
		}
	}

	/*
	 * Provide utility methods to transfer text and binary data
	 */
	public static class TransferUtility {

		/*
		 * Transfer text data from reader to writer
		 */
		public static void transferText(Reader reader, Writer writer) throws IOException {

			// use buffered reader/writer to optimize performance
			BufferedReader in = new BufferedReader(reader);
			BufferedWriter out = new BufferedWriter(writer);

			int c;

			// continue read/write loop until EOF (-1) is reached
			while ((c = in.read()) != -1) {
				out.write(c);
			}

			// flush the writer as it is buffered
			out.flush();

			// close both reader and writer
			in.close();
			out.close();

		}

		/*
		 * Transfer binary data from input stream to output stream
		 */
		public static void transferBinary(InputStream inStream, OutputStream outStream) throws IOException {

			// use buffered streams to optimize performance
			BufferedInputStream in = new BufferedInputStream(inStream);
			BufferedOutputStream out = new BufferedOutputStream(outStream);

			int c;
			// continue read/write loop until EOF (-1) is reached
			while ((c = in.read()) != -1) {
				out.write(c);
			}

			// flush the output stream as it is buffered
			out.flush();

			// close both streams
			in.close();
			out.close();
		}
	}
}