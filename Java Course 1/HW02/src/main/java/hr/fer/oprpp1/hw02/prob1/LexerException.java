package hr.fer.oprpp1.hw02.prob1;

public class LexerException extends RuntimeException {
	/**
	 * Exception LexerExceptions is thrown when error occurs during lexical analysis
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * default constructor
	 */
	public LexerException() {
		
	}
	/**
	 * Constructor with given error message
	 * @param msg
	 */
	public LexerException(String msg) {
		super(msg);
	}
}
