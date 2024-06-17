package hr.fer.oprpp1.custom.scripting.parser;

/**
 * SmartScriptParserException is thrown when error occurs during parsing of some text.
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * default constructor
	 */
	public SmartScriptParserException() {
		
	}
	
	/**
	 * constructor that accepts error message
	 * @param msg
	 */
	public SmartScriptParserException(String msg) {
		super(msg);
	}
}
