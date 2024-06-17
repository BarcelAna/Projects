package hr.fer.oprpp1.math;

import java.util.ArrayList;

import java.util.List;

/**
 * Class ComplexRootedPolynomial models polinom of complex numbers in format like f(z) = z0*(z-z1)*...*(z-zn).
 * z1,...,zn represents complex zeros of funciton f(z), while z0 is a complex constant of polynom.
 * @author anace
 *
 */
public class ComplexRootedPolynomial {
	private Complex constant;
	private List<Complex> roots;
	
	/**
	 * Constructor which accepts complex constant z0 and complex roots: z1, ..., zn
	 * @param constant
	 * @param roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = constant;
		this.roots = new ArrayList<>();
		for(Complex r : roots) {
			this.roots.add(r);
		}
	}

	/**
	 * Computes polynomial value at given point z
	 * @param z
	 * @return complex solution of f(z) for given z
	 */
	public Complex apply(Complex z) {
		Complex result = constant;
		for(Complex r : roots) {
			result = result.multiply(z.sub(r));
		}
		return result;
	}
	
	/**
	 * Converts complex rooted polynomial to complex polynomial type given as f(z) = zn*z^n+z(n-1)*z^(n-1)+...+z1*z + z0 
	 * @return complex polynomial type of current complex rooted polynomial
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant); 
		for(Complex r : roots) { //z - r
			result = result.multiply(new ComplexPolynomial(r.negate(), Complex.ONE));
		}
		return result;
	}
	
	/**
	 * Returns string representation of complex rooted polynomial.
	 * @return complex rooted polynomial as string
	 */
	@Override
	public String toString() {
		String result = "("+constant.toString()+")";
		for(Complex r : roots) {
			result+="*(z-(" + r + "))";
		}
		return result;
	}
	
	/**
	 * Finds index of closest root for given complex number z that is within treshold.
	 * If there is no such root, returns -1.
	 * First root has index 0, second 1, etc.
	 * @param z
	 * @param treshold
	 * @return index of closes root within treshold if such exists, -1 otherwise.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		double min = -1;
		int index = -1;
		for(int i = 0; i < roots.size(); ++i) {
			Complex r = roots.get(i);
			double distance = r.sub(z).module();
			if((distance <= min || min==-1)&&distance<=treshold) {
				min = distance;
				index = i;
			}
		}
		return index;
	}
}
