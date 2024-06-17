package hr.fer.oprpp1.hw04.db;
/**
 * Interface IComparisonOperator represents a general strategy for comparison operators used in queries. 
 * @author anace
 *
 */
public interface IComparisonOperator {
	/**
	 * Checks whether given values satisfy comparison operator
	 * @param value1
	 * @param value2
	 * @return true if given values satisfy concrete strategy, false otherwise.
	 */
	public boolean satisfied(String value1, String value2);
}
