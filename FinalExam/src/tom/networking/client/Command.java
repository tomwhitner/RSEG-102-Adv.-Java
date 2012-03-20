package tom.networking.client;

public interface Command extends tom.networking.Command {

	/*
	 * Open a connection to the server
	 */
	static final String OPEN = "OPEN";
	/*
	 * Close the connection to the server
	 */
	static final String CLOSE = "CLOSE";
	/*
	 * Quit the client
	 */
	static final String QUIT = "QUIT";
	/*
	 * Kill the server and close the connection, if authorized
	 */
	static final String KILL = "KILL";
	/*
	 * Get a file from the server
	 */
	static final String GET = "GET";
	/*
	 * Put a file to the server
	 */
	static final String PUT = "PUT";
	/*
	 * Set the file type: ascii or binary
	 */
	static final String MODE = "MODE";
	/*
	 * Provides the user with basic help
	 */
	static final String HELP = "HELP";

}
