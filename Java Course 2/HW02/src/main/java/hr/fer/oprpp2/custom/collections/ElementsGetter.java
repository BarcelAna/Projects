package hr.fer.oprpp2.custom.collections;
/**
 * ElementsGetter is an interface that lists methods for elements getter objects.
 * @author anace
 *
 */
public interface ElementsGetter {
	
	/**
	 * This method process all elements that haven't been fetched yet by the elements getter.
	 * @param p
	 */
	public default void processRemaining(Processor p) {
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
	/**
	 * Checks whether collection has some elements which haven't been fetched yet.
	 * @return true if there is next element, false otherwise
	 * @throws ConcurrentModificationException - if the method is called on modified collection
	 */
	public boolean hasNextElement();
	
	/**
	 * Returns next element from collection
	 * @return next element
	 * @throws NoSuchElementException - if there is no next element to get from collection.
	 * @throws ConcurrentModificationException - if the method is called on modified collection
	 */
	public Object getNextElement();
}
