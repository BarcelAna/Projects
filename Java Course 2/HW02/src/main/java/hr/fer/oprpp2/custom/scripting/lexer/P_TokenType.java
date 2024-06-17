package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Enum TokenType represents types of tokens like EOF, END_OF_TAG, NUMBER, FUNCTION etc.
 * @author anace
 *
 */
public enum P_TokenType {
	EOF,
	END_OF_TAG,
	START_OF_TAG,
	NUMBER,
	FUNCTION,
	OPERATOR,
	STRING,
	VARIABLE
}
