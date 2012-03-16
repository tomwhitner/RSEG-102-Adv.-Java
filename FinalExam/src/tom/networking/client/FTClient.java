package tom.networking.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tom.networking.TransferMode;
import tom.networking.TransferUtility;

public class FTClient {

	private File fileDir = new File("client");
	private Map<String, Command> commands = new HashMap<String, Command>();
	private TransferMode mode = TransferMode.ASCII;
	private BufferedReader screenIn = null;
	private PrintWriter screenOut = null;

	private Scanner serverIn = null;
	private PrintWriter serverOut = null;

	private Socket socket = null;
	
	private static final int PORT = 8189;
	private static final int DATA_PORT = 8190;

	public static void main(String[] args) {
		new FTClient().run();
	}
		
	void run() {

		screenIn = new BufferedReader(new InputStreamReader(System.in));
		screenOut = new PrintWriter(System.out, true /* autoFlush */);
		
		// configure commands based on closed state
		configureCommands(false);
		
		try {
			
			boolean proceed = true;
			String line;

			screenOut.println("Welcome to FTClient...");
			
			while (((line = screenIn.readLine()) != null) && proceed) {

				String[] lines = line.split(" ");

				Command cmd = getCommand(lines[0]);

				proceed = cmd.execute(lines);
			}
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	/*
	 * Get the specified command object form the map
	 */
	private Command getCommand(String commandName) {
		
		// attempt to retrieve command from map
		Command cmd = commands.get(commandName.toUpperCase());
		
		// if no command is found, return the special unknown command
		if (cmd == null) {
			cmd = commands.get(Command.UNKNOWN);
		}
		
		return cmd;
	}
	
	/*
	 * Sends a command to the server and parses the response code
	 */
	private int sendCommandToServer(String command, String ... parameters) {
		
		// assemble the command line with parameters
		String cmdLine = command;
		for (String p : parameters) {
			cmdLine = cmdLine + " " + p;
		}
		
		// send the command line to the server
		serverOut.println(cmdLine);
		
		// output each result line from the server
		String restultLine;
		do {
			restultLine = serverIn.nextLine();
			screenOut.println(restultLine);
		} while (restultLine.charAt(3) != ' ');
		
		// parse the result code from the final result line
		int resultCode = Integer.parseInt(restultLine.substring(0, 3));
		
		return resultCode;
	}
	
	// optimzation, create each command only once
	private final OpenCommand OPEN_COMMAND = new OpenCommand();
	private final QuitCommand QUIT_COMMAND = new QuitCommand();
	private final CloseCommand CLOSE_COMMAND = new CloseCommand();
	private final KillCommand KILL_COMMAND = new KillCommand();
	private final GetCommand GET_COMMAND = new GetCommand();
	private final PutCommand PUT_COMMAND = new PutCommand();
	private final ModeCommand MODE_COMMAND = new ModeCommand();
	private final InvalidCommand INVALID_COMMAND = new InvalidCommand();
	private final UnknownCommand UNKNOWN_COMMAND = new UnknownCommand();
	
	/*
	 * Configure commands based on state of connection
	 */
	private void configureCommands(boolean open) {
		
		if (open) {
			// configure commands based on open state
			commands.put(Command.OPEN, INVALID_COMMAND);
			commands.put(Command.QUIT, QUIT_COMMAND);
			commands.put(Command.CLOSE, CLOSE_COMMAND);
			commands.put(Command.KILL, KILL_COMMAND);
			commands.put(Command.GET, GET_COMMAND);
			commands.put(Command.PUT, PUT_COMMAND);
			commands.put(Command.MODE, MODE_COMMAND);			
			commands.put(Command.UNKNOWN, UNKNOWN_COMMAND);
		} else {
			// configure commands based on closed state
			commands.put(Command.OPEN, OPEN_COMMAND);
			commands.put(Command.QUIT, QUIT_COMMAND);
			commands.put(Command.CLOSE, INVALID_COMMAND);
			commands.put(Command.KILL, INVALID_COMMAND);
			commands.put(Command.GET, INVALID_COMMAND);
			commands.put(Command.PUT, INVALID_COMMAND);
			commands.put(Command.MODE, INVALID_COMMAND);
			commands.put(Command.UNKNOWN, UNKNOWN_COMMAND);
		}
		
	}
	

	class OpenCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameters.length == 2) {
				
				String host = parameters[1];
				
				try {
					socket = new Socket(host, PORT);
					socket.setSoTimeout(5000);
					
					serverIn = new Scanner(socket.getInputStream());
					serverOut = new PrintWriter(socket.getOutputStream(), true);
					
					socket.setSoTimeout(60000);

					// welcome
					screenOut.println(serverIn.nextLine());
					
					// user
					screenOut.println("User:");
					String user = screenIn.readLine();
					sendCommandToServer(tom.networking.server.Command.USER, user);
					
					// password 
					screenOut.println("Password:");
					String pwd = screenIn.readLine();
					int result = sendCommandToServer(tom.networking.server.Command.PASS, pwd);
					
					// if login was successful
					if (result == 200) {						
						configureCommands(true);
						screenOut.println("Connected to " + host);
					} else {
						screenOut.println("Failed to connect to " + host);
					}
					
				} catch (UnknownHostException e) {
					screenOut.println("Unknown host: " + host);
				} catch (ConnectException ex) {
					screenOut.println("Connection refused: " + host);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			} else {
				screenOut.println("Incorrect number of command arguments specified.");
			}

			return true;
		}
	}
	
	class CloseCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if ((socket != null)  && (!socket.isClosed())) {
				try {
					
					sendCommandToServer(tom.networking.server.Command.QUIT);
					
					socket.close();
					
					configureCommands(false);
					
					screenOut.println("Connnection closed.");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return true;
		}
	}
	
	class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			// get the close command
			Command cmd = getCommand(Command.CLOSE);
			
			// if the close command is acutally a close command, execute it
			if (cmd.getClass() == CloseCommand.class) {
				cmd.execute(parameters);
			}
			
			screenOut.println("Quitting.  Goodbye.");

			// tell main loop to terminate
			return false;
		}
	}
	
	class GetCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameters.length == 2) {

				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				try {
					PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println(Command.GET + " " + fileName);
					switch (mode) {
						case ASCII:
							TransferUtility.transferText(new InputStreamReader(socket.getInputStream()), new FileWriter(file));
							break;
						case BINARY:
							TransferUtility.transferBinary(socket.getInputStream(), new FileOutputStream(file));
							break;
					}

				} catch (FileNotFoundException e) {

					screenOut.println("File does not exist.");
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else {
				screenOut.println("Incorrect number of command arguments specified.");
			}
			return true;

		}

	}
	
	class PutCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			screenOut.println("This command is not implemented yet.");
			return true;
		}
	}
	
	class ModeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			sendCommandToServer(tom.networking.server.Command.TYPE, parameters[1]);

			return true;
		}
	}
	
	class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			screenOut.println("This command is not implemented yet.");
			return true;
		}
	}
	
	class InvalidCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			screenOut.println("This command is not valid in the current state.");
			return true;
		}
	}
	
	class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			screenOut.println("Unknown command: " + parameters[0]);
			return true;
		}
	}
	


}
