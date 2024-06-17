package hr.fer.oprpp2.custom.scripting.elems;

/**
 * ElementConstantInteger class represents simple integer constant.
 * It extends Element class.
 * @author anace
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * constant value
	 */
	private int value;
	
	/**
	 * constructor that accepts value of integer constant
	 * @param value
	 */
	public ElementConstantInteger(int value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	@Override
	public String asText() {
		return Integer.toString(value);
	}

}
