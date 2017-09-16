package graph;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.Config;

import color.ColorGenerator;

import clock.Updatable;
import state.State;

/**
 * Contains all of the methods common to any 2D based graph of Cells. Contains
 * methods for updating each cells state and iterating through each state.
 * 
 * @author Brian Nakayama
 * @see graph.Cell
 */
public abstract class GraphMap implements Updatable, Iterable<Cell> {

	/**
	 * The cells belonging to this map. Each are notified using an Observable
	 * pattern.
	 */
	private Cell[][] cells;
	/**
	 * The last cell that food was present in.
	 * 
	 * @see state.Food
	 */
	private Cell lastFoodCell;
	/**
	 * The current x position of the cell the mouse is over.
	 */
	private int mouseX;
	/**
	 * The current y position of the cell the mouse is over.
	 */
	private int mouseY;
	/**
	 * Picks colors for cells.
	 */
	private ColorGenerator gen;
	/**
	 * True iff food should be randomly generated (for a traditional snake game)
	 */
	private boolean createFood = true;
	/**
	 * The number generator used for selecting coloring cells.
	 * @see #initialize()
	 * @see color.ColorGenerator
	 */
	private Random r = Config.RANDOM;
	/**
	 * The class for the Food State (to be implemented later).
	 */
	private Class<? extends State> food = Config.STATES.get('F');
	/**
	 * The class for the Crab State (to be implemented later).
	 */
	private Class<? extends State> crab = Config.STATES.get('D');
	/**
	 * The distance (in pixels) between cells.
	 */
	private int distance = Config.GRAPH_DISTANCE;


	/**
	 * Sets the color generator to the default. Also checks if the Food class
	 * exists.
	 * 
	 * @see state.Food
	 */
	public GraphMap() {
		try {
			gen = Config.DEFAULT_COLOR_GEN.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(
					"Unable to create a default color generator.");
		}
		Class<? extends State> food = Config.STATES.get('F');
		if (food == null) {
			createFood = false;
		}

	}

