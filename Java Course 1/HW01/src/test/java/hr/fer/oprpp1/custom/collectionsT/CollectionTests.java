package hr.fer.oprpp1.custom.collectionsT;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.Collection;

class CollectionTests {

	@Test
	public static void isEmptyTest(Collection c) {
		assertEquals(true, c.isEmpty());
	}
	
	@Test
	public static void notEmptyTest(Collection c) {
		c.add("A");
		assertEquals(false, c.isEmpty());
	}
	
	@Test
	public static void sizeTest(Collection c) {
		c.add("A");
		c.add("B");
		assertEquals(2, c.size());
	}
	@Test
	public static void addInEmptyColTest(Collection c) {
		c.add("B");
		Object[] arr = c.toArray();
		assertEquals("B", arr[0]);
		assertEquals(1, c.size());
	}
	
	@Test
	public static void addInNotEmptyColTest(Collection c) {
		for(int i = 0; i < 3; ++i) {
			c.add(i);
		}
		Object[] arr = c.toArray();
		assertEquals(0, arr[0]);
		assertEquals(1, arr[1]);
		assertEquals(2, arr[2]);
		assertEquals(3, c.size());
	}
	
	
	@Test
	public static void addExceptionTest(Collection c) {
		assertThrows(NullPointerException.class, () -> {
			c.add(null);
		});
	}
	
	@Test
	public static void containsTest(Collection c) {
		c.add("A");
		c.add("B");
		c.add("C");
		assertEquals(true, c.contains("A"));
		assertEquals(false, c.contains("D"));
	}
	
	@Test
	public static void containsNullTest(Collection c) {
		c.add("A");
		assertEquals(false, c.contains(null));
	}
	
	@Test
	public static void removeTest(Collection c) {
		c.add("A");
		c.add("B");
		c.add("C");
		assertEquals(true, c.remove("B"));
		assertEquals(false, c.remove("D"));
	}
	
	@Test
	public static void removeShiftTest(Collection c) {
		c.add("A");
		c.add("B");
		c.add("C");
		c.add("D");
		c.remove("B");
		assertEquals("C", c.toArray()[1]);
		assertEquals("D", c.toArray()[2]);
		assertEquals(3, c.size());
	}
	
	@Test
	public static void removeFromEndTest(Collection c) {
		c.add("A");
		c.add("B");
		c.add("C");
		c.remove("C");
		assertEquals("B", c.toArray()[1]);
	}
	
	@Test
	public static void removeFromIndexTest(Collection c) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		c.add("A");
		c.add("B");
		Method remove = c.getClass().getMethod("remove", int.class);
		remove.invoke(c, 1);
		assertEquals(1, c.size());
		assertEquals("A", c.toArray()[0]);
	}
	
	@Test
	public static void removeExceptionTest(Collection c) {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			c.remove(1);
		});
		assertThrows(IndexOutOfBoundsException.class, () -> {
			c.remove(-1);
		});	
	}
	
	@Test
	public static void addAllTest(Collection c, Collection other) {
		c.addAll(other);
		Object[] arr = c.toArray();
		
		assertEquals("A", arr[0]);
		assertEquals("B", arr[1]);
		assertEquals("C", arr[2]);
		assertEquals(3, c.size());
	}
	
	@Test
	public static void clearTest(Collection c) {
		c.add("A");
		c.add("B");
		c.add("C");
		c.clear();
		assertEquals(0, c.size());
	}
	
	@Test
	public static void getTest(Collection c) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		    Method get = c.getClass().getMethod("get", int.class); //java refleciton - jako cool
			c.add("A");
			c.add("B");
			c.add("C");
			c.add("D");
			assertEquals("B", get.invoke(c, 1));
	}
	
	@Test
	public static void insertMiddleTest(Collection c) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		c.add("A");
		c.add("B");
		c.add("C");
		
		Method insert = c.getClass().getMethod("insert", Object.class, int.class);
		insert.invoke(c, "D", 1);
		
		Method get = c.getClass().getMethod("get", int.class);
		
		assertEquals("A", get.invoke(c, 0));
		assertEquals("D", get.invoke(c, 1));
		assertEquals("B", get.invoke(c, 2));
		assertEquals("C", get.invoke(c, 3));
		assertEquals(4, c.size());
	}
	
	@Test
	public static void insertEndTest(Collection c) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		c.add("A");
		c.add("B");
		c.add("C");
		
		Method insert = c.getClass().getMethod("insert", Object.class, int.class);
		insert.invoke(c, "D", 3);
		
		Method get = c.getClass().getMethod("get", int.class);
	
		assertEquals("D", get.invoke(c, 3));
		assertEquals(4, c.size());
	}
	
	
	@Test
	public static void indexOfTest(Collection c) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		c.add("A");
		c.add("B");
		c.add("C");
		
		Method indexOf = c.getClass().getMethod("indexOf", Object.class);
		
		assertEquals(1, indexOf.invoke(c, "B"));
		assertEquals(-1, indexOf.invoke(c, "D"));
		assertEquals(-1, indexOf.invoke(c, (Object)null));
	}
	
}
