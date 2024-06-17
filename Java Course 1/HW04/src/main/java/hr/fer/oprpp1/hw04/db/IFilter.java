package hr.fer.oprpp1.hw04.db;

/**
 * Functional interface IFilter with method accepts(StudentRecord):boolean.
 * @author anace
 *
 */
public interface IFilter {
	/**
	 * Method Accepts checks whether the given record satisfies the given query or not.
	 * @param record
	 * @return true if given record is accepted by the filter, false otherwise
	 */
	public boolean accepts(StudentRecord record);
}
