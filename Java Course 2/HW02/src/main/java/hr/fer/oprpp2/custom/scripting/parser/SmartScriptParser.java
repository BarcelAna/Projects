package hr.fer.oprpp2.custom.scripting.parser;

import hr.fer.oprpp2.custom.collections.ArrayIndexedCollection;

import hr.fer.oprpp2.custom.collections.ObjectStack;
import hr.fer.oprpp2.custom.scripting.elems.Element;
import hr.fer.oprpp2.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp2.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp2.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp2.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp2.custom.scripting.elems.ElementString;
import hr.fer.oprpp2.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp2.custom.scripting.lexer.NodeType;
import hr.fer.oprpp2.custom.scripting.lexer.P_Lexer;
import hr.fer.oprpp2.custom.scripting.lexer.P_Token;
import hr.fer.oprpp2.custom.scripting.lexer.P_TokenType;
import hr.fer.oprpp2.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp2.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp2.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp2.custom.scripting.nodes.Node;
import hr.fer.oprpp2.custom.scripting.nodes.TextNode;

/**
 * Class SmartScriptParser represents simple implementation of parser.
 * @author anace
 *
 */
public class SmartScriptParser {
	/**
	 * lexer which is used in parser for token generation
	 */
	private P_Lexer lexer;
	
	/**
	 * stack used for building of a tree structured document model
	 */
	private ObjectStack stack;
	
	/**
	 * state of parser
	 */
	private NodeType parserState;
	
	/**
	 * 
	 */
	int firstTime = 1;
	
