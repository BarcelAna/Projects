package hr.fer.oprpp1.custom.collections;

/**
 * Class represents an implementation of resizable array-backed collection.
 * This class extends Collection class.
 * @author anace
 *
 */
public class ArrayIndexedCollection extends Collection {
	private int size;
	private Object[] elements;
	
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
	
	/**
	 * Removes element at the given position.
	 * @param index of element that should be removed
	 * @return true if element is successfully removed or false otherwise
	 * @throws IndexOutOfBoundsException - if given index is less than 0 or equal or greater than collection size
	 */
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
	}
	
	@Override
	public boolean remove(Object value) {
		if(contains(value)) {
			for(int i = 0; i < size; ++i) {
				if(elements[i].equals(value)) {
					remove(i);
					return true;
				}
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
	public void forEach(Processor processor) {
		for(int i = 0; i < size; ++i) {
			processor.process(elements[i]);
		}
	}
	
	@Override
	public void clear() {
		for(int i = 0; i < size; ++i) {
			elements[i] = null;
		}
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
		return elements[index];
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
		}
	}
	
	/**
	 * Returns index of first occurrence of given object in collection.
	 * @param value which index is searched
	 * @return index of passed value in collection, or -1 if the given value is not stored in collection 
	 */
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
	}
	 
		
}