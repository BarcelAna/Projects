package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Class ParserLexer is an implementation of simple lexer that generates stream of tokens from input text.
 */

public class P_Lexer {
	/**
	 * input text as array
	 */
	private char[] data;
	
	/**
	 * current position in input text
	 */
	private int currentIndex;
	
	/**
	 * last generated token
	 */
	private P_Token token;
	
	/**
	 * current state of this lexer
	 */
	private P_LexerState state;
	
	/**
	 * Constructor which accepts input text and determines the state of lexer.
	 * @param text
	 */
	public P_Lexer(String text) {
		data = text.toCharArray();
		currentIndex = 0;
	    state = P_LexerState.TEXT;
	}
	
	/**
	 * Returns next token from input text.
	 * @return next token
	 */
	public P_Token nextToken() {
		extractToken();
		return getToken();
	}
	
	/**
	 * Returns last generated token.
	 * It does not start generating next token.
	 * @return generated token
	 */
	public P_Token getToken() {
		return token;
	}
	
	/**
	 * Extracts new token.
	 * @throws P_LexerException - if an error occurs.
	 */
	private void extractToken() {

		if(token != null && token.getTokenType() == P_TokenType.EOF) {
			throw new P_LexerException("No tokens available");
		}
		
		skipBlanks();
		
		if(currentIndex >= data.length) {
			token = new P_Token(null, P_TokenType.EOF, null);
			return;
		}
		
		if(state == P_LexerState.TAG) {
			workInTagState();
		} else {
			workInTextState();
		}
	}
	
	/**
	 * Private method which represents lexer work while reading simple text.
	 */
	private void workInTextState() {	
		String text = "";
		if(currentIndex <= (data.length - 2) && data[currentIndex] == '{' && data[currentIndex+1] == '$') {
			token = new P_Token("{$", P_TokenType.START_OF_TAG, null);
			changeState();
			return;
		}
		while(currentIndex < data.length && 
				!(currentIndex <= (data.length - 2) && data[currentIndex] == '{' && data[currentIndex+1] == '$')
				) {
			if((data[currentIndex] == (data.length - 1) && data[currentIndex] == '\\') || data[currentIndex] == '\\' && !(data[currentIndex+1] == '\\' || data[currentIndex+1] == '{')) {
				throw new P_LexerException("Invalid use of escape sign!");
			}
			if((data[currentIndex] == '\\' && data[currentIndex + 1] == '{') ||
					data[currentIndex] == '\\' && data[currentIndex+1] == '\\') {
				++currentIndex;
				text += data[currentIndex];
				++currentIndex;
			} else {
				text += data[currentIndex];
				++currentIndex;
			}
		}
		token = new P_Token(text, P_TokenType.STRING, NodeType.TEXT);	
	}
	
	/**
	 * Private method which represents lexer work while reading from tag.
	 */
	private void workInTagState() {
		if(currentIndex < (data.length - 1) && data[currentIndex] == '$' && data[currentIndex + 1] == '}') {
			changeState();
			token = new P_Token("$}", P_TokenType.END_OF_TAG, null);
			return;
		}
		if(token != null && token.getTokenType() == P_TokenType.START_OF_TAG) {
			if(data[currentIndex] == '=') {
				++currentIndex;
				tokenizeEcho();
			} else if(Character.toUpperCase(data[currentIndex]) == 'F'){
				currentIndex += 3;
				tokenizeForLoop();
			} else {
				tokenizeEndTag();
			}
		} else if(token != null && token.getNodeType() == NodeType.FOR) {
			tokenizeForLoop();
		}  else if(token != null && token.getNodeType() == NodeType.ECHO) {
			tokenizeEcho();
		}
	}

	/**
	 * Private method for creating end tag token
	 */
	private void tokenizeEndTag() {	
		skipBlanks();
		currentIndex += 3;
		token = new P_Token("END", P_TokenType.STRING, NodeType.END);
	}

	/**
	 * Private method for creating for loop element tag token
	 */
	private void tokenizeForLoop() {
		tokenizeTag(NodeType.FOR);
	}

	/**
	 * Private method for creating echo tag element token
	 */
	private void tokenizeEcho() {
		tokenizeTag(NodeType.ECHO);
	}
	
