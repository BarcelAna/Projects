package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.SimpleHashtable.TableEntry;

class SimpleHashtableTests {


	@Test
	void emptyConstructorTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		assertEquals(0, (table.size()));
	}
	
	
	@Test
	void constructorExceptionTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			new SimpleHashtable<String, Integer>(0);
		});
	}
	
	@Test
	void putNotExistingTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		assertNull(table.put("Ana", 16));
	}
	
	@Test
	void putExistingReturnTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 16);
		assertEquals(16, table.put("Ana", 10));
	}
	
	@Test
	void putWithIncreasedCapacityTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		table.put("Ana", 10);
		table.put("Sara", 11);
		table.put("Petar", 5);
		assertEquals(3, table.size());
		assertTrue(table.containsKey("Petar"));
	}
	
	@Test
	void putException() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		assertThrows(NullPointerException.class, () -> {
			table.put(null, 16);
		});
		
	}
	
	@Test
	void getTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 16);
		assertEquals(16, table.get("Ana"));
	}
	
	@Test
	void getTestWithReplacement() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 16);
		table.put("Ana", 10);
		assertEquals(10, table.get("Ana"));
	}
	
	@Test
	void getNullTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		assertNull(table.get(null));
	}
	
	@Test
	void sizeTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(17);
		table.put("Ana", 10);
		table.put("Sara", 20);
		table.put("Luka", 30);
		assertEquals(3, table.size());
	}
	
	@Test
	void containsTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Sara", 2);
		assertTrue(table.containsKey("Sara"));
		assertFalse(table.containsKey("Ana"));
		assertFalse(table.containsKey(null));
	}
	
	@Test
	void containsValueTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 10);
		table.put("Sara", 15);
		assertTrue(table.containsValue(10));
		assertTrue(table.containsValue(15));
		assertFalse(table.containsValue(2));
	}
	
	@Test
	void containsValueNullTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", null);
		assertTrue(table.containsValue(null));
	}
	
	@Test
	void isRemovedTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 10);
		table.put("Sara", 20);
		table.remove("Ana");
		assertFalse(table.containsKey("Ana"));
	}
	
	@Test
	void removeValueTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 10);
		table.put("Sara", 20);
		assertEquals(10, table.remove("Ana"));
	}
	
	@Test
	void isEmptyTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		assertTrue(table.isEmpty());
		table.put("Ana", 10);
		assertFalse(table.isEmpty());
	}
	
	@Test
	void toStringTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		table.put("Ivana", 1);
		table.put("Ante", 2);
		table.put("Jasna", 3);
		table.put("Kristina", 4);
		assertEquals("[Ante=2, Ivana=1, Jasna=3, Kristina=4]", table.toString());
	}
	
	@Test
	void toArrayTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		table.put("Ivana", 1);
		table.put("Ante", 2);
		table.put("Jasna", 3);
		table.put("Kristina", 4);
		TableEntry<String, Integer>[] arr = table.toArray();
		assertEquals(4, arr.length);
		TableEntry<String, Integer> element = arr[1];
		assertEquals("Ivana", element.getKey());
	}
		
	@Test
	void clearTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		table.put("Ana", 1);
		table.put("Sara", 2);
		table.put("Mama", 3);
		table.put("Tata", 4);
		table.clear();
		assertEquals(0, table.size());
		assertFalse(table.containsKey("Sara"));
	}

}
