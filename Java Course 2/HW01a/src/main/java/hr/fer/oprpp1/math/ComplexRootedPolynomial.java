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
	private hr.fer.oprpp1.math.Complex constant;
	private List<hr.fer.oprpp1.math.Complex> roots;
	
	/**
	 * Constructor which accepts complex constant z0 and complex roots: z1, ..., zn
	 * @param constant
	 * @param roots
	 */
	public ComplexRootedPolynomial(hr.fer.oprpp1.math.Complex constant, hr.fer.oprpp1.math.Complex... roots) {
		this.constant = constant;
		this.roots = new ArrayList<>();
		for(hr.fer.oprpp1.math.Complex r : roots) {
			this.roots.add(r);
		}
	}

	/**
	 * Computes polynomial value at given point z
	 * @param z
	 * @return complex solution of f(z) for given z
	 */
	public hr.fer.oprpp1.math.Complex apply(hr.fer.oprpp1.math.Complex z) {
		hr.fer.oprpp1.math.Complex result = constant;
		for(hr.fer.oprpp1.math.Complex r : roots) {
			result = result.multiply(z.sub(r));
		}
		return result;
	}
	
	/**
	 * Converts complex rooted polynomial to complex polynomial type given as f(z) = zn*z^n+z(n-1)*z^(n-1)+...+z1*z + z0 
	 * @return complex polynomial type of current complex rooted polynomial
	 */
	public hr.fer.oprpp1.math.ComplexPolynomial toComplexPolynom() {
		hr.fer.oprpp1.math.ComplexPolynomial result = new hr.fer.oprpp1.math.ComplexPolynomial(constant);
		for(hr.fer.oprpp1.math.Complex r : roots) { //z - r
			result = result.multiply(new ComplexPolynomial(r.negate(), hr.fer.oprpp1.math.Complex.ONE));
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
		for(hr.fer.oprpp1.math.Complex r : roots) {
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
	public int indexOfClosestRootFor(hr.fer.oprpp1.math.Complex z, double treshold) {
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
