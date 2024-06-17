package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexPolynomialTests {

	@Test
	void testApplyRooted() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
				);
		Complex result = crp.apply(new Complex(1,0));
		assertEquals(0, result.getRe());
		assertEquals(0, result.getIm());
	}
	
	@Test
	void toComplexPolynomialTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG
				);
		ComplexPolynomial cp = crp.toComplexPolynom();
		assertEquals(4, cp.order());
	}
	
	@Test
	void complexRootedPolynomialToStringTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 1), new Complex(3, 6), new Complex(2, 0));
		assertEquals("(2.0+i1.0)*(z-(3.0+i6.0))*(z-(2.0+i0.0))", crp.toString());
	}
	
	
	@Test
	void complexPolynomialMultiplyTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(2, 0));
		ComplexPolynomial cp2 = new ComplexPolynomial(new Complex(-1, 0), new Complex(1, 0));
		ComplexPolynomial result = cp1.multiply(cp2);
		assertEquals(-2, result.coefficients.get(0).getRe(), 1e-6);
		assertEquals(2, result.coefficients.get(1).getRe(), 1e-6);
	}
	
	@Test
	void complexPolynomialDeriveTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0), new Complex(7, 2));
		assertEquals(21, cp1.derive().coefficients.get(2).getRe(), 1e-6);
		assertEquals(5, cp1.derive().coefficients.get(0).getRe(), 1e-6);
	}
	
	@Test
	void applyComplexPolynomialTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(1, 0), new Complex(5, 0), new Complex(2, 0), new Complex(7, 2));
		Complex result = cp1.apply(new Complex(1, 0));
		assertEquals(15, result.getRe(), 1e-6);
		assertEquals(2, result.getIm(), 1e-6);
	}

}
