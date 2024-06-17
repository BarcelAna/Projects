package hr.fer.oprpp1.custom.collectionsT;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

class ObjectStackMethodsTests {
	ObjectStack stack = new ObjectStack();
	
	@Test
	public void isEmptyTest() {
		assertEquals(true, stack.isEmpty());
	}
	
	@Test
	public void sizeTest() {
		assertEquals(0, stack.size());
		stack.push(1);
		assertEquals(1, stack.size());
	}
	
	@Test
	public void pushNpeekTest() {
		stack.push("B");
		assertEquals("B", stack.peek());
	}
	
	@Test
	public void peekEmptyTest() {
		assertThrows(EmptyStackException.class, () -> {
			stack.peek();
		});
	}
	
	@Test
	public void popTest() {
		stack.push("A");
		stack.push("B");
		assertEquals("B", stack.pop());
		assertEquals("A", stack.pop());
	}
	
	@Test
	public void popExceptionTest() {
		assertThrows(EmptyStackException.class, () -> {
			stack.pop();
		});
	}
	
	@Test
	public void clearTest() {
		stack.clear();
		assertEquals(true, stack.isEmpty());
	}

}
