import java.lang.*;
import java.io.*;
import java.net.*;

public class Server {
	private ServerSocket server;
	private Socket serverSocket;
	private PrintStream sendStream;
	private BufferedReader receiveStream;
	private static int score = 0;	
	private static boolean state = true;

	public static void main(String[] args) {
		Server runServer = new Server();
		runServer.Run();
	}		

	public Server() {
		server = null;
		serverSocket = null;
		sendStream = null;
		receiveStream = null;
	}
	
	private void Run() {
		String stringNumber = "";
		try {
			server = new ServerSocket(2209);
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		while (true) {
			System.out.println("Waiting for client connect ... ");
			Server.score = 0;			
			this.ConnectSocket();
			stringNumber = ServerCommon.GenerateString();
			this.SendMessage(stringNumber);
			System.out.println("Connect successful!");
			System.out.println("Playing ...");
			// Get key type
			while (true) {
				String stringRequest = this.ReceiveMessage();
				if (stringRequest.equals("EXIT")) break;
				stringNumber = ServerCommon.Analyze(stringRequest, stringNumber);
				this.SendMessage(stringNumber);

				this.SendMessage(String.valueOf(Server.score));
				if (ServerCommon.CheckIsWin()) {
					this.SendMessage("WIN");
					break;
				}			

				else {
					if (!Server.state) {
						Server.setState(true);
						this.SendMessage("LOSE");
						break;
					}
					else {
						this.SendMessage("NORMAL");
					}
				}
				
			}
			System.out.println("Close connection complete!");
			try {
				this.serverSocket.close();
			}
			catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	// Set method for variable 'score'
	public static void setScore(int score) {
		Server.score = score;
	}
	// Get method for variable 'score'
	public static int getScore() {
		return Server.score;
	}
	
	// Set method for variable 'state'
	public static void setState(boolean state) {
		Server.state = state;
	}

	private void ConnectSocket() {
		try {
			this.serverSocket = this.server.accept();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	private void SendMessage(String string) {
		try {
			this.sendStream = new PrintStream(this.serverSocket.getOutputStream(), true);
			this.sendStream.print(string);
			this.sendStream.close();
			this.serverSocket.close();
			this.ConnectSocket();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}

	private String ReceiveMessage() {
		try {
			this.receiveStream = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
			String string = this.receiveStream.readLine();
			this.receiveStream.close();
			this.serverSocket.close();
			this.ConnectSocket();
			return string;
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		return "";
	}

}
	
