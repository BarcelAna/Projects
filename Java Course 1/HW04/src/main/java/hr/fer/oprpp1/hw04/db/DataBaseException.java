package hr.fer.oprpp1.hw04.db;

/**
 * Exception thrown when there is invalid record in database file.
 * @author anace
 *
 */
public class DataBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor
	 */
	public DataBaseException() {
		super();
	}
	
	/**
	 * Constructor that accepts error message.
	 * @param msg
	 */
	public DataBaseException(String msg) {
		super(msg);
	}

}
