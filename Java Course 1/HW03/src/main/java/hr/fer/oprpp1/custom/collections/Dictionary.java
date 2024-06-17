package hr.fer.oprpp1.custom.collections;

/**
 * Class Dictionary is parameterized class that represents a simple implementation of map-like object in Java
 * @author anace
 *
 * @param <K> - type of key object
 * @param <V> - type of value object
 */
public class Dictionary<K, V> {
	/**
	 * intern ArrayIndexedCollection object for storing dictionary elements
	 */
	private ArrayIndexedCollection<Pair<K, V>> dictionary;
	
	/**
	 * default constructor which initializes intern dictionary collection
	 */
	public Dictionary() {
		dictionary = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Checks whether dictionary contains any elements or not
	 * @return true if dictionary is empty, false otherwise
	 */
	public boolean isEmpty() {
		return dictionary.isEmpty();
	}
	
	/**
	 * Returns size of dictionary.
	 * @return number of elements stored in dictionary
	 */
	public int size() {
		return dictionary.size();
	}
	
	/**
	 * Removes all elements from dictionary
	 */
	public void clear() {
		dictionary.clear();
	}
	
	/**
	 * Stores one more Pair element into dictionary.
	 * If there is already pair with given key, old value is replaced with given new value.
	 * @param key 
	 * @param value
	 * @return old value stored with given key if exists, null otherwise
	 * @throws NullPointerException
	 */
	public V put(K key, V value) {
		if(key == null) throw new NullPointerException("Key can't be null!");
		V oldValue = findAndRemoveIfExists(key);
		Pair<K, V> newElement = new Pair<>(key, value);
		dictionary.add(newElement);
		return oldValue;
	}

	private V findAndRemoveIfExists(K key) {
		V oldValue = null;
		for(int i = 0; i < dictionary.size(); ++i) {
			if(dictionary.get(i).getKey().equals(key)) {
				oldValue = dictionary.get(i).getValue();
				dictionary.remove(i);
				break;
			}
		}
		return oldValue;
	}

	/**
	 * Returns value for given key.
	 * @param key
	 * @return value or null if there is no value stored in dictionary for given key
	 */
	public V get(Object key) {
		for(int i = 0; i < size(); ++i) {
			Pair<K, V> current = dictionary.get(i);
			if(current.getKey().equals(key)) {
				return current.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Removes pair from dictionary which key value is given key.
	 * @param key
	 * @return value of pair which is removed or null if such pair does not exist.
	 */
	public V remove(K key) {
		V oldValue = null;
		for(int i = 0; i < size(); ++i) {
			Pair<K, V> current = dictionary.get(i);
			if(current.getKey().equals(key)) {
				oldValue = current.getValue();
				dictionary.remove(i);
			}
		}
		return oldValue;
	}
	
	/**
	 * Private class that represents one element of dictionary - a simple pair of key and value.
	 * @author anace
	 *
	 * @param <T>
	 * @param <P>
	 */
	private class Pair<T, P> {
		private T key; //not null
		private P value;
		
		/**
		 * Constructor which sets key and value for current pair.
		 * @param key
		 * @param value
		 */
		public Pair(T key, P value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Key getter.
		 * @return key value
		 */
		public T getKey() {
			return key;
		}
		
		/**
		 * Value getter
		 * @return value
		 */
		public P getValue() {
			return value;
		}
	}
}
