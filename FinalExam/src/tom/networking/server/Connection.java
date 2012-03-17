package tom.networking.server;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import tom.networking.TransferMode;
import tom.networking.TransferUtility;

class Connection implements Runnable {

	private final File fileDir = new File("server");
	private final Map<String, Command> commands = new HashMap<String, Command>();

	private final Socket commandSocket;
	private final ServerSocket dataPort;
	private Socket dataSocket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private static final String USER_ANONYMOUS = "ANONYMOUS";
	private static final String USER_ADMIN = "ADMIN";
	private String user = null;
	private boolean admin = false;
	private TransferMode mode = TransferMode.ASCII;
	
	private final UnknownCommand UNKNOWN_COMMAND = new UnknownCommand();

	public Connection(Socket socket, ServerSocket data) {

		this.commandSocket = socket;
		this.dataPort = data;

		commands.put(Command.PASV, new PasvCommand());
		commands.put(Command.RETR, new RetrCommand());
		commands.put(Command.STOR, new StorCommand());
		commands.put(Command.TYPE, new TypeCommand());
		commands.put(Command.QUIT, new QuitCommand());
		commands.put(Command.KILL, new KillCommand());
		commands.put(Command.USER, new UserCommand());
		commands.put(Command.PASS, new PassCommand());

	}

	/*
	 * This is the main method for the connection
	 * It loops, accepting and processing commands from the client.
	 */
	@Override
	public void run() {

		try {

			in = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
			out = new PrintWriter(commandSocket.getOutputStream(), true /* autoFlush */);

			outputToClient(Result.INPROGRESS, "FTServer Ready.", true);

			boolean proceed = true;

			// stop when command indicates loop should exit (Quit, Kill)
			while (proceed) {
				// read a line from client input
				String line = in.readLine();
				
				System.out.println("Processing: " + line);

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
	void close() {
		
		// close the client input stream if necessary
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// Not much can/should be done here.  Exiting anyway.
				e.printStackTrace();
			}
			in = null;
		}
		
		// close the client output stream if necessary
		if (out != null) {
			out.close();
			out = null;
		}
		
		// close the command socket if necessary
		if (commandSocket != null) {
			if (!commandSocket.isClosed()) {
				try {
					commandSocket.close();
				} catch (IOException e) {
					// Not much can/should be done here.  Exiting anyway.
					e.printStackTrace();
				}
			}
		}
		
		// close the data socket if necessary
		if (dataSocket != null) {
			if (!dataSocket.isClosed()) {
				try {
					dataSocket.close();
				} catch (IOException e) {
					// Not much can/should be done here.  Exiting anyway.
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Send well-formatted message to client
	 * This includes codes that enable the client to determine success or failure
	 */
	private void outputToClient(int code, String message, boolean last) {

		// sanity check that no invalid codes are sent to client
		assert (code >= 100) && (code <= 599) : "Invalid code";

		// construct the message
		StringBuilder sb = new StringBuilder(message.length() + 1);
		sb.append(code);
		sb.append(last ? " " : "-");
		sb.append(message);
		
		// send the message to the client
		out.println(sb.toString());
	}

	/*
	 * Verifies the expected number of parameters are present.
	 * Reports error to client if not.
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
			msg.append("  ")
			  .append(expCount)
			  .append(" parameter");
			if (expCount != 1) {
				msg.append("s were");
			} else {
				msg.append(" was");
			}
			msg.append(" expected.");
			
			outputToClient(Result.FAILURE, msg.toString(), true);
			
			return false;
		}

		return true;
	}

	/*
	 * Get the specified command object from the map
	 */
	private Command getCommand(String commandName) {
		// lookup the command in the map
		Command cmd = commands.get(commandName.toUpperCase());
		// if command is not found
		if (cmd == null) {
			// return the unknown command
			cmd = UNKNOWN_COMMAND;
		}
		return cmd;
	}

	/*
	 * Authenticate the user
	 */
	private boolean authenticate(String usr, String pwd) {

		// all anonymous users are allowed
		if (usr.toUpperCase().equals(USER_ANONYMOUS))
		{
			return true;
		}

		// Admin is the only named account
		if (usr.toUpperCase().equals(USER_ADMIN) && pwd.equals("admin"))
		{
			return true;
		}

		// all other users are not allowed
		return false;
	}

	/*
	 * This command sets the type (or mode) of the file transfer to Ascii or Binary
	 */
	private class TypeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				// get the new mode
				String newMode = parameters[1];

				// if "A" set to ascii
				if (newMode.toUpperCase().equals("A")) {
					outputToClient(Result.SUCCESS, "Mode set to Ascii.", true);
					mode = TransferMode.ASCII;
					return true;
				} 
				
				// if "B" set to binary
				if (newMode.toUpperCase().equals("B")) {
					outputToClient(Result.SUCCESS, "Mode set to Binary.", true);
					mode = TransferMode.BINARY;
					return true;
				} 
				
				// anything else is invalid
				outputToClient(Result.FAILURE, "Invalid mode specified.  'A' or 'B' are accepted.", true);
			}
			return true;
		}
	}

	/*
	 * This command accepts the user name attempting to login
	 */
	private class UserCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {
				// get the user name
				user = parameters[1];
				
				// notify that user is accepted
				StringBuilder message = new StringBuilder(50);
				message.append("User '")
				  .append(user)
				  .append("' accepted.");
				outputToClient(Result.SUCCESS, message.toString(), false);
				
				// clear message
				message.setLength(0);
				
				// notify of password requirements
				if (user.toUpperCase().equals(USER_ANONYMOUS)) {
					message.append("Send email for password.");
					
				} else {
					message.append("Password required for user '")
					  .append(user)
					  .append("'");
				}
				outputToClient(Result.SUCCESS, message.toString(), true);
			}
			return true;
		}
	}

