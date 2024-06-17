package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Class QueryParser represents a parser of input query
 * @author anace
 *
 */
public class QueryParser {
	/**
	 * list of conditional expressions parsed from query
	 */
	private List<ConditionalExpression> expressions;
	
	/**
	 * lexer used for generating tokens
	 */
	private QueryLexer lexer;
	
	private boolean isShowing;
	
	private String[] fields;

	/**
	 * Constructor that accepts query, initializes expressions list and lexer and starts parsing of input query.
	 * @param queryExp
	 */
	public QueryParser(String queryExp) {
		super();
		this.expressions = new ArrayList<>();
		this.lexer = new QueryLexer(queryExp);
		parse(queryExp);
	}
	
	/**
	 * Private method for parsing input query.
	 * It forms conditional expressions from tokens generated from the query.
	 * @param query
	 */
	private void parse(String query) {
		IFieldValueGetter fieldGetter = null;
		String literal = null;
		IComparisonOperator compOperator = null;
		QueryTokenType prev = null;
		isShowing = false;
		
		while(lexer.nextToken().getType() != QueryTokenType.EOF) {
			if(lexer.getToken().getType() == QueryTokenType.FIELD) {
				if(!(prev == null || prev == QueryTokenType.AND)) {
					throw new DataBaseException("Incorrect query format!");
				}
				if(lexer.getToken().getValue().equals("firstName")){
					fieldGetter = FieldValueGetters.FIRST_NAME;
				} else if(lexer.getToken().getValue().equals("lastName")){
					fieldGetter = FieldValueGetters.LAST_NAME;
				} else if(lexer.getToken().getValue().equals("jmbag")){
					fieldGetter = FieldValueGetters.JMBAG;
				} else {
					throw new DataBaseException("Incorrect query format!");
				}
			} else if(lexer.getToken().getType() == QueryTokenType.OPERATOR) {
				if(prev != QueryTokenType.FIELD) {
					throw new DataBaseException("Incorrect query format!");
				}
				if(lexer.getToken().getValue().equals(">")) {
					compOperator = ComparisonOperators.GREATER;
				} else if(lexer.getToken().getValue().equals(">=")) {
					compOperator = ComparisonOperators.GREATER_OR_EQUALS;
				} else if(lexer.getToken().getValue().equals("<")) {
					compOperator = ComparisonOperators.LESS;
				} else if(lexer.getToken().getValue().equals("<=")) {
					compOperator = ComparisonOperators.LESS_OR_EQUALS;
				} else if(lexer.getToken().getValue().equals("=")) {
					compOperator = ComparisonOperators.EQUALS;
				} else if(lexer.getToken().getValue().equals("!=")) {
					compOperator = ComparisonOperators.NOT_EQUALS;
				} else if(lexer.getToken().getValue().equals("LIKE")) {
					compOperator = ComparisonOperators.LIKE;
				} else {
					throw new DataBaseException("Incorrect query format!");
				}
			} else if(lexer.getToken().getType() == QueryTokenType.LITERAL) {
				if(prev != QueryTokenType.OPERATOR) {
					throw new DataBaseException("Incorrect query format!");
				}
				literal = lexer.getToken().getValue();
			} else if(lexer.getToken().getType() == QueryTokenType.AND) {
				if(prev != QueryTokenType.LITERAL) {
					throw new DataBaseException("Incorrect query format!");
				}
				expressions.add(new ConditionalExpression(fieldGetter, literal, compOperator));
			} else if(lexer.getToken().getType() == QueryTokenType.SHOWING) {
				int i = 0;
				while(lexer.nextToken().getType() != QueryTokenType.EOF) {
					fields[i] = lexer.getToken().getValue();
					++i;
				}
				break;
			}
			prev = lexer.getToken().getType();
		}
		if(fieldGetter == null || literal == null || compOperator == null || prev != QueryTokenType.LITERAL) {
			throw new DataBaseException("Incorrect query format!");
		}
		expressions.add(new ConditionalExpression(fieldGetter, literal, compOperator));
	}

	/**
	 * Checks whether the given query was a direct query(jmbag="xxx").
	 * @return true if the given query is direct, false otherwise.
	 */
	public boolean isDirectQuery() {
		if(expressions.size() == 1) {
			ConditionalExpression exp = expressions.get(0);
			if(exp.getGetterStrategy().equals(FieldValueGetters.JMBAG) && exp.getCompOperator().equals(ComparisonOperators.EQUALS)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If the query was direct, this method returns queried jmbag.
	 * @throws IllegalStateException - if given query wasn't direct
	 * @return queried jmbag
	 */
	public String getQueriedJMBAG() {
		if(!isDirectQuery()) throw new IllegalStateException("Can't get JMBAG from query that is nod direct!");
		
		return expressions.get(0).getLiteral();
	}
	
	/**
	 * Returns list of conditional expressions from query.
	 * @return list of conditional expressions
	 */
	public List<ConditionalExpression> getQuerry() {
		return expressions;
	}
	
	public boolean isShowing() {
		return isShowing;
	}

	public String[] getShowingFields() {
		return fields;
	}
}
