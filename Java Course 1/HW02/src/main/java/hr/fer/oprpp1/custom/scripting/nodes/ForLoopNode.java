package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * Class ForLoopNode represents a single for loop construct.
 * It extends Node class.
 * @author anace
 *
 */
public class ForLoopNode extends Node {
	/**
	 * variable of for loop
	 */
	private ElementVariable variable;
	
	/**
	 * start expression of for loop
	 */
	private Element startExpression;
	
	/**
	 * end expression of for loop
	 */
	private Element endExpression;
	
	/**
	 * step expression of for loop, it can be null
	 */
	private Element stepExpression; 
	
	/**
	 * Constructor that accepts variable, start expression, end expression and step expression
	 * @param variable
	 * @param startExp
	 * @param endExp
	 * @param stepExp
	 */
	public ForLoopNode(ElementVariable variable, Element startExp, Element endExp, Element stepExp) {
		this.variable = variable;
		this.startExpression = startExp;
		this.endExpression = endExp;
		this.stepExpression = stepExp;
	}
	
	@Override 
	public String toString() {
		String se;
		String ee;
		String ste;
		if(startExpression instanceof ElementString && startExpression.asText().substring(1, startExpression.asText().length()-1).contains("\"")) {
			se = startExpression.asText().substring(1, startExpression.asText().length() - 1);
			se = "\"" +  se.replace("\"", "\\\"") + "\"";
		} else if(startExpression instanceof ElementString && startExpression.asText().substring(1, startExpression.asText().length()-1).contains("\\")){
			se = startExpression.asText().substring(1, startExpression.asText().length() - 1);
			se = "\"" +  se.replace("\\", "\\\\") + "\"";
		}else if(startExpression instanceof ElementString && startExpression.asText().substring(1, startExpression.asText().length()-1).contains("\n")){
			se = startExpression.asText().substring(1, startExpression.asText().length() - 1);
			se = "\"" +  se.replace("\n", "\\n") + "\"";
		}else if(startExpression instanceof ElementString && startExpression.asText().substring(1, startExpression.asText().length()-1).contains("\r")){
			se = startExpression.asText().substring(1, startExpression.asText().length() - 1);
			se = "\"" +  se.replace("\r", "\\r") + "\"";
		}else if(startExpression instanceof ElementString && startExpression.asText().substring(1, startExpression.asText().length()-1).contains("\t")){
			se = startExpression.asText().substring(1, startExpression.asText().length() - 1);
			se = "\"" +  se.replace("\t", "\\t") + "\"";
		}else {
			se = startExpression.asText();
		}
		if(endExpression instanceof ElementString && endExpression.asText().substring(1, endExpression.asText().length()-1).contains("\"")) {
			ee = endExpression.asText().substring(1, endExpression.asText().length() - 1);
			ee = "\"" +  ee.replace("\"", "\\\"") + "\"";
		}else if(endExpression instanceof ElementString && endExpression.asText().substring(1, endExpression.asText().length()-1).contains("\\")){
			ee = endExpression.asText().substring(1, endExpression.asText().length() - 1);
			ee = "\"" +  ee.replace("\\", "\\\\") + "\"";
		}else if(endExpression instanceof ElementString && endExpression.asText().substring(1, endExpression.asText().length()-1).contains("\n")){
			ee = endExpression.asText().substring(1, endExpression.asText().length() - 1);
			ee = "\"" +  ee.replace("\n", "\\n") + "\"";
		}else if(endExpression instanceof ElementString && endExpression.asText().substring(1, endExpression.asText().length()-1).contains("\r")){
			ee = endExpression.asText().substring(1, endExpression.asText().length() - 1);
			ee = "\"" +  ee.replace("\r", "\\r") + "\"";
		}else if(endExpression instanceof ElementString && endExpression.asText().substring(1, endExpression.asText().length()-1).contains("\t")){
			ee = endExpression.asText().substring(1, endExpression.asText().length() - 1);
			ee = "\"" +  ee.replace("\t", "\\t") + "\"";
		}else {
			ee = endExpression.asText();
		}
			
		String string = new String("{$ FOR " + variable.asText() + " " + se + " " + ee + " ");
		if(stepExpression != null) {
			if(stepExpression instanceof ElementString && stepExpression.asText().substring(1, stepExpression.asText().length()-1).contains("\"")) {
				ste = stepExpression.asText().substring(1, stepExpression.asText().length() - 1);
				ste = "\"" +  ste.replace("\"", "\\\"") + "\"";
			} else if(stepExpression instanceof ElementString && stepExpression.asText().substring(1, stepExpression.asText().length()-1).contains("\\")){
				ste = stepExpression.asText().substring(1, stepExpression.asText().length() - 1);
				ste = "\"" +  ee.replace("\\", "\\\\") + "\"";
			}else if(stepExpression instanceof ElementString && stepExpression.asText().substring(1, stepExpression.asText().length()-1).contains("\n")){
				ste = stepExpression.asText().substring(1, stepExpression.asText().length() - 1);
				ste = "\"" +  ee.replace("\n", "\\n") + "\"";
			}else if(stepExpression instanceof ElementString && stepExpression.asText().substring(1, stepExpression.asText().length()-1).contains("\r")){
				ste = stepExpression.asText().substring(1, stepExpression.asText().length() - 1);
				ste = "\"" +  ee.replace("\r", "\\r") + "\"";
			}else if(stepExpression instanceof ElementString && stepExpression.asText().substring(1, stepExpression.asText().length()-1).contains("\t")){
				ste = stepExpression.asText().substring(1, stepExpression.asText().length() - 1);
				ste = "\"" +  ee.replace("\t", "\\t") + "\"";
			}else {
				ste = stepExpression.asText();
			}
			string += (ste + " ");
		}
		string += "$} ";
		if(this.numberOfChildren() > 0) {
			int numOfChildren = numberOfChildren();
			for(int i = 0; i < numOfChildren; ++i) {
				string += this.getChild(i).toString();
			}
		}
		string += "{$ END $}";
		return string;
	}

}
