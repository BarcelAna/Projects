package hr.fer.oprpp1.fractals.complexParsing;

import java.util.List;

import hr.fer.oprpp1.math.Complex;

/**
 * ComplexParser class represents utility class used for parsing user's input of complex roots.
 * @author anace
 *
 */
public class ComplexParser {
	/**
	 * Lexical analyzer 
	 */
	hr.fer.oprpp1.fractals.complexParsing.ComplexLexer lexer;
	
	/**
	 * Constructor which accepts string representation of user's input and list of complex roots into which given root should be inserted if no errors occur during parsing.
	 * @param root
	 * @param rootsList
	 */
	public ComplexParser(String root, List<Complex> rootsList) {
		lexer = new ComplexLexer(root);
		parse(rootsList);
	}
	
	/**
	 * Performs parsing of given root. 
	 * @param rootsList
	 * @throws ComplexParserException if error occurs while parsing input string
	 */
	private void parse(List<Complex> rootsList) {
		hr.fer.oprpp1.fractals.complexParsing.TokenType prev = null;
		Double re = null;
		Double im = null;
		boolean imExists = false;
		boolean isNeg = false;
		
		while(lexer.nextToken().getTokenType() != hr.fer.oprpp1.fractals.complexParsing.TokenType.EOF) {
			if(lexer.getToken().getTokenType() == hr.fer.oprpp1.fractals.complexParsing.TokenType.I) {
				if(prev != null && prev != hr.fer.oprpp1.fractals.complexParsing.TokenType.OP || !lexer.getToken().getValue().equals("i")) {
					throw new ComplexParserException();
				} else {
					imExists = true;
				}
			} else if(lexer.getToken().getTokenType() == hr.fer.oprpp1.fractals.complexParsing.TokenType.NUM) {
				if(prev == null) {
					re = Double.parseDouble(lexer.getToken().getValue());
				} else if(prev == hr.fer.oprpp1.fractals.complexParsing.TokenType.I) {
					im = Double.parseDouble(lexer.getToken().getValue());
				} else {
					throw new ComplexParserException();
				}
			} else if(lexer.getToken().getTokenType() == hr.fer.oprpp1.fractals.complexParsing.TokenType.OP) {
				if(prev == null || prev == TokenType.NUM && re != null && (lexer.getToken().getValue().equals("+") || lexer.getToken().getValue().equals("-"))) {
					if(lexer.getToken().getValue().equals("-")) {
						isNeg = true;
					}
				} else {
					throw new ComplexParserException();
				}
			}
			prev = lexer.getToken().getTokenType();
		}
		if(re == null) re = 0.0;
		if(im == null) {
			if(imExists) im = 1.0;
			else im = 0.0;
		}
		if(isNeg) im = -im;
		rootsList.add(new Complex(re, im));
	}
}
