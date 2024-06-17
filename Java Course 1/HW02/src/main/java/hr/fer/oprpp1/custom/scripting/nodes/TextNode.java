package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Class TextNode represents text piece of a document.
 * It extends Node class.
 * @author anace
 *
 */
public class TextNode extends Node {
	/**
	 * text content of the current node
	 */
	private String text;
	
	/**
	 * Constructor that accepts given text value of the node.
	 * @param text
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String string = text.replace("\\", "\\\\");
		string = string.replace("{$", "\\{$");
		return string;
	}
}
