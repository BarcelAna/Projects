package hr.fer.oprpp1.hw04.db;

/**
 * Class ConditionalExpression represents model of conditional expression object from entered query.
 * @author anace
 *
 */
public class ConditionalExpression {
	/**
	 * concrete strategy for reading concrete field from student record
	 */
	private IFieldValueGetter getterStrategy;
	
	/**
	 * string literal on the right side of the operator in  query
	 */
	private String literal;
	
	/**
	 * concrete operator used in expression between student record field and string literal
	 */
	private IComparisonOperator compOperator;
	
	/**
	 * Constructor that creates new ConditionalExpression object based on given getter strategy, string literal and comparison operator strategy.
	 * @param getterStrategy
	 * @param literal
	 * @param compOperator
	 */
	public ConditionalExpression(IFieldValueGetter getterStrategy, String literal, IComparisonOperator compOperator) {
		super();
		this.getterStrategy = getterStrategy;
		this.literal = literal;
		this.compOperator = compOperator;
	}

	/**
	 * Getter for IFieldValueGetter strategy
	 * @return getterStrategy
	 */
	public IFieldValueGetter getGetterStrategy() {
		return getterStrategy;
	}

	/**
	 * Getter for String literal
	 * @return string literal
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Getter for IComparisonOperator strategy
	 * @return comparison operator strategy
	 */
	public IComparisonOperator getCompOperator() {
		return compOperator;
	}
	
	
	
}
