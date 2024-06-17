package hr.fer.oprpp1.java.gui.layouts;

import java.awt.Component;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class CalcLayout represents custom layout manager which is used for displaying GUI of simple calculator application.
 * It implements LayoutManager2 interface.
 * @author anace
 *
 */
public class CalcLayout implements LayoutManager2 {
	/**
	 * Space between rows and columns in calc layout
	 */
	private double space;
	
	/**
	 * Mapping component to it's RCPosition
	 */
	private Map<Component, RCPosition> map;
	
	/**
	 * Defined number of rows available to layout manager
	 */
	private static final int NUM_OF_ROWS = 5;
	
	/**
	 * Defined number of columns available to layout manager
	 */
	private static final int NUM_OF_COLS = 7;
	
	/**
	 * Defined number of columns available for first component at position 1,1
	 */
	private static final int COLS_TAKEN_BY_FIRST_EL = 5;
	
	
	/**
	 * Default constructor which sets space between components to 0.
	 */
	public CalcLayout() {
		this.space = 0;
		this.map = new HashMap<>();
	}
	
	/**
	 * Constructor which accepts value of space between rows and columns in calc layout.
	 * @param space
	 */
	public CalcLayout(int space) {
		this.space = space;
		this.map = new HashMap<>();
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(constraints == null || comp == null) {
			throw new NullPointerException("Passed constraint and component must not be null.");
		}
		if(!(constraints instanceof String || constraints instanceof RCPosition)) {
			throw new IllegalArgumentException("Passed constraint must be of type String or RCPosition.");
		}
		if(constraints instanceof String) {
			try {
				constraints = RCPosition.parse((String)constraints);
			} catch(ParsingException e) {
				throw new CalcLayoutException("Given string constraint is not in valid format.");
			}
		}
		if(((RCPosition)constraints).getRow() < 1 ||((RCPosition)constraints).getRow() > 5 || ((RCPosition)constraints).getColumn() < 1 || ((RCPosition)constraints).getColumn() > 7) {
			throw new CalcLayoutException("Number of rows must be a number between 1 and 5. Number of columns must be a number between 1 and 7.");
		}
		if(map.keySet().contains(comp) || map.values().contains(constraints)) {
			throw new CalcLayoutException("Multiple components on the same position and component on multiple position is not allowed.");
		}
		if(map.keySet().size() >= 31) {
			throw new CalcLayoutException("Maximum number of components is 31.");
		}
		if(((RCPosition)constraints).getRow() == 1 && (
				((RCPosition)constraints).getColumn() == 2 ||
				((RCPosition)constraints).getColumn() == 3 ||
				((RCPosition)constraints).getColumn() == 4 ||
				((RCPosition)constraints).getColumn() == 5
		)) throw new CalcLayoutException("Adding to positions (1, 2), (1, 3), (1, 4) or (1, 5) is not allowed.");
		
		map.put(comp, (RCPosition)constraints);
		
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
		
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();	
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		map.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateSize(parent, "pref");
	}
	

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateSize(target, "max");
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateSize(parent, "min");
	}

	@Override
	public void layoutContainer(Container parent) {
		double parentWidth = parent.getWidth() - parent.getInsets().left - parent.getInsets().right;
		double parentHeight = parent.getHeight() - parent.getInsets().top - parent.getInsets().bottom;
		double cHeight = parentHeight / (double)CalcLayout.NUM_OF_ROWS;
		double cWidth = parentWidth / (double)CalcLayout.NUM_OF_COLS;
		int i = 0;
		for(Entry<Component, RCPosition> e : map.entrySet()) {
			Component c = e.getKey();
			RCPosition p = e.getValue();
			double width = i % 2 == 0 ? cWidth: cWidth + 1;
			if(p.getRow() == 1 && p.getColumn() == 1) {
				width = CalcLayout.COLS_TAKEN_BY_FIRST_EL * cWidth + (CalcLayout.COLS_TAKEN_BY_FIRST_EL-1) * space ;
			}
			double height = i % 2 == 0 ? cHeight : cHeight + 1;
			double x = parent.getInsets().left;
			double y = parent.getInsets().top;
			if(p.getRow() != 1) y = cHeight * (p.getRow()-1) + space * (p.getRow()-1) + parent.getInsets().top;
			if(p.getColumn() != 1) x = cWidth * (p.getColumn() - 1) + space * (p.getColumn()-1) + parent.getInsets().left;
			if(p.getColumn() == CalcLayout.NUM_OF_COLS) {
				width = parentWidth - x + parent.getInsets().left;
			}
			if(p.getRow() == CalcLayout.NUM_OF_ROWS) {
				height = parentHeight  - y + parent.getInsets().top;
			}
			c.setBounds((int)x, (int)y, (int)width, (int)height);
			++i;
		}
	}
	
	/**
	 * Utility method for calculating preferred, maximum or minimum size of container.
	 * @param parent component - container
	 * @param type - string identifier of size to be calculated. "pref" for preferred size, "min" for minimum size and "max" for maximum size.
	 * @return Dimension object
	 */
	private Dimension calculateSize(Container parent, String type) {	
		Dimension firstElementDim = null;
		Dimension maxDim = null;
		boolean containsFirstElement = false;
		
		if(type.equals("pref")) {
			firstElementDim = checkForFirstElement("pref");
			if(firstElementDim != null) containsFirstElement = true;
			maxDim = findComponentDim("pref", containsFirstElement);
		}else if(type.equals("min")) {
			firstElementDim = checkForFirstElement("min");
			if(firstElementDim != null) containsFirstElement = true;
			maxDim = findComponentDim("min", containsFirstElement);
		}else if(type.equals("max")) {
			firstElementDim = checkForFirstElement("max");
			if(firstElementDim != null) containsFirstElement = true;
			maxDim = findComponentDim("max", containsFirstElement);
		}
		return calculateContainerDimension(maxDim.width, maxDim.height, containsFirstElement, firstElementDim == null ? 0 : firstElementDim.width, parent);
	}
	
	/**
	 * Utility method for calculating maximum preferred size of container's components, maximum minimum size of container's components or minimum maximum size of container's components depending on given type value
	 * @param type - can be "pref", "max" or "min"
	 * @param containsFirstElement
	 * @return calculated dimension
	 */
	private Dimension findComponentDim(String type, boolean containsFirstElement) {
		Dimension d = new Dimension(0,0);
		double width = 0;
		double height = 0;
		List<Integer> widths = new ArrayList<>();
		List<Integer> heights = new ArrayList<>();
		for(Component c : map.keySet()) {
			if(map.get(c).getRow() == 1 && map.get(c).getColumn()==1) {
				continue;
			}
			if(type.equals("pref")) {
				if(c.getPreferredSize()!=null) {
					widths.add(c.getPreferredSize().width);
					heights.add(c.getPreferredSize().height);
				}
			}else if(type.equals("max")) {
				if(c.getMaximumSize()!=null) {
					widths.add(c.getMaximumSize().width);
					heights.add(c.getMaximumSize().height);
				}
			} else if(type.equals("min")) {
				if(c.getMinimumSize()!=null) {
					widths.add(c.getMinimumSize().width);
					heights.add(c.getMinimumSize().height);
				}
			}
		}
		if(!widths.isEmpty() && !heights.isEmpty()) {
			if(type.equals("pref") || type.equals("min")) {
				width = Collections.max(widths);
				height = Collections.max(heights);
			}else if(type.equals("max")) {
				width = Collections.min(widths);
				height = Collections.min(heights);
			}
			d.setSize((int)width, (int)height);
		}
		
		return d;
	}

	/**
	 * Utility method which checks whether parent container contains element on RCPosition 1,1
	 * @param type
	 * @return dimensions of component on position 1,1 or null if there is no such element
	 */
	private Dimension checkForFirstElement(String type) {
		Dimension d = new Dimension(0,0);
		for(Component c : map.keySet()) {
			if(map.get(c).getRow() == 1 && map.get(c).getColumn() == 1) {
				if(type.equals("pref")) {
					d.setSize(c.getPreferredSize());
				} else if(type.equals("max")) {
					d.setSize(c.getMaximumSize());
				} else if(type.equals("min")) {
					d.setSize(c.getMinimumSize());
				}
				return d;
			}
		}
		return null;
	}

	/**
	 * Calculates container's dimensions
	 * @param maxWidth
	 * @param maxHeight
	 * @param containsFirstElement
	 * @param widthFirst
	 * @param parent
	 * @return container's dimensions or null if container does not want to state this size
	 */
	private Dimension calculateContainerDimension(double maxWidth, double maxHeight,
			boolean containsFirstElement, double widthFirst, Container parent) {
		double width = 0.0;
		double height = 0.0;
		
		if(!(maxWidth == 0.0 || maxHeight == 0.0)) {
			if(containsFirstElement) {
				width = widthFirst + (CalcLayout.NUM_OF_COLS-1) * space + (CalcLayout.NUM_OF_COLS-CalcLayout.COLS_TAKEN_BY_FIRST_EL) * maxWidth + parent.getInsets().left  + parent.getInsets().right;
				height = (CalcLayout.NUM_OF_ROWS-1) * space + CalcLayout.NUM_OF_ROWS * maxHeight + parent.getInsets().top + parent.getInsets().bottom;
			} else {
				width = maxWidth * CalcLayout.NUM_OF_COLS + (CalcLayout.NUM_OF_COLS-1) * space + parent.getInsets().left + parent.getInsets().right;
				height = (CalcLayout.NUM_OF_ROWS-1) * space + CalcLayout.NUM_OF_ROWS * maxHeight + parent.getInsets().top + parent.getInsets().bottom;
			}
			return new Dimension((int)width, (int)height);
		} else {
			return null;
		}
		
		
	}
}
