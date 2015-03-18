import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Client implements KeyListener {
	// Private variable
	private JFrame frame;
	private JPanel panel;
	private JLabel[] label = new JLabel[16];
	private Font numberFont;
	private Color[] color = new Color[12];
	private int score = 0;
	private String stringNumber = "";
	private String key = "";	
	private static String address = "127.0.0.1";

	// Private variable for socket
	private Socket gameSocket = null;	
	private PrintStream sendStream = null;
	private BufferedReader receiveStream = null;
    
	public static void main(String[] args) {
		System.out.print("Enter IP Address that you want to connect");
		System.out.print("(Ex: 192.168.1.69, 127.0.0.1 ...) : ");
		address = (new Scanner(System.in)).nextLine();
		Client client = new Client();
		client.RunGame();
	}

	// Constructor
	public Client() {
		this.OpenSocket();
		
		this.Initialize();
		this.Setup();
	}
	
	// Initializing method
	private void Initialize() {
		// Initializing main Frame
		this.frame = new JFrame("Game 2048");
		
		// Initializing main Panel
		this.panel = new JPanel();
		
		// Initializing label
		for (int i = 0; i < 16; i++) {
			this.label[i] = new JLabel();
		}
		
		// Initializing font for number
		this.numberFont = new Font("SanSerif", Font.CENTER_BASELINE, 25);
		
		// Call member method DifineColor();
		this.DefineColor();				
	}
	
	private void Setup() {
		// Set property for main Frame
		this.frame.setSize(400, 400);
		this.frame.setLocation(400, 100);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.getContentPane().add(this.panel, "Center");
		this.frame.addKeyListener(this);
		
		// Set layout for main Panel
		this.panel.setLayout(new GridLayout(4, 4));
		
		// Set property for labels
		for (int i = 0; i < 16; i++) {
			this.label[i].setHorizontalAlignment(JLabel.CENTER);
			this.label[i].setBorder(new LineBorder(Color.BLACK));
			this.label[i].setBackground(this.color[0]);
			this.label[i].setOpaque(true);
			this.label[i].setForeground(Color.WHITE);
			this.label[i].setFont(this.numberFont);
			this.panel.add(this.label[i]);
		}
		
	}
	
	// Define color 
	private void DefineColor() {
		// Set RGB value for colors
		color[0] = new Color(0, 199, 254);					// Color for blank cell
		color[1] = new Color(0, 150, 255);					// Color for cell has number 2 
		color[2] = new Color(0, 101, 255);					// Color for cell has number 4
		color[3] = new Color(50, 0, 255);					// Color for cell has number 8
		color[4] = new Color(101, 0, 200);					// Color for cell has number 16
		color[5] = new Color(29, 198, 12);					// Color for cell has number 32
		color[6] = new Color(200, 0, 99);					// Color for cell has number 64
		color[7] = new Color(254, 0, 0);					// Color for cell has number 128
		color[8] = new Color(200, 50, 0);					// Color for cell has number 256
		color[9] = new Color(0, 100, 0);					// Color for cell has number 512
		color[10] = new Color(232, 43, 87);					// Color for cell has number 1024
		color[11] = new Color(138, 51, 217);					// Color for cell has number 2048
	}
	
	// Display main Frame
	public void RunGame() {
		this.frame.setVisible(true);
		this.setStringNumber(this.ReceiveMessage());
	}

	// Set string number
	public void setStringNumber(String stringNumber) {
		this.stringNumber = stringNumber;
		this.setTextLabel();
	}

	// Set text for labels
	private void setTextLabel() {
		int count = 0;
		int point = 0;
		for (int index = 0; index < stringNumber.length(); index++) {
			if (stringNumber.charAt(index) == ';') {
				String tmp = stringNumber.substring(point, index);
				int iTmp = Integer.parseInt(tmp);
				if (iTmp != 0) {
					this.label[count].setText(tmp);
					this.label[count].setBackground(this.color[(int)(Math.log10(iTmp)/Math.log10(2))]);
				}
				else {
					this.label[count].setText("");
					this.label[count].setBackground(this.color[0]);
				}
				point = index + 1;
				count ++;
			}
		}
	}

	// Set method for variable 'key'
	public void setKey(String key) {
		this.key = key;
	}
	// Get method for variable 'key'
	public String getKey() {
		return this.key;
	}
	
	private void ExitIfServerClosed() {
		JOptionPane.showConfirmDialog(this.frame, "Socket has closed from Server :( ", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	private void OpenSocket() {
		try {
			this.gameSocket = new Socket(this.address, 2209);
		}
		catch (IOException io) {
			this.ExitIfServerClosed();
			io.printStackTrace();
		}
	}
	
	private void SendMessage(String string) {
		try {
			this.sendStream = new PrintStream(this.gameSocket.getOutputStream(), true);
			this.sendStream.print(string);
			this.sendStream.close();
			this.gameSocket.close();
			this.OpenSocket();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}

	private String ReceiveMessage() {
		try {
			this.receiveStream = new BufferedReader(new InputStreamReader(this.gameSocket.getInputStream()));
			String string = this.receiveStream.readLine();
			this.receiveStream.close();
			this.gameSocket.close();
			this.OpenSocket();
			return string;
		}
		catch (IOException io) {
			io.printStackTrace();
		}
		return "";
	}

	// Set method for variable 'score'
	public void setScore(int score) {
		this.score = score;
		this.frame.setTitle("Score: " + String.valueOf(this.score));
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.frame) {
			switch (e.getKeyCode()) {
				// Key for up
				case KeyEvent.VK_UP: 
					this.SendMessage("UP");
					break;
			
				// Key for down
				case KeyEvent.VK_DOWN:
					this.SendMessage("DOWN");
					break;
			
				// Key for left
				case KeyEvent.VK_LEFT:
					this.SendMessage("LEFT");
					break;
			
				// Key for right
				case KeyEvent.VK_RIGHT:
					this.SendMessage("RIGHT");
					break;
				
				// Key for exit
				case KeyEvent.VK_Q:
					this.SendMessage("EXIT");
					System.exit(0);
					break;

				// Default key
				default:
					this.SendMessage("NONE");
					break;
			}
			
			this.setStringNumber(this.ReceiveMessage());
			this.setScore(Integer.parseInt(this.ReceiveMessage()));

			String respond = this.ReceiveMessage();
		
			if (respond.equals("WIN")) {
				JOptionPane.showConfirmDialog(this.frame, "You win!!!\nYour score is " + String.valueOf(this.score), "Congratulation", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
	
			if (respond.equals("LOSE")) {
				JOptionPane.showConfirmDialog(this.frame, "You lose!!!\nYour score is " + String.valueOf(this.score), "Game Over", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
	}
}
