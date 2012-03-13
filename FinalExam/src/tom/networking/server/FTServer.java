package tom.networking.server;

import java.io.IOException;
import java.net.ServerSocket;

public class FTServer {
	
	public static void main(String[] args) {
		
		try {
			// obtain the port
			ServerSocket s = new ServerSocket(8189);
			
			while (true) {
				
				
				new Thread(new Connection(s.accept())).start();
				
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
