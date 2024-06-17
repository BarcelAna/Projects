package hr.fer.oprpp2.custom.scripting.nodes;

import hr.fer.oprpp2.custom.scripting.elems.Element;
import hr.fer.oprpp2.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp2.custom.scripting.elems.ElementString;

import java.io.IOException;

/**
 * Class EchoNode represents a command that generates some textual output dynamically.
 * It extends Node class.
 * @author anace
 *
 */
public class EchoNode extends Node {
	/**
	 * Element array that stores all elements of echo command
	 */
	private Element[] elements;
	
	/**
	 * Constructor that accepts Object array and it's length.
	 * It stores all of the other array's elements to current elements.
	 * @param other
	 * @param length
	 */
	public EchoNode(Object[] other, int length) {
		elements = new Element[length];
		Element e = null;
		for(int i = 0; i < length; ++i) {
			if(other[i] instanceof Element) e = (Element) other[i];
			elements[i] = e;
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String string = "{$ = ";
		String newS;
		for(Element e : elements) {
			if(e instanceof ElementString && e.asText().substring(1, e.asText().length()-1).contains("\"")) {
				newS = e.asText().substring(1, e.asText().length() - 1);
				newS = "\"" +  newS.replace("\"", "\\\"") + "\"";
			}else if(e instanceof ElementString && e.asText().substring(1, e.asText().length()-1).contains("\\")) {
				newS = e.asText().substring(1, e.asText().length() - 1);
				newS = "\"" +  newS.replace("\\", "\\\\") + "\"";
			}else if(e instanceof ElementString && e.asText().substring(1, e.asText().length()-1).contains("\n")) {
				newS = e.asText().substring(1, e.asText().length() - 1);
				newS = "\"" +  newS.replace("\n", "\\n") + "\"";
			}else if(e instanceof ElementString && e.asText().substring(1, e.asText().length()-1).contains("\r")) {
				newS = e.asText().substring(1, e.asText().length() - 1);
				newS = "\"" +  newS.replace("\r", "\\r") + "\"";
			}else if(e instanceof ElementString && e.asText().substring(1, e.asText().length()-1).contains("\t")) {
				newS = e.asText().substring(1, e.asText().length() - 1);
				newS = "\"" +  newS.replace("\t", "\\t") + "\"";
			}
			else if(e instanceof ElementFunction){
				newS = "@" + e.asText();
			} else {
				newS = e.asText();
			}
			string += newS + " ";
		}
		string += "$}";
		return string;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		try {
			visitor.visitEchoNode(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Element[] getElements() {
		return elements;
	}
}
