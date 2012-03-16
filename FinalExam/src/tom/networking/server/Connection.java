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
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import tom.networking.TransferMode;
import tom.networking.TransferUtility;


class Connection implements Runnable {
	
	private Socket socket = null;
	private Socket dataSocket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private File fileDir = new File("server");
	private Map<String, Command> commands = new HashMap<String, Command>();
	private boolean admin = false;
	private TransferMode mode = TransferMode.ASCII;

	public Connection(Socket socket) {

		this.socket = socket;

		commands.put(Command.RETR, new RetrCommand());
		commands.put(Command.STOR, new StorCommand());
		commands.put(Command.TYPE, new TypeCommand());
		commands.put(Command.QUIT, new QuitCommand());
		commands.put(Command.KILL, new KillCommand());
		commands.put(Command.USER, new UserCommand());
		commands.put(Command.PASS, new PassCommand());
		commands.put(Command.UNKNOWN, new UnknownCommand());

	}

	@Override
	public void run() {

		try {

			in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);

			out.println("Welcome to FTServer ...");

			boolean proceed = true;

			String line;
			
			while (((line = in.readLine()) != null) && proceed) {
				
				System.out.println("Processing: " + line);

				String[] lines = line.split(" ");

				Command cmd = getCommand(lines[0]);
				proceed = cmd.execute(lines);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void outputToClient(int code, String message, boolean last) {
		
		assert (code >= 100) && (code <= 599) : "Invalid code";
		
		StringBuilder sb = new StringBuilder(message.length() + 1);
		
		sb.append(code);
		sb.append(last ? " " : "-");
		sb.append(message);
		out.println(sb.toString());
	}
	
	private boolean parameterCountIsOK(String[] parameters, int expectedCount) {
		
	
		if (parameters.length < expectedCount) {
			outputToClient(400, "Too few parameters specified. " + expectedCount + " were expected.", true);
			return false;
		}
		if (parameters.length > expectedCount) {
			outputToClient(400, "Too many parameters specified. " + expectedCount + " were expected.", true);
			return false;
		}
		return true;
		
	}

	private boolean authenticate(String usr, String pwd) {

		if (usr.toUpperCase().equals(USER_ANONYMOUS))
			return true;

		if (usr.toUpperCase().equals(USER_ADMIN) && pwd.equals("admin"))
			return true;

		return false;
	}

	private Command getCommand(String commandName) {
		Command cmd = commands.get(commandName.toUpperCase());
		if (cmd == null) {
			cmd = commands.get(Command.UNKNOWN);
		}
		return cmd;
	}
	
	class TypeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters,2)) {

				String type = parameters[1];
				
				if (type.toUpperCase().equals("A")) {
					outputToClient(200, "Mode set to Ascii.", true);
					mode = TransferMode.ASCII;
				} else {

					if (type.toUpperCase().equals("B")) {
						outputToClient(200, "Mode set to Binary.", true);
						mode = TransferMode.BINARY;
					} else {
						outputToClient(400, "Invalid mode specified.  'A' or 'B' are accepted.", true);
					}
				}
			}
			return true;
		}
	}
	
	private String user = null;
	private static final String USER_ANONYMOUS = "ANONYMOUS";
	private static final String USER_ADMIN = "ADMIN";
	
	class UserCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if (parameterCountIsOK(parameters,2)) {
				user = parameters[1];
				outputToClient(200, "User: " + user + " - accepted.", false);
				if (user.toUpperCase().equals(USER_ANONYMOUS)) {
					outputToClient(200, "Send email for password.", true);
				} else {				
					outputToClient(200, "Password required for user " + user, true);
				}
				
			}
			return true;
		}
	}

	class PassCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if (parameterCountIsOK(parameters,2)) {
				String pwd = parameters[1];
				if (authenticate(user, pwd)) {
					outputToClient(200, "User: " + user + " - logged in.", true);
					admin = user.toUpperCase().equals(USER_ADMIN);
				} else {
					outputToClient(400, "User: " + user + " - Not authorized.", true);
				}
			}
			return true;
		}
	}

	
	class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			outputToClient(400, "Unknown command: " + parameters[0], true);
			return true;
		}
	}

	class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			try {
				outputToClient(200, "Goodbye!", true);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	class KillCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (admin) {
				try {
					outputToClient(200, "Terminating Server.  Goodbye!", true);
					socket.close();
					System.exit(0);
					return false;

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				outputToClient(400, "User not authorized to KILL server.", true);
			}
			return true;
		}
	}
	
	class PasvCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			// prepare to accept another connection for data from the client on a different port.
			return true;
		}
	}


	class RetrCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameterCountIsOK(parameters,2)) {

				File file = new File(fileDir, parameters[1]);

				try {
					
					switch (mode) {
						case ASCII:
							TransferUtility.transferText(new FileReader(file), new OutputStreamWriter(socket.getOutputStream()));
							break;
						case BINARY:
							TransferUtility.transferBinary(new FileInputStream(file), socket.getOutputStream());
							break;
					}

				} catch (FileNotFoundException e) {
					outputToClient(400, "File does not exist.", true);
				} catch (IOException e) {

					e.printStackTrace();
				}

			} 
			return true;

		}

	}

	class StorCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if (parameterCountIsOK(parameters,2)) {

				File file = new File(fileDir, parameters[1]);

				try {

					switch (mode) {
					case ASCII:
						TransferUtility.transferText(new InputStreamReader(socket.getInputStream()), new FileWriter(file));
						break;
					case BINARY:
						TransferUtility.transferBinary(socket.getInputStream(), new FileOutputStream(file));
						break;
				}

				} catch (FileNotFoundException e) {
					outputToClient(400, "File does not exist.", true);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return true;

		}
	}
}