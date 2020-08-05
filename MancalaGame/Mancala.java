package MancalaGame;

/****************************************************************************
 * @author Ava Guo
 * Date: June 4, 2017
 * 
 * Mancala Class
 *
 * Purpose: This class implements the rules of Mancala game (see individual 
 * methods for different game rules implemented)
 * 
 * Methods:
 * Mancala() - constructor of this class
 * reset() - returns void
 * switchPlayers() - returns void
 * findPitNum(int, int) - returns int
 * getNumOfMarbles(int, int) - returns int
 * findNewPit(int, int) - returns int
 * emptyPit(int) - returns boolean
 * checkMove(int, int) - returns MoveType
 * isStore(int) - returns boolean
 * checkResult(int) - returns MoveResult
 * applyMove(int, int) - returns MoveResult
 * checkGameOver() - returns GameStatus
 * getCurrentPlayer() - returns String
 * 
 ****************************************************************************/

public class Mancala {
	private int currentPlayer;  // store the current player
	
	/**
	 * all possible game statuses
	 */
	public static enum GameStatus {
		PLAYER1WON, PLAYER2WON, TIED_GAME, IN_PROGRESS
	}
	
	private GameStatus gameOver;
	
	public static final int N_PLAYERS = 2;  // number of players
	public static final int N_PITS = 7;  // number of pits per player (including store)
	public static final int INIT_MARBLES = 4;  // initial number of marbles per pit
	
	public static final int BOARD_SIZE = N_PLAYERS * N_PITS; 
	public static final int STORE1 = 0;  // index of board pit that refers to player 1's store
	public static final int STORE2 = STORE1 + N_PITS;  // index of board pit that refers to player 2's store
	
	private int[] shadowBoard;  // do the actual operation for the game
	
	/**
	 * all types of movements
	 */
	public static enum MoveType {
		SUCCESS, EMPTY_PIT, WRONG_PLAYER
	}
	
	/**
	 * results of movements
	 */
	public static enum MoveResult {
		NORMAL, CAPTURE, FREE_TURN
	}
	
	// constructor
	public Mancala() {
		shadowBoard = new int[BOARD_SIZE];  // initialize shadow board
		reset();
	}
	
	/**
	 * Sets variable to default state
	 */
	public void reset() {
		gameOver = GameStatus.IN_PROGRESS;  // set gameOver to "in progress" (game is not over yet)
		currentPlayer = 1;
		
		// initialize the shadow board
		for (int i = 0; i < BOARD_SIZE; i++)
			shadowBoard[i] = INIT_MARBLES;
		shadowBoard[STORE1] = shadowBoard[STORE2] = 0;
	}
	
	/**
	 * Switch between players, return current player
	 */
	private void switchPlayers() {
		currentPlayer = (currentPlayer == 1) ? 2 : 1;
	}
	
	/**
	 * Find the pit number (the index number of the shadow board)
	 */
	private int findPitNum(int row, int col) {
		return row == 0 ? col : (BOARD_SIZE - 1 - col); // If row is zero, column is the same as row; otherwise, it's not.
	}
	
	/**
	 * Returns the value currently stored at (row, col)
	 */
	public int getNumOfMarbles(int row, int col) {
		int pitNum = findPitNum(row, col);
		return shadowBoard[pitNum];
	}
	
	/**
	 * Find the correct pit to place the marble
	 */
	private int findNewPit(int pitNum, int distance) {
		int newPit = pitNum - distance;
		while (newPit < 0)
			newPit += BOARD_SIZE;
		return newPit;
	}
	
	/**
	 * Returns whether this pit has no marbles
	 */
	private boolean emptyPit(int pitNum) {
		return (shadowBoard[pitNum] == 0);
	}
	
	/**
	 * Check for invalid move
	 */
	public MoveType checkMove(int row, int col) {
		int pitNum = findPitNum(row, col); 
		
		// Indicate player 1's and 2's bounds
		boolean inBounds1 = (pitNum > STORE1 && pitNum < STORE1+N_PITS);
		boolean inBounds2 = (pitNum > STORE2 && pitNum < STORE2+N_PITS);
		
		/* If the current player is correct while the move is in the current player's bounds,
	 	and the chosen pit is not empty, move is valid */
		if (currentPlayer == 1 && inBounds1 && !emptyPit(pitNum))
        	return MoveType.SUCCESS;
		else if (currentPlayer == 2 && inBounds2 && !emptyPit(pitNum))
        	return MoveType.SUCCESS;
		
		// Invalid move (either it's empty pit or wrong player side)
		else {
			boolean inBounds = inBounds1 || inBounds2;
			if (inBounds && emptyPit(pitNum))
				return MoveType.EMPTY_PIT;
			else
				return MoveType.WRONG_PLAYER;
		}
	}
	
