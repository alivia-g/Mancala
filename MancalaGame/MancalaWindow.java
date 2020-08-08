package MancalaGame;

/*********************************************************************************************************
 * @author Ava Guo
 *
 * MancalaWindow class
 *
 * Purpose: This class contains the main method extends JFrame to check the functionality of Mancala.java
 * and MancalaPanel.java
 *
 * Method:
 * main - calls constructor to create a new frame for the game (also sets its location and size)
 ********************************************************************************************************/

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

public class MancalaWindow extends JFrame {
	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 4151914803872255049L;
	static final int WIDTH = 1500;
	static final int HEIGHT = 800;

	// constructor of Mancala window
	public MancalaWindow(String title) {
		// Set the title of the frame, must be before variable declarations
		super(title);

		MancalaPanel basicPanel;
		Container container;

		// Instantiate and add the basicPanel to the frame
		basicPanel = new MancalaPanel();
		basicPanel.setBackground(Color.getHSBColor(250, 200, 200));
		container = getContentPane();

		setLocationByPlatform(true);
		container.add(basicPanel);
	   	container.validate();
	}

	public static void main(String[] args) {
		// instantiate a FirstApplication object so as to display it
		MancalaWindow frame = new MancalaWindow("Mancala Game");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

	    frame.setLocation(0,0);
	    // frame.setLocationRelativeTo(null);

	    // set the size of the application window
	    frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
	}
}
