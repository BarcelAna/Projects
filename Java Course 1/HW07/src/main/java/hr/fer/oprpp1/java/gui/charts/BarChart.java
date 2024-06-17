package hr.fer.oprpp1.java.gui.charts;

import java.util.List;

/**
 * Class BarChart represents a model of bar chart
 * @author anace
 *
 */
public class BarChart {
	/**
	 * values displayed in bar chart
	 */
	@SuppressWarnings("unused")
	public List<XYValue> values;
	
	/**
	 * title of x-axis
	 */
	@SuppressWarnings("unused")
	private String xTitle;
	
	/**
	 * title of y-axis
	 */
	@SuppressWarnings("unused")
	private String yTitle;
	
	/**
	 * minimum y value on y-axis
	 */
	@SuppressWarnings("unused")
	private int minY;
	
	/**
	 * maximum y value on y-axis
	 */
	@SuppressWarnings("unused")
	private int maxY;
	
	/**
	 * distance between two neighbour values on y-axis
	 */
	@SuppressWarnings("unused")
	private int deltaY;

	/**
	 * Constructor which sets all properties of barchart to given values
	 * @param list
	 * @param xTitle
	 * @param yTitle
	 * @param minY
	 * @param maxY
	 * @param deltaY
	 */
	public BarChart(List<XYValue> list, String xTitle, String yTitle, int minY, int maxY, int deltaY) {
		if(minY < 0) throw new IllegalArgumentException("Minimum y value must not have negative value.");
		if(maxY <= minY) throw new IllegalArgumentException("Maximum y must be greater than given minimum y");
		if((maxY - minY) % deltaY != 0) {
			while(true) {
				deltaY += 1;
				if((maxY-minY) % deltaY == 0) {
					break;
				}
			}
		}
		for(XYValue value : list) {
			if(value.getY() < minY) throw new IllegalArgumentException("No y value of list values shold be smaller than given minimum y.");
		}
		
		this.values = list;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.minY = minY;
		this.maxY = maxY;
		this.deltaY = deltaY;
	}

	/**
	 * Returns list of values
	 * @return list
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Returns title of x-axis
	 * @return title
	 */
	public String getxTitle() {
		return xTitle;
	}

	/**
	 * Returns title of y-axis
	 * @return title
	 */
	public String getyTitle() {
		return yTitle;
	}

	/**
	 * Returns minimum y value on y-axis
	 * @return minimum y
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Returns maximum y value on y-axis
	 * @return maximum y
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Returns distance between values on y-axis
	 * @return distance
	 */
	public int getDeltaY() {
		return deltaY;
	}
	
	
}