	/*
	 * This command accepts the password and attempts the authentication of the user (previously specified)
	 */
	private class PassCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {
				
				// if there is not user, notify client and exit
				if (user == null) {
					outputToClient(Result.FAILURE, "User has not been specified.", true);
					return true;
				}
				
				// get the password
				String pwd = parameters[1];
				
				StringBuilder message = new StringBuilder(50);
				// attempt authentication and notify user
				if (authenticate(user, pwd)) {
					message.append("User '")
					  .append(user)
					  .append("' logged in.");
					
					// determine if user is an administrator
					admin = user.toUpperCase().equals(USER_ADMIN);
					
					outputToClient(Result.SUCCESS, message.toString(), !admin);
					
					if (admin) {
						outputToClient(Result.SUCCESS, "User has administrator priveleges.", true);						
					}

				} else {
					message.append("User '")
					  .append(user)
					  .append(" is not authorized.");
					outputToClient(Result.FAILURE, message.toString(), true);
				}
			}
			return true;
		}
	}

	/*
	 * This command closes the connection
	 */
	private class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			// notify client 
			outputToClient(Result.SUCCESS, "Goodbye!", true);

			// close and release all resources
			close();
			
			// tell main loop to exit
			return false;
		}
	}

	/*
	 * This command kills the server including all open connections
	 */
	private class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (admin) {
				
				// notify client 
				outputToClient(Result.SUCCESS, "Terminating Server.  Goodbye!", true);

				// close the connection and all resources
				close();
				
				// terminate the process
				System.exit(0);
				
				return false;

			} else {
				// notify client they are not admin
				outputToClient(Result.FAILURE, "User not authorized to KILL server.", true);
				return true;
			}
		}
	}
	
	/*
	 * This command prepares the server to accept a data connection from the client
	 */
	private class PasvCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			try {
				// prepare to accept another connection for data from the client
				outputToClient(Result.SUCCESS, "Ready to accept data connection.", true);

				// wait for client connection
				dataSocket = dataPort.accept();

			} catch (IOException e) {
				outputToClient(Result.FAILURE, "IOException: " + e.getMessage(), true);
				e.printStackTrace();
			}

			return true;
		}
	}
	
	/*
	 * This abstract command implements the algorithm for both
	 * the RETR and STOR commands that transfer files
	 */
	private abstract class TransferCommand implements Command {
	
		@Override
		public boolean execute(String[] parameters) {
			
			// verify that the data connection exists
			if (dataSocket == null) {
				// notify client and exit if it does not
				outputToClient(Result.FAILURE, "Data connection not established.", true);
				return true;
			}

			if (parameterCountIsOK(parameters, 1)) {

				try {

					// get the file object
					String fileName = parameters[1];
					File file = new File(fileDir, fileName);

					// notify client to begin send/receive
					outputToClient(Result.INPROGRESS, "Begin send/receive.", true);
					
					// perform the transfer, honoring requested mode/type
					switch (mode) {
					case ASCII:
						doAsciiTransfer(file, dataSocket);
						break;
					case BINARY:
						doBinaryTransfer(file, dataSocket);
						break;
					}
					
					// close the data socket
					dataSocket.close();
					dataSocket = null;

					// notify client that transfer is complete
					outputToClient(Result.SUCCESS, "File sent/received.", true);

				} catch (FileNotFoundException e) {
					outputToClient(Result.FAILURE, "File does not exist.", true);
				} catch (IOException e) {
					outputToClient(Result.FAILURE, "IOException: " + e.getMessage(), true);
					e.printStackTrace();
				}

			}
			return true;
		}
		
		// performs ascii file transfer
		protected abstract void doAsciiTransfer(File file, Socket socket) throws FileNotFoundException, IOException;
		
		// performs binary file transfer
		protected abstract void doBinaryTransfer(File file, Socket socket) throws FileNotFoundException, IOException;
		
	}

	/*
	 * This command retrieves a file from the server and sends it to the client
	 */
	private class RetrCommand extends TransferCommand {

		// performs ascii file transfer
		@Override
		protected void doAsciiTransfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferText(new FileReader(file), new OutputStreamWriter(dataSocket.getOutputStream()));
		}

		// performs binary file transfer
		@Override
		protected void doBinaryTransfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferBinary(new FileInputStream(file), dataSocket.getOutputStream());
		}
	}

	/*
	 * This command receives a file from the client and stores it on the server
	 */
	private class StorCommand extends TransferCommand {

		// performs ascii file transfer
		@Override
		protected void doAsciiTransfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferText(new InputStreamReader(dataSocket.getInputStream()), new FileWriter(file));
		}

		// performs binary file transfer
		@Override
		protected void doBinaryTransfer(File file, Socket socket) throws FileNotFoundException, IOException {
			TransferUtility.transferBinary(dataSocket.getInputStream(), new FileOutputStream(file));
		}
	}

	/*
	 * This command is executed whenever the client sends any command which is
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

			outputToClient(Result.FAILURE, sb.toString(), true);

			return true;
		}
	}
}