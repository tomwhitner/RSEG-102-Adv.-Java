package tom.networking;

/*
 * Command interface to implement basic command pattern
 */
public interface Command {

	/*
	 * Execute the specific command.
	 * Return true if process should continue; false if process should terminate.
	 */
	boolean execute(String[] parameters);

}
