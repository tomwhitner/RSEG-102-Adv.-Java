package tom.networking.server;

/*
 * This class represents the result from a server command.
 */
public class Result {

	/*
	 * Standard result code values
	 */
	public static final int INPROGRESS = 100;
	public static final int SUCCESS = 200;
	public static final int FAILURE = 400;
	
	private static final char LAST_INDICATOR = ' ';
	private static final char CONTINUE_INDICATOR = '-';

	private final int resultCode;
	private final String message;
	private final boolean last;

	/*
	 * constructs a Result from parts.  Used by server.
	 */
	Result(int resultCode, String message, boolean last) {
		
		// sanity check that no invalid codes are sent to client
		assert (resultCode >= 100) && (resultCode <= 599) : "Invalid result code.";

		this.resultCode = resultCode;
		this.message = message;
		this.last = last;
	}
	 
	/*
	 * Constructs a Result from result string. Used by client.
	 */
	public Result(String resultString) {

		// parse the result code from the final result line
		this.resultCode = Integer.parseInt(resultString.substring(0, 3));
		this.message = resultString.substring(4);
		this.last = (resultString.charAt(3) == LAST_INDICATOR);
	}

	/*
	 * Outputs a result string for sending to client.
	 */
	@Override
	public String toString() {
		
		// construct the message
		StringBuilder sb = new StringBuilder(message.length() + 4);
		sb.append(resultCode);
		sb.append(last ? LAST_INDICATOR : CONTINUE_INDICATOR);
		sb.append(message);
		return sb.toString();
	}

	/*
	 * Gets the result code.
	 */
	public int getResult() {
		return resultCode;
	}

	/*
	 * Get the message text
	 */
	public String getMessage() {
		return message;
	}

	/* 
	 * Get the last indicator
	 */
	public boolean getLast() {
		return last;
	}

	/*
	 * Gets value indicating if the command succeeded
	 */
	public boolean succeeded() {
		return ((resultCode == SUCCESS) || (resultCode == INPROGRESS));
	}

	/*
	 * Gets value indicating if the command failed
	 */
	public boolean failed() {
		return resultCode == FAILURE;
	}
}
