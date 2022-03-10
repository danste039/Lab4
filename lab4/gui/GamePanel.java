package lab4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import lab4.data.GameGrid;

/**
 * A panel providing a graphical view of the game board
 * 
 * @author Daniel M , Gustav
 */

public class GamePanel extends JPanel implements Observer {

	private final int UNIT_SIZE = 20;
	private int dim;
	private GameGrid grid;

	/**
	 * The constructor
	 * 
	 * @param grid The grid that is to be displayed
	 */
	public GamePanel(GameGrid grid) {
		this.grid = grid;
		dim = grid.getSize() * UNIT_SIZE;
		grid.addObserver(this);
		Dimension d = new Dimension(dim + 1, dim + 1);
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Returns a grid position given pixel coordinates of the panel
	 * 
	 * @param x the x coordinates
	 * @param y the y coordinates
	 * @return an integer array containing the [x, y] grid position
	 */
	public int[] getGridPosition(int x, int y) {
		int colum = x / UNIT_SIZE;
		int row = y / UNIT_SIZE;
		int[] position = { row, colum };
		return position;
	}

	public void update(Observable arg0, Object arg1) {
		this.repaint();
	}

	/**
	 * Draws the board
	 */
	public void paintComponent(Graphics g) {
		int row;
		int colum;
		super.paintComponent(g);
		g.setColor(Color.BLACK);

		for (int i = 0; i < grid.getSize(); i++) {
			for (int j = 0; j < grid.getSize(); j++) {
				row = i * UNIT_SIZE;
				colum = j * UNIT_SIZE;
				g.drawRect(row, colum, UNIT_SIZE, UNIT_SIZE);

				if (grid.getLocation(j, i) == grid.ME) {
					g.setColor(Color.green);
					g.fillRect(colum + 1, row + 1, UNIT_SIZE, UNIT_SIZE);
					g.setColor(Color.black);
				}
				if (grid.getLocation(j, i) == grid.OTHER) {
					g.setColor(Color.red);
					g.fillRect(colum + 1, row + 1, UNIT_SIZE, UNIT_SIZE);
					g.setColor(Color.black);
				}
			}
		}

	}

	/**
	 * Takes the dimension of the board
	 * 
	 * @return dimension of the board
	 */
	public int getDim() {
		return dim;
	}

}