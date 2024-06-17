package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DictionaryTests {

	@Test
	void emptyTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		assertEquals(true, d.isEmpty());
	}
	
	@Test
	void sizeEmptyTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		assertEquals(0, d.size());
	}
	
	@Test
	void putNotExistingKeyTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		assertNull(d.put("Ana", 10));
	}
	
	@Test
	void putExistingKeyTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		d.put("Ana", 5);
		assertEquals(5, d.put("Ana", 10));
	}
	
	@Test
	void getTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		d.put("Ana", 5);
		assertEquals(5, d.get("Ana"));
		d.put("Ana", 10);
		assertEquals(10, d.get("Ana"));
	}
	
	@Test
	void getNotExistingTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		assertNull(d.get("Sara"));
	}
	
	@Test
	void removeTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		d.put("Ana", 10);
		d.put("Sara", 22);
		assertEquals(10, d.remove("Ana"));
		assertNull(d.remove("Ana"));
	}
	
	@Test
	void clearTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		d.put("Ana", 20);
		d.put("Sara", 20);
		d.put("Iva", 48);
		d.put("Dino", 52);
		d.clear();
		assertEquals(0, d.size());
	}
	
	@Test
	void notEmptyTest() {
		Dictionary<String, Integer> d = new Dictionary<>();
		d.put("Ana", 10);
		assertEquals(false, d.isEmpty());
	}

}
