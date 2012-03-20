package tom.networking.server;

/*
 * Base for server-side commands.  Includes constants for command names.
 */
public abstract class Command extends tom.networking.Command {

	/*
	 * Execute the specific command. 
	 * Return true if process should continue; false if process should terminate.
	 */
	@Override
	public boolean execute (String[] parameters) {
		
		StringBuilder sb = new StringBuilder(40);
		
		sb.append(parameters[0]).append(" (");
		
		for (int i=1; i< parameters.length; i++){
			if (i>1) sb.append(", ");
			sb.append(parameters[i]);
		}
		
		sb.append(")");
		
		System.out.println("Processing: " + sb.toString());

		return super.execute(parameters);
	}

	/*
	 * Transmit user name to server
	 */
	public static final String USER = "USER";
	/*
	 * Transmit password to server
	 */
	public static final String PASS = "PASS";
	/*
	 * Set the file type: ascii or binary
	 */
	public static final String TYPE = "TYPE";
	/*
	 * Request a separate socket for file transfer
	 */
	public static final String PASV = "PASV";
	/*
	 * Retrieve (get) a file from the server
	 */
	public static final String RETR = "RETR";
	/*
	 * Store (put) a file to the server
	 */
	public static final String STOR = "STOR";
	/*
	 * Close the connection
	 */
	public static final String QUIT = "QUIT";
	/*
	 * Kill the server and close the connection, if authorized
	 */
	public static final String KILL = "KILL";
}
