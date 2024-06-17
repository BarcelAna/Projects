package hr.fer.oprpp1.custom.collections;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface Collection lists methods for some general collection object.
 * @author anace
 *
 */
public interface Collection<E> {
	
	public default <P> void copyTransformedInfoIfAllowed(Collection<? super P> destination, Function<? super E, P> transformer, Predicate<? super P> tester) {
		ElementsGetter<? extends E> getter = createElementsGetter();
		while(getter.hasNextElement()) {
			E element = getter.getNextElement();
			P transformedElement = transformer.apply(element);
			if(tester.test(transformedElement)) {
				destination.add(transformedElement);
			}
		}
	}
	
	/**
	 * Adds all elements that satisfy tester condition from collection col to the current collection.
	 * Uses ElementsGetter for fetching all elements from collection. 
	 * @param col - collection which elements are tested
	 * @param tester for testing collection's elements
	 */
	public default void addAllSatisfying(Collection<? extends E> col, Tester<? super E> tester) {
		ElementsGetter<? extends E> getter = col.createElementsGetter();
		while(getter.hasNextElement()) {
			E element = getter.getNextElement();
			if(tester.test(element)) {
				add(element);
			}
		}
	}
	
	/**
	 * Default implementation calls getter's method processRemaining for each element of collection.
	 * @param Processor object for processing elements of collection
	 */
	public default void forEach(Processor<? super E> processor) {
		ElementsGetter<E> getter = this.createElementsGetter();
		getter.processRemaining(processor);
	}
	
	/**
	 * Checks whether the collection contains any elements or not.
	 * @return true if collection is empty or false otherwise
	 */
	public default boolean isEmpty() {
		if(size() <= 0) return true;
		return false;
	}
	
	/**
	 * Returns size of collection.
	 * @return number of elements stored in collection
	*/
	public int size();
	
	/**
	 * Adds passed value to collection.
	 * @throws NullPointerException - if passed value is null
	 */
	public void add(E value);
	
	/**
	 * Checks whether the collection contains given value or not
	 * @param object value to search for
	 * @return true if collection contains passed value or false otherwise.
	 */
	public boolean contains(Object value);
	
	/**
	 * Removes passed value from collection.
	 * @param object value to remove from collection
	 * @return true if the given value is successfully removed, false otherwise.
	 */
	public boolean remove(Object value);
	
	/**
	 * Allocates new array with the size of collection, fills it with collection content and returns the array
	 * @return new array of collection's elements
	 */
	public Object[] toArray();
	
	/**
	 * Adds all elements of given collection to the current collection.
	 * Other collection remains unchanged.
	 * @param other collection which elements are then stored in the current collection
	 */
	public default void addAll(Collection<? extends E> other) { 

		class LocalProcessor implements Processor<E> { 
			@Override
			public void process(E value) {
				if(value != null) add(value);
			}
		}
		
		other.forEach(new LocalProcessor());
	}
	
	/**
	 * Removes all elements from collection.
	 */
	public void clear();
	
	public ElementsGetter<E> createElementsGetter();
	
}