package hr.fer.oprpp1.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Complex is a library used for modeling complex numbers.
 * @author anace
 *
 */
public class Complex {

	/**
	 * Complex representation of origin
	 */
	public static final Complex ZERO = new Complex(0,0);
	
	/**
	 * Complex representation of vector i = (1, 0)
	 */
	public static final Complex ONE = new Complex(1,0);
	
	/**
	 * Complex representation of vector -i = (-1, 0)
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	
	/**
	 * Complex representation of vector j = (0, 1)
	 */
	public static final Complex IM = new Complex(0,1);
	
	/**
	 * Complex representation of vector -j = (0, -1)
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Real part of complex number
	 */
	private final double re;
	/**
	 * Imaginary part of complex number
	 */
	private final double im;

	/**
	 * Default constructor which initializes complex number as origin
	 */
	public Complex() {
		this.re = 0;
		this.im = 0;
	}
	
	/**
	 * Constructor which accepts values for real and imaginary parts of complex numbers and sets it's values to given numbers.
	 * @param re - real part of complex number
	 * @param im - imaginary part of complex number
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates module of complex number
	 * @return module
	 */
	public double module() {
		return Math.sqrt(re*re + im*im);
	}

	/**
	 * Multiplies current complex number with given complex number.
	 * @param c
	 * @return result of multiplication, new complex number
	 */
	public Complex multiply(Complex c) {		
		double resultRe = this.re*c.re - this.im*c.im;
		double resultIm = this.re*c.im + this.im*c.re;
		
		return new Complex(resultRe, resultIm);
	}

	/**
	 * Divides current complex number with given complex number.
	 * @param c
	 * @return result of division, new complex number
	 */
	public Complex divide(Complex c) {
		Complex conjugated = new Complex(c.re, -c.im);
		Complex numerator = multiply(conjugated);
		double denominator = c.re*c.re + c.im*c.im;
		return new Complex(numerator.re/denominator, numerator.im/denominator);
	}

	/**
	 * Calculates sum of two complex numbers
	 * @param c
	 * @return result of sum, new complex number
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);
	}

	/**
	 * Calculates subtraction of two complex numbers
	 * @param c
	 * @return result of subtraction, new complex number
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);
	}

	/**
	 * Negates current complex number and returns new complex number as a result,
	 * @return new complex number
	 */
	public Complex negate() {
		return new Complex(-this.re, - this.im);
	}

	/**
	 * Calculates n-th power of current given number.
	 * @param n
	 * @return new complex number as a result
	 */
	public Complex power(int n) {
		PolarComplex polar = new PolarComplex(this);
		PolarComplex polarResult = polar.power(n);
		return polarResult.toComplex();
	}

	/**
	 * Calculates n-th root of current complex number.
	 * @param n - power
	 * @return list of complex roots
	 */
	public List<Complex> root(int n) {
		PolarComplex polar = new PolarComplex(this);
		List<PolarComplex> polarRoots = polar.root(n);
		List<Complex> roots = new ArrayList<>();
		for(PolarComplex p : polarRoots) {
			roots.add(p.toComplex());
		}
		return roots;
	}

	/**
	 * Returns string representation of complex number.
	 * String is in format: x + iy where x is real part and y is imaginary part of complex number.
	 * @return complex number as string
	 */
	@Override
	public String toString() {
		if(this.im < 0) return this.re + "-i" + (-1)*this.im;  
		return this.re + "+i" + this.im;  
	}
	
	/**
	 * Returns real part of complex number.
	 * @return real part
	 */
	public double getRe() {
		return this.re;
	}
	
	/**
	 * Returns imaginary part of complex number.
	 * @return imaginary part.
	 */
	public double getIm() {
		return this.im;
	}
	
}