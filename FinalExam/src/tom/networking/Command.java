package tom.networking;

public interface Command {

	boolean execute(String[] parameters);
	
	/*
	 * An unrecognized command
	 */
	static final String UNKNOWN = "UNKNOWN";

}
