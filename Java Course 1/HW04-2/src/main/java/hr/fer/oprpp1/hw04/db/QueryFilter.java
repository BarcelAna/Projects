package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Class QueryFilter represents model of an filter object for student records.
 * It implements IFilter interface
 * @author anace
 *
 */
public class QueryFilter implements IFilter{
	/**
	 * list of conditional expressions from query
	 */
	List<ConditionalExpression> list;
	
	public QueryFilter(List<ConditionalExpression> list) {
		super();
		this.list = list;
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for(ConditionalExpression exp : list) {
			IComparisonOperator operator = exp.getCompOperator();
			if(!operator.satisfied(exp.getGetterStrategy().get(record), exp.getLiteral())) {
				return false;
			}
		}
		return true;
	}

}
