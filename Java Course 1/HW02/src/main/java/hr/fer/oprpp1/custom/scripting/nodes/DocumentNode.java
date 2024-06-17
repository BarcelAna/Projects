package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Class DocumentNode represents an entire document.
 * It extends Node class.
 * @author anace
 *
 */
public class DocumentNode extends Node {
	
	/**
	 * Returns string that represents original content of parsed document body
	 * @return original document content
	 */
	@Override
	public String toString() {
		String string = "";
		int  numOfChildren = this.numberOfChildren();
		for(int i = 0; i < numOfChildren; ++i) {
			Node child = this.getChild(i);
			string += child.toString();
		}
		return string;
	}
	
	/**
	 * Compares two DocumentNode objects based on their tree structure and content of each node of the tree.
	 * @return true if document nodes are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		DocumentNode otherDN = null;
		if(other instanceof DocumentNode)
			otherDN = (DocumentNode) other;
		
		String firstDocument = this.toString().toLowerCase().replaceAll("\\s+", "");
		String secondDocument = otherDN.toString().toLowerCase().replaceAll("\\s+", "");
		
		return firstDocument.equals(secondDocument);
		
	}
}