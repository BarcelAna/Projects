package hr.fer.oprpp1.fractals.complexParsing;

/**
 * Lexical analyzer used in process of parsing of complex numbers.
 * @author anace
 *
 */
public class ComplexLexer {
	/**
	 * Current position in given string
	 */
	private int currentIndex;
	
	/**
	 * Last generated token
	 */
	private Token token;
	
	/**
	 * Array of characters of given string
	 */
	private char[] data;
	
	/**
	 * Constructor which accepts string to be analyzed.
	 * Sets currentIndex to 0 and fills data array with all characters from given string 
	 * @param root - inserted string
	 */
	public ComplexLexer(String root) {
		currentIndex = 0;
		data = root.toCharArray();
	}
	
	/**
	 * Returns next token from given string.
	 * @return token
	 */
	public Token nextToken() {
		extractToken();
		return getToken();
	}
	
	/**
	 * Utility method for extracting next token from string.
	 * @throws ComplexLexerException if error occurs while extracting next token 
	 */
	private void extractToken() {
		if(token != null && token.getTokenType() == TokenType.EOF) {
			throw new ComplexLexerException();
		}
		
		skipBlanks();
		
		if(currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		String value = "";
		if(Character.isDigit(data[currentIndex])) {
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				value+=data[currentIndex];
				++currentIndex;
			}
			token = new Token(TokenType.NUM, value);
		} else if(Character.isLetter(data[currentIndex])) {
			value = Character.toString(data[currentIndex]);
			++currentIndex;
			token = new Token(TokenType.I, value);
		} else if(currentIndex +1 < data.length && data[currentIndex] == '-' && Character.isDigit(data[currentIndex+1])) {
			value+="-";
			++currentIndex;
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				value+=data[currentIndex];
				++currentIndex;
			}
			token = new Token(TokenType.NUM, value);
		} else {
			value = Character.toString(data[currentIndex]);
			++currentIndex;
			token = new Token(TokenType.OP, value);
		}
	}

	/**
	 * Utility method for skipping blanks from input string.
	 */
	private void skipBlanks() {
		while(currentIndex < data.length) {
			if(data[currentIndex] == ' ') {
				++currentIndex;
			}
			break;
		}
		
	}

	/**
	 * Returns last generated token
	 * @return token
	 */
	public Token getToken() {
		return token;
	}
	
	

}
