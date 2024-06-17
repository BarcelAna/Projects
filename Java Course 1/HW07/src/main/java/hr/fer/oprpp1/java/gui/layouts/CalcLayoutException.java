package hr.fer.oprpp1.java.gui.layouts;

/**
 * Exception thrown when attempt of adding new component to layout is against the rules defined within CalcLayoutManager.
 * @author anace
 *
 */
public class CalcLayoutException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor wich accpets exception message.
	 * @param msg
	 */
	public CalcLayoutException(String msg) {
		super(msg);
	}

}
