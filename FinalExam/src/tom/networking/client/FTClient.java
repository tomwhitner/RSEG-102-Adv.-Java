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
import tom.networking.server.Result;

public class FTClient {

	private final File fileDir = new File("client");
	private final Map<String, Command> commands = new HashMap<String, Command>();
	private final BufferedReader screenIn;
	private final PrintWriter screenOut;

	// optimization, create each command object only once
	private final OpenCommand OPEN_COMMAND = new OpenCommand();
	private final QuitCommand QUIT_COMMAND = new QuitCommand();
	private final CloseCommand CLOSE_COMMAND = new CloseCommand();
	private final KillCommand KILL_COMMAND = new KillCommand();
	private final GetCommand GET_COMMAND = new GetCommand();
	private final PutCommand PUT_COMMAND = new PutCommand();
	private final ModeCommand MODE_COMMAND = new ModeCommand();
	private final HelpCommand HELP_COMMAND = new HelpCommand();
	private final InvalidCommand INVALID_COMMAND = new InvalidCommand();
	private final UnknownCommand UNKNOWN_COMMAND = new UnknownCommand();

	private static final int COMMAND_PORT = 8189;
	private static final int DATA_PORT = 8190;
	private String host = null;
	private Socket socket = null;
	private Scanner serverIn = null;
	private PrintWriter serverOut = null;
	
	private ConnectionState connectionState;
	private TransferMode mode = TransferMode.ASCII;

	/*
	 * Program main method.
	 */
	public static void main(String[] args) {
		new FTClient().run();
	}

	/*
	 * Constructor
	 */
	FTClient() {
		screenIn = new BufferedReader(new InputStreamReader(System.in));
		screenOut = new PrintWriter(System.out, true /* autoFlush */);		
	}
	
	/*
	 * This is the main method for the client application
	 * It loops, accepting and processing commands from the user.
	 */
	void run() {

		screenOut.println("Welcome to FTClient...");

		// set the initial connection state
		setConnectionState(ConnectionState.CLOSED);

		try {
	
			boolean proceed = true;

			do {
				// read a line from user input
				String line = screenIn.readLine();

				// split it into tokens
				String[] parameters = line.split(" ");
				
				// first token is the command name
				String commandName = parameters[0];

				// retrieve the command object by name
				Command cmd = getCommand(commandName);

				// execute the command
				proceed = cmd.execute(parameters);
				
				// stop when command indicates loop should exit (Quit)
			} while (proceed);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close all resources
			close();
		}
	}
	
