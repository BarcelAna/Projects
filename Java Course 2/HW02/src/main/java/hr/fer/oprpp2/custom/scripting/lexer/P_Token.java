package hr.fer.oprpp2.custom.scripting.lexer;

/**
 * Class Token represents simple token with value, token type and node type.
 * @author anace
 *
 */
public class P_Token {
	/**
	 * value of token
	 */
	private Object value;
	
	/**
	 * type of token
	 */
	private P_TokenType tokenType;
	
	/**
	 * type of node which contains current token
	 */
	private NodeType nodeType;
	
	/**
	 * Constructor which accepts token value, type and node type.
	 * @param value
	 * @param word
	 * @param nodeType
	 */
	public P_Token(Object value, P_TokenType tokenType, NodeType nodeType) {
		this.tokenType = tokenType;
		this.nodeType = nodeType;
		this.value = value;
	}
	
	/**
	 * Returns token value.
	 * @return value of token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns token type.
	 * @return token type
	 */
	public P_TokenType getTokenType() {
		return tokenType;
	}
	
	/**
	 * Returns the type of node which part is the current token.
	 * @return node type
	 */
	public NodeType getNodeType() {
		return nodeType;
	}
}
