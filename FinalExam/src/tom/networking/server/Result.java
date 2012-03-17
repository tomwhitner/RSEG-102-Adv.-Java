package tom.networking.server;

public class Result {
	
	public static final int SUCCESS = 200;
	public static final int FAILURE = 400;
	
	private final int result;
	
	public Result(int result) {
		this.result = result;
	}
	
	public boolean succeeded () {
		return result == SUCCESS;
	}
	
	public boolean failed () {
		return result == FAILURE;
	}

}
