package hr.fer.oprpp1.custom.collectionsT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;

class LinkedListIndexedCollectionConstructorsTests {

   @Test
   public void emptyConstructorTest() {
	   assertEquals(0, new LinkedListIndexedCollection().size());
   }
   
   @Test
   public void otherCollConstructorTest() {
	   Collection other = new ArrayIndexedCollection(3);
	   other.add("A");
	   other.add("B");
	   other.add("C");
	   LinkedListIndexedCollection c = new LinkedListIndexedCollection(other);
	   assertEquals(3, c.size());
	   assertEquals("A", c.get(0));
	   assertEquals("B", c.get(1));
   }

}
