package lab4;

import lab4.client.GomokuClient;
import lab4.data.GomokuGameState;
import lab4.gui.GomokuGUI;

/**
 * The main class that start the program
 * 
 * @author Daniel M
 */
public class GomokuMain {

	static int port;

	/**
	 * Takes the first argument as the port but if nothing is given a standard value
	 * is given. Starts the clients, state and GUI
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			port = 4000;
		} else {
			port = Integer.parseInt(args[0]);
		}
		GomokuClient client = new GomokuClient(port);
		GomokuGameState state = new GomokuGameState(client);
		GomokuGUI gui = new GomokuGUI(state, client);

		GomokuClient client2 = new GomokuClient(5000);
		GomokuGameState state2 = new GomokuGameState(client2);
		GomokuGUI gui2 = new GomokuGUI(state2, client2);
	}
}
