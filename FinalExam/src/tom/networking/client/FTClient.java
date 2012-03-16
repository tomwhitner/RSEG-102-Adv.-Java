package tom.networking.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

	private final File fileDir = new File("client");
	private final Map<String, Command> commands = new HashMap<String, Command>();
	private TransferMode mode = TransferMode.ASCII;
	private final BufferedReader screenIn;
	private final PrintWriter screenOut;

	private Scanner serverIn = null;
	private PrintWriter serverOut = null;

	private Socket socket = null;

	private static final int PORT = 8189;
	private static final int DATA_PORT = 8190;

	public static void main(String[] args) {
		new FTClient().run();
	}

	FTClient() {
		screenIn = new BufferedReader(new InputStreamReader(System.in));
		screenOut = new PrintWriter(System.out, true /* autoFlush */);		
	}
	
	void run() {


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
	private int sendCommandToServer(String command, String... parameters) {

		// assemble the command line with parameters
		String cmdLine = command;
		for (String p : parameters) {
			cmdLine = cmdLine + " " + p;
		}

		// send the command line to the server
		serverOut.println(cmdLine);

		return waitForServer();
	}
	
	private int waitForServer() {
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

	// optimization, create each command object only once
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

	private boolean parameterCountIsOK(String[] parameters, int expectedCount) {

		if (parameters.length < expectedCount) {
			screenOut.println("Too few parameters specified. " + (expectedCount - 1)
					+ " were expected.");
			return false;
		}
		if (parameters.length > expectedCount) {
			screenOut.println("Too many parameters specified. " + (expectedCount - 1)
					+ " were expected.");
			return false;
		}
		return true;

	}

	/*
	 * This command opens a connection to the server.
	 */

	String host = null;
	class OpenCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 2)) {

				host = parameters[1];

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
					sendCommandToServer(tom.networking.server.Command.USER,
							user);

					// password
					screenOut.println("Password:");
					String pwd = screenIn.readLine();
					int result = sendCommandToServer(
							tom.networking.server.Command.PASS, pwd);

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

			} 
			
			return true;
		}
	}

	/*
	 * This command closes the connection to the server
	 */
	class CloseCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				if ((socket != null) && (!socket.isClosed())) {

					try {

						int result = sendCommandToServer(tom.networking.server.Command.QUIT);

						if (result == 200) {

							socket.close();

							configureCommands(false);

							screenOut.println("Connnection closed.");
						} else {
							screenOut.println("Failed to close connection.");
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return true;
		}
	}

	/*
	 * This command quits the client after closing the connection to the server
	 */
	class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {
				// get the close command
				Command cmd = getCommand(Command.CLOSE);

				// if the close command is actually a close command, execute it
				// to close the connection
				if (cmd.getClass() == CloseCommand.class) {
					cmd.execute(parameters);
				}

				screenOut.println("Quitting.  Goodbye.");

				// tell main loop to terminate
				return false;
			} else {
				// quit failed, main loop should continue
				return true;
			}
		}
	}

	/*
	 * This command retrieves a file from the server using the current mode/type
	 */
	class GetCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 2)) {

				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				try {
					
					// ask server to accept second connection for file transfer
					int result = sendCommandToServer(tom.networking.server.Command.PASV);
					
					// if server agrees
					if (result == 200) {
						
						// open the socket
						Socket fileSocket = new Socket(host, DATA_PORT);
						fileSocket.setSoTimeout(10000);
						fileSocket.getInputStream();
						
						// ask server to send file
						result = sendCommandToServer(tom.networking.server.Command.RETR, fileName);
						
						if (result == 200) {
							
							// transfer based on mode/type
							switch (mode) {
							case ASCII:
								TransferUtility.transferText(new InputStreamReader(
										fileSocket.getInputStream()), new FileWriter(file));
								break;
							case BINARY:
								TransferUtility.transferBinary(fileSocket.getInputStream(),
										new FileOutputStream(file));
								break;
							}
						}
						
						result = waitForServer();
						
						fileSocket.close();
						
						screenOut.println("File received.");
					}

				} catch (FileNotFoundException e) {

					screenOut.println("File does not exist.");
				} catch (IOException e) {

					e.printStackTrace();
				}

			} 
			return true;

		}

	}

	/*
	 * This command stores a file to the server using the current mode/type
	 */
	class PutCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 2)) {

				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				try {
					
					// ask server to accept second connection for file transfer
					int result = sendCommandToServer(tom.networking.server.Command.PASV);
					
					// if server agrees
					if (result == 200) {
						
						// open the socket
						Socket fileSocket = new Socket(host, DATA_PORT);
						fileSocket.setSoTimeout(10000);
							
						// ask server to receive file
						sendCommandToServer(tom.networking.server.Command.STOR, fileName);

						// transfer based on mode/type
						switch (mode) {
						case ASCII:
							TransferUtility.transferText(new FileReader(file), new OutputStreamWriter(fileSocket.getOutputStream()));
							break;
						case BINARY:
							TransferUtility.transferBinary(new FileInputStream(file), fileSocket.getOutputStream());
							break;
						}
						
						result = waitForServer();
						
						fileSocket.close();
						
						screenOut.println("File sent.");
					}

				} catch (FileNotFoundException e) {

					screenOut.println("File does not exist.");
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			return true;
		}
	}

	/*
	 * This command sets the current mode/type
	 */
	class ModeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 2)) {

				String modeCode = parameters[1];

				int result = sendCommandToServer(
						tom.networking.server.Command.TYPE, modeCode);

				if (result == 200) {
					if (modeCode.toUpperCase().equals("A")) {
						mode = TransferMode.ASCII;
					}

					if (modeCode.toUpperCase().equals("B")) {
						mode = TransferMode.BINARY;
					}
				}
			}

			return true;
		}
	}

	/*
	 * This command instructs the server to terminate. The user must be
	 * authorized to do this; enforced by server.
	 */
	class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			try {

				if (parameterCountIsOK(parameters, 1)) {

					int result = sendCommandToServer(tom.networking.server.Command.KILL);

					if (result == 200) {

						socket.close();

						configureCommands(false);

						screenOut.println("Server terminated.");
					} else {
						screenOut.println("Not authorized to terminate server.");

					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}
	}

	/*
	 * This command is executed in place of any normal command that is not valid
	 * in a given state (open/close).
	 */
	class InvalidCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			screenOut.println("The "
							+ parameters[0]
							+ " command is not valid for the current connection state.");
			return true;
		}
	}

	/*
	 * This command is executed whenever the user enters any command which is
	 * not recognized.
	 */
	class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			screenOut.println("Unknown command: " + parameters[0]);
			return true;
		}
	}

}
