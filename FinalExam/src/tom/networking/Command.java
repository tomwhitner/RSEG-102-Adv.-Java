package tom.networking;

/*
 * Command interface to implement basic command pattern
 */
public abstract class Command {
	
	/*
	 * Execute the specific command. 
	 * Return true if process should continue; false if process should terminate.
	 */
	public boolean execute (String[] parameters) {
		
		return doExecute(parameters);
		
	}
	
	/*
	 * Used to implement actual command execution for each specific command.
	 * Return true if process should continue; false if process should terminate.
	 */
	protected abstract boolean doExecute(String[] parameters);

}
