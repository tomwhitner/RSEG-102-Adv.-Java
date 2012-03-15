package tom.networking;

public interface Command {

	boolean execute(String[] parameters);
	
	static final String GET = "GET";
	static final String PUT = "PUT";
	static final String ASCII = "ASCII";
	static final String BINARY = "BINARY";
	static final String BYE = "BYE";
	static final String KILL = "KILL";
	static final String UNKNOWN = "UNKNOWN";
	static final String OPEN = "OPEN";
	static final String CLOSE = "CLOSE";
	static final String QUIT = "QUIT";
	
}
