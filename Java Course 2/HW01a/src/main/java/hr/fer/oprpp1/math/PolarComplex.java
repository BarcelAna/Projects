package hr.fer.oprpp1.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Class PolarComplex represents polar form of complex number.
 * Complex numbers in polar form: r*(cos(fi)+isin(fi)) where r is module of z and fi is radian angle calculated as atan(y / x) where z = x + iy.
 * @author anace
 *
 */
public class PolarComplex {
	/**
	 * Radius of polar complex number
	 */
	double r;
	
	/**
	 * Radian angle of polar complex number.
	 */
	double fi;
	
	/**
	 * Constructor which accepts radius r and angle fi and sets its' values to given numbers.
	 * @param r
	 * @param fi
	 */
	public PolarComplex(double r, double fi) {
		this.r = r;
		this.fi = fi;
	}
	
	/**
	 * Constructor with given complex number from which polar complex number is then created.
	 * @param c
	 */
	public PolarComplex(hr.fer.oprpp1.math.Complex c) {
		r = c.module();
		fi = Math.atan2(c.getIm(), c.getRe());
	}
	
	/**
	 * Method for calculating n-th root of polar complex number.
	 * @param n
	 * @return List of polar complex roots.
	 */
	public List<PolarComplex> root(int n) {
		List<PolarComplex> result = new ArrayList<>();
		double exp = 1. / n;
		double resultR = Math.pow(r, exp);
		for(int k = 0; k < n; ++k) {
			result.add(new PolarComplex(resultR, (fi + 2 * Math.PI * k) / n));
		}
		return result;
	}
	
	/**
	 * Method for transforming complex number in polar format to regular complex number.
	 * @return complex number
	 */
	public hr.fer.oprpp1.math.Complex toComplex() {
		double re = r*Math.cos(fi);
		double im = r*Math.sin(fi);
		return new Complex(re, im);
	}
	
	/**
	 * Method for calculating n-th power of complex number in polar form
	 * @param n
	 * @return result as polar complex number 
	 */
	public PolarComplex power(int n) {
		return new PolarComplex(Math.pow(r, n), n * fi);
	}
}