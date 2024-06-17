package hr.fer.oprpp1.fractals.complexParsing;

/**
 * Enum which represents types of parsed string of complex number.
 * Values are: num - value of real or imaginary part of complex number
 * 			   op - operation between real and imaginary part, can be + or -
 * 			   i - i vector of imaginary part of complex number z = a + ib
 * 			   eof - end of file 
 * @author anace
 *
 */
public enum TokenType {
	NUM,
	OP,
	I, 
	EOF
}
