package lab4.data;

import java.util.Observable;

/**
 * Represents the 2-d game grid
 * 
 * @author Daniel S
 */

public class GameGrid extends Observable {
	public static int EMPTY = 0;
	public static int ME = 1;
	public static int OTHER = 2;
	public static int INROW = 5;
	private int[][] board;

	/**
	 * Constructor
	 * 
	 * @param size The width/height of the game grid
	 */
	public GameGrid(int size) {
		board = new int[size][size];
	}

	/**
	 * Reads a location of the grid
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return the value of the specified location
	 */
	public int getLocation(int x, int y) {
		return board[y][x];
	}

	/**
	 * Returns the size of the grid
	 * 
	 * @return the grid size
	 */
	public int getSize() {
		return board.length;
	}

	/**
	 * Enters a move in the game grid
	 * 
	 * @param x      the x position
	 * @param y      the y position
	 * @param player
	 * @return true if the insertion worked, false otherwise
	 */
	public boolean move(int x, int y, int player) {
		if (board[y][x] == EMPTY) {
			board[y][x] = player;
			setChanged();
			notifyObservers();
			return true;
		}
		return false;
	}

	/**
	 * Clears the grid of pieces
	 */
	public void clearGrid() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = EMPTY;
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Checks if a player wins by rows
	 * 
	 * @return true if player wins or false if the players move is not a winner
	 */
	private boolean kollaRader(int player) {
		int count = 0;
		// Checka rader
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == player) {
					count += 1;
				}
				if (board[i][j] != player) {
					count = 0;
				}
				if (count == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if a player wins by columns
	 * 
	 * @return true if player wins or false if the players move is not a winner
	 */
	private boolean kollaKolumner(int player) {
		int count = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[j][i] == player) {
					count += 1;
				}
				if (board[j][i] != player) {
					count = 0;
				}
				if (count == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if a player wins by diagonal
	 * 
	 * @return true if player wins or false if the players move is not a winner
	 */
	private boolean kollaSnedt(int player, int x, int y) {
		int count = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;

		// Kollar diagonalen uppåt till vänster
		for (int i = 0; i < INROW; i++) {
			if (y - i >= 0 && x - i >= 0 && y - i < board.length && x - i < board.length) {
				if (board[y - i][x - i] == player) {
					count += 1;
				}
				if (board[y - i][x - i] != player) {
					count = 0;
				}
				if (count == INROW) {
					return true;
				}
			}
		}
		// Kollar diagonalen uppåt till höger
		for (int i = 0; i < INROW; i++) {
			if (y - i >= 0 && x + i >= 0 && y - i < board.length && x + i < board.length) {
				if (board[y - i][x + i] == player) {
					count2 += 1;
				}
				if (board[y - i][x + i] != player) {
					count2 = 0;
				}
				if (count2 == INROW) {
					return true;
				}
			}
		}
		// Kollar diagonalen neråt åt vänster
		for (int i = 0; i < INROW; i++) {
			if (y + i >= 0 && x - i >= 0 && y + i < board.length && x - i < board.length) {
				if (board[y + i][x - i] == player) {
					count3 += 1;
				}
				if (board[y + i][x - i] != player) {
					count3 = 0;
				}
				if (count3 == INROW) {
					return true;
				}
			}
		}
		// Diagonalen neråt åt höger
		for (int i = 0; i < INROW; i++) {
			if (y + i >= 0 && x + i >= 0 && y + i < board.length && x + i < board.length) {
				if (board[y + i][x + i] == player) {
					count4 += 1;
				}
				if (board[y + i][x + i] != player) {
					count4 = 0;
				}
				if (count4 == INROW) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if a player has 5 in row
	 * 
	 * @param player the player to check for
	 * @param x      the x coordinate of box pressed
	 * @param y      the y coordinate of box pressed
	 * @return true if player has 5 in row, false otherwise
	 */
	public boolean isWinner(int player, int x, int y) {
		if (kollaRader(player) == true) {
			return true;
		}
		if (kollaKolumner(player) == true) {
			return true;
		}
		if (kollaSnedt(player, x, y) == true) {
			return true;
		}
		return false;
	}

}
