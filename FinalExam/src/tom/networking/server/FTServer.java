package tom.networking.server;

import java.io.IOException;
import java.net.ServerSocket;

public class FTServer {

	private static final int CMD_PORT = 8189;
	private static final int DATA_PORT = 8190;
	
	public static void main(String[] args) {

		try {
			// obtain the command port
			ServerSocket cmd = new ServerSocket(CMD_PORT);
			
			// obtain the data port - used for file transfer
			ServerSocket data = new ServerSocket(DATA_PORT);

			while (true) {

				new Thread(new Connection(cmd.accept(), data)).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
