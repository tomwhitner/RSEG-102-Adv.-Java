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
			msg.append("  ");
			msg.append(expCount);
			msg.append(" parameter");
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
		Command cmd = commands.get(commandName.toUpperCase());
		if (cmd == null) {
			cmd = commands.get(new UnknownCommand());
		}
		return cmd;
	}

	private boolean authenticate(String usr, String pwd) {

		if (usr.toUpperCase().equals(USER_ANONYMOUS))
			return true;

		if (usr.toUpperCase().equals(USER_ADMIN) && pwd.equals("admin"))
			return true;

		return false;
	}

	private class TypeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {

				String type = parameters[1];

				if (type.toUpperCase().equals("A")) {
					outputToClient(Result.SUCCESS, "Mode set to Ascii.", true);
					mode = TransferMode.ASCII;
				} else {

					if (type.toUpperCase().equals("B")) {
						outputToClient(Result.SUCCESS, "Mode set to Binary.", true);
						mode = TransferMode.BINARY;
					} else {
						outputToClient(
								Result.FAILURE,
								"Invalid mode specified.  'A' or 'B' are accepted.",
								true);
					}
				}
			}
			return true;
		}
	}

	private class UserCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {
				user = parameters[1];
				outputToClient(Result.SUCCESS, "User: " + user + " - accepted.", false);
				if (user.toUpperCase().equals(USER_ANONYMOUS)) {
					outputToClient(Result.SUCCESS, "Send email for password.", true);
				} else {
					outputToClient(Result.SUCCESS, "Password required for user " + user,
							true);
				}

			}
			return true;
		}
	}

	private class PassCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters, 1)) {
				String pwd = parameters[1];
				if (authenticate(user, pwd)) {
					outputToClient(Result.SUCCESS, "User: " + user + " - logged in.", true);
					admin = user.toUpperCase().equals(USER_ADMIN);
				} else {
					outputToClient(Result.FAILURE, "User: " + user + " - Not authorized.",
							true);
				}
			}
			return true;
		}
	}

	private class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			outputToClient(Result.FAILURE, "Unknown command: " + parameters[0], true);
			return true;
		}
	}

	private class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			try {
				outputToClient(Result.SUCCESS, "Goodbye!", true);
				commandSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	private class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (admin) {
				try {
					outputToClient(Result.SUCCESS, "Terminating Server.  Goodbye!", true);
					commandSocket.close();
					System.exit(0);
					return false;

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				outputToClient(Result.FAILURE, "User not authorized to KILL server.", true);
			}
			return true;
		}
	}


	private class PasvCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			try {
				// prepare to accept another connection for data from the client
				outputToClient(Result.SUCCESS, "Ready to accept data connection.", true);

				// wait for client connection
				dataSocket = dataPort.accept();

			} catch (IOException e) {

				e.printStackTrace();
			}

			return true;
		}
	}

	private class RetrCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (dataSocket == null) {
				outputToClient(Result.FAILURE, "Data connection not established.", true);
			}

			if (parameterCountIsOK(parameters, 1)) {

				try {

					String fileName = parameters[1];
					File file = new File(fileDir, fileName);

					outputToClient(Result.INPROGRESS, "Begin receiving.", true);

					switch (mode) {
					case ASCII:
						TransferUtility.transferText(
								new FileReader(file),
								new OutputStreamWriter(dataSocket
										.getOutputStream()));
						break;
					case BINARY:
						TransferUtility.transferBinary(
								new FileInputStream(file),
								dataSocket.getOutputStream());
						break;
					}

					dataSocket.close();
					dataSocket = null;

					outputToClient(Result.SUCCESS, "File sent.", true);

				} catch (FileNotFoundException e) {
					outputToClient(Result.FAILURE, "File does not exist.", true);
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
			return true;

		}

	}

	private class StorCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (dataSocket == null) {
				outputToClient(Result.FAILURE, "Data connection not established.", true);
			}

			if (parameterCountIsOK(parameters, 1)) {

				try {

					String fileName = parameters[1];
					File file = new File(fileDir, fileName);

					outputToClient(Result.INPROGRESS, "Begin sending.", true);

					switch (mode) {
					case ASCII:
						TransferUtility.transferText(new InputStreamReader(dataSocket.getInputStream()), new FileWriter(file));
						break;
					case BINARY:
						TransferUtility.transferBinary(dataSocket.getInputStream(), new FileOutputStream(file));
						break;
					}

					outputToClient(Result.SUCCESS, "File received.", true);
					
				} catch (FileNotFoundException e) {
					outputToClient(Result.FAILURE, "File does not exist.", true);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return true;

		}
	}
}