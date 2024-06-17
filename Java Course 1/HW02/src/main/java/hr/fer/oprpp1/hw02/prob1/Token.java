package hr.fer.oprpp1.hw02.prob1;

/**
 * Token class implements simple token with value and token type.
 * @author anace
 *
 */
public class Token {
	/**
	 * token value
	 */
	private Object value;
	
	/**
	 * token type
	 */
	private TokenType type;
	
	/**
	 * Constructor with passed token type and value
	 * @param type
	 * @param value
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns token value.
	 * @return token value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns token type.
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}
}