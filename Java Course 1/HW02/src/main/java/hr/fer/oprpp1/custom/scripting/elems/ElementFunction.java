package hr.fer.oprpp1.custom.scripting.elems;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * ElementFunction class represents simple function element.
 * It extends Element class.
 * @author anace
 *
 */
public class ElementFunction extends Element {
	/**
	 * function name
	 */
	private String name;
	
	/**
	 * Constructor that accepts name of the function.
	 * First it checks whether the given name is valid.
	 * @param value
	 */
	public ElementFunction(String name) {
		// TODO Auto-generated constructor stub
		checkIfValid(name);
		this.name = name.substring(1);
	}

	@Override
	public String asText() {
		return name;
	}

	/**
	 * Checks whether the given name of a function starts with @ and a letter and later contains letters, numbers or underscores.
	 * @param name
	 * @throws SmartScriptParserException - if given name is invalid
	 */
	private void checkIfValid(String name) {
		// TODO Auto-generated method stub
		if(name.length() < 2 || !name.startsWith("@") || !Character.isLetter(name.charAt(1))) throw new SmartScriptParserException("Function name must start with @ and a letter!");
		
		name = name.substring(1);
		for(char c : name.toCharArray()) {
			if(Character.isAlphabetic(c) || c == '_') continue;
			else throw new SmartScriptParserException("Funciton name can contain only letters, digits or underscores!");
		}
	}
}
