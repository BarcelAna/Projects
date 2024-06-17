package hr.fer.oprpp1.hw04.db;

import java.util.HashSet;
import java.util.Set;

/**
 * Class QueryLexer represents a simple lexer that reads input query and transform it into tokens.
 * @author anace
 *
 */
public class QueryLexer {
	/**
	 * current position in input query
	 */
	private int currentIndex;
	/**
	 * last generated token
	 */
	private QueryToken token;
	/**
	 * set of valid operators
	 */
	private Set<String> validOperators = new HashSet<>();
	
	private String query;
	
	/**
	 * Constructor that accepts input query and initializes valid operators set with strings of valid operators.
	 * @param query
	 */
	public QueryLexer(String query) {
		this.query = query;
		currentIndex = 0;
		validOperators.add(">");
		validOperators.add("<");
		validOperators.add(">=");
		validOperators.add("<=");
		validOperators.add("=");
		validOperators.add("!=");
		validOperators.add("LIKE");
	}
	
	/**
	 * Extracts next token from input query.
	 * @return next token
	 */
	public QueryToken nextToken() {
		extractNext();
		return getToken();
	}
	
	/**
	 * Private method for extracting tokens.
	 * It stores extracted token to private field token.
	 */
	private void extractNext() {
		String value = "";
		
		if(token != null && token.getType() == QueryTokenType.EOF) {
			throw new DataBaseException("Complete query has already been tokenized!");
		}
		
		skipBlanks();
		
		if(currentIndex >= query.length()) {
			token = new QueryToken(null,QueryTokenType.EOF);
			return;
		}
		
		if(query.charAt(currentIndex) == '"') {
			++currentIndex;
			while(currentIndex < query.length() && query.charAt(currentIndex) != '"') {
				value += query.charAt(currentIndex);
				++currentIndex;
			}
			++currentIndex;
			token = new QueryToken(value, QueryTokenType.LITERAL);
		} else if(currentIndex < query.length() - 3 && 
				Character.toUpperCase(query.charAt(currentIndex)) == 'A' &&
				Character.toUpperCase(query.charAt(currentIndex+1)) == 'N' &&
				Character.toUpperCase(query.charAt(currentIndex+2)) == 'D'){
			currentIndex+=3;
			token = new QueryToken("AND", QueryTokenType.AND);
		}else if (Character.isLetter(query.charAt(currentIndex)) && query.charAt(currentIndex) != 'L') {
			while(currentIndex < query.length() && Character.isLetter(query.charAt(currentIndex)) && query.charAt(currentIndex) != 'L') {
				value += query.charAt(currentIndex);
				++currentIndex;
			}
			if(currentIndex < query.length() && query.charAt(currentIndex)==',') {
				++currentIndex;
			}
			token = new QueryToken(value, QueryTokenType.FIELD);
		}else {
			while(currentIndex < query.length() && query.charAt(currentIndex) != ' ' && query.charAt(currentIndex) != '"' ) {
				value += query.charAt(currentIndex);
				++currentIndex;
			}
			token = new QueryToken(value, QueryTokenType.OPERATOR);
		}
	}

	private void skipBlanks() {
		while(currentIndex < query.length()) {
			if(query.charAt(currentIndex) == ' ' || query.charAt(currentIndex) == '\t') {
				++currentIndex;
				continue;
			}
			break;
		}
	}

	/**
	 * Returns last extracted token.
	 * @return last generated token
	 */
	public QueryToken getToken() {
		return token;
	}
}
