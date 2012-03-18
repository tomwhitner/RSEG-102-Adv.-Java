package tom.networking.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FTServer {

	private static final int CMD_PORT = 8189;
	private static final int DATA_PORT = 8190;
	
	private static ServerSocket cmd = null;
	private static ServerSocket data = null;
	
	public static void main(String[] args) {

		try {	
			System.out.println("Welcome to FTServer ...");

			// obtain the command port
			cmd = new ServerSocket(CMD_PORT);
			
			// obtain the data port - used for file transfer
			data = new ServerSocket(DATA_PORT);

			while (true) {

				new Thread(new Connection(cmd.accept())).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (cmd != null) {
				try {
					cmd.close();
					cmd = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (data !=null) {
				try {
					data.close();
					data = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * Accept a data connection from the client
	 */
	public static synchronized Socket acceptDataConnection(InetAddress expected, String token) throws IOException {
		
		Socket s = data.accept();
		
		InetAddress addr = s.getInetAddress();
		
		if (addr.equals(expected)) {
			Scanner scanner = new Scanner(s.getInputStream());
			String t = scanner.nextLine();
			if (t.contains(token)) {
				return s;
			}	
		}
		
		s.close();
		return null;
	}
}
