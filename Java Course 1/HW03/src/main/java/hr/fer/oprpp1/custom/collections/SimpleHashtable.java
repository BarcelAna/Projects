package hr.fer.oprpp1.custom.collections;


import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class SimpleHashtable represents simple implementation of hash table.
 * @author anace
 *
 * @param <K>
 * @param <V>
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>>{
	private int size;
	/**
	 * Intern array of references to the first elements in each slot
	 */
	private TableEntry<K, V>[] table; //tablica od 2 elementa koja će sadržavati reference na prvi
	/**
	 * counts any modification like adding or removing elements
	 */
	private int modificationCount;
	
	/**
	 * default constructor that sets intern array capacity to 16
	 */
	public SimpleHashtable() {
		this(16); //na kraju veličine 16
	}
	
	/**
	 * constructor that sets intern array capacity to given value
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < 1) throw new IllegalArgumentException("Capacity must be at least 1");
		capacity = nextPowerOfTwo(capacity);
		table = (TableEntry<K, V>[]) new TableEntry[capacity];
		size = 0;
		modificationCount = 0;
	}

	/**
	 * Puts new TableEntry object with given key and value to hash table.
	 * If there is already entry with given key, old value for given key is replaces with passed new value.
	 * @param key
	 * @param value
	 * @return old value if entry with given key was already stored in table, null otherwise
	 * @throws NullPointerException - if given key is null
	 */
	public V put(K key, V value) {
		if(key == null) throw new NullPointerException("Key must be greater than 0!");
		checkOccupancy();
		V oldValue = null;
		TableEntry<K, V> newEntry = new TableEntry<>(key, value);
		int slotNumber = Math.abs(key.hashCode()) % table.length;
		if(table[slotNumber] == null) {
			table[slotNumber] = newEntry;
			++modificationCount;
			++size;
		} else {
			boolean found = false;
			TableEntry<K, V> current = table[slotNumber];
			if(current.getKey().equals(key)) {
				oldValue = current.getValue();
				current.setValue(value);
				found = true;
			}
			while(!found && current.next != null) {
				current = current.next;
				if(current.getKey().equals(key)) {
					oldValue = current.getValue();
					current.setValue(value);
					found = true;
				}
			}
			if(!found) {
				current.next = newEntry;
				++modificationCount;
				++size;
			}
		}
		return oldValue;
	}
	
	/**
	 * Checks whether size of hash table equals to 75% or more of hash table length.
	 * If it is, hash table capacity is set to double of previous capacity
	 */
	private void checkOccupancy() {
		double occupancy = size / table.length;
		if(occupancy >= 0.75) {
			doubleTableSize(table.length*2);
		}
		
	}

	/**
	 * Creates new array of table entries and reallocates intern hash table to this new array.
	 * @param newSize
	 */
	@SuppressWarnings("unchecked")
	private void doubleTableSize(int newSize) {
		TableEntry<K, V>[] arr = this.toArray();
		this.table = (TableEntry<K, V>[]) new TableEntry[newSize];
		this.size = 0;
		for(TableEntry<K, V> e : arr) {
			this.put(e.getKey(), e.getValue());
		}
		++modificationCount;
	}

	/**
	 * Returns value for given key.
	 * @param key
	 * @return value or null if such element does not exist in hash table
	 */
	public V get(Object key) {
		V value = null;
		if(key != null) {
			int slotNumber = Math.abs(key.hashCode()) % table.length;
			TableEntry<K, V> current = table[slotNumber];
			if(current != null) {
				if(current.getKey().equals(key)) {
					value = current.getValue();
				} else {
					while(current.next != null) { 
						current = current.next;
						if(current.getKey().equals(key)) {
							value = current.getValue();
							break;
						}
					}
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Returns number of elements stored in hash table
	 * @return size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks whether hash table contains entry with given key or not.
	 * @param key
	 * @return true if element with given key exists in table, false otherwise
	 */
	public boolean containsKey(Object key) {
		if(key == null) return false;
		
		int slotNumber = Math.abs(key.hashCode()) % table.length;
		TableEntry<K, V> current = table[slotNumber];
		if(current == null) return false;
		
		if(current.getKey().equals(key)) return true;
		while(current.next != null) {
			current = current.next;
			if(current.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether hash table contains entry with given value or not.
	 * @param value
	 * @return true if element with given value exists in table, false otherwise
	 */
	public boolean containsValue(Object value) {
		for(int i = 0; i < table.length; ++i) {
			if(table[i] == null) continue;
			TableEntry<K, V> current = table[i];
			if(table[i].getValue() == null && value == null) return true;
			if(table[i].getValue().equals(value)) {
				return true;
			}
			while(current.next != null) {
				current = current.next;
				if(current.getValue() == null && value == null) return true;
				if(current.getValue().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes element with given key and returns value removed element.
	 * @param key
	 * @return removed value if such element exists in table, null otherwise
	 */
	public V remove(Object key) {
		V removedValue = null;
		
		if(key == null) return removedValue;
		if(!containsKey(key)) return removedValue;
		
		int slotNumber = Math.abs(key.hashCode())%table.length;
		TableEntry<K, V> current = table[slotNumber];
			
		if(current.getKey().equals(key)) {
			table[slotNumber] = current.next;
			removedValue = current.getValue();
		} else {
			while(current.next != null) {
				TableEntry<K, V> prev = current;
				current = current.next;
				if(current.getKey().equals(key)) {
					removedValue = current.getValue();
					prev.next = current.next;
					break;
				}
			}
		}
		--size;
		++modificationCount;
		return removedValue;
	}
	
	/**
	 * Checks whether the hash table contains no elements.
	 * @return true if table is empty, false otherwise
	 */
	public boolean isEmpty() {
		for(int i = 0; i < table.length; ++i) {
			if(table[i] != null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns string representation of hash table elements in form: {key1=value1, key2=value2, ... , keyn=valuen].
	 * @return string
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		for(int i = 0; i < table.length; ++i) {
			TableEntry<K, V> current = table[i];
			if(current == null) continue;
			sb.append(current.getKey() + "=" + current.getValue() + ", ");
			while(current.next != null) {
				current = current.next;
				sb.append(current.getKey() + "=" + current.getValue() + ", ");
			}
		}
		sb.append("]");
		return sb.toString().replace(", ]", "]");
		
	}
	
	/**
	 * Returns new array of TableEntrys from hash table of hash table size.
	 * @return TableEntry array
	 */
	public TableEntry<K, V>[] toArray() {
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] arr = (TableEntry<K, V>[]) new TableEntry[this.size];
		//TableEntry<K, V>[] arr = (TableEntry<K, V>[]) Array.newInstance(TableEntry.class, size);
		int j = 0;
		for(int i = 0; i < table.length; ++i) {
			if(table[i] == null) continue;
			TableEntry<K, V> current = table[i];
			arr[j] = current;
			++j;
			while(current.next != null) {
				current = current.next;
				arr[j] = current;
				++j;
			}
		}
		return arr;
	}
	
	/**
	 * Removes all elements from hash table.
	 */
	public void clear() {
		for(int i = 0; i < table.length; ++i) {
			table[i] = null;
		}
		size = 0;
		++modificationCount;
	}
	/**
	 * Returns elements iterator for the hash table.
	 */
	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Calculates next power of two for given number
	 * @param capacity
	 * @return next power of two or given number if it is already power of two
	 */
	private int nextPowerOfTwo(int capacity) {
		int count = 0;
		if(capacity > 0 && (capacity & (capacity-1)) == 0) 
			return capacity;
		while(capacity != 0) {
			capacity >>= 1; //dijelenje s 7/2=3 3/2 = 1 1/2 = 0: cnt = 3
			count++;
		}
		return 1 << count;
	}
	
	/**
	 * Static class TableEntry represents one element of hash table which contains key, value and reference to the next element in the hash table slot.
	 * @author anace
	 *
	 * @param <K>
	 * @param <V>
	 */
	public static class TableEntry<K, V> { 
		private K key;
		private V value;
		private TableEntry<K,V> next; 
		
		/**
		 * Constructor which sets entry's key and value to the given values
		 * @param key
		 * @param value
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Getter for key.
		 * @return key
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Getter for value.
		 * @return value
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Setter for value.
		 * @param value
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
	}
	
	/**
	 * Implementation of hash table iterator.
	 * @author anace
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
		/**
		 * value of has table modification counter in the moment of initializaton of new iterator
		 */
		private int savedModificationCnt;
		/**
		 * index of currently iterated slot in hash table
		 */
		private int currentSlot = -1;
		/**
		 * next element to be returned by iterator
		 */
		private TableEntry<K, V> nextElement;
		/**
		 * element to which current iterator points until next() method is called
		 */
		private TableEntry<K, V> currentElement;
		/**
		 * reference to the hash table which is being iterated by this iterator
		 */
		private SimpleHashtable<K, V> obj;
		
		/**
		 * Constructor which accepts reference of an object to iterate
		 * @param obj
		 */
		public IteratorImpl() {
			savedModificationCnt = modificationCount;
			if(findNextSlot())
				nextElement = currentElement = table[currentSlot];
		}
		
		/**
		 * Checks whether there is next element to return in hash table.
		 * @return true if such element exists, false otherwise 
		 * @throws ConcurrentModificationException if structure of hash table is changed while iterating through it
		 */
		@Override
		public boolean hasNext() {
			if(savedModificationCnt != modificationCount) throw new ConcurrentModificationException();
			if(nextElement != null) return true;
			return false;
		}
		
		/**
		 * Private method for finding next not null slot to iterate through
		 * @return true if such slot exists, false otherwise
		 */
		private boolean findNextSlot() {
			for(int i = currentSlot + 1; i < table.length; ++i) {
				if(table[i] != null) {
					currentSlot = i;
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns next element in hash table.
		 * @return next entry
		 * @throws ConcurrentModificationException if structure of hash table is changed while iterating through it
		 */
		@Override
		public TableEntry<K, V> next() {
			if(savedModificationCnt != modificationCount) throw new ConcurrentModificationException();
			if(nextElement == null) throw new NoSuchElementException();
			currentElement = nextElement;
			if(nextElement.next != null) {
				nextElement = nextElement.next;
			} else {
				boolean hasNextSlot = findNextSlot();
				if(!hasNextSlot) {
					nextElement = null;
				}else {
					nextElement = table[currentSlot];
				}
				
			}
			return currentElement;
		}
		
		/**
		 * Removes current element from hash table.
		 * @throws ConcurrentModificationException if structure of hash table is changed while iterating through it
		 * @throws IllegalStateException if remove method is called more than ones on the same element
		 */
		public void remove() {
			if(savedModificationCnt != modificationCount) {
				throw new ConcurrentModificationException();
			}
			if(SimpleHashtable.this.remove(currentElement.getKey()) == null) {
				throw new IllegalStateException("Can't call remove multiple times on the same element!");
			}
			savedModificationCnt++;
		}
	}
}