	/* 
	 * Makes sure all resources are closed
	 */
	void close() {
		
		// close the server input stream if necessary
		if (serverIn != null) {
			serverIn.close();
			serverIn = null;
		}
		
		// close the server output stream if necessary
		if (serverOut != null) {
			serverOut.close();
			serverOut = null;
		}
		
		// close the socket if necessary
		if (socket != null) {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					// Not much can/should be done here.  Exiting anyway.
					e.printStackTrace();
				}
			}
			socket = null;
		}
		
		// make sure connection state is correct
		setConnectionState(ConnectionState.CLOSED);
	}

	/*
	 * Get the specified command object form the map
	 */
	private Command getCommand(String commandName) {

		// attempt to retrieve command from map
		Command cmd = commands.get(commandName.toUpperCase());

		// if no command is found, return the special unknown command
		if (cmd == null) {
			cmd = UNKNOWN_COMMAND;
		}

		return cmd;
	}

	/*
	 * Sends a command to the server and parses the response code
	 */
	private Result sendCommandToServer(String command, String... parameters) {

		// assemble the command line with parameters
		String cmdLine = command;
		for (String p : parameters) {
			cmdLine = cmdLine + " " + p;
		}

		// send the command line to the server
		serverOut.println(cmdLine);

		// wait for the server response
		return waitForServer();
	}
	
	/*
	 * Waits for, parses, and echos server responses
	 */
	private Result waitForServer() {
		
		// output each result line from the server
		String restultLine;
		do {
			restultLine = serverIn.nextLine();
			screenOut.println(restultLine);
		} while (restultLine.charAt(3) != ' ');

		// parse the result code from the final result line
		int resultCode = Integer.parseInt(restultLine.substring(0, 3));

		return new Result(resultCode);
	}
	
	/*
	 * Enumeration used to track connection state
	 */
	private enum ConnectionState {
		OPEN,
		CLOSED 
	}

	/*
	 * Returns the current connection state
	 */
	private ConnectionState getConnectionState() {
		return connectionState;
	}

	/*
	 * Sets the current connection state and updates commands accordingly
	 */
	private void setConnectionState(ConnectionState connectionState) {
		
		if (this.connectionState == connectionState) return;
		
		this.connectionState = connectionState;
		
		switch (this.connectionState) {
		case OPEN:
			// configure commands based on open state
			commands.put(Command.OPEN, INVALID_COMMAND);
			commands.put(Command.QUIT, QUIT_COMMAND);
			commands.put(Command.CLOSE, CLOSE_COMMAND);
			commands.put(Command.KILL, KILL_COMMAND);
			commands.put(Command.GET, GET_COMMAND);
			commands.put(Command.PUT, PUT_COMMAND);
			commands.put(Command.MODE, MODE_COMMAND);
			commands.put(Command.HELP, HELP_COMMAND);
			break;
		case CLOSED:
			// configure commands based on closed state
			commands.put(Command.OPEN, OPEN_COMMAND);
			commands.put(Command.QUIT, QUIT_COMMAND);
			commands.put(Command.CLOSE, INVALID_COMMAND);
			commands.put(Command.KILL, INVALID_COMMAND);
			commands.put(Command.GET, INVALID_COMMAND);
			commands.put(Command.PUT, INVALID_COMMAND);
			commands.put(Command.MODE, INVALID_COMMAND);
			commands.put(Command.HELP, HELP_COMMAND);
			break;
		}
	}
	
	private TransferMode getMode() {
		return mode;
	}

	private void setMode(TransferMode xfrMode) {
		this.mode = xfrMode;
	}

	/*
	 * Verifies the expected number of parameters are present.
	 * Reports error to user if not.
	 * NOTE: Command is first element in parameters[], but is not counted
	 */
	private boolean parameterCountIsOK(String[] parameters, int expCount) {
		
		int paramCount = parameters.length - 1;
		
		StringBuilder msg = null;

		if (paramCount < expCount) {
			msg = new StringBuilder("Too few parameters were specified.");
		}
		
		if (paramCount > expCount) {
			msg = new StringBuilder("Too many parameters were specified.");
		}
		
		if (msg != null) {
			msg.append("  ");
			msg.append(expCount);
			msg.append(" parameter");
			if (expCount != 1) {
				msg.append("s were");
			} else {
				msg.append(" was");
			}
			msg.append(" expected.");
			
			screenOut.println(msg.toString());
			
			return false;
		}

		return true;
	}

	/*
	 * This command opens a connection to the server.
	 */
	private class OpenCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				// store the host name for future use
				host = parameters[1];

				try {
					// create socket connection to server
					socket = new Socket(host, COMMAND_PORT);
					
					// temporarily reduce timeout during connection
					socket.setSoTimeout(5000);

					// store the server input and output streams
					serverIn = new Scanner(socket.getInputStream());
					serverOut = new PrintWriter(socket.getOutputStream(), true);

					// increase timeout to allow for file transfers
					socket.setSoTimeout(60000);

					// echo welcome message from server
					waitForServer();
					//screenOut.println(serverIn.nextLine());

					// send user name to server
					screenOut.println("User:");
					String user = screenIn.readLine();
					Result result = sendCommandToServer(tom.networking.server.Command.USER, user);

					// send password to server
					screenOut.println("Password:");
					String pwd = screenIn.readLine();
					result = sendCommandToServer(tom.networking.server.Command.PASS, pwd);

					// if login was successful
					if (result.succeeded()) {
						setConnectionState(ConnectionState.OPEN);
						screenOut.println("Connected to " + host);
					} else {
						// make sure resources are released
						setConnectionState(ConnectionState.CLOSED);
						screenOut.println("Failed to connect to " + host);
					}
				} catch (UnknownHostException e) {
					screenOut.println("Unknown host: " + host);
				} catch (ConnectException ex) {
					screenOut.println("Connection refused: " + host);
				} catch (IOException e) {
					screenOut.println("IOException occured: " + e.getMessage());
					e.printStackTrace();
				} finally {
					// if something went wrong, the connection will not be open
					if (getConnectionState() == ConnectionState.CLOSED) {
						// we need to call close to make sure resources are released.
						close();
					}
				}
			} 
			return true;
		}
	}

	/*
	 * This command closes the connection to the server
	 */
	private class CloseCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 0)) {

				// make sure we still have a connection
				if ((socket != null) && (!socket.isClosed())) {

					// ask the server to terminate the connection
					Result result = sendCommandToServer(tom.networking.server.Command.QUIT);

					if (result.succeeded()) {

						// release all resources
						close();

						screenOut.println("Connnection closed.");
					} else {
						screenOut.println("Failed to close connection.");
					}			
				} else {
					// socket is null or closed.
					// call close() to make sure streams are closed
					close();
				}
			}
			return true;
		}
	}

	/*
	 * This command quits the client after closing the connection to the server
	 */
	private class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 0)) {
				
				// get the close command
				Command cmd = getCommand(Command.CLOSE);

				// if the close command is actually a close command, execute it
				// to close the connection
				if (cmd.getClass() == CloseCommand.class) {
					cmd.execute(null);
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
	private class GetCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				// construct the file object to store to locally
				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				try {
					
					// ask server to accept second connection for file transfer
					Result result = sendCommandToServer(tom.networking.server.Command.PASV);
					
					// if server agrees
					if (result.succeeded()) {
						
						// open the socket
						Socket fileSocket = new Socket(host, DATA_PORT);
						fileSocket.setSoTimeout(10000);
						fileSocket.getInputStream();
						
						// ask server to send file
						result = sendCommandToServer(tom.networking.server.Command.RETR, fileName);
						
						if (result.succeeded()) {
							
							// transfer based on mode/type
							switch (getMode()) {
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
						
						// wait for file transfer to complete
						result = waitForServer();
						
						// close the socket
						fileSocket.close();
						
						screenOut.println("File received.");
					}

				} catch (FileNotFoundException e) {
					screenOut.println("File does not exist.");
				} catch (IOException e) {
					screenOut.println("IOException occured: " + e.getMessage());
					e.printStackTrace();
				}
			} 
			return true;
		}
	}

	/*
	 * This command stores a file to the server using the current mode/type
	 */
	private class PutCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				try {
					
					// ask server to accept second connection for file transfer
					Result result = sendCommandToServer(tom.networking.server.Command.PASV);
					
					// if server agrees
					if (result.succeeded()) {
						
						// open the socket
						Socket fileSocket = new Socket(host, DATA_PORT);
						fileSocket.setSoTimeout(10000);
							
						// ask server to receive file
						sendCommandToServer(tom.networking.server.Command.STOR, fileName);

						// transfer based on mode/type
						switch (getMode()) {
						case ASCII:
							TransferUtility.transferText(new FileReader(file), new OutputStreamWriter(fileSocket.getOutputStream()));
							break;
						case BINARY:
							TransferUtility.transferBinary(new FileInputStream(file), fileSocket.getOutputStream());
							break;
						}
						
						// wait for file transfer to complete
						result = waitForServer();
						
						// close the socket
						fileSocket.close();

						screenOut.println("File sent.");
					}

				} catch (FileNotFoundException e) {
					screenOut.println("File does not exist.");
				} catch (IOException e) {
					screenOut.println("IOException occured: " + e.getMessage());
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	/*
	 * This command sets the current mode/type
	 */
	private class ModeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				String newMode = parameters[1];

				// ask the server to change mode/type
				Result result = sendCommandToServer(tom.networking.server.Command.TYPE, newMode);

				// if successful, set local mode to match
				if (result.succeeded()) {
					if (newMode.toUpperCase().equals("A")) {
						setMode(TransferMode.ASCII);
					}

					if (newMode.toUpperCase().equals("B")) {
						setMode(TransferMode.BINARY);
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
	private class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 0)) {

				// ask the server to kill itself
				Result result = sendCommandToServer(tom.networking.server.Command.KILL);

				// if it works
				if (result.succeeded()) {

					close();

					screenOut.println("Server terminated.");
				} else {
					screenOut.println("Not authorized to terminate server.");
				}
			}

			return true;
		}
	}

	/*
	 * This command is executed in place of any normal command that is not valid
	 * in a given state (open/close).
	 */
	private class InvalidCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			String command = parameters[0];
			
			StringBuilder sb = new StringBuilder(75);
			sb.append("The '");
			sb.append(command.toUpperCase());
			sb.append("' command is not available when the connection is ");
			sb.append(getConnectionState());
			sb.append(".");
			
			screenOut.println(sb.toString());
					
			return true;
		}
	}

	/*
	 * This command is executed whenever the user enters any command which is
	 * not recognized.
	 */
	private class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			String command = parameters[0];
			
			StringBuilder sb = new StringBuilder(75);
			sb.append("The '");
			sb.append(command.toUpperCase());
			sb.append("' command is not recognized.");

			screenOut.println(sb.toString());

			return true;
		}
	}
	
	/*
	 * This command enumerates the various commands that are available
	 */
	private class HelpCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			screenOut.println("Supported commands are:");
			screenOut.println("");
			screenOut.println(Command.OPEN + " hostname - open a connection to the specified host");
			screenOut.println(Command.CLOSE + " - closes the connection");
			screenOut.println(Command.QUIT + " - quits the client, closing the connection if open");
			screenOut.println(Command.MODE + " A|B - sets the transfer mode to Ascii or Binary");
			screenOut.println(Command.GET + " filename - retrieves the specified file from the server");
			screenOut.println(Command.PUT + " filename - store the specified file to the server");
			screenOut.println(Command.KILL + " - kills the server (admin priveleges required)");

			return true;
		}
	}

}
