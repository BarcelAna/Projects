package hr.fer.oprpp1.java.gui.layouts;

import java.util.Objects;

/**
 * Class RCPosition represents container restrictions.
 * This class have two private int attributes: row and column.
 * Row must be number between 1 and 5, column must be number between 1 and 7.
 * @author anace
 *
 */
public class RCPosition {
	/**
	 * number of row
	 */
	private int row;
	/**
	 * number of column
	 */
	private int column;
	
	/**
	 * Constructor which sets number of rows and columns.
	 * @param row
	 * @param coulmn
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Returns number of rows.
	 * @return number of rows
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns number of columns;
	 * @return number of column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Static method for parsing RCPosition object passed as string.
	 * String must be in format {rows},{columns}, if not exception is thrown.
	 * Rows and columns must be parsable to integer, if not exception is thrown.
	 * @param text - string representation of RCPosition object
	 * @return RCPosition object
	 * @throws ParsinException - if string format is invalid 
	 */
	public static RCPosition parse(String text) {
		if(!text.contains(",") || text.startsWith(",") || text.endsWith(",")) {
			throw new ParsingException("Given string is not valid rcposition declaration. It must be in format {rows},{columns}");
		}
		String[] rowColumnArr = text.split(","); 
		if(rowColumnArr.length != 2) {
			throw new ParsingException("Given string is not valid rcposition declaration. It must be in format {rows},{columns}");
		}
		int row = 0;
		int column = 0;
		try {
			row = Integer.parseInt(rowColumnArr[0]);
			column = Integer.parseInt(rowColumnArr[1]);
		} catch(IllegalArgumentException e) {
			throw new ParsingException("Given string is not valid rcposition declaration. It must be in format {rows},{columns}");
		}
		return new RCPosition(row, column);
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
	
	
	
	
}
