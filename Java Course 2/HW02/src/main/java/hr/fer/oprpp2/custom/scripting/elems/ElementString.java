package hr.fer.oprpp2.custom.scripting.elems;

/**
 * ElementString class represents simple string element.
 * It extends Element class.
 * @author anace
 *
 */
public class ElementString extends Element {
	/**
	 * string value
	 */
	private String value;
	
	/**
	 * constructor that accepts value of string
	 * @param value
	 */
	public ElementString(String value) {
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	@Override
	public String asText() {
		return new String("\"" + value + "\"");
		//return value;
	}

}
