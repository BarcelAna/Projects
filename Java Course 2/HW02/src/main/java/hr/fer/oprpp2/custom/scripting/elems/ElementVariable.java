package hr.fer.oprpp2.custom.scripting.elems;

import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParserException;

/**
 * Class ElementVariable represents variable element.
 * This class extends Element class.
 * @author anace
 *
 */
public class ElementVariable extends Element {
	/**
	 * Variable name
	 */
	private String name;
	
	/**
	 * Constructor that accepts variable name.
	 * It first checks whether the given name is valid.
	 * @param name
	 */
	public ElementVariable(String name) {
		checkIfValid(name); 
		this.name = name;
	}
	
	/**
	 * Checks if given variable name is valid.
	 * Variable name must start with a letter, and can contain only letters, numbers or underscores.
	 * @param name
	 * @throws SmartScriptParserException - if variable name is not valid
	 */
	private void checkIfValid(String name) {
		if(!Character.isLetter(name.charAt(0))) throw new SmartScriptParserException("Variable name must start with a latter!");
		for(char c : name.toCharArray()) {
			if(Character.isAlphabetic(c) || c == '_')
				continue;
			else
				throw new SmartScriptParserException("Variable name can contain only letters, numbers or underscores!");
		}
	}
	
	@Override
	public String asText() {
		return name;
	}
	
}
