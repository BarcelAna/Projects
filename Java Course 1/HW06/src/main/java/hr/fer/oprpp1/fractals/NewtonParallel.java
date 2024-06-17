package hr.fer.oprpp1.fractals;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
 * Program for calculating and displaying Newton fractal using multi-threaded approach.
 * @author anace
 *
 */
public class NewtonParallel {
	
	private static boolean workersSet = false;
	private static boolean tracksSet = false;
	private static int workers = 0;
	private static int tracks = 0;
	private static int cnt = 0;
	private static List<Complex> rootsList;
	private static ComplexRootedPolynomial polynom;
	private static ComplexPolynomial derived;
	
	public static void main(String[] args) {
		try {
			checkCommandFormat(args);
		} catch(CommandFormatException e) {
			System.out.println("Wrong command format!");
			System.out.println("Correct formats for defining number of workers are: --workers=[number of workers] or -w [number of workers]");
			System.out.println("Correct formats for defining number of tracks are: --tracks=[number of tracks] or -w [number of tracks]");
			return;
		}
		if(workers == 0) workers = Runtime.getRuntime().availableProcessors();
		if(tracks == 0) tracks = 4 * Runtime.getRuntime().availableProcessors();
		
		System.out.println("Welcome to Newton-Raphson multi-threaded-based fractal viewer.");
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
		
		polynom =  new ComplexRootedPolynomial(Complex.ONE, rootsList.toArray(new Complex[0]));
		derived = polynom.toComplexPolynom().derive();
		
		System.out.println("Image of fractal will appear shortly. Thank you.");	
		System.out.println("Number of threads:" + workers);
		System.out.println("Number of jobs:" + tracks);
		
		FractalViewer.show(new MojProducer());
	}
	
	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		private PosaoIzracuna() {
		}
		
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}
		
		@Override
		public void run() {
			int offset = this.yMin*this.width;
			for(int y = this.yMin; y <= this.yMax; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					double module;
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
			
		}
	}
	
	public static class MojProducer implements IFractalProducer {
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int m = 16*16*16;
			short[] data = new short[width * height];
			if(tracks > height) tracks = height;
			final int brojTraka = tracks;
			int brojYPoTraci = height / brojTraka; 
			
			final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

			Thread[] radnici = new Thread[workers];
			for(int i = 0; i < radnici.length; i++) {
				radnici[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							PosaoIzracuna p = null;
							try {
								p = queue.take();
								if(p==PosaoIzracuna.NO_JOB) break;
							} catch (InterruptedException e) {
								continue;
							}
							p.run();
						}
					}
				});
			}
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);
				while(true) {
					try {
						queue.put(posao);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						queue.put(PosaoIzracuna.NO_JOB);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynom.toComplexPolynom().order()+1), requestNo);
		}
	}

	/**
	 * Utility method for checking entered command format.
	 * @param args
	 * @throws CommandFormatException if command is in wrong format
	 */
	private static void checkCommandFormat(String[] args) {
		if(args.length == 0) {
			return;
		}
		if(args[0].startsWith("--workers=") && !workersSet) {
			++cnt;
			try {
				workers = Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1));
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			workersSet = true;
		} else if(args[0].startsWith("--tracks") && !tracksSet) {
			++cnt;
			try {
				tracks = Integer.parseInt(args[0].substring(args[0].indexOf('=') + 1));
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			tracksSet = true;
		} else if(args[0].equals("-w") && !workersSet && args[1] != null) {
			cnt += 2;
			try {
				workers = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			workersSet = true;
		} else if(args[0].equals("-t") && !tracksSet && args[1] != null) {
			cnt += 2;
			try {
				tracks = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			tracksSet = true;
		} else {
			throw new CommandFormatException("Wrong command format!");
		}
		
		if(cnt >= args.length) {
			return;
		}
		if(args[cnt].startsWith("--workers=") && !workersSet) {
			try {
				workers = Integer.parseInt(args[cnt].substring(args[cnt].indexOf('=') + 1));
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			workersSet = true;
		} else if(args[cnt].startsWith("--tracks") && !tracksSet) {
			try {
				tracks = Integer.parseInt(args[cnt].substring(args[cnt].indexOf('=') + 1));
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			tracksSet = true;
		} else if(args[cnt].equals("-w") && !workersSet && args[cnt+1] != null) {
			try {
				workers = Integer.parseInt(args[cnt+1]);
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			workersSet = true;
		} else if(args[cnt].equals("-t") && !tracksSet && args[cnt + 1] != null) {
			try {
				tracks = Integer.parseInt(args[cnt+1]);
			} catch(NumberFormatException e) {
				throw new CommandFormatException("Wrong command format!");
			}
			tracksSet = true;
		} else {
			throw new CommandFormatException("Wrong command format!");
		}
	}
}
