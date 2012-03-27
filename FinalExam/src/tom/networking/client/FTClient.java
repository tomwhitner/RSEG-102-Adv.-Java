package tom.networking.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tom.networking.TransferStrategy;
import tom.networking.server.FTServer;
import tom.networking.server.Result;

/*
 * The client application
 */
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

	private static final int TIMEOUT_CONNECT = 5000; // five second connect timeout						
	private static final int TIMEOUT_NORMAL = 0; // no timeout after connect

	private String host = null;
	private Socket socket = null;
	private Scanner serverIn = null;
	private PrintWriter serverOut = null;

	private ConnectionState connectionState;

	private TransferStrategy transferStrategy = TransferStrategy.AsciiTransfer.getInstance();

	/*
	 * Program main method.
	 */
	public static void main(String[] args) {
		// instantiate a new instance and run it.
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
	 * This is the main method for the client application It loops, accepting
	 * and processing commands from the user.
	 */
	void run() {

		screenOut.println("Welcome to FTClient...");

		// set the initial connection state
		setConnectionState(ConnectionState.CLOSED);

		try {

			boolean proceed = true;

			// stop when command indicates loop should exit (Quit)
			while (proceed) {
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
			}

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
	private void close() {

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
			try {
				socket.close();
			} catch (IOException e) {
				// Not much can/should be done here. Exiting anyway.
				e.printStackTrace();
			}
			socket = null;
		}

		// make sure connection state is correct
		setConnectionState(ConnectionState.CLOSED);
	}

	/*
	 * Get the specified command object from the map
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

		// wait for and return the server response
		return waitForServer();
	}

	/*
	 * Waits for, parses, and echos server responses
	 */
	private Result waitForServer() {

		Result result = null;
		
		do {
			// read the result string form the server
			String restultLine = serverIn.nextLine();
			// output each result line 
			screenOut.println(restultLine);
			// construct a new result object
			result = new Result(restultLine);
		} while (!result.getLast());

		// return the result
		return result;
	}

	/*
	 * Enumeration used to track connection state
	 */
	private enum ConnectionState {
		OPEN, CLOSED
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

		// if the state hasn't changed, just exit
		if (this.connectionState == connectionState)
			return;

		// store the new state
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

	/*
	 * Gets the current transfer strategy
	 */
	protected TransferStrategy getTransferStrategy() {
		return transferStrategy;
	}

	/*
	 * Sets the current transfer strategy
	 */
	protected void setTransferStrategy(TransferStrategy transferStrategy) {
		this.transferStrategy = transferStrategy;
	}

	/*
	 * Verifies the expected number of parameters are present. Reports error to
	 * user if not. NOTE: Command is first element in parameters[], but is not
	 * counted
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
	private class OpenCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				// store the host name for future use
				host = parameters[1];

				try {
					// create socket connection to server
					socket = new Socket(host, FTServer.CMD_PORT);

					// temporarily reduce timeout during connection
					socket.setSoTimeout(TIMEOUT_CONNECT);

					// store the server input and output streams
					serverIn = new Scanner(socket.getInputStream());
					serverOut = new PrintWriter(socket.getOutputStream(), true);

					// increase timeout to allow for file transfers
					socket.setSoTimeout(TIMEOUT_NORMAL);

					// echo welcome message from server
					waitForServer();
					// screenOut.println(serverIn.nextLine());

					// send user name to server
					screenOut.print("User: ");
					screenOut.flush();
					String user = screenIn.readLine();
					Result result = sendCommandToServer(tom.networking.server.Command.USER, user);

					// if server accepted user
					if (result.succeeded()) {
						// send password to server
						screenOut.print("Password: ");
						screenOut.flush();
						String pwd = screenIn.readLine();
						result = sendCommandToServer(tom.networking.server.Command.PASS, pwd);
					}

					// if login was successful (both previous commands executed
					// successfully)
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
						// we need to call close to make sure resources are
						// released.
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
	private class CloseCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

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
	private class QuitCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			if (parameterCountIsOK(parameters, 0)) {

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
	 * This abstract command implements the algorithm for both the GET and PUT
	 * commands that transfer files
	 */
	private abstract class TransferCommand extends Command {

		private final String serverCommand;
		private final String successMessage;
		private final String failureMessage;
		private final boolean fileRequired;

		/*
		 * constructor
		 */
		protected TransferCommand(String serverCommand, String successMessage, String failureMessage,
				boolean fileRequired) {
			this.serverCommand = serverCommand;
			this.successMessage = successMessage;
			this.failureMessage = failureMessage;
			this.fileRequired = fileRequired;
		}

		@Override
		public boolean doExecute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				// construct the file object to store to locally
				String fileName = parameters[1];
				File file = new File(fileDir, fileName);

				if (fileRequired && !file.exists()) {
					screenOut.println("File does not exist.");
					return true;
				}

				Socket dataSocket = null;

				try {

					// ask server to accept second connection for file transfer
					Result result = sendCommandToServer(tom.networking.server.Command.PASV);

					// if server agrees
					if (result.succeeded()) {

						// open the socket
						dataSocket = new Socket(host, FTServer.DATA_PORT);
						dataSocket.setSoTimeout(10000);

						// send the token to verify connection
						PrintWriter pw = new PrintWriter(dataSocket.getOutputStream());
						pw.println(result.getMessage());
						pw.flush();

						// ask server to send file
						result = sendCommandToServer(serverCommand, fileName);

						if (result.succeeded()) {

							// perform the transfer
							doTransfer(getTransferStrategy(), file, dataSocket);

							// wait for file transfer to complete
							result = waitForServer();

							screenOut.println(successMessage);
						} else {
							screenOut.println(failureMessage);
						}
					}

				} catch (FileNotFoundException e) {
					screenOut.println("File does not exist.");
				} catch (IOException e) {
					screenOut.println("IOException occured: " + e.getMessage());
					e.printStackTrace();
				} finally {
					// close the socket
					try {
						if (dataSocket != null) {
							dataSocket.close();
							dataSocket = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		}

		// performs file transfer
		protected abstract void doTransfer(TransferStrategy transferStrategy, File file, Socket socket)
				throws FileNotFoundException, IOException;
	}

	/*
	 * This command retrieves a file from the server using the current mode/type
	 */
	private class GetCommand extends TransferCommand {

		/*
		 * constructor
		 */
		public GetCommand() {
			super(tom.networking.server.Command.RETR, "File received.", "File was not received.", false);
		}

		// performs file transfer (socket -> file)
		@Override
		protected void doTransfer(TransferStrategy transferStrategy, File file, Socket socket)
				throws FileNotFoundException, IOException {
			transferStrategy.transfer(socket, file);
		}
	}

	/*
	 * This command stores a file to the server using the current mode/type
	 */
	private class PutCommand extends TransferCommand {

		/*
		 * constructor
		 */
		public PutCommand() {
			super(tom.networking.server.Command.STOR, "File sent.", "File was not sent.", true);
		}

		// performs file transfer (file -> socket)
		@Override
		protected void doTransfer(TransferStrategy transferStrategy, File file, Socket socket)
				throws FileNotFoundException, IOException {
			transferStrategy.transfer(file, socket);
		}
	}

	/*
	 * This command sets the current mode/type by setting the current transfer
	 * strategy
	 */
	private class ModeCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				String newMode = parameters[1];

				// ask the server to change mode/type
				Result result = sendCommandToServer(tom.networking.server.Command.TYPE, newMode);

				// if successful, set the local transfer strategy accordingly
				if (result.succeeded()) {
					if (newMode.toUpperCase().equals("A")) {
						setTransferStrategy(TransferStrategy.AsciiTransfer.getInstance());
					}

					if (newMode.toUpperCase().equals("B")) {
						setTransferStrategy(TransferStrategy.BinaryTransfer.getInstance());
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
	private class KillCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			if (parameterCountIsOK(parameters, 0)) {

				// ask the server to kill itself
				Result result = sendCommandToServer(tom.networking.server.Command.KILL);

				// if it works
				if (result.succeeded()) {

					// release all resources
					close();

					screenOut.println("Server accepted termination request.");
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
	private class InvalidCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

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
	 * This command enumerates the various commands that are available
	 */
	private class HelpCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			screenOut.println("Supported commands are:");
			screenOut.println("");
			screenOut.println(Command.OPEN + " hostname - open a connection to the specified host");
			screenOut.println(Command.CLOSE + " - closes the connection");
			screenOut.println(Command.QUIT + " - quits the client, closing the connection if open");
			screenOut.println(Command.MODE + " A|B - sets the transfer mode to Ascii or Binary");
			screenOut.println(Command.GET + " filename - retrieves the specified file from the server");
			screenOut.println(Command.PUT + " filename - stores the specified file to the server");
			screenOut.println(Command.KILL + " - kills the server (admin priveleges required)");
			screenOut.println(Command.HELP + " - prints this listing");

			return true;
		}
	}

	/*
	 * This command is executed whenever the user enters any command which is
	 * not recognized.
	 */
	private class UnknownCommand extends Command {

		@Override
		public boolean doExecute(String[] parameters) {

			String command = parameters[0];

			StringBuilder sb = new StringBuilder(75);
			sb.append("The '");
			sb.append(command.toUpperCase());
			sb.append("' command is not recognized.");

			screenOut.println(sb.toString());

			return true;
		}
	}

}
