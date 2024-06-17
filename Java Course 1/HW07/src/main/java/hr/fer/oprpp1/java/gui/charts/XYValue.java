package hr.fer.oprpp1.java.gui.charts;

/**
 * Class xy value represents one coordinate in graph with it's x and y value
 * @author anace
 *
 */
public class XYValue {
	/**
	 * x value
	 */
	private int x;
	
	/**
	 * y value
	 */
	private int y;
		
	/**
	 * Constructor wich accepts values for x and y
	 * @param x
	 * @param y
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns value of x
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns value of y
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	
}
