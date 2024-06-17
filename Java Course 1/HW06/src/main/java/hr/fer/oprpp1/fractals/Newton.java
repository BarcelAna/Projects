package hr.fer.oprpp1.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.oprpp1.fractals.complexParsing.ComplexParser;
import hr.fer.oprpp1.fractals.complexParsing.ComplexParserException;
import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Program for displaying calculating and displaying newton fractal.
 * @author anace
 *
 */
public class Newton {
	private static List<Complex> rootsList;
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		int i = 1;
		String root = "";
		Scanner sc = new Scanner(System.in);
		rootsList = new ArrayList<>();
		@SuppressWarnings("unused")
		ComplexParser p;
		do {
			System.out.print("Root "+i+"> ");
			root = sc.nextLine();
			try{
				if(!root.equals("done")) {
					p = new ComplexParser(root, rootsList);	
					++i;
				}
			} catch(ComplexParserException e) {
				System.out.println("Invalid input format! Complex roots must be in format: a, ia, a + ib, a - ib.");
			}	
		} while(!root.equals("done"));
		sc.close();
		System.out.println("Image of fractal will appear shortly. Thank you.");
		FractalViewer.show(new MojProducer());
	}
	
	public static class MojProducer implements IFractalProducer {
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int m = 16*16*16;
			int offset = 0;
			short[] data = new short[width * height];
			ComplexRootedPolynomial polynom =  new ComplexRootedPolynomial(Complex.ONE, rootsList.toArray(new Complex[0]));
			ComplexPolynomial derived = polynom.toComplexPolynom().derive();
			for(int y = 0; y < height; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					double module = 0;
					int iters = 0;
					Complex zn = new Complex(cre, cim);
					do {
						Complex numerator = polynom.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						++iters;
					} while(iters < m && module > 0.001);
					int index = polynom.indexOfClosestRootFor(zn, 0.002);
					data[offset] = (short)(index+1);
					offset++;
				}
			}
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynom.toComplexPolynom().order()+1), requestNo);
		}
	}
}
