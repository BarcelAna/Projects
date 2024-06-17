package hr.fer.oprpp1.custom.scripting.elems;

/**
 * ElementConstantDouble class represents simple double constant.
 * It extends Element class.
 * @author anace
 *
 */
public class ElementConstantDouble extends Element {
	/**
	 * double constant value
	 */
	private double value;
	
	/**
	 * constructor that accepts value of double constant
	 * @param value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
