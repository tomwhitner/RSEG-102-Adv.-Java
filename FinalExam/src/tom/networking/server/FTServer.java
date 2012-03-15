package tom.networking.server;

import java.io.IOException;
import java.net.ServerSocket;

public class FTServer {

	private static final int CMD_PORT = 8189;
	private static final int DATA_PORT = 8190;
	
	public static void main(String[] args) {

		try {
			// obtain the port
			ServerSocket s = new ServerSocket(CMD_PORT);

			while (true) {

				new Thread(new Connection(s.accept())).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
