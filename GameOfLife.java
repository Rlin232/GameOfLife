import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOfLife extends JFrame implements ActionListener {
	
	private int N;
	
	//Components that make up the console and input field
	private JLabel console;
	private JTextField input;
	private JPanel text;
	
	//Components that make up the options that the player can select
	private JPanel options;
	private JButton startGame;
	private JButton stopGame;
	private JButton done;
	private JButton reset;
	
	//Components that run the actual game
	private JPanel board;
	private JButton[][] buttons;
	private boolean[][] isLive;
	private boolean[][] isLiveNextTurn;
	
	public GameOfLife() {

		//Setting up the GUI
		super("Game Of Life");
		
		this.setLayout(new BorderLayout());
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		//Console & Input
		text = new JPanel(new GridLayout(2,1));
		console = new JLabel("Please Input Grid Size");
		console.setVisible(true);
		text.add(console);
		input = new JTextField(); //For getting input from user
		input.addActionListener(this);
		text.add(input);
		this.add(text, BorderLayout.NORTH);
		
		//Options Menu
		options = new JPanel(new GridLayout(1,3));
		
		startGame = new JButton("Start Game");
		startGame.setEnabled(true);
		startGame.addActionListener(this);
		startGame.setActionCommand("Start Game");
		startGame.setVisible(true);
		options.add(startGame);
		
		stopGame = new JButton("Stop Game");
		stopGame.setEnabled(true);
		stopGame.addActionListener(this);
		stopGame.setActionCommand("Stop Game");
		stopGame.setVisible(true);
		options.add(stopGame);
		
		reset = new JButton("Reset");
		reset.setEnabled(true);
		reset.addActionListener(this);
		reset.setActionCommand("Reset");
		reset.setVisible(true);
		options.add(reset);
		
		done = new JButton("Done");
		done.setEnabled(true);
		done.addActionListener(this);
		done.setActionCommand("Done");
		done.setVisible(true);
		options.add(done);
		
		this.add(options, BorderLayout.SOUTH);
	}
	
	//Creates and adds a board to the GUI
	public void createBoard(int N) {
		this.N = N;
		//System.out.println(this.N); //for debugging purposes
		board = new JPanel(new GridLayout(N, N));
		board.setVisible(true);
		buttons = new JButton[N][N];
		board.setBackground(Color.DARK_GRAY);
		isLive = new boolean[N][N];
		isLiveNextTurn = new boolean[N][N];
		for (int i = 0; i < N; i++) {    
			for (int j = 0; j < N; j++) {
				buttons[i][j] = new JButton("DEAD");
				buttons[i][j].setBackground(Color.RED);
				buttons[i][j].setOpaque(true);
				buttons[i][j].setEnabled(true);
				buttons[i][j].addActionListener(this);
				buttons[i][j].setActionCommand(i + " " + j);
				buttons[i][j].setVisible(true);
				board.add(buttons[i][j]);
				isLive[i][j] = false;
			}
		}
		this.add(board, BorderLayout.CENTER);
	}
	
	//Sets all buttons to not be enabled, and starts the game
	public void start() {
		for (int i = 0; i < N; i++) {    
			for (int j = 0; j < N; j++) {
				buttons[i][j].setEnabled(false);
			}
		}
		startGame.setEnabled(false);
		Helper.started = true;
		System.out.println(Helper.started);
	}
	
	//Resets game
	public void reset() {
		for (int i = 0; i < N; i++) {    
			for (int j = 0; j < N; j++) {
				buttons[i][j].setEnabled(true);
				buttons[i][j].setBackground(Color.RED);
				buttons[i][j].setText("DEAD");
				isLive[i][j] = false;
				isLiveNextTurn[i][j] = false;
			}
		}
		startGame.setEnabled(true);
		Helper.started = false;
	}
	
	public void run() {
		//System.out.println(Helper.started);
		if(Helper.started) {
			System.out.println("Running");

			this.nextGeneration();
			System.out.println("Next Gen");
		}
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}
	}
	//Moves the game onto the next generation
	public void nextGeneration() {
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(this.checkNeighbors(i,j) > 3 || this.checkNeighbors(i, j) < 2) {
					isLiveNextTurn[i][j] = false;
				} else if(this.checkNeighbors(i,j) == 3) {
					isLiveNextTurn[i][j] = true;
				} else if(this.checkNeighbors(i,j) == 2) {
					if(isLive[i][j]) {
						isLiveNextTurn[i][j] = true;
					}
						
				}
			}
		}
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(isLiveNextTurn[i][j]) {
					isLive[i][j] = true;
					buttons[i][j].setText("LIVE");
					buttons[i][j].setBackground(Color.GREEN);
				} else {
					isLive[i][j] = false;
					buttons[i][j].setText("DEAD");
					buttons[i][j].setBackground(Color.RED);
				}
			}
		}
	}
	
	public int checkNeighbors(int i, int j) {
		int neighborsAlive = 0;
		if(i == 0) {
			if(j == 0) { //Top-Left Corner
				if(isLive[i+1][j])
					neighborsAlive++;
				if(isLive[i][j+1])
					neighborsAlive++;
				if(isLive[i+1][j+1])
					neighborsAlive++;
			} else if(j == N - 1) { //Top-Right Corner
				if(isLive[i+1][j])
					neighborsAlive++;
				if(isLive[i][j-1])
					neighborsAlive++;
				if(isLive[i+1][j-1])
					neighborsAlive++;
			} else { //Top-Row (excluding corners)
				if(isLive[i+1][j])
					neighborsAlive++;
				if(isLive[i][j+1])
					neighborsAlive++;
				if(isLive[i+1][j+1])
					neighborsAlive++;
				if(isLive[i][j-1])
					neighborsAlive++;
				if(isLive[i+1][j-1])
					neighborsAlive++;
			}
		} else if(i == N - 1) {
			if(j == 0) { //Bottom-Left Corner
				if(isLive[i][j+1])
					neighborsAlive++;
				if(isLive[i-1][j])
					neighborsAlive++;
				if(isLive[i-1][j+1])
					neighborsAlive++;
			} else if(j == N - 1) { //Bottom-Right Corner
				if(isLive[i][j-1])
					neighborsAlive++;
				if(isLive[i-1][j])
					neighborsAlive++;
				if(isLive[i-1][j-1])
					neighborsAlive++;
			} else { //Bottom-Row (excluding corners)
				if(isLive[i][j-1])
					neighborsAlive++;
				if(isLive[i-1][j-1])
					neighborsAlive++;
				if(isLive[i][j+1])
					neighborsAlive++;
				if(isLive[i-1][j])
					neighborsAlive++;
				if(isLive[i-1][j+1])
					neighborsAlive++;
			}
		} else if(j == 0) { //Left Row (excluding corners)
			if(isLive[i][j+1])
				neighborsAlive++;
			if(isLive[i-1][j+1])
				neighborsAlive++;
			if(isLive[i+1][j])
				neighborsAlive++;
			if(isLive[i-1][j])
				neighborsAlive++;
			if(isLive[i+1][j+1])
				neighborsAlive++;
		} else if(j == N - 1) { //Right Row (excluding corners)
			if(isLive[i][j-1])
				neighborsAlive++;
			if(isLive[i-1][j-1])
				neighborsAlive++;
			if(isLive[i+1][j])
				neighborsAlive++;
			if(isLive[i-1][j])
				neighborsAlive++;
			if(isLive[i+1][j-1])
				neighborsAlive++;
		} else { //All other cases
			if(isLive[i][j+1])
				neighborsAlive++;
			if(isLive[i][j-1])
				neighborsAlive++;
			if(isLive[i+1][j])
				neighborsAlive++;
			if(isLive[i-1][j])
				neighborsAlive++;
			if(isLive[i-1][j-1])
				neighborsAlive++;
			if(isLive[i-1][j+1])
				neighborsAlive++;
			if(isLive[i+1][j-1])
				neighborsAlive++;
			if(isLive[i+1][j+1])
				neighborsAlive++;
		}
		return neighborsAlive;
	}
	
	private static class Helper extends Thread{
		static boolean started;
		public Helper() {
			super();
			started = false;
		}
		public static void setStarted(boolean start) {
			started = start;
		}
		public void run() {
			GameOfLife game = new GameOfLife();
			game.setVisible(true);
			System.out.println(started);
			while(true) {
				game.run();
			}
		}
		public Thread currentHelper() {
			return Thread.currentThread();
		}
	}
	public void actionPerformed(ActionEvent e) {

		String s = e.getActionCommand(); 
		
		try {
			console.setText("First configure your initial setup, then click the Start Game button below");
			createBoard(Integer.parseInt(s));
		} catch (Exception e1){
			if (s.equals("Start Game")) {
				this.start();
			} else if (s.equals("Stop Game")) {
				Helper.started = false;
				startGame.setEnabled(true);
			} else if(s.equals("Reset")) { 
				this.reset();
			} else if(s.equals("Done")){
				System.exit(0);
			} else {
				Scanner input = new Scanner(s);
				String rowString = input.next();
				String colString = input.next();
	
				int row = Integer.parseInt(rowString);
				int col = Integer.parseInt(colString);
	
				JButton button = buttons[row][col];
				if(!isLive[row][col]) {
					button.setText("LIVE");
					button.setBackground(Color.GREEN);
					isLive[row][col] = true;
				} else {
					button.setText("DEAD");
					button.setBackground(Color.RED);
					isLive[row][col] = false;
				}
			}
		}
	}
	public static void main(String[] args) {
		Thread t = Thread.currentThread();
		Helper listen = new Helper();
		listen.run();
		t.run();
		System.out.println("Main running");

	}
}
