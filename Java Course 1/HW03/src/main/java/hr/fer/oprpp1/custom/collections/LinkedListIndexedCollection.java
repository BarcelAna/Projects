package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class represents implementation of linked list-backed collection of objects.
 * This class implements interface List.
 * @author anace
 *
 */

public class LinkedListIndexedCollection<E> implements List<E> {
	/**
	 * number of elements stored in list
	 */
	private int size;
	/**
	 * reference to the head of the list
	 */
	private ListNode first;
	/**
	 * reference to the last element of the list
	 */
	private ListNode last;
	/**
	 * counts number of structural modifications of the list
	 */
	private long modificationCount = 0;
	
	/**
	 * default constructor that sets references to the first and the last node of the list to null
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
		size = 0;
	}
	
	/**
	 * Constructor that accepts other collection which elements are then copied into this newly constructed collection.
	 * @param otherCollection
	 */
	public LinkedListIndexedCollection(Collection<? extends E> other) {
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
	public void add(E value) {
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
		++modificationCount;
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
	
	public void remove(int index) {
		if(index < 0 || index >= this.size) throw new IndexOutOfBoundsException();
		
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
		
		size--;
		++modificationCount;
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
	public void clear() {
		first = last = null;
		size = 0;
		++modificationCount;
	}
	
	@SuppressWarnings("unchecked")
	public E get(int index) {
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
		return (E)o;
	}
	
	public void insert(E value, int position) {
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
			++modificationCount;
		}
	}
	
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
	
	/**
	 * Creates elements getter for LinkedListIndexedCollection object.
	 * @return getter
	 */
	@Override
	public ElementsGetterLinkedList<E> createElementsGetter() {
		ElementsGetterLinkedList<E> eg = new ElementsGetterLinkedList<>(this);
		return eg;
	}
	

	/**
	 * Private static class which represents a model of linked list getter.
	 * It implements ElementsGetter interface.
	 * @author anace
	 */
	private static class ElementsGetterLinkedList<E> implements ElementsGetter<E>{
		/**
		 * reference to collection from which elements are then fetched
		 */
		private LinkedListIndexedCollection<E> collection;
		/**
		 * current node in the list 
		 */
		private ListNode currentNode;
		/**
		 * saved value of modificationCount variable
		 */
		private long savedModificationCount;
		
		/**
		 * constructor which accepts LinkedList collection from which elements will be fetched
		 * @param collection
		 */
		public ElementsGetterLinkedList(LinkedListIndexedCollection<E> col) {
			collection = col;
			currentNode = col.first;
			savedModificationCount = col.modificationCount;
		}
		
		public boolean hasNextElement() {
			if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException();
			if(currentNode == null) return false;
			else return true;
			
		}
		
		@SuppressWarnings("unchecked")
		public E getNextElement() {
			if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException();
			if(currentNode == null) throw new NoSuchElementException();
			Object element = currentNode.value;
			currentNode = currentNode.next;
			return (E)element;
		}
	}
}
