package hr.fer.oprpp1.custom.collections;

/**
 * Interface processor is a functional interface with method process.
 * @author anace
 *
 */
public interface Processor<E> {
	
	public void process(E value);
	
}
