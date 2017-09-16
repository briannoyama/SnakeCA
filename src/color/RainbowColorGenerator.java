package color;

import java.awt.Color;
import java.util.Random;

import main.Config;

/**
 * Randomly creates one of 6 dark rainbow colors: Blue, Green, Yellow, Orange,
 * Red, Purple, Blue.
 * 
 * @author Brian Nakayama
 * 
 */
public class RainbowColorGenerator implements ColorGenerator {

	/**
	 * Dark rainbow colors for this generator.
	 */
	public static final Color[] COLORS = { new Color(0.25f, 0.0f, 0.0f),
			new Color(0.25f, 0.125f, 0.0f), new Color(0.25f, 0.25f, 0.0f),
			new Color(0.0f, 0.25f, 0.0f), new Color(0.0f, 0.0f, 0.25f),
			new Color(0.25f, 0.0f, 0.25f), };

	/**
	 * The number generator for creating colors.
	 */
	private Random r = Config.RANDOM;

	/*
	 * (non-Javadoc)
	 * 
	 * @see color.ColorGenerator#getColor()
	 */
	@Override
	public Color createColor() {
		return COLORS[r.nextInt(6)];
	}
}
