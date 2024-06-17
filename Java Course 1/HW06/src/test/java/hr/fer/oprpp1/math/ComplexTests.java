package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ComplexTests {

	@Test
	void testDefaultConstructor() {
		assertEquals(0, new Complex().getRe());
		assertEquals(0, new Complex().getIm());
	}
	
	@Test
	void testConstructor() {
		Complex c = new Complex(1, 19);
		assertEquals(1, c.getRe());
		assertEquals(19, c.getIm());
	}
	
	@Test
	void testModuleIntValues() {
		Complex c = new Complex(3, 4);
		assertEquals(5, c.module());
	}
	
	@Test
	void testModeluDoubleValue() {
		Complex c = new Complex(-2.1, 3.3);
		assertEquals(3.911521, c.module(), 10e-6);
	}
	
	@Test
	void testMultiplyIntegerValue() {
		Complex c = new Complex(1, 1);
		Complex other = new Complex(2, -3);
		assertEquals(5, c.multiply(other).getRe());
		assertEquals(-1, c.multiply(other).getIm());
	}
	
	@Test
	void testMultiplyDoubleValue() {
		Complex c = new Complex(1.1, 0);
		Complex other = new Complex(-2.7, -3.4);
		assertEquals(-2.97, c.multiply(other).getRe(), 1e-6);
		assertEquals(-3.74, c.multiply(other).getIm(), 1e-6);
	}
	
	@Test
	void testDevide() {
		Complex c = new Complex(5, 10);
		Complex other = new Complex(3, 4);
		assertEquals(2.2, c.divide(other).getRe(), 1e-6);
		assertEquals(0.4, c.divide(other).getIm(), 1e-6);
	}
	
	@Test
	void addTest() {
		Complex c = new Complex(1, -1);
		Complex other = new Complex(2, 2);
		Complex result = c.add(other);
		assertEquals(3, result.getRe());
		assertEquals(1, result.getIm());
	}
	
	@Test
	void subTest() {
		Complex c = new Complex(1, -1);
		Complex other = new Complex(2, 2);
		Complex result = c.sub(other);
		assertEquals(-1, result.getRe());
		assertEquals(-3, result.getIm());
	}
	
	@Test
	void negateTest() {
		Complex c = new Complex(3, -2);
		assertEquals(-3, c.negate().getRe());
		assertEquals(2, c.negate().getIm());
	}
	
	@Test
	void powerTest() {
		Complex c = new Complex(1, -1);
		Complex result = c.power(3);
		assertEquals(-2, result.getRe(), 1e-6);
		assertEquals(-2, result.getIm(), 1e-6);
	}
	
	@Test
	void rootTest() {
		Complex c = new Complex(2, 3);
		List<Complex> roots = c.root(3);
		assertEquals(1.451856 ,roots.get(0).getRe(), 1e-6);
		assertEquals(0.493403, roots.get(0).getIm(), 1e-6);
		assertEquals(-0.298628 ,roots.get(2).getRe(), 1e-6);
		assertEquals(-1.504046, roots.get(2).getIm(), 1e-6);
	}
	
	@Test
	void polarConstructorTest() {
		Complex c = new Complex(2, 3);
		PolarComplex polar = new PolarComplex(c);
		assertEquals(3.605551, polar.r, 1e-6);
		assertEquals(0.982793, polar.fi, 1e-6);
	}
	
	@Test
	void fromPolarTest() {
		Complex c = new Complex(2, 3);
		PolarComplex polar = new PolarComplex(c);
		assertEquals(2, polar.toComplex().getRe(), 1e-6);
		assertEquals(3, polar.toComplex().getIm(), 1e-6);
	}
	
	@Test
	void polarRootTest() {
		Complex c = new Complex(2, 3);
		PolarComplex polar = new PolarComplex(c);
		List<PolarComplex> roots = polar.root(3);
		assertEquals(1.5334062, roots.get(0).r, 1e-6);
		assertEquals(0.3275979, roots.get(0).fi, 1e-6);
	}

}
