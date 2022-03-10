package lab4.gui;

import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import lab4.client.GomokuClient;
import lab4.data.GameGrid;
import lab4.data.GomokuGameState;
import lab4.gui.ConnectionWindow;
import lab4.gui.GamePanel;

/**
 * The GUI class
 * 
 * @author Daniel S
 */

public class GomokuGUI implements Observer {

	private GomokuClient client;
	private GomokuGameState gamestate;
	private GamePanel gameGridPanel;
	private JButton connectButton;
	private JButton newGameButton;
	private JButton disconnectButton;
	private JLabel messageLabel;

	/**
	 * The constructor that makes the window and add buttons and grid of the game
	 * 
	 * @param g The game state that the GUI will visualize
	 * @param c The client that is responsible for the communication
	 */
	public GomokuGUI(GomokuGameState g, GomokuClient c) {
		this.client = c;
		this.gamestate = g;
		client.addObserver(this);
		gamestate.addObserver(this);

		// Skapar JFrame
		JFrame frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Skapar JPanel, den som visar brädet
		gameGridPanel = new GamePanel(gamestate.getGameGrid());
		JPanel panel = new JPanel();
		Dimension d = new Dimension(gameGridPanel.getDim() + 100, gameGridPanel.getDim() + 100);

		connectButton = new JButton("Connect");
		newGameButton = new JButton("New Game");
		disconnectButton = new JButton("Disconnect");
		messageLabel = new JLabel(gamestate.getMessageString());

		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		Box box3 = Box.createHorizontalBox();

		box1.add(gameGridPanel);
		box2.add(connectButton);
		box2.add(Box.createRigidArea(new Dimension(10, 0)));
		box2.add(newGameButton);
		box2.add(Box.createRigidArea(new Dimension(10, 0)));
		box2.add(disconnectButton);
		box3.add(messageLabel);

		panel.add(box1);
		panel.add(box2);
		panel.add(Box.createRigidArea(new Dimension(gameGridPanel.getDim() + 100, 0)));
		panel.add(box3);

		frame.setPreferredSize(d);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);

		gameGridPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int[] square = gameGridPanel.getGridPosition(e.getX(), e.getY());
				gamestate.move(square[1], square[0]);

			}
		});

		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionWindow connectionWindow = new ConnectionWindow(client);
			}
		});

		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.newGame();
			}
		});

		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamestate.disconnect();
			}
		});
	}

	public void update(Observable arg0, Object arg1) {

		// Update the buttons if the connection status has changed
		if (arg0 == client) {
			if (client.getConnectionStatus() == GomokuClient.UNCONNECTED) {
				connectButton.setEnabled(true);
				newGameButton.setEnabled(false);
				disconnectButton.setEnabled(false);
			} else {
				connectButton.setEnabled(false);
				newGameButton.setEnabled(true);
				disconnectButton.setEnabled(true);
			}
		}

		// Update the status text if the gamestate has changed
		if (arg0 == gamestate) {
			messageLabel.setText(gamestate.getMessageString());
		}

	}

}