	/**
	 * Is this pit one of the player's stores?
	 */
	private boolean isStore(int pitNum) {
		return pitNum == STORE1 || pitNum == STORE2;
	}
	
	/**
	 * Check for extra turn and capture conditions
	 */
	private MoveResult checkResult(int lastPit) {
		int opposite = BOARD_SIZE - lastPit;  // The directly opposite pit
		boolean inCPBounds;  // Indicate the current player's bounds

		// If the last marble is dropped in one of the pits of the current player
		if (currentPlayer == 1)
			inCPBounds = (lastPit > STORE1 && lastPit < STORE1+N_PITS);
		else
			inCPBounds = (lastPit > STORE2 && lastPit < STORE2+N_PITS);
		
		if (isStore(lastPit)) {
			// check for extra turn
			return MoveResult.FREE_TURN;
		} else if (inCPBounds && shadowBoard[lastPit] == 1 && shadowBoard[opposite] >= 1) {  // both non-empty
			// check for capture
			
			int numOfMarbles = shadowBoard[opposite] + 1;  // store the total number of stones captured by the current player
			
			// clear pits
			shadowBoard[lastPit] = shadowBoard[opposite] = 0;
			shadowBoard[currentPlayer == 1 ? STORE1 : STORE2] += numOfMarbles;
			
			return MoveResult.CAPTURE;
		} else
			return MoveResult.NORMAL;
	}
	
	/**
	 * Apply a general move
	 */
	public MoveResult applyMove(int row, int col) {
		// Find the corresponding pit on the shadow board
		int pitNum = findPitNum(row, col);
		
		// Move stones
		int numOfMarbles = shadowBoard[pitNum];  // Number of marbles in the pit of the shadow board
		int offset = 0;  // Skip the opponent's store	
		
		// Reduce the number of marbles in the selected pit to zero
		shadowBoard[pitNum] = 0;
		
		int newPit = -1;
		for (int moveStep = 1; moveStep <= numOfMarbles; moveStep++) {
			newPit = findNewPit(pitNum, moveStep+offset);
			
			if (currentPlayer == 1) {  // if it's player 1's turn
				if (newPit == STORE2) {  // if the pit is player 2's store
					offset++;
					newPit = findNewPit(pitNum, moveStep+offset);
				}
			}
			else {  // if it's player 2's turn
				if (newPit == STORE1) {  // if the pit is player 1's store
					offset++;
					newPit = findNewPit(pitNum, moveStep+offset);
				}
			}
			
			// place one marble into the corresponding pit
			shadowBoard[newPit]++;
		}
		
		MoveResult result = checkResult(newPit);  // Move result
		if (result != MoveResult.FREE_TURN)
			switchPlayers();
		return result;
	}
	
	/**
	 * Check and store the game state (whether a player has won)
	 */
	public GameStatus checkGameOver() {
		int remainingMarbles1 = 0, remainingMarbles2 = 0;
		  
		// check whether player 1's pits are all empty
		for (int i = STORE1+1; i < STORE2; i++)
			remainingMarbles1 += shadowBoard[i];
		  
		// check whether player 2's pits are all empty
		for (int i = STORE2+1; i < BOARD_SIZE; i++)
			remainingMarbles2 += shadowBoard[i];
		
		boolean isGameOver = (remainingMarbles1 == 0 || remainingMarbles2 == 0);
		
		// clear all pits
		if (isGameOver) {
			for (int i = 0; i < BOARD_SIZE; i++)
				if (!isStore(i))  // as long as pits are not the players' stores
					shadowBoard[i] = 0;
			shadowBoard[STORE1] += remainingMarbles1;  // get all of the remained marbles
			shadowBoard[STORE2] += remainingMarbles2;
			
			if (shadowBoard[STORE1] > shadowBoard[STORE2])
				gameOver = GameStatus.PLAYER1WON;
			else if (shadowBoard[STORE2] > shadowBoard[STORE1])
				gameOver = GameStatus.PLAYER2WON;
			else
				gameOver = GameStatus.TIED_GAME;
		} else {
			gameOver = GameStatus.IN_PROGRESS;
		}

		return gameOver;
	}
	
	/**
	 * Returns the current player as a string
	 */
	public String getCurrentPlayer() {
		return String.valueOf(currentPlayer);
	}
}