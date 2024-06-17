package hr.fer.oprpp1.hw02.prob1;

/**
 * Lexer class implements a simple lexer.
 * It reads input text and generates token stream as output.
 * @author anace
 *
 */
public class Lexer {
	/**
	 * input text as char array
	 */
	private char[] data; // ulazni tekst
	
	/**
	 * last generated token
	 */
	private Token token; // trenutni token
	
	/**
	 * current position in input text
	 */
	private int currentIndex; // indeks prvog neobrađenog znaka
	
	/**
	 * state of this lexer
	 */
	private LexerState state;

	/**
	 * Constructor with given input text.
	 * Turns string text to char array and initializes lexer state to basic.
	 * @param text
	 */
	public Lexer(String text) {
		this.data = text.toCharArray();
		this.currentIndex = 0;
		this.state = LexerState.BASIC;
	}

	/**
	 * Generates next token from input text.
	 * @return next token
	 * @throws LexerException - if some error occurs during extraction of next token
	 */
	public Token nextToken() {
		extractNextToken();
		return getToken();
	}
	
	/**
	 * Returns last generated token.
	 * This method does not generate next token.
	 * @return last generated token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Extracts next token from input text and saves it to token member variable.
	 */
	private void extractNextToken() {
		
		if(token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("No tokens available");
		}
		
		skipBlanks();
		
		if(currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		if(state == LexerState.BASIC)
			workInBasic();
		else
			workInExtended();
		
	}
	

	/**
	 * Method represents basic work mode for lexer.
	 */
	private void workInBasic() {
		Object newToken;
		TokenType newTokenType;
		
		if(Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
			String word = "";
			while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {
				if(data[currentIndex] == '\\') {
					if(currentIndex == (data.length - 1) ||
							(!Character.isDigit(data[currentIndex + 1])) && data[currentIndex+1] != '\\') throw new LexerException("Invalid use of escape!");
					
					word += data[currentIndex + 1];
					currentIndex += 2;
				} else {
					word += data[currentIndex];
					++currentIndex;
				}
			}
			newToken = word;
			newTokenType = TokenType.WORD;
		} else if(Character.isDigit(data[currentIndex])) {
			Long number;
			String temp = "";
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				temp += data[currentIndex];
				++currentIndex;
			}
			try {
				number = Long.valueOf(temp);
			} catch(NumberFormatException e) {
				throw new LexerException("Number must be in the range of long type");
			}
			newToken = number;
			newTokenType = TokenType.NUMBER;
		} else {
			Character specialChar = null;
			
			if(data[currentIndex] == '#') {
				token= new Token(TokenType.SYMBOL, '#');
				++currentIndex;
				changeState();
				return;
			}
	
			specialChar = data[currentIndex];
			++currentIndex;
			
			newToken = specialChar;
			newTokenType = TokenType.SYMBOL;
		}
		
		token = new Token(newTokenType, newToken);
	}
	
	/**
	 * Method represents extended work mode for lexer after the # sign is read from input stream
	 */
	private void workInExtended() {
		String newToken = "";
		TokenType newTokenType = TokenType.WORD;
		if(data[currentIndex] != '#') {
			while(currentIndex < data.length && data[currentIndex] != ' ' && data[currentIndex] != '\n' && data[currentIndex] != '\t' && data[currentIndex] != '\r' && data[currentIndex] != '#') {
				newToken += data[currentIndex];
				++currentIndex;
			}
			token = new Token(newTokenType, newToken);
		} else {
			token = new Token(TokenType.SYMBOL, '#');
			++currentIndex;
			changeState();
		}
	}
	
	/**
	 * Skips all blanks in input text including '\n', '\t', '\r' and ' '
	 */
	private void skipBlanks() {
		while(currentIndex < data.length) {
			char chr = data[currentIndex];
			if(chr == ' ' || chr == '\n' || chr == '\r' || chr == '\t') {
				currentIndex++;
				continue;
			}
			break;
		}
	}
	
	/**
	 * Puts lexer in given state.
	 * @param state
	 * @throws NullPointerException - if given state is null
	 */
	public void setState(LexerState state) {
		if(state == null) throw new NullPointerException();
		this.state = state;
	}
	
	/**
	 * Changes state of lexer. 
	 * If the lexer is in BASIC state, this method puts it in EXTENDED and vice versa.
	 */
	private void changeState() {
		
		if(state == LexerState.BASIC) {
			setState(LexerState.EXTENDED);
		} else {
			setState(LexerState.BASIC);
		}
	}

	

}
