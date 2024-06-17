package hr.fer.oprpp1.hw04.db;

/**
 * Class ComparisonOperators offers concrete comparison operators strategies which can be used for checking on some comparison operators in query expressions. 
 * @author anace
 *
 */
public class ComparisonOperators{
	/**
	 * Instance of IComparisonOperator interface that represents < operator in query expression.
	 */
	public static final IComparisonOperator LESS = (s1, s2) -> {
		int result = s2.compareTo(s1);
		if(result > 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents <= operator in query expression.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> {
		int result = s2.compareTo(s1);
		if(result >= 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents > operator in query expression.
	 */
	public static final IComparisonOperator GREATER = (s1, s2) -> {
		int result = s1.compareTo(s2);
		if(result > 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents >= operator in query expression.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> {
		int result = s1.compareTo(s2);
		if(result >= 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents = operator in query expression.
	 */
	public static final IComparisonOperator EQUALS = (s1, s2) -> {
		int result = s1.compareTo(s2);
		if(result == 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents != operator in query expression.
	 */
	public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> {
		int result = s1.compareTo(s2);
		if(result != 0) return true;
		else return false;
	};
	
	/**
	 * Instance of IComparisonOperator interface that represents LIKE operator in query expression.
	 */
	public static final IComparisonOperator LIKE = (s1, s2) -> {
		if(s2.contains("*")) {
			int cnt = 0;
			int position = 0;
			for(int i = 0; i < s2.length(); ++i) {
				if(s2.charAt(i) == '*') {
					++cnt;
					position = i;
				}
			}
			
			if(cnt > 1) throw new DataBaseException("LIKE pattern can contain maximum one character *");
			
			if(position == 0) {
				s2 = s2.substring(1);
				s1 = s1.substring(s1.length()-s2.length());
			} else if(position == (s2.length()-1)) {
				s2 = s2.substring(0, position);
				s1 = s1.substring(0, s2.length());
			} else {
				if((s2.length()-1) > s1.length()) return false;
				String[] arr = s2.split("\\*");
				
				String front = s1.substring(0, arr[0].length());
				int result = front.compareTo(arr[0]);
				if(result != 0) return false;
				
				String back = s1.substring(s1.length()-arr[1].length());
				result = back.compareTo(arr[1]);
				if(result == 0) return true;
				else return false;
			}
		}
		int result = s1.compareTo(s2);
		if(result == 0) return true;
		else return false;
	};
}
