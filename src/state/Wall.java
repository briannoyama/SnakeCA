package state;

import graph.Cell;

import java.awt.Color;

/**
 * An impassable state with the color White. Doesn't do anything. A "#" in the
 * map file.
 * 
 * @author Brian Nakayama
 * 
 */
public class Wall implements State {

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#handle(graph.Cell)
	 */
	@Override
	public void handle(Cell cell) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#getColor()
	 */
	@Override
	public Color getColor() {
		return Color.WHITE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#isPassable()
	 */
	@Override
	public boolean isPassable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#toChar()
	 */
	@Override
	public char toChar() {
		return '#';
	}

}
