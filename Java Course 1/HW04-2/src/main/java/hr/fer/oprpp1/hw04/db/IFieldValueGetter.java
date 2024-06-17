package hr.fer.oprpp1.hw04.db;
/**
 * Interface IFieldValueGetter is representing a getter for requested student record
 * @author anace
 *
 */
public interface IFieldValueGetter {
	/**
	 * Get method for requested student record
	 * @param record
	 * @returns string field of concrete strategy
	 */
	public String get(StudentRecord record);
}