	/**
	 * Constructor accepts string that contains document body.
	 * It creates an instance of lexer and initialize it with obtained text and delegates parsing to parse method.
	 * @param documentBody
	 * @throws SmartScriptParserException - if any exception occurs during parsing
	 */
	public SmartScriptParser(String documentBody) {
		this.lexer = new P_Lexer(documentBody);
		try {
			parse();
		} catch(Exception e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}
	
	/**
	 * Parses given token stream from lexer.
	 */
	public void parse() {
		stack = new ObjectStack();
		DocumentNode documentNode = new DocumentNode();
		stack.push(documentNode);
		P_Token prev = null;
		
		while(lexer.nextToken().getTokenType() != P_TokenType.EOF) {
			parserState = lexer.getToken().getNodeType();	
			if(lexer.getToken().getTokenType() == P_TokenType.END_OF_TAG && prev != null && prev.getTokenType() == P_TokenType.START_OF_TAG) {
				throw new SmartScriptParserException("Invalid tag format");
			}
			if(parserState == NodeType.TEXT) {
				createTextNode();
			} else if(parserState == NodeType.FOR) {
				createForNode();
			} else if(parserState == NodeType.ECHO){
				createEchoNode();
			} else if(parserState == NodeType.END) {
				endTag();
			}
			prev = lexer.getToken();
		}
		
		if(stack.size() > 1) throw new SmartScriptParserException(); //provjera je li iza svakog for loopa slijedio end tag
	}

	/**
	 * After reading end tag token, parser pops one element from stack.
	 * @throws SmartScriptParserException - if stack remains empty
	 */
	private void endTag() {
		stack.pop();
		if(stack.isEmpty()) throw new SmartScriptParserException("Stack should not empty!");
	}

	/**
	 * Creates one echo node and puts it in document tree structure.
	 */
	private void createEchoNode() {
		
		ArrayIndexedCollection elements = new ArrayIndexedCollection();
		int length = 0;
		while(lexer.getToken().getTokenType() != P_TokenType.END_OF_TAG) {
			if(lexer.getToken().getTokenType() == P_TokenType.VARIABLE) {
				elements.add(new ElementVariable((String)lexer.getToken().getValue()));
			} else if(lexer.getToken().getTokenType() == P_TokenType.STRING) {
				elements.add(new ElementString((String)lexer.getToken().getValue()));
			} else if(lexer.getToken().getTokenType() == P_TokenType.OPERATOR) {
				elements.add(new ElementOperator((String)lexer.getToken().getValue()));
			} else if(lexer.getToken().getTokenType() == P_TokenType.NUMBER) {
				if(lexer.getToken().getValue().toString().contains(".")) {
					elements.add(new ElementConstantDouble(Double.parseDouble((String)lexer.getToken().getValue())));
				} else {
					elements.add(new ElementConstantInteger(Integer.parseInt((String)lexer.getToken().getValue())));
				}
			} else if(lexer.getToken().getTokenType() == P_TokenType.FUNCTION) {
				elements.add(new ElementFunction((String)lexer.getToken().getValue()));
			}
			++length;
			lexer.nextToken();
		}
		EchoNode echoNode = new EchoNode(elements.toArray(), length);
		Node parent = (Node)stack.peek();
		parent.addChildNode(echoNode);
		
	}
	
	/**
	 * Creates for node and puts it in document structure.
	 * @throws SmartScriptParserException - if error occurs.
	 */
	private void createForNode() {	
		int forLoopCnt = 0;
		ElementVariable variable = null;
		Element startExp = null;
		Element endExp = null;
		Element stepExp = null;
		while(lexer.getToken().getTokenType() != P_TokenType.END_OF_TAG) {
			++forLoopCnt;
			if(forLoopCnt == 1) {
				if(lexer.getToken().getTokenType() != P_TokenType.VARIABLE) {
					throw new SmartScriptParserException("For loop must start with variable!");
				}
				variable = new ElementVariable((String)lexer.getToken().getValue());
			} else if(lexer.getToken().getTokenType() != P_TokenType.STRING &&
					lexer.getToken().getTokenType() != P_TokenType.NUMBER &&
					lexer.getToken().getTokenType() != P_TokenType.VARIABLE) {
				throw new SmartScriptParserException("For loop can containt only elements of string, number or variable");
			} else if(forLoopCnt == 2) {
				if(lexer.getToken().getTokenType() == P_TokenType.STRING) {
					startExp = new ElementString((String)lexer.getToken().getValue());
				} else if(lexer.getToken().getTokenType() == P_TokenType.VARIABLE) {
					startExp = new ElementVariable((String)lexer.getToken().getValue());
				} else {
					if(lexer.getToken().getValue().toString().contains(".")) {
						startExp = new ElementConstantDouble(Double.parseDouble((String)lexer.getToken().getValue()));
					} else {
						startExp = new ElementConstantInteger(Integer.parseInt((String)lexer.getToken().getValue()));
					}
				}
			} else if(forLoopCnt == 3) {
				if(lexer.getToken().getTokenType() == P_TokenType.STRING) {
					endExp = new ElementString((String)lexer.getToken().getValue());
				} else if(lexer.getToken().getTokenType() == P_TokenType.VARIABLE) {
					endExp = new ElementVariable((String)lexer.getToken().getValue());
				} else {
					if(lexer.getToken().getValue().toString().contains(".")) {
						endExp = new ElementConstantDouble(Double.parseDouble((String)lexer.getToken().getValue()));
					} else {
						endExp = new ElementConstantInteger(Integer.parseInt((String)lexer.getToken().getValue()));
					}
				}
			} else if(forLoopCnt == 4) {
				if(lexer.getToken().getTokenType() == P_TokenType.STRING) {
					stepExp = new ElementString((String)lexer.getToken().getValue());
				} else if(lexer.getToken().getTokenType() == P_TokenType.VARIABLE) {
					stepExp = new ElementVariable((String)lexer.getToken().getValue());
				} else {
					if(lexer.getToken().getValue().toString().contains(".")) {
						stepExp = new ElementConstantDouble(Double.parseDouble((String)lexer.getToken().getValue()));
					} else {
						stepExp = new ElementConstantInteger(Integer.parseInt((String)lexer.getToken().getValue()));
					}
				}
			} else {
				throw new SmartScriptParserException("Too many arguments in ForLoop!");
			}
			lexer.nextToken();
		}
		
		if(forLoopCnt < 2) {
			throw new SmartScriptParserException("Too few arguments in ForLoop!");
		}
		
		ForLoopNode forLoopNode = new ForLoopNode(variable, startExp, endExp, stepExp);
		Node parentNode = (Node)stack.peek();
		parentNode.addChildNode(forLoopNode);
		stack.push(forLoopNode);
	}

	/**
	 * Creates text node and adds it in tree document structure.
	 */
	private void createTextNode() {
		TextNode textNode = new TextNode((String)lexer.getToken().getValue());
		Node parent = (Node) stack.peek();
		parent.addChildNode(textNode);	
	}

	/**
	 * Returns root document node.
	 * @return document node
	 */
	 public DocumentNode getDocumentNode() {
		 return (DocumentNode)stack.peek(); 
	 }
}
