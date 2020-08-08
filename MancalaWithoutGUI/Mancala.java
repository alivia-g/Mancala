package MancalaWithoutGUI;

import java.util.Scanner;

public class Mancala {

	private static boolean player1gameEnd, player2gameEnd;

	static final int STORE1 = 0; // index of gameBoard pit that refers to player 1's store
	static final int STORE2 = 7; // index of gameBoard pit that refers to player 2's store
	static final int BOARD_SIZE = 14;  // number of pits

	static final Scanner scanner = new Scanner(System.in);

	// constructor
	public Mancala() {
		player1gameEnd = false;
		player2gameEnd = false;
	}

  /**
   *  Mancala
   */
	public static void main(String[] args) {
		do {
		// start of game
	    int[] board = initializeBoard();
	    boolean player1IsNext = true; // If true, player 1 is the next player. Otherwise, player 2 is the next player.
	    // main game loop
	    while (!gameEnd(board)) {
	      player1IsNext = nextTurn(board, player1IsNext);
	    }

	    // end of game
	    displayFinalScore(board);
		} while (restart());
	}

	/**
	 * Return a boolean to determine if the player wants to restart the game
	 */
	public static boolean restart() {
		char answer;
		System.out.println("Restart the game? (Y/N)");
		answer = scanner.next().charAt(0);

		if (answer == 'Y' || answer == 'y')
			return true;
		else
			return false;
	}

  /**
   * Given the state of the board, and the next player to play:
   *  1. displays the current board to screen
   *  2. gets the next move from the appropriate player
   *  3. executes the move
   *  4. determines who is the next player to play
   */
  public static boolean nextTurn(int[] board, boolean player1IsNext) {
    displayBoard(board);
    int move = getMove(board, player1IsNext);
    return applyMove(board, player1IsNext, move);
  }

  /**
   * Asks the current player for their move. Checks to make sure it is
   * a valid one.
   */
  /*public static int getMove(int[] board, boolean player1IsNext) {
	int player1Lower = STORE1 + 1, player1Upper = STORE2 - 1;
	int player2Lower = STORE2 + 1, player2Upper = BOARD_SIZE - 1;
	int nextPlayer = player1IsNext ? 1 : 2;
	System.out.print("\nIt is player " + Integer.toString(nextPlayer) + "'s turn.");
    if (player1IsNext)
    	System.out.println(Integer.toString(player1Lower) + " to " + Integer.toString(player1Upper) + ":");
    else
    	System.out.println(Integer.toString(player2Lower) + " to " + Integer.toString(player2Upper) + ":");
    int move = 0;
    boolean validMove = false;

    // loop until a valid move is selected
    while (!validMove) {
      move = scanner.nextInt();
      boolean inBounds1 = (move >= player1Lower && move <= player1Upper);
      boolean inBounds2 = (move >= player2Lower && move <= player2Upper);
      if (player1IsNext && inBounds1 && board[move] > 0)
        validMove = true;
      else if (!player1IsNext && inBounds2  && board[move] > 0)
        validMove = true;
      else {
    	  boolean inBounds = inBounds1 || inBounds2;
    	  if (inBounds && checkEmpty(move, board))
        	  System.out.println("Pit is empty, try another one with marbles in it.");
          else
        	  System.out.println("Invalid move. Try again.");
      }
    }
    return move;
  }*/

  public static int getMove(int[] board, boolean player1IsNext) {
    if (player1IsNext) {
      System.out.println("\nIt is player 1's turn. Enter a house number from 1 to 6:");
    }
    else {
      System.out.println("\nIt is player 2's turn. Enter a house number from 8 to 13:");
    }
    int move = 0;
    boolean validMove = false;

    // loop until a valid move is selected
    while (!validMove) {
      move = scanner.nextInt();
      if (player1IsNext && move >= 1 && move <= 6 && board[move] > 0) {
        validMove = true;
      }
      else if (!player1IsNext && move >= 8 && move <= 13  && board[move] > 0) {
        validMove = true;
      }
      else {
        System.out.println("Invalid move. Try again.");
      }
    }
    return move;
  }

  /**
   * Check if the pit is empty and return a boolean value
   */
/*  private static boolean checkEmpty(int move, int [] board) {
	  if (board[move] == 0)
		  return true;
	  return false;
  }*/

  /**
   * Print the game board to screen.
   */
  public static void displayBoard(int[] board) {
    System.out.println("\t\tMancala Game");
    // Player 1's houses
    System.out.print("    ");
    for (int i = 1; i < 7; i++) {
      System.out.printf("%2d:%2d  ", i, board[i]);
    }

    System.out.println();
    System.out.printf("Store 1:%2d                            ", board[STORE1]);  // "%2d" decimal format (2 indicates space 2)
    System.out.printf("Store 2:%2d\n", board[STORE2]);

    // Player 2's houses
    System.out.print("    ");
    for (int i = 13; i > 7; i--) {
      System.out.printf("%2d:%2d  ", i, board[i]);
    }
    System.out.println();
  }

