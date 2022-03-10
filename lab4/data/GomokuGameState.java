package lab4.data;

import java.util.Observable;
import java.util.Observer;

import lab4.client.GomokuClient;

/**
 * Represents the state of a game
 * 
 * @author Daniel M , Gustav
 */

public class GomokuGameState extends Observable implements Observer {

	// Game variables
	private final int DEFAULT_SIZE = 15;
	private GameGrid gameGrid;

	// Possible game states
	private final int NOT_STARTED = 0;
	private int currentState;
	private int MY_TURN = 1;
	private int OTHERS_TURN = 2;
	private int FINISHED = 3;

	private GomokuClient client;

	private String message;

	/**
	 * The constructor
	 * 
	 * @param gc The client used to communicate with the other player
	 */
	public GomokuGameState(GomokuClient gc) {
		client = gc;
		client.addObserver(this);
		gc.setGameState(this);
		currentState = NOT_STARTED;
		gameGrid = new GameGrid(DEFAULT_SIZE);
	}

	/**
	 * Returns the message string
	 * 
	 * @return the message string
	 */
	public String getMessageString() {
		return message;
	}

	/**
	 * Returns the game grid
	 * 
	 * @return the game grid
	 */
	public GameGrid getGameGrid() {
		return gameGrid;
	}

	/**
	 * This player makes a move at a specified location and sends messages based on
	 * what happens
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void move(int x, int y) {
		if (currentState == MY_TURN) {
			if (gameGrid.move(x, y, MY_TURN) == true) {
				client.sendMoveMessage(x, y);
				message = "Nice Move!";

				if (gameGrid.isWinner(MY_TURN, x, y)) {
					currentState = FINISHED;
					message = "You are the WINNER!";
					setChanged();
					notifyObservers();
					return;
				}
				currentState = OTHERS_TURN;
				setChanged();
				notifyObservers();
			} else if (gameGrid.move(x, y, MY_TURN) == false) {
				message = "You can't do that!";
				setChanged();
				notifyObservers();
			}
		}

		if (currentState == NOT_STARTED) {
			message = "The game isn't started!";
			setChanged();
			notifyObservers();
			return;
		}
		if (currentState == FINISHED) {
			message = "The game is over!";
			setChanged();
			notifyObservers();
			return;
		}
	}

	/**
	 * Starts a new game with the current client
	 */
	public void newGame() {
		gameGrid.clearGrid();
		currentState = OTHERS_TURN;
		message = "Game started it is your opponent's turn";
		client.sendNewGameMessage();
		setChanged();
		notifyObservers();
	}

	/**
	 * Other player has requested a new game, so the game state is changed
	 * accordingly
	 */
	public void receivedNewGame() {
		gameGrid.clearGrid();
		message = "Game started. Your turn";
		currentState = MY_TURN;
		setChanged();
		notifyObservers();
	}

	/**
	 * The connection to the other player is lost, so the game is interrupted
	 */
	public void otherGuyLeft() {
		gameGrid.clearGrid();
		message = "Your opponent has left";
		setChanged();
		notifyObservers();
	}

	/**
	 * The player disconnects from the client
	 */
	public void disconnect() {
		gameGrid.clearGrid();
		message = "Disconnected";
		setChanged();
		notifyObservers();
		client.disconnect();
	}

	/**
	 * The player receives a move from the other player
	 * 
	 * @param x The x coordinate of the move
	 * @param y The y coordinate of the move
	 */
	public void receivedMove(int x, int y) {
		gameGrid.move(x, y, OTHERS_TURN);
		if (gameGrid.isWinner(OTHERS_TURN, x, y)) {
			message = "The opponent has won. Better luck next time";
			currentState = FINISHED;
			setChanged();
			notifyObservers();
			return;
		}
		currentState = MY_TURN;
		setChanged();
		notifyObservers();
	}

	public void update(Observable o, Object arg) {

		switch (client.getConnectionStatus()) {
		case GomokuClient.CLIENT:
			message = "Game started, it is your turn!";
			currentState = MY_TURN;
			break;
		case GomokuClient.SERVER:
			message = "Game started, waiting for other player...";
			currentState = OTHERS_TURN;
			break;
		}
		setChanged();
		notifyObservers();

	}

}