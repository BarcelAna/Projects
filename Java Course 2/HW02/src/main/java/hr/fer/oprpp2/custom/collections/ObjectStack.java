package hr.fer.oprpp2.custom.collections;
/**
 * Class that represents implementation of the stack-like collection.
 * It adapts ArrayIndexedCollection to serve the purpose of stack. 
 * @author anace
 *
 */
public class ObjectStack {
	private ArrayIndexedCollection stack; 
	
	/**
	 * Default constructor which reference stack to new ArrayIndexedCollection
	 */
	public ObjectStack() {
		stack = new ArrayIndexedCollection();
	}
	
	/**
	 * Checks whether the stack is empty or not.
	 * @return true if stack contains elements or false otherwise 
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Returns size of stack.
	 * @return number of elements stored in stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Pushes object on top of stack.
	 * @param value to be pushed on top of stack
	 */
	public void push(Object value) {
		stack.add(value);
	}
	
	/**
	 * Removes element from the top of the stack and returns it.
	 * @return element popped from top of stack
	 * @throws EmptyStackException - if stack contains no elements 
	 */
	public Object pop() { 
		if(isEmpty()) throw new EmptyStackException();
		Object obj = stack.get(stack.size()-1);
		stack.remove(stack.size()-1);
		return obj;
	}
	
	/**
	 * Returns copy of object on top of stack, does not remove it.
	 * @return copy of object on top of stack
	 * @throws EmptyStackException - if stack contains no elements.
	 */
	public Object peek() {
		if(isEmpty()) throw new EmptyStackException();
		return stack.get(stack.size()-1);
	}
	
	/**
	 * Removes all elements from stack
	 */
	public void clear() {
		stack.clear();
	}
	
}
