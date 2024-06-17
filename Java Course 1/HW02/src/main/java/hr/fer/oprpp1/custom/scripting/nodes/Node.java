package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Class Node is an object representation of document parts which are then use for representation of structured documents.
 * @author anace
 */
public class Node {
	/**
	 * Collection of children nodes of the current node
	 */
	private ArrayIndexedCollection childrenNodes;
	
	/**
	 * number of children nodes
	 */
	private int numberOfChildren = 0;
	
	/**
	 * Adds given child to collection of children.
	 * @param child
	 */
	public void addChildNode(Node child) {
		if(childrenNodes == null) childrenNodes = new ArrayIndexedCollection();
		++numberOfChildren;
		childrenNodes.add(child);
	}
	
	/**
	 * Returns number of direct children
	 * @return number of children
	 */
	public int numberOfChildren() {
	 return numberOfChildren;
	}
	
	/**
	 * Returns child at given position.
	 * @param index
	 * @return child at the given index in collection
	 * @throws IllegalArgumentException - if index is not valid
	 */
	public Node getChild(int index) {
		return (Node)childrenNodes.get(index);
	}
	
}
