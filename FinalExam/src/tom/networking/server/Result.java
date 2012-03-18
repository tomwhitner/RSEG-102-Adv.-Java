package tom.networking.server;

public class Result {
	
	public static final int INPROGRESS = 100;
	public static final int SUCCESS = 200;
	public static final int FAILURE = 400;
	
	private final int result;
	private final String message;
	
	public Result(int result, String message) {
		this.result = result;
		this.message = message;
	}
	
	public int getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean succeeded () {
		return ((result == SUCCESS) || (result == INPROGRESS));
	}
	
	public boolean failed () {
		return result == FAILURE;
	}

}
