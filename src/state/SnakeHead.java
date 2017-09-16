package state;

import graph.Cell;

import java.awt.Color;


import main.Config;

/**
 * An impassable state that follows the mouse's position or moves randomly if it
 * can't move towards the mouse. If it still cannot move, the game ends. If the
 * snake can move, it replaces its current cell with a SnakeSegment before
 * moving. If it finds Food, it increments its length. Uses the default snake
 * variables. An "S" in the map file.
 * 
 * @author Brian Nakayama
 * @see state.SnakeSegment
 * @see state.Food
 */
public class SnakeHead implements State, Snake {

	/**
	 * Keeps track of cycles for changing states.
	 */
	private int timer = 0;

	/**
	 * The length of the snake. Defines how many cells trail the snake.
	 */
	private int length = 4;

	/**
	 * Gets the length of the snake.
	 * 
	 * @return The length
	 */
	public int getLength() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#handle(graph.Cell)
	 */
	@Override
	public void handle(Cell cell) {
		timer += 1;
		if (timer == Config.MAX_SNAKE_TIMER) {
			timer = 0;
			Cell next = cell.getRandomCloser();
			// Make a method instead of
			if (next == null) {
				next = cell.getRandomOpen();
				if (next == null) {
					Config.endGame(length);
				}
			}
			if (next.getState() instanceof Food) {
				length += 1;
			}
			cell.moveState(next);
			cell.setState(new SnakeSegment(this));
			cell = next;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.State#getColor()
	 */
	@Override
	public Color getColor() {
		return Config.SNAKE_COLORS[timer];
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
		return 'S';
	}

}
