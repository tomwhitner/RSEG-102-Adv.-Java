package tom.networking.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Connection implements Runnable {

	private Socket socket = null;
	private Scanner in = null;
	private PrintWriter out = null;
	private File fileDir = new File("files");
	private Map<String, Command> commands = new HashMap<String, Command>();
	private boolean admin = false;

	private final String CMD_GET = "GET";
	private final String CMD_PUT = "PUT";
	private final String CMD_BYE = "BYE";
	private final String CMD_KILL = "KILL";
	private final String CMD_UNKNOWN = "UNKNOWN";

	public Connection(Socket socket) {

		this.socket = socket;

		commands.put(CMD_GET, new GetCommand());
		commands.put(CMD_PUT, new PutCommand());
		commands.put(CMD_BYE, new ByeCommand());
		commands.put(CMD_KILL, new KillCommand());
		commands.put(CMD_UNKNOWN, new UnknownCommand());

	}

	@Override
	public void run() {

		try {

			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true /* autoFlush */);

			out.println("Welcome to FTServer ...");

			if (login()) {

				boolean proceed = true;

				while (in.hasNextLine() && proceed) {

					String line = in.nextLine();
					String[] lines = line.split(" ");

					Command cmd = getCommand(lines[0]);
					proceed = cmd.execute(lines);
				}
			} else {

				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean login() {

		out.println("User (required): ");
		String usr = in.nextLine();

		out.println("Password (required): ");
		String pwd = in.nextLine();

		boolean result = authenticate(usr, pwd);

		if (result) {
			out.println("User " + usr + " logged in.");
			admin = usr.equals("Admin");
		} else {
			out.println("User not authorized.");
		}

		return result;
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
			cmd = commands.get(CMD_UNKNOWN);
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

					Scanner s = new Scanner(file);

					while (s.hasNextLine()) {
						out.println(s.nextLine());

					}

				} catch (FileNotFoundException e) {

					out.println("File does not exist.");
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

					file.createNewFile();
					PrintWriter pw = new PrintWriter(file);

					while (in.hasNextLine()) {
						pw.println(in.nextLine());
						pw.flush();
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