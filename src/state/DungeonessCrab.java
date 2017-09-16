package state;

import graph.Cell;

/**
 * Randomly moves around according to the cycle of food. A "C" in a map file.
 * 
 * @author Brian Nakayama
 * 
 */
public class DungeonessCrab extends Food {

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.Food#handle(graph.Cell)
	 */
	@Override
	public void handle(Cell cell) {
		super.handle(cell);
		if (getCounter() == 0) {
			Cell next = cell.getRandomOpen();
			if (next != null && next.getState() == null) {
				cell.moveState(next);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see state.Food#toChar()
	 */
	@Override
	public char toChar() {
		return 'D';
	}

}
