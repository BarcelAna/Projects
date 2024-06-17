package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Class represents an implementation of resizable array-backed collection.
 * This class implements List interface.
 * @author anace
 *
 */

public class ArrayIndexedCollection implements List {
	/**
	 * number of elements stored in collection
	 */
	private int size;
	/**
	 * array in which elements are stored
	 */
	private Object[] elements;
	/**
	 * counts structural modifications of collection
	 */
	private long modificationCount = 0;
	
	/**
	 * default constructor which sets the capacity of elements array to 16
	 */
	public ArrayIndexedCollection() {
		this(16);
	}
	
	/**
	 * constructor that sets capacity of elements array to given value
	 * @param initialCapacity
	 * @throws IllegalArgumentException - if initialCapacity is less than 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) throw new IllegalArgumentException("Capacity can't be less than 1!");
		
		this.size = 0;
		this.elements = new Object[initialCapacity];
	}
	
	/**
	 * Constructor that accepts other collection which elements are then copied into this newly constructed collection.
	 * @param otherCollection
	 */
	public ArrayIndexedCollection(Collection otherCollection) {
		this(otherCollection, otherCollection.size());
	}
	
	/**
	 * Constructor that accepts other collection which elements are then copied into this newly constructed collection.
	 * If given initialCapacity is smaller than size of the given collection, the size of given collection is used for elements array preallocation.
	 * @param otherCollection
	 * @param initialCapacity
	 * @throws NullPointerException - if passed collection is null
	 */
	public ArrayIndexedCollection(Collection otherCollection, int initialCapacity){
		if(otherCollection == null) throw new NullPointerException("Other collection can't be null!");
			
		size = otherCollection.size();
		
		if(initialCapacity < otherCollection.size()) {
			elements = new Object[otherCollection.size()];
		}else {
			this.elements = new Object[initialCapacity];
		}
				
		for(int i = 0; i < size; ++i) {
			elements[i] = otherCollection.toArray()[i];
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	 
	/**
	 * Adds passed value to collection.
	 * If intern array of elements is full, then the capacity of elements array is increased to double of initial capacity and modificationCount is increased by one.
	 * @throws NullPointerException - if passed value is null
	*/
	@Override
	public void add(Object value) {
		if(value == null) throw new NullPointerException("Can't add null in collection!");
		if(elements.length == size) {
			increaseSize(size*2);
		}
		elements[size] = value;
		++size;
	}
	
	@Override
	public boolean contains(Object value) {
		for(int i = 0; i < size; ++i) {
			if(elements[i].equals(value))
				return true;
		}
		return false;
	}
	
	@Override
	public void remove(int index) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		if(index == (size - 1)) elements[index] = null;
		else {
			int i;
			for(i = index; i < (size-1); ++i) {
				elements[i] = elements[i + 1];
			}
			elements[i] = null;
		}
		--size;
		++modificationCount;
	}
	
	@Override
	public boolean remove(Object value) {
		for(int i = 0; i < size; ++i) {
			if(elements[i].equals(value)) {
				remove(i);
				return true;
			}
		}
		return false;	
	}
	
	@Override
	public Object[] toArray() {
		Object[] newArr = new Object[size];
		for(int i = 0; i < size; ++i) {
			newArr[i] = elements[i];
		}
		return newArr;
	}
	
	@Override
	public void clear() {
		for(int i = 0; i < size; ++i) {
			this.elements[i] = null;
		}
		this.size = 0;
		++modificationCount;
	}
	
	@Override
	public Object get(int index) {
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		return elements[index];
	}
	
	@Override
	public void insert(Object value, int position) {
		if(position < 0 || position > size) throw new IndexOutOfBoundsException();
		if(position == size) {
			add(value);
		}
		else {
			if(size == elements.length) {
				increaseSize(2*size);
			}
			int i;
			for(i = size; i > position; --i) {
				elements[i] = elements[i-1];
			}
			elements[i] = value;
			size++;
			modificationCount++;
		}
	}
	
	@Override
	public int indexOf(Object value) {
		int index = -1;
		for(int i = 0; i < size; ++i) {
			if(elements[i].equals(value)) {
				index = i;
				break;
			}
		}
		return index;
	}
	/**
	 * Increases size of elements array to given value
	 * @param newSize
	 */
	private void increaseSize(int newSize) {
		Object[] newArr = new Object[newSize];
		for(int i = 0; i < size; ++i) {
			newArr[i] = elements[i];
		}
		elements = newArr;
		++modificationCount;
	}
	
	/**
	 * Creates elements getter for ArrayIndexedCollection object.
	 * @return getter
	 */
	public ElementsGetterArrayList createElementsGetter() {
		ElementsGetterArrayList eg = new ElementsGetterArrayList(this);
		return eg;
	}
	
	/**
	 * Private static class which represents a model of array list getter.
	 * It implements ElementsGetter interface.
	 * @author anace
	 *
	 */
	private static class ElementsGetterArrayList implements ElementsGetter {
		/**
		 * reference to collection from which elements are then fetched
		 */
		private ArrayIndexedCollection collection;
		/**
		 * current position in elements array
		 */
		private int position = 0;
		/**
		 * saved value of modificationCount variable
		 */
		private long savedModificationCount;
		
		/**
		 * constructor which accepts ArrayIndexed collection from which elements will be fetched
		 * @param collection
		 */
		public ElementsGetterArrayList(ArrayIndexedCollection collection) {
			this.collection = collection;
			this.savedModificationCount = collection.modificationCount;
		}
		

		public boolean hasNextElement() {
			if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException();
			if(position >= collection.size) return false;
			return true;
			
		}
		
		public Object getNextElement() {
			if(savedModificationCount != collection.modificationCount) throw new ConcurrentModificationException();
			if(!hasNextElement()) throw new NoSuchElementException();
			else {
				Object element = collection.elements[position];
				position++;
				return element;
			}
		}
	}	
}