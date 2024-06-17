package hr.fer.oprpp1.custom.collections;

/**
 * Class Collection represents some general collection of objects.
 * @author anace
 *
 */
public class Collection {
	/**
	 * default constructor
	 */
	protected Collection() { 
		
	}
	/**
	 * Checks whether the collection contains any elements or not.
	 * @return true if collection is empty or false otherwise
	 */
	public boolean isEmpty() {
		if(size() <= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns size of collection.
	 * @return number of elements stored in collection
	*/
	public int size() {
		return 0;
	}
	
	/**
	 * Adds passed value to collection.
	 * @throws NullPointerException - if passed value is null
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Checks whether collection contains given value or not
	 * @param object value to search for
	 * @return true if collection contains passed value or false otherwise.
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes passed value from collection.
	 * @param object value to remove from collection
	 * @return true if given value is successfully removed, false otherwise.
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with the size of collection, fills it with collection content and returns the array
	 * @return new array of collection's elements
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Calls processor's method process for each element of collection.
	 * @param Processor object for processing elements of collection
	 */
	public void forEach(Processor processor) {
		
	}
	
	/**
	 * Adds all elements of given collection to the current collection.
	 * Other collection remains unchanged.
	 * @param other collection which elements are then stored in the current collection
	 */
	public void addAll(Collection other) { 
		class LocalProcessor extends Processor { 
			public void process(Object value) {
				add(value); 
			}
			
		}
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from collection.
	 */
	public void clear() {
		
	}
	
	
}