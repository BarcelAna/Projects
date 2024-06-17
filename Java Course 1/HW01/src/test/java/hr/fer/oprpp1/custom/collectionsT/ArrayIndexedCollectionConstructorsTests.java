package hr.fer.oprpp1.custom.collectionsT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

class ArrayIndexedCollectionConstructorsTests {
	
	@Test
	void emptyConstructorTest() {
		assertEquals(0, new ArrayIndexedCollection().size());
	}
	
	@Test
	void initCapacityConstructorException() {
		assertThrows(IllegalArgumentException.class, () ->  {
			new ArrayIndexedCollection(-1);
		});
	}
	
	@Test
	void otherCollectionConstructor() {
		ArrayIndexedCollection other = new ArrayIndexedCollection(10);
		other.add("A");
		other.add("B");
		other.add("C");
		assertEquals(3, new ArrayIndexedCollection(other).size());
	}
	
	@Test
	void otherCollectionConstructorException() {
		assertThrows(NullPointerException.class, () -> {
			new ArrayIndexedCollection(null, 10);
		});
	}

}
