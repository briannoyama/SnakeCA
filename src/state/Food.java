package state;

import graph.Cell;

import java.awt.Color;

import main.Config;

/**
 * A flashing passable state that uses the default food variables. An "F" in a
 * map file.
 * 
 * @author Brian Nakayama
 * 
 */
public class Food implements State {

	/**
	 * Keeps track of cycles for changing states.
	 */
	private int counter = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#getColor()
	 */
	@Override
	public Color getColor() {
		return Config.FOOD_COLORS[counter];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#handle(graph.Cell)
	 */
	@Override
	public void handle(Cell cell) {
		counter = (counter + 1) % Config.MAX_FOOD_TIMER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#toChar()
	 */
	@Override
	public char toChar() {
		return 'F';
	}

	/**
	 * Gets the internal count of frames that controls the intensity of the
	 * food's color.
	 * 
	 * @return The internal count
	 */
	protected int getCounter() {
		return counter;
	}

}
