package tom.networking.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public 	class Connection implements Runnable {
	Socket socket = null;
	
	public Connection(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {

			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);
			
			out.println("Welcome to tom....");
			
			while (in.hasNextLine()) {
				String s = in.nextLine();
				if (s.equals("BYE")) {
					out.println("Goodbye!");
					break;
				}
				out.println(s);
			}
			
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}