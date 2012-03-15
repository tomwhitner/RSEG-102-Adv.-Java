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
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private Socket socket = null;
	
	private static final int PORT = 8189;

	public static void main(String[] args) {
		new FTClient().run();
	}
		
	void run() {

		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out, true /* autoFlush */);
		
		commands.put(Command.UNKNOWN, new UnknownCommand());
		commands.put(Command.GET, new GetCommand());
		commands.put(Command.OPEN, new OpenCommand());
		commands.put(Command.QUIT, new QuitCommand());
		commands.put(Command.CLOSE, new CloseCommand());

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
			cmd = commands.get(Command.UNKNOWN);
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
	
	class OpenCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {

			if (parameters.length == 2) {
				
				String host = parameters[1];
				
				try {
					socket = new Socket(host, PORT);
					socket.setSoTimeout(5000);
					
					Scanner s = new Scanner(socket.getInputStream());
					PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
					
					socket.setSoTimeout(60000);

					// welcome
					out.println(s.nextLine());
					
					// user 
					out.println(s.nextLine());
					pw.println(in.readLine());
					pw.flush();
					
					// pwd
					out.println(s.nextLine());
					pw.println(in.readLine());

					out.println(s.nextLine());
					out.println("Connected to " + host);
					
				} catch (UnknownHostException e) {
					out.println("Unknown host: " + host);
				} catch (ConnectException ex) {
					out.println("Connection to " + host + " refused.ope");
				}
				catch (IOException e) {

					e.printStackTrace();
				}
			
			} else {
				out.println("Incorrect number of command arguments specified.");
			}

			return true;
		}
	}
	
	class QuitCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if ((socket != null)  && (!socket.isClosed())) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return false;
		}
	}
	
	class CloseCommand implements Command {

		@Override
		public boolean execute(String[] parameters) {
			
			if ((socket != null)  && (!socket.isClosed())) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return true;
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

}
