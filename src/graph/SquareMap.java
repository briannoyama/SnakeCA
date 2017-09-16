package graph;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * A square lattice where each cell has 4 neighbors set up in a checker board pattern.
 * @author Brian Nakayama
 *
 */
public class SquareMap extends GraphMap{

	@Override
	public int getPixelWidth() {
		return (getCells()[0].length+1) * getDistance();
	}

	@Override
	public int getPixelHeight() {
		return (getCells().length+1) * getDistance();
	}

	@Override
	public Cell[] createNeighbors(int x, int y) {
		
		Cell[][] map = getCells();
		
		List<Cell> cells = new ArrayList<Cell>();
		if (x - 1 >= 0){
			cells.add(map[y][x - 1]);
		}
		
		if (x + 1 < map[y].length){
			cells.add(map[y][x + 1]);
		}
		
		if (y - 1 >= 0){
			cells.add(map[y-1][x]);
		}
		
		if (y + 1 < map.length){
			cells.add(map[y + 1][x]);
		}
		
		return cells.toArray(new Cell[0]);
	}

	@Override
	protected Point selectClosestIndex(int x, int y) {
		x -= getDistance()/2;
		y -= getDistance()/2;
		return new Point(x/getDistance(), y/getDistance());
	}

	@Override
	public Polygon createPolygon(int x, int y) {
		int xOffset = x * getDistance() + getDistance()/2;
		int yOffset = y * getDistance() + getDistance()/2;

		int[] xCoor = {xOffset, xOffset, xOffset + getDistance(), xOffset + getDistance()};
		int[] yCoor = {yOffset, yOffset + getDistance(), yOffset + getDistance(), yOffset};
		return new Polygon(xCoor, yCoor, 4);
	}

}
