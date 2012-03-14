package tom.networking.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import tom.networking.Command;
import tom.networking.TransferMode;

public class FTClient {

	private File fileDir = new File("client");
	private Map<String, Command> commands = new HashMap<String, Command>();
	private TransferMode mode = TransferMode.ASCII;
	private BufferedReader in = null;
	private PrintWriter out = null;

	public static void main(String[] args) {
		new FTClient().run();
	}
		
	void run() {

		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out, true /* autoFlush */);
		
		commands.put(Command.CMD_UNKNOWN, new UnknownCommand());

		boolean proceed = true;
		String line;

		try {
			
			out.println("Welcome to FTClient...");
			
			while (((line = in.readLine()) != null) && proceed) {

				String[] lines = line.split(" ");

				Command cmd = getCommand(lines[0]);

				proceed = cmd.execute(lines);
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private Command getCommand(String commandName) {
		Command cmd = commands.get(commandName.toUpperCase());
		if (cmd == null) {
			cmd = commands.get(Command.CMD_UNKNOWN);
		}
		return cmd;
	}
	
	class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			out.println("Unknown command: " + parameters[0]);
			return true;
		}
	}

}