  /**
   * Return an int array representing the game board.
   */
  public static int[] initializeBoard() {
	  int[] board = new int[BOARD_SIZE];
	  for (int i = 0; i < BOARD_SIZE; i++)
		  board[i] = 4;
	  board[STORE1] = board[STORE2] = 0;

    return board;
  }

  /**
   * Find the correct pit
   */
  private static int newMove(int move, int distance) {
	  int new_move = move - distance;
	  while (new_move < 0)
		  new_move += BOARD_SIZE;
	  return new_move;
  }

  /**
   * Given the board, the current player, and the house that is selected,
   * move the stones, applying the special rules for capture and extra turns.
   *
   * Returns a boolean, which is:
   *   true - if player 1 is the next player
   *   false - if player 2 is the next player
   */
  public static boolean applyMove(int[] board, boolean player1IsNext, int move) {
    // move stones
	int numOfStones = board[move];
	int offset = 0;  // ignore the opponent's store

	board[move] = 0;  // reduce the number of marbles to zero

	for (int i = 1; i <= numOfStones; i++) {
		int new_move = newMove(move, i+offset);

		if (player1IsNext) {  // if it's player 1's turn
			if (new_move == STORE2) {  // if the pit is player 2's store
				offset++;
				new_move = newMove(move, i+offset);
			}
		}
		else {  // if it's player 2's turn
			if (new_move == STORE1) {  // if the pit is player 1's store
				offset++;
				new_move = newMove(move, i+offset);
			}
		}
		board[new_move]++;  // place one marble into the corresponding pit
	}

    // check if capture
	int last_move = newMove(move, numOfStones+offset);
	int opposite = BOARD_SIZE - last_move;
	boolean is_store = (last_move == STORE1 || last_move == STORE2);
	if (is_store) {
		// check for extra turn
		player1IsNext = (last_move == STORE1);
    	System.out.println("\nFree turn for player " + (player1IsNext ? "1" : "2") + "!");
		nextTurn(board, player1IsNext);
	} else if (board[last_move] == 1 && board[opposite] >= 1) {
		numOfStones = board[opposite] + 1;  // store the total number of stones captured by the current player

		// set pits empty
		board[last_move] = board[opposite] = 0;

		board[player1IsNext ? STORE1 : STORE2] += numOfStones;
	}

    return !player1IsNext;
  }

  /**
   * Given a game board, returns true if one side's houses has no seeds (the game is over.)
   * Otherwise, return false.
   */
  public static boolean gameEnd(int[] board) {
	  boolean allEmpty1 = true, allEmpty2 = true;

	  // check player 1's pits
	  for (int i = STORE1+1; i < STORE2; i++)
		  allEmpty1 &= (board[i] == 0);
	  setPlayer1gameEnd(allEmpty1);

	  // check player 2's pits
	  for (int i = STORE2+1; i < BOARD_SIZE; i++)
		  allEmpty2 &= (board[i] == 0);
	  setPlayer2gameEnd(allEmpty2);

	  // check if the pits of any one of the players are empty, game ends
	  return player1gameEnd || player2gameEnd;
  }

  /**
   * Given the game board at the end of the game, determines the winner and prints it out.
   */
  	public static void displayFinalScore(int[] board) {
		  System.out.println("Game Over!");
		  int stones_remained = 0;

		  // sum the number of remained stones on player 1's side
		  for (int i = STORE1+1; i < STORE2; i++) {
			  stones_remained += board[i];
		  }
		  // sum the number of remained stones on player 2's side
		  for (int i = STORE2+1; i < BOARD_SIZE; i++) {
			  stones_remained += board[i];
		  }

		  // distribute the remained stones to the player who still has some stones
		  if (isPlayer1gameEnd())  // if player 1's pits are all emptied
			  board[STORE2] = stones_remained;
		  else  // if player 2's pits are all emptied
			  board[STORE1] = stones_remained;

		  // calculate score
		  System.out.println("\nPlayer 1 Score: " + board[STORE1] + "\nPlayer 2 Score: " + board[STORE2]);

		  if (board[STORE1] > board[STORE2])  // the winner is player 1
			  System.out.println("\nPlayer 1 Win!!!");
		  else
			  System.out.println("\nPlayer 2 Win!!!");

		  displayBoard(board);
  	}

  	// getters and setters of the private fields
	public static boolean isPlayer1gameEnd() {
		return player1gameEnd;
	}

	public static void setPlayer1gameEnd(boolean player1GameEnd) {
		player1gameEnd = player1GameEnd;
	}

	public static boolean isPlayer2gameEnd() {
		return player2gameEnd;
	}

	public static void setPlayer2gameEnd(boolean player2GameEnd) {
		player2gameEnd = player2GameEnd;
	}
}
