package tom.networking.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/*
 * The server application
 */
public class FTServer {

	public static final int CMD_PORT = 8189;
	public static final int DATA_PORT = 8190;

	private static ServerSocket dataSocket = null;
	private static boolean stopped = false;
	private static Collection<Connection> connections = new ArrayList<Connection>();

	/*
	 * Main execution loop
	 */
	public static void main(String[] args) {

		System.out.println("Welcome to FTServer ...");

		ServerSocket cmd = null;

		try {

			// obtain the command port
			System.out.println("Opening port " + CMD_PORT + " for commands.");
			cmd = new ServerSocket(CMD_PORT);
			cmd.setSoTimeout(10000);

			// obtain the data port - used for file transfer
			System.out.println("Opening port " + DATA_PORT + " for data.");
			setDataSocket(new ServerSocket(DATA_PORT));

			// until a kill request is received continue waiting for and
			// accepting connections
			while (!isStopped()) {

				Socket s = null;

				// wait up to 10 seconds for a connection request
				try {
					s = cmd.accept();
					System.out.println("Connection accepted.");
				} catch (SocketTimeoutException ex) {
					// if the socket times out, loop to check if stopped
					continue;
				}

				// create a new connection (Runnable)
				Connection c = new Connection(s);

				// add the connection to the collection for tracking
				addConnection(c);

				// run the connection
				new Thread(c).start();
			}

			System.out.println("Main loop exited; no longer accepting connections.");

			if (connectionsOpen()) {
				System.out.println("Connections are open; waiting to exit.");

				// at this point, the server has been stopped
				// we must keep running until all connections are closed
				// but don't accept any new connections
				while (connectionsOpen()) {
					Thread.sleep(10000);
				}
			}

			System.out.println("All connections are closed.");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			// close the command ServerSocket
			if (cmd != null) {
				try {
					cmd.close();
					cmd = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// close the data ServerSocket
			ServerSocket dataSocket = getDataSocket();
			if (dataSocket != null) {
				try {
					dataSocket.close();
					setDataSocket(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Exiting.");
		}
	}

	/*
	 * Gets the current data socket
	 */
	public static synchronized ServerSocket getDataSocket() {
		return dataSocket;
	}

	/*
	 * sets the current data socket
	 */
	public static synchronized void setDataSocket(ServerSocket data) {
		FTServer.dataSocket = data;
	}

	/*
	 * Gets the current stopped status
	 */
	public static synchronized boolean isStopped() {
		return stopped;
	}

	/*
	 * Sets the current stopped status
	 */
	public static synchronized void setStopped(boolean stopped) {
		FTServer.stopped = stopped;
	}

	/*
	 * Adds a connection to the collection (for new connection)
	 */
	private static synchronized void addConnection(Connection connection) {
		connections.add(connection);
	}

	/*
	 * Removes a connection from the collection (after connection terminates)
	 */
	private static synchronized void removeConnection(Connection connection) {
		connections.remove(connection);
	}

	/*
	 * Returns true of any connections are open
	 */
	private static synchronized boolean connectionsOpen() {
		return !connections.isEmpty();
	}

	/*
	 * Accepts a new connection on the data port
	 */
	private static synchronized Socket acceptDataConnection() throws IOException {
		// accept a connection
		return getDataSocket().accept();
	}

	// ////////////////////////////////////////////////////////////////
	// These methods represent the API used by the server connection

	/*
	 * Flag the server to stop when remaining connections are closed
	 */
	static void stop() {
		System.out.println("Stop request receieved.");
		setStopped(true);
	}

	/*
	 * The specified connection has ended and can be removed from the collection
	 */
	static void connectionEnded(Connection connection) {
		removeConnection(connection);
	}

	/*
	 * Accept a data connection from the client
	 */
	static Socket acceptDataConnection(InetAddress expected, String token) throws IOException {

		Socket socket = acceptDataConnection();
		System.out.println("Data connection requested.");

		// if the connection came from the expected client
		if (socket.getInetAddress().equals(expected)) {

			// create a scanner for the input stream
			Scanner scanner = new Scanner(socket.getInputStream());

			// read the first line, we are checking for the expected token
			// to ensure this is the actual client
			String line = scanner.nextLine();

			// if the line contains our token, we are good to go
			if (line.contains(token)) {
				System.out.println("Data connection accepted.");
				// return the socket
				return socket;
			}
		}

		System.out.println("Data connection rejected.");

		// either the IP address was incorrect or the line
		// did not include the token
		// close the socket and return null
		socket.close();
		return null;
	}
}
