package hr.fer.oprpp1.custom.collections;
/**
 * Class represents implementation of linked list-backed collection of objects.
 * This class extends class Collection.
 * @author anace
 *
 */
public class LinkedListIndexedCollection extends Collection {
	
	private int size;
	private ListNode first;
	private ListNode last;
	
	/**
	 * default constructor that sets references to first and last node of the list to null
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
		size = 0;
	}
	/**
	 * Constructor that accepts other collection which elements are then copied into this newly constructed collection.
	 * @param otherCollection
	 */
	public LinkedListIndexedCollection(Collection other) {
		this();
		for(Object o : other.toArray()) {
				ListNode newNode = new ListNode();
				newNode.value = o;
			
				if(first == null) {
					first = newNode;
					last = newNode;
				} else {
					newNode.next = null;
					newNode.previous = last;
					last.next = newNode;
					last = newNode;
				}
		}
		size = other.size();
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void add(Object value) {
		if(value == null) throw new NullPointerException();
		
		ListNode newNode = new ListNode();
		newNode.value = value;
		
		if(first == null) {
			first = newNode;
			last = newNode;
		} else {
			newNode.next = null;
			newNode.previous = last;
			last.next = newNode;
			last = newNode;
		}
		size++;
	}
	
	@Override
	public boolean contains(Object value) {
		ListNode n = first;
		for(int i = 0; i < size; ++i) {
			if(n.value.equals(value)) return true;
			n = n.next;
		}
		return false;
	}
	
	/**
	 * Removes element at the given position.
	 * @param index of element that should be removed
	 * @return true if element is successfully removed or false otherwise
	 * @throws IndexOutOfBoundsException - if given index is less than 0 or equal or greater than collection size
	 */
	public void remove(int index) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		if(index == (size - 1)) {
			last = last.previous;
			last.next = null; 
		} else if(index == 0) {
			first = first.next;
			first.previous = null;
		} else {
			ListNode n = first;
			for(int i = 0; i < size; ++i) {
				if(i != index) n = n.next;
				else break;
			}
			n.previous.next = n.next;
			n.next.previous = n.previous;
		}
		
		--size;
	}
	
	@Override
	public boolean remove(Object value) {
		if(contains(value)) {
			ListNode n = first;
			for(int i = 0; i < size; ++i) {
				if(n.value.equals(value)) {
					remove(i);
					return true;
				}
				n = n.next;
			}
		}
		return false;
	}
	
	@Override
	public Object[] toArray() {
		Object[] newArr = new Object[size];
		ListNode n = first;
		for(int i = 0; i < size; ++i) {
			newArr[i] = n.value;
			n = n.next;
		}
		return newArr;
	}

	@Override
	public void forEach(Processor processor) {
		ListNode n = first;
		for(int i = 0; i < size; ++i) {
			processor.process(n.value);
			n = n.next;
		}
	}

	@Override
	public void clear() {
		first = last = null;
		size = 0;
	}
	
	/**
	 * Returns the object stored at the given position in collection
	 * @param index of element which should be fetched
	 * @return element at the given position in collection
	 * @throws IndexOutOfBoundsException - if index is less than 0 or equal or greater than collection size
	 */
	public Object get(int index) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		Object o = null;
		if(index > (size/2)) {
			ListNode n = last;
			for(int i = (size/2 + 1); i < size; ++i) {
				if(i == index) {
					o = n.value;
					break;
				}
				n = n.previous;
			}
		} else {
			ListNode n = first;
			for(int i = 0; i < (size/2 + 1); ++i) {
				if(i == index) {
					o = n.value;
					break;
				}
				n = n.next;
			}
		}
		return o;
	}
	
	/**
	 * Inserts given object to given position in collection.
	 * Before insertion, elements shift one place towards the end so that an empty place is created at the position.
	 * @param object that is supposed to be inserted
	 * @param position at which the passed object should be inserted
	 * @throws IndexOutOfBoundsException - if position is less than 0 or greater that collection size
	 */
	public void insert(Object value, int position) {
		if(position < 0 || position > size) throw new IndexOutOfBoundsException();
		
		if(position == size) add(value);
		else {
			ListNode newNode = new ListNode();
			newNode.value = value;
			
			if(position == 0) {
				first.previous = newNode;
				newNode.next = first;
				first = newNode;
			} else {
				ListNode current = first;
				for(int i = 0; i < size; ++i) {
					if(position == i) {
						current.previous.next = newNode;
						newNode.previous = current.previous;
						current.previous = newNode;
						newNode.next = current;
						break;
					}
					current = current.next;
				}
			}
			++size;
		}
		
	}
	
	/**
	 * Returns index of first occurrence of given object in collection.
	 * @param value which index is searched
	 * @return index of passed value in collection, or -1 if the given value is not stored in collection 
	 */
	public int indexOf(Object value) {
		int index = -1;
		ListNode n = first;
		for(int i = 0; i < size; ++i) {
			if(n.value.equals(value)) {
				index = i;
				break;
			}
			n = n.next;
		}
		return index;
	}
	
	/**
	 * Private static class that represents node of linked list.
	 * @author anace
	 *
	 */
	private static class ListNode {
		
		public ListNode previous;
		public ListNode next;
		public Object value;
	}
}
