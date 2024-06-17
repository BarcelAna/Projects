package hr.fer.oprpp2.custom.scripting.elems;

import java.util.HashSet;
import java.util.Set;

import hr.fer.oprpp2.custom.scripting.parser.SmartScriptParserException;

/**
 * ElementOperator class represents simple operator element.
 * It extends Element class.
 * @author anace
 *
 */
public class ElementOperator extends Element {
	/**
	 * String representation of operator
	 */
	private String symbol;
	
	/**
	 * set of listed valid operators
	 */
	private static Set<String> validOperators;
	
	static {
		validOperators = new HashSet<>();
		validOperators.add("+");
		validOperators.add("-");
		validOperators.add("*");
		validOperators.add("/");
		validOperators.add("^");
	}
	
	/**
	 * Constructor that accepts given symbol sign.
	 * First checks whether the given symbol is valid.
	 * @param symbol
	 */
	public ElementOperator(String symbol) {
		checkIfValid(symbol);
		this.symbol = symbol;
	}
	
	/**
	 * Checks if the given symbol is part of validOperators set.
	 * @param symbol
	 * @throws SmartScriptParserException - if symbol is invalid
	 */
	private void checkIfValid(String symbol) {
		if(!validOperators.contains(symbol)) throw new SmartScriptParserException("Not valid operator!");
	}

	@Override
	public String asText() {
		return symbol;
	}
}
