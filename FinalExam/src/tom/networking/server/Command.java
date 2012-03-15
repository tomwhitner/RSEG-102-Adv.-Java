package tom.networking.server;

public interface Command extends tom.networking.Command {

	/*
	 * Transmit user name to server
	 */
	static final String USER = "USER";
	/*
	 * Transmit password to server
	 */
	static final String PASS = "PASS";
	/*
	 * Set the file type: ascii or binary
	 */
	static final String TYPE = "TYPE";
	/*
	 * Request a separate socket for file transfer
	 */
	static final String PASV = "PASV";
	/*
	 * Retrieve (get) a file from the server
	 */
	static final String RETR = "RETR";
	/*
	 * Store (put) a file to the server
	 */
	static final String STOR = "STOR";
	/*
	 * Close the connection
	 */
	static final String QUIT = "QUIT";
	/*
	 * Kill the server and close the connection, if authorized
	 */
	static final String KILL = "KILL";

}
