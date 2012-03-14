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
import tom.networking.Command;

class Connection implements Runnable {
	
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private File fileDir = new File("server");
	private Map<String, Command> commands = new HashMap<String, Command>();
	private boolean admin = false;
	private TransferMode mode = TransferMode.ASCII;

	public Connection(Socket socket) {

		this.socket = socket;

		commands.put(Command.CMD_GET, new GetCommand());
		commands.put(Command.CMD_PUT, new PutCommand());
		commands.put(Command.CMD_ASCII, new AsciiCommand());
		commands.put(Command.CMD_BINARY, new BinaryCommand());
		commands.put(Command.CMD_BYE, new ByeCommand());
		commands.put(Command.CMD_KILL, new KillCommand());
		commands.put(Command.CMD_UNKNOWN, new UnknownCommand());

	}

	@Override
	public void run() {

		try {

			in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);

			out.println("Welcome to FTServer ...");

			if (login()) {

				boolean proceed = true;

				String line;
				
				while (((line = in.readLine()) != null) && proceed) {

					String[] lines = line.split(" ");

					Command cmd = getCommand(lines[0]);
					proceed = cmd.execute(lines);
				}
			} else {

				socket.close();
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

	private boolean login() {

		try {
		out.println("User (required): ");
		String usr = in.readLine();

		out.println("Password (required): ");
		String pwd = in.readLine();


		if (authenticate(usr, pwd)) {
			out.println("User " + usr + " logged in.");
			admin = usr.equals("Admin");
			return true;
		} else {
			out.println("User not authorized.");
		}
		
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return false;
	}

	private boolean authenticate(String usr, String pwd) {

		if (usr.equals("Guest") && pwd.equals("guest"))
			return true;

		if (usr.equals("Admin") && pwd.equals("admin"))
			return true;

		return false;
	}

	private Command getCommand(String commandName) {
		Command cmd = commands.get(commandName.toUpperCase());
		if (cmd == null) {
			cmd = commands.get(Command.CMD_UNKNOWN);
		}
		return cmd;
	}
	
	class AsciiCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			out.println("Mode set to Ascii.");
			mode = TransferMode.ASCII;
			return true;
		}
	}

	class BinaryCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			out.println("Mode set to Binary.");
			mode = TransferMode.BINARY;
			return true;
		}
	}

	class UnknownCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			out.println("Unknown command: " + parameters[0]);
			return true;
		}
	}

	class ByeCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			try {
				out.println("Goodbye!");
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
					out.println("Terminating Server.  Goodbye!");
					socket.close();
					System.exit(0);
					return false;

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				out.println("User not authorized to KILL server.");
			}
			return true;
		}
	}

	class GetCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameters.length == 2) {

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

					out.println("File does not exist.");
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else {
				out.println("Incorrect number of command arguments specified.");
			}
			return true;

		}

	}

	class PutCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if (parameters.length == 2) {

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
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				out.println("Incorrect number of command arguments specified.");
			}
			return true;

		}
	}
}