package hr.fer.oprpp1.custom.collectionsT;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;

class LinkedListIndexedCollectionMethodsTests {

	 LinkedListIndexedCollection collectionExample = new LinkedListIndexedCollection();
		
		@Test
		public void isEmptyTest() {
			CollectionTests.isEmptyTest(collectionExample);
		}
		
		@Test
		public void notEmptyTest() {
			CollectionTests.notEmptyTest(collectionExample);
		}
		
		@Test
		public void sizeTest() {
			CollectionTests.sizeTest(collectionExample);
		}
		
		@Test
		public void addInEmptyColTest() {
			CollectionTests.addInEmptyColTest(collectionExample);
		}
		
		@Test
		public void addInNotEmptyColTest() {
			CollectionTests.addInNotEmptyColTest(collectionExample);
		}
		
		
		@Test
		public void addExceptionTest() {
			CollectionTests.addExceptionTest(collectionExample);
		}
		
		@Test
		public void containsTest() {
			CollectionTests.containsTest(collectionExample);
		}
		
		@Test
		public void containsNullTest() {
			CollectionTests.containsNullTest(collectionExample);
		}
		
		@Test
		public void removeTest() {
			CollectionTests.removeTest(collectionExample);
		}
		
		@Test
		public void removeShiftTest() {
			CollectionTests.removeShiftTest(collectionExample);
		}
		
		@Test
		public void removeFirstTest() {
			collectionExample.add("A");
			collectionExample.add("B");
			collectionExample.add("C");
			collectionExample.remove(0);
			assertEquals("B", collectionExample.toArray()[0]);
		}
		
		@Test
		public void removeFromIndexTest() {
			try {
				CollectionTests.removeFromIndexTest(collectionExample);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Test
		public void removeExceptionTest() {
			assertThrows(IndexOutOfBoundsException.class, () -> {
				collectionExample.remove(1);
			});
		}
		
		@Test
		public void addAllTest() {
			LinkedListIndexedCollection other = new LinkedListIndexedCollection();
			other.add("A");
			other.add("B");
			other.add("C");
			CollectionTests.addAllTest(collectionExample, other);
		}
		
		@Test
		public void clearTest() {
			CollectionTests.clearTest(collectionExample);
		}
		
		@Test
		public void getTest() {
			try {
				CollectionTests.getTest(collectionExample);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void insertMiddleTest() {
			try {
				CollectionTests.insertMiddleTest(collectionExample);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		

		@Test
		public void insertLastTest() {
			try {
				CollectionTests.insertEndTest(collectionExample);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void insertFirstTest() {
			collectionExample.add("A");
			collectionExample.add("B");
			collectionExample.add("C");
			collectionExample.insert("D", 0);
			assertEquals("D", collectionExample.get(0));
			assertEquals("A", collectionExample.get(1));
		}
		
		@Test
		public void indexOfTest() {
			try {
				CollectionTests.indexOfTest(collectionExample);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