	/**
	 * Fills this map with cells, and initializes each cell based off of the
	 * child implementation of the graph. Below is an example descriptor.
	 * 
	 * <pre>
	 * "-----------###-----------"
	 * "--####-------------####--"
	 * "--#-------------------#--"
	 * "--#--####---#---####--#--"
	 * "--#---------#---------#--"
	 * "--#---------#---------#--"
	 * "------------#------------"
	 * "---------#######---------"
	 * "-------------------------"
	 * "------------S------------"
	 * "-------------------------"
	 * "---------#######---------"
	 * "------------#------------"
	 * "--#---------#---------#--"
	 * "--#---------#---------#--"
	 * "--#--####---#---####--#--"
	 * "--#-------------------#--"
	 * "--####-------------####--"
	 * "-----------###-----------"
	 * </pre>
	 * 
	 * @param descriptor
	 *            an array of strings representing a 2d grid of characters.
	 */
	public void initialize(String[] descriptor) {
		List<Cell[]> cellList = new ArrayList<Cell[]>();
		int y = 0;
		for (String line : descriptor) {
			Cell[] cellRow = new Cell[line.length()];
			for (int x = 0; x < cellRow.length; x++) {
				cellRow[x] = new Cell(gen.createColor(), createPolygon(x, y));
				char c = line.charAt(x);
				Class<? extends State> clazz = Config.STATES.get(c);
				try {
					if (clazz != null) {
						cellRow[x].setState(clazz.newInstance());
					}
				} catch (Exception e) {
					System.out.println("Unable to create an instance for "
							+ clazz.getName());
				}

			}
			y++;
			cellList.add(cellRow);
		}
		cells = cellList.toArray(new Cell[0][]);
		for (y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setNeighbors(createNeighbors(x, y));
			}
		}
		lastFoodCell = cells[0][0];
	}

	/**
	 * Initialize this map using the default configuration.
	 * 
	 * @see main.Config
	 */
	public void initialize() {
		cells = new Cell[Config.GRAPH_DEFAULT_HEIGHT][Config.GRAPH_DEFAULT_WIDTH];
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x] = new Cell(gen.createColor(), createPolygon(x, y));
			}
		}

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].setNeighbors(createNeighbors(x, y));
			}
		}

		Class<? extends State> snake = Config.STATES.get('S');
		try {
			cells[cells[0].length / 2][cells.length / 2].setState(snake
					.newInstance());
		} catch (Exception e) {
			System.out.println("Unable to create a snake for the default map.");
		}
		lastFoodCell = cells[0][0];
	}

	/**
	 * Set a color generator for initializing maps.
	 * 
	 * @param gen
	 *            A color generator
	 */
	public void setColorGenerator(ColorGenerator gen) {
		this.gen = gen;
	}

	/**
	 * Set the distance between cells in pixels.
	 * 
	 * @param distance
	 *            The distance in pixels
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Set whether or not this graph should randomly give food states to cells.
	 * 
	 * @param createFood
	 *            True iff the graph should randomly generate food
	 */
	public void setCreateFood(boolean createFood) {
		this.createFood = createFood;
	}

	/**
	 * Clear all distances from the mouse.
	 */
	public void clearAllDistances() {
		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].clearDistance();
			}
		}
	}

	/**
	 * Select the closest cell to the x and y pixel coordinates
	 * 
	 * @param x
	 *            A coordinate in pixels
	 * @param y
	 *            A coordinate in pixels
	 * @return (Approximately) The closest cell to that point
	 */
	public Cell selectClosestCell(int x, int y) {
		Point p = selectClosestIndex(x, y);

		p.y = Math.max(p.y, 0);
		p.y = Math.min(p.y, cells.length - 1);

		p.x = Math.max(p.x, 0);
		p.x = Math.min(p.x, cells[p.y].length - 1);

		return cells[p.y][p.x];
	}

	/**
	 * Update the x and y coordinate of the mouse in pixels
	 * 
	 * @param x
	 *            The coordinate in pixels
	 * @param y
	 *            The coordinate in pixels
	 */
	public void updateMouse(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
	}

	/**
	 * Randomly create food, and attempt to create a Dungeoness Crab in the map.
	 * @see state.Food
	 * @see state.DungeonessCrab
	 */
	public void createFood() {
		boolean added = false;
		do {

			Cell randomCell = cells[r.nextInt(cells.length)][r
					.nextInt(cells[0].length)];
			if (randomCell.getState() == null) {
				added = true;
				try {
					randomCell.setState(food.newInstance());
					lastFoodCell = randomCell;
				} catch (Exception e) {
					return;
				}

				randomCell = cells[r.nextInt(cells.length)][r
						.nextInt(cells[0].length)];
				if (randomCell.getState() == null) {
					try {
						randomCell.setState(crab.newInstance());
					} catch (Exception e) {
						return;
					}
				}
			}
		} while (!added);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see clock.Updatable#update()
	 */
	@Override
	public void update() {
		selectClosestCell(mouseX, mouseY).updateMouseDistance();

		for (int y = 0; y < cells.length; y++) {
			for (int x = 0; x < cells[y].length; x++) {
				cells[y][x].update();
			}
		}

		if (createFood && !food.isInstance(lastFoodCell.getState())) {
			createFood();
		}
	}

	/**
	 * Get the distance in pixels between cells.
	 * @return The distance between cell in pixels
	 */
	protected int getDistance() {
		return distance;
	}
	
	/**
	 * 
	 * @return
	 */
	protected Cell[][] getCells() {
		return cells;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Cell> iterator() {
		return new GraphMapIterator(this);
	}

	/**
	 * Gets the width in pixels for rendering.
	 * @return The width in pixels
	 */
	public abstract int getPixelWidth();

	/**
	 * Gets the height in pixels for rendering.
	 * @return The height in pixels
	 */
	public abstract int getPixelHeight();

	/**
	 * Create an array of neighbors for the cell at index (x, y).
	 * @param x The x index of a Cell
	 * @param y The y index of a Cell
	 * @return An array containing adjacent cells
	 */
	public abstract Cell[] createNeighbors(int x, int y);

	/**
	 * Get the closest (x, y) index for a pixel (x, y) coordinate.
	 * @param x The x coordinate in pixels
	 * @param y The y coordinate in pixels
	 * @return The closest (x, y) index
	 */
	protected abstract Point selectClosestIndex(int x, int y);

	/**
	 * Create a polygon for the cell at index (x, y). 
	 * @param x The x index of a Cell
	 * @param y The y index of a Cell
	 * @return A polygon positioned correctly for rendering
	 */
	public abstract Polygon createPolygon(int x, int y);
}
