package tom.networking;

public interface Command {

	boolean execute(String[] parameters);
	
	static final String CMD_GET = "GET";
	static final String CMD_PUT = "PUT";
	static final String CMD_ASCII = "ASCII";
	static final String CMD_BINARY = "BINARY";
	static final String CMD_BYE = "BYE";
	static final String CMD_KILL = "KILL";
	static final String CMD_UNKNOWN = "UNKNOWN";
	
}
