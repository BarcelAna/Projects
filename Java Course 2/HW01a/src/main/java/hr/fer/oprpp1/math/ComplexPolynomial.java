package hr.fer.oprpp1.math;

import java.util.ArrayList;

import java.util.List;

/**
 * Class ComplexPolynomial represents complex number in format f(z) = zn*z^n + z(n-1)*z^(n-1)+...+ z1*z + z0.
 * @author anace
 *
 */
public class ComplexPolynomial {
	/**
	 * List of polynomial coefficients: z0,...,zn
	 */
	public List<hr.fer.oprpp1.math.Complex> coefficients;
	
	/**
	 * Constructor which accepts list of complex coefficients for this complex polynomial.
	 * @param factors
	 */
	public ComplexPolynomial(hr.fer.oprpp1.math.Complex...coefficients) {
		this.coefficients = new ArrayList<>();
		for(hr.fer.oprpp1.math.Complex c : coefficients) {
			this.coefficients.add(c);
		}
	}
	
	/**
	 * Returns order of this polynom.
	 * @return order
	 */
	public short order() {
		return (short)(coefficients.size()-1);
	}
	
	/**
	 * Computes a new polynomial from this*p
	 * @param p
	 * @return solution as polynomial
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		hr.fer.oprpp1.math.Complex factors[] = new hr.fer.oprpp1.math.Complex[this.order() + p.order()+1];
		for(int i = 0; i < factors.length; ++i) 
			factors[i] = new hr.fer.oprpp1.math.Complex(0,0);
		for(int i = 0; i < this.coefficients.size(); ++i) {
			for(int j = 0; j < p.coefficients.size(); ++j) {
				factors[i+j] = factors[i+j].add(this.coefficients.get(i).multiply(p.coefficients.get(j)));
			}
		}
		return new ComplexPolynomial(factors);
	}
	
	/**
	 * Computes first derivative of this polynomial.
	 * @return new complex polynomial
	 */
	public ComplexPolynomial derive() {
		hr.fer.oprpp1.math.Complex factors[] = new hr.fer.oprpp1.math.Complex[this.coefficients.size()-1];
		for(int i = 1; i < this.coefficients.size(); ++i) { 
			factors[i-1] = this.coefficients.get(i).multiply(new hr.fer.oprpp1.math.Complex(i, 0));
		}
		return new ComplexPolynomial(factors);
	}
	
	/**
	 * Computes polynomial value at given point z
	 * @param z
	 * @return calculated value as complex number
	 */
	public hr.fer.oprpp1.math.Complex apply(hr.fer.oprpp1.math.Complex z) {
		Complex result = this.coefficients.get(0);
		for(int i = 1; i < this.coefficients.size(); ++i) {
			result = result.add(this.coefficients.get(i).multiply(z.power(i)));
		}
		return result;
	}
	
	/**
	 * Returns string representation of complex polynomial.
	 * @return string
	 */
	@Override
	public String toString() {
		String result = "";
		for(int i = this.coefficients.size()-1; i > 0; --i) {
			result+="(" + this.coefficients.get(i) +")*z^" + i + "+";
		}
		result += "(" + this.coefficients.get(0) + ")";
		return result;
	}
}
