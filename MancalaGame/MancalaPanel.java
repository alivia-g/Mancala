package MancalaGame;

/****************************************************************************
 * @author Ava Guo
 *
 * MancalaPanel Class
 *
 * Purpose: This class extends JPanel Class and implements Action Listener and Mouse Listener
 * interface. The purpose of this class is to add various components onto the panel (i.e. various
 * buttons and labels), set size, color and various locations of the components and make them
 * react to the actions performed by the user (i.e. mouse clicked, pressed, released, etc.)
 *
 * Methods:
 * MancalaPanel() - constructor of the class
 * labelAllPits() - returns void
 * mouseClicked(MouseEvent) - returns void
 * mouseReleased(MouseEvent) - return void
 * mouseEntered(MouseEvent) - return void
 * mouseExited(MouseEvent) - return void
 * mousePressed(MouseEvent) - return void
 * setListeners(boolean) - return void
 * actionPerformed(ActionEvent) - returns void - event handler for the buttons
 * resetGame() - returns void
 *
 ****************************************************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MancalaGame.Mancala.MoveResult;

public class MancalaPanel extends JPanel implements ActionListener,MouseListener{

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 4114089239288622943L;

	private Mancala mancala;
	private JButton[][] board;  // game board (buttons)
	private JButton 	exitButton, resetButton;
	private JLabel 		playerLabel, statusLabel, titleLabel, scoreLabel1, scoreLabel2, infoLabel1, infoLabel2;

	private final static String OK_STATUS = "Please make a move.";  // the initial game status

	private final static int BUTTON_WIDTH = 110, BUTTON_HEIGHT = 110;
	private final static int STORE_WIDTH = 110, STORE_HEIGHT = 230;

	private final static Color FOREGROUND_JLABEL = Color.WHITE, BACKGROUND_JLABEL = Color.MAGENTA;
	private final static Color PLAYER_1_COLOR = Color.RED, PLAYER_2_COLOR = Color.BLUE;

	// constructor
	public MancalaPanel() {
		mancala	= new Mancala();

		// set the layout for the panel to not have a layout manager
		setLayout(null);

		// Create and add a title JLabel
		titleLabel = new JLabel("Welcome to Mancala");
		titleLabel.setBounds(520, -170, 500, 450);
		titleLabel.setFont(new Font("Blackmoor LET", Font.BOLD, 35));
		titleLabel.setForeground(FOREGROUND_JLABEL);
		add(titleLabel);

		// Create and add a player JLabel
		//playerLabel = new JLabel("Player 1's turn");
		playerLabel = new JLabel("Player " + mancala.getCurrentPlayer() + "'s turn");
		playerLabel.setBounds(600, 450, 240, 45);
		playerLabel.setFont(new Font("Blackmoor LET", Font.BOLD, 25));
		playerLabel.setForeground(FOREGROUND_JLABEL);
		add(playerLabel);

		// Create and add a status JLabel
		statusLabel = new JLabel(OK_STATUS);
		statusLabel.setBounds(570, 500, 400, 50);
		statusLabel.setFont(new Font("Times New Roman", Font.ITALIC, 28));
		statusLabel.setForeground(FOREGROUND_JLABEL);
		add(statusLabel); // Add the label to this Frame

		// Create and add the information JLabels
		infoLabel1 = new JLabel("Player 1 = RED");
		infoLabel2 = new JLabel("Player 2 = BLUE");
		infoLabel1.setBounds(1020, -170, 500, 500);
		infoLabel2.setBounds(1020, -140, 500, 500);
		infoLabel1.setFont(new Font("Times New Roman", Font.ITALIC, 25));
		infoLabel2.setFont(new Font("Times New Roman", Font.ITALIC, 25));
		infoLabel1.setForeground(FOREGROUND_JLABEL);
		infoLabel2.setForeground(FOREGROUND_JLABEL);
		add(infoLabel1);
		add(infoLabel2);

		// Create score Labels
		scoreLabel1 = new JLabel("Player 1 Score: " + mancala.getNumOfMarbles(0, 0));
		scoreLabel2 = new JLabel("Player 2 Score: " + mancala.getNumOfMarbles(1, 6));
		scoreLabel1.setBounds(1000, 510, 240, 45);  // 150, 550
		scoreLabel2.setBounds(1000, 550, 240, 45);
		scoreLabel1.setFont(new Font("Blackmoor LET", Font.BOLD, 25));
		scoreLabel2.setFont(new Font("Blackmoor LET", Font.BOLD, 25));
		scoreLabel1.setForeground(FOREGROUND_JLABEL);
		scoreLabel2.setForeground(FOREGROUND_JLABEL);
		add(scoreLabel1);
		add(scoreLabel2);

		// Create JButtons
		board = new JButton[Mancala.N_PLAYERS][Mancala.STORE2];
		for (int row = 0; row < Mancala.N_PLAYERS; row++) {
			for (int column = Mancala.STORE1; column < Mancala.STORE2; column++) {
				board[row][column] = new JButton("");
				board[row][column].setFont(new Font("Arial", Font.PLAIN, 40));
				board[row][column].setBackground(new Color(255, 255, 255));
				board[row][column].setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

				// add border to the buttons
				if (row == 0)
					board[row][column].setForeground(PLAYER_1_COLOR);
				else
					board[row][column].setForeground(PLAYER_2_COLOR);

				add(board[row][column]);

				//Set all buttons to work with the event handlers
				board[row][column].addActionListener(this);
				board[row][column].addMouseListener(this);
			}
		}

		// set the size of the two stores on each side of the board
		board[0][0].setSize(STORE_WIDTH, STORE_HEIGHT);
		board[1][6].setSize(STORE_WIDTH, STORE_HEIGHT);

		// set various locations of the buttons
		for (int column = 0; column < Mancala.N_PITS; column++)
			board[0][column].setLocation(200 + column*120, 170);

		for (int column = 0; column < Mancala.N_PITS; column++)
			board[1][column].setLocation(320 + column*120, 290);
		board[1][6].setLocation(1040, 170);

		//Add exit button
		exitButton = new JButton("Exit");
		exitButton.setFont(new Font("Arial", Font.PLAIN, 30));
		exitButton.setBackground(new Color(255,255,255));
		exitButton.setForeground(FOREGROUND_JLABEL);
		exitButton.setBackground(BACKGROUND_JLABEL);
		exitButton.setLocation(680, 600);
		exitButton.setSize(200,50);
		add(exitButton);

		//Add reset button to allow for a new game
		resetButton = new JButton("New Game");
		resetButton.setFont(new Font("Arial", Font.PLAIN, 30));
		resetButton.setBackground(new Color(255,255,255));
		resetButton.setForeground(FOREGROUND_JLABEL);
		resetButton.setBackground(BACKGROUND_JLABEL);
		resetButton.setLocation(470, 600);
		resetButton.setSize(200,50);
		add(resetButton);

		// Add event listeners for the buttons
		exitButton.addActionListener(this);
		resetButton.addActionListener(this);
		exitButton.addMouseListener(this);
		resetButton.addMouseListener(this);

		labelAllPits();  // set texts to the game board (i.e. two dimensional buttons)

		validate();
	}

	/**
	 * Label all pits (show the number of marbles in each pit) and update player score labels
	 */
	private void labelAllPits() {
		for (int row = 0; row < Mancala.N_PLAYERS; row++)
			for (int column = 0; column < Mancala.N_PITS; column++)
				board[row][column].setText(String.valueOf(mancala.getNumOfMarbles(row, column)));

		// update players' score labels
		scoreLabel1.setText("Player 1 Score: " + mancala.getNumOfMarbles(0, 0));
		scoreLabel2.setText("Player 2 Score: " + mancala.getNumOfMarbles(1, 6));
	}

	@Override
	/**
	 * event handler for mouse clicking
	 */
	public void mouseClicked(MouseEvent event) {
		// add button press sound effect
		String soundName = "Tiny Button Push-SoundBible.com-513260752.wav";
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		clip.start();  // here the sound starts

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent event) {

	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * event handler for mouse pressing
	 */
	public void mousePressed(MouseEvent event) {
		// Get location of mouse press
		int x = event.getXOnScreen() - super.getLocationOnScreen().x;
		int y = event.getYOnScreen() - super.getLocationOnScreen().y;

		int buttonRow, buttonCol;  // Store the location of the clicked button
		//System.out.println("y: " + y + "\nx: " + x);

		// Determine the row and column of the button
		if((event.getButton()>0) && (y < 410) && (x < 1150)){
			if (x < 320) {
				buttonRow = 0;
				buttonCol = 0;
			}
			else if (x > 1030) {
				buttonRow = 1;
				buttonCol = 6;
			}
			else {
				buttonRow = (y-170)/(BUTTON_HEIGHT + 10);

				if (buttonRow == 0)
					buttonCol = (x-200)/(BUTTON_WIDTH + 10);
				else
					buttonCol = ((x-200)/(BUTTON_WIDTH + 10)) - 1;
			}

			// System.out.println("buttonRow: " + buttonRow + "\nbuttonCol: " + buttonCol + "\n\n");

			switch (mancala.checkMove(buttonRow, buttonCol)) {
			// if move is valid, apply move and label all pits with a new number of marbles
			case SUCCESS:
				MoveResult result = mancala.applyMove(buttonRow, buttonCol);
				labelAllPits();
				if (result == MoveResult.FREE_TURN)
					statusLabel.setText("Free turn for player " + mancala.getCurrentPlayer() + "!");
				else
					statusLabel.setText(OK_STATUS);
				break;

			// invalid move detected, inform the player
			case EMPTY_PIT:
				statusLabel.setText("Invalid Move: Empty pit");
				break;
			case WRONG_PLAYER:
				statusLabel.setText("Invalid Move: Wrong player side");
				break;
			}

			// switch players
			playerLabel.setText("Player " + mancala.getCurrentPlayer() + "'s turn");

			switch (mancala.checkGameOver()) {
			case PLAYER1WON:
				statusLabel.setText("Player 1 is the winner!");

				// disable mouse listener and action listener for the game board buttons when game over
				setListeners(false);
				break;
			case PLAYER2WON:
				statusLabel.setText("Player 2 is the winner!");

				// disable mouse listener and action listener for the game board buttons when game over
				setListeners(false);
				break;
			case TIED_GAME:
				statusLabel.setText("Tied game!");
				setListeners(false);
				break;
			case IN_PROGRESS:
				// game is in progress
				break;
			}
			labelAllPits();  // label all pits after shadow board is cleared
			repaint();
		}
		return;
	}

	/**
	 * Enable/disable listeners for new game/game over
	 */
	private void setListeners(boolean enable) {
		for (int row = 0; row < Mancala.N_PLAYERS; row++) {
			for (int column = 0; column < Mancala.N_PITS; column++) {
				if (enable) {  // enables listeners
					board[row][column].addActionListener(this);
					board[row][column].addMouseListener(this);
				} else {  // disables listeners
					board[row][column].removeActionListener(this);
					board[row][column].removeMouseListener(this);
				}
			}
		}
	}

	/**
	* This is the event handler for the button
	*/
	public void actionPerformed(ActionEvent e) {
	//Ask the event which button it represents
		if (e.getActionCommand().equals("Exit"))
			System.exit(0);

		if (e.getActionCommand().equals("New Game")){
		// Call the method to reset the game
			resetGame();
		}
		return;
	}

	/**
	 * Change everything back to the beginning
	 */
	private void resetGame() {
		mancala.reset();  // set game status to "in progress", set current player to 1, and re-initialize shadow board
		setListeners(true);
		labelAllPits();

		// reset texts on the labels
		statusLabel.setText(OK_STATUS);
		playerLabel.setText("Player " + mancala.getCurrentPlayer() + "'s turn");
	}
}
