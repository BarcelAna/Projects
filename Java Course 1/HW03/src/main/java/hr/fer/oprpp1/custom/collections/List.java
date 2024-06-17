package hr.fer.oprpp1.custom.collections;

/**
 * List interface lists methods for any list object.
 * It extends Collection interface.
 * @author anace
 *
 */
public interface List<E> extends Collection<E> {
	/**
	 * Returns the object stored at the given position in collection
	 * @param index of element which should be fetched
	 * @return element at the given position in collection
	 * @throws IndexOutOfBoundsException - if index is less than 0 or equal or greater than collection size
	 */
	public E get(int index);
	
	/**
	 * Inserts given object to given position in collection.
	 * Before insertion, elements shift one place towards the end so that an empty place is created at the position.
	 * @param object that is supposed to be inserted
	 * @param position at which the passed object should be inserted
	 * @throws IndexOutOfBoundsException - if position is less than 0 or greater that collection size
	 */
	public void insert(E value, int position);
	
	/**
	 * Returns index of first occurrence of given object in collection.
	 * @param value which index is searched
	 * @return index of passed value in collection, or -1 if the given value is not stored in collection 
	 */
	public int indexOf(Object value);
	
	/**
	 * Removes element at the given position.
	 * @param index of element that should be removed
	 * @throws IndexOutOfBoundsException - if the given index is less than 0 or equal or greater than collection size
	 */
	public void remove(int index);
}
