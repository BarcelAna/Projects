package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

class IteratorTests {

	@Test
	void hasNextText() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertFalse(i.hasNext());
	}
	
	@Test
	void hasNext() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 11);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertTrue(i.hasNext());
		assertTrue(i.hasNext());
	}
	
	@Test
	void hasNextExceptionTest() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 11);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertThrows(ConcurrentModificationException.class, () -> {
			hashTable.put("Klara", 5);
			i.hasNext();
		});
	}
	
	@Test
	void nextTest() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 11);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertEquals("Ana", i.next().getKey());
		assertEquals("Sara", i.next().getKey());
	}
	
	@Test
	void nextExceptionTest() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertThrows(NoSuchElementException.class, () -> {
			i.next();
		});
	}
	
	@Test
	void nextExceptionTest2() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 5);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		assertThrows(NoSuchElementException.class, () -> {
			i.next();
			i.next();
			i.next();
		});
	}
	
	@Test
	void nextExceptionTest3() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 5);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		hashTable.remove("Sara");
		assertThrows(ConcurrentModificationException.class, () -> {
			i.next();
		});
	}
	
	@Test
	void removeTest() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 5);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		i.remove();
		assertFalse(hashTable.containsKey("Ana"));
		assertTrue(hashTable.containsKey("Sara"));
	}
	
	@Test
	void removeExceptionTest() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 5);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		i.remove();
		assertThrows(IllegalStateException.class, () -> {
			i.remove();
		});
	}
	
	@Test
	void removeExceptionTest2() {
		SimpleHashtable<String, Integer> hashTable = new SimpleHashtable<>(2);
		hashTable.put("Ana", 10);
		hashTable.put("Sara", 5);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> i = hashTable.iterator();
		hashTable.remove("Ana");
		assertThrows(ConcurrentModificationException.class, () -> {
			i.remove();
		});
	}

}