	/**
	 * Private method for tokenizeing any tag
	 * @param nodeType
	 */
	private void tokenizeTag(NodeType nodeType) {
		skipBlanks();
		if(currentIndex<(data.length - 1) && data[currentIndex] == '$' && data[currentIndex+1] == '}') {
			token = new P_Token("$}", P_TokenType.END_OF_TAG, null);
		} else if(data[currentIndex] == '"') {
			String string = "";
			++currentIndex;
			while(data[currentIndex] != '"') {
				if(currentIndex < (data.length-1) && data[currentIndex] == '\\' &&
						!(data[currentIndex+1]== '"' ||
						data[currentIndex+1] == '\\' ||
						data[currentIndex+1] == 'n' ||
						data[currentIndex+1] == 't' ||
						data[currentIndex+1] == 'r')) {
							throw new P_LexerException("Invalid use of escape sign!");
				} 
				if((data[currentIndex] == '\\' && data[currentIndex+1] == '\"') ||
					(data[currentIndex] == '\\' && data[currentIndex+1] == '\\')) {
						++currentIndex;
						string += data[currentIndex];
						++currentIndex;
				} else if(data[currentIndex] == '\\' && data[currentIndex+1] == 'n') {
						string += '\n';
						currentIndex +=2;
				}else if(data[currentIndex] == '\\' && data[currentIndex+1] == 't') {
						string += '\t';
						currentIndex +=2; 
				}else if(data[currentIndex] == '\\' && data[currentIndex+1] == 'r') {
						string += '\r';
						currentIndex +=2;
				}else if(data[currentIndex] != '\\'){
						string += data[currentIndex];
						++currentIndex;
				}
			}
			++currentIndex;
			token = new P_Token(string, P_TokenType.STRING, nodeType);
		} else if(currentIndex < (data.length -1) && data[currentIndex] == '-' && Character.isDigit(data[currentIndex+1])) {
			String number = "-";
			++currentIndex;
			while(Character.isDigit(data[currentIndex]) || data[currentIndex] == '.') {
				number += data[currentIndex];
				++currentIndex;
			}
			token = new P_Token(number, P_TokenType.NUMBER, nodeType);
		} else if(Character.isDigit(data[currentIndex])) {
			String number = "";
			while(Character.isDigit(data[currentIndex]) || data[currentIndex]=='.') {
				number += data[currentIndex];
				++currentIndex;
			}
			token = new P_Token(number, P_TokenType.NUMBER, nodeType);
 		} else if(Character.isLetter(data[currentIndex])){
 			String variable = "";
 			while(Character.isAlphabetic(data[currentIndex]) || data[currentIndex] == '_') {
 				variable += data[currentIndex];
 				++currentIndex;
 			}
 			token = new P_Token(variable, P_TokenType.VARIABLE, nodeType);
 		} else if(data[currentIndex] == '@') {
 			String function = "@";
 			++currentIndex;
 			while(Character.isAlphabetic(data[currentIndex]) || data[currentIndex] == '_') {
 				function += data[currentIndex];
 				++currentIndex;
 			}
 			token = new P_Token(function, P_TokenType.FUNCTION, nodeType);
 		} else {
 			String operator = Character.toString(data[currentIndex]);
 			++currentIndex;
 			token = new P_Token(operator, P_TokenType.OPERATOR, nodeType);
 		}
	}
	
	/**
	 * Changes state of lexer.
	 */
	private void changeState() {
		currentIndex += 2;
		if(state == P_LexerState.TAG) {
			state = P_LexerState.TEXT;
		} else {
			state = P_LexerState.TAG;
		}
	}
		
	/**
	 * Skips blanks from input text.
	 */
	private void skipBlanks() {
		while(currentIndex < data.length) {
			char chr = data[currentIndex];
			if(state.equals(P_LexerState.TEXT)) {
				if(chr == ' ') {
					currentIndex++;
					continue;
				} else {
					break;
				}
			} else {
				//dodala sam ovu granu zbog primjera u kojem tag sadrži next line character
				//kojeg je bilo potrebno samo preskočiti
				if(chr == ' ' || chr=='\n' || chr=='\r' || chr=='\t') {
					currentIndex++;
					continue;
				} else {
					break;
				}
			}

		}
	}

	
}
