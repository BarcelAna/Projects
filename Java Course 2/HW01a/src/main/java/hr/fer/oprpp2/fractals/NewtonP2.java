package hr.fer.oprpp2.fractals;

import hr.fer.oprpp1.fractals.CommandFormatException;
import hr.fer.oprpp1.fractals.complexParsing.ComplexParser;
import hr.fer.oprpp1.fractals.complexParsing.ComplexParserException;
import hr.fer.oprpp1.math.Complex;
import hr.fer.oprpp1.math.ComplexPolynomial;
import hr.fer.oprpp1.math.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonP2 {
    private static boolean workersSet = false;
    private static boolean tracksSet = false;
    private static boolean minTracksSet = false;
    private static int workers = 0;
    private static int tracks = 0;
    private static int minTracks = 0;
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
            System.out.println("Correct formats for defining minimum number of tracks are: --mintracks=[number of tracks] or -m [number of tracks]");
            return;
        }
        if(workers == 0) workers = Runtime.getRuntime().availableProcessors();
        if(tracks == 0) tracks = 4 * Runtime.getRuntime().availableProcessors();
        if(minTracks==0) minTracks = 16;

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

    public static class MojProducer implements IFractalProducer {
        private ForkJoinPool pool;

        @Override
        public void setup() {
            pool = new ForkJoinPool();
        }
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int m = 16*16*16;
            short[] data = new short[width * height];
            if(tracks > height) tracks = height;

            pool.invoke(new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, 0, height-1, m, data, cancel));

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short)(polynom.toComplexPolynom().order()+1), requestNo);
        }

        @Override
        public void close() {
            pool.shutdown();
        }
    }

    public static class PosaoIzracuna extends RecursiveAction {
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
        public void compute() {
            int brojYPoTraci = (yMax-yMin+1) / tracks;

            if(brojYPoTraci<=minTracks) {
                computeDirect();
                return;
            }

            int brojTraka = tracks;
            List<PosaoIzracuna> poslovi = new ArrayList<>();
            for(int i = 0; i < brojTraka; i++) {
                int yMin = i*brojYPoTraci+this.yMin;
                int yMax = (i+1)*brojYPoTraci-1 + this.yMin;
                if(i==brojTraka-1) {
                    yMax = this.yMax;
                }
                PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel);
                poslovi.add(posao);
            }
            invokeAll(poslovi);
        }

        private void computeDirect() {
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

    private static void checkCommandFormat(String[] args) {
        if (args.length == 0) {
            return;
        }
        for(int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if(arg.equals("-w") || arg.startsWith("--workers=")) {
                if(workersSet) {
                    throw new CommandFormatException("Wrong command format!");
                }
                workersSet = true;
                if(arg.equals("-w")) {
                    workers = Integer.parseInt(args[i+1]);
                    ++i;
                } else {
                    workers = Integer.parseInt(args[i].substring(args[i].indexOf("=")+1));
                }
            } else if(arg.equals("-t") || arg.startsWith("--tracks=")) {
                if(tracksSet) {
                    throw new CommandFormatException("Wrong command format!");
                }
                tracksSet = true;
                if(arg.equals("-t")) {
                    tracks = Integer.parseInt(args[i+1]);
                    ++i;
                } else {
                    tracks = Integer.parseInt(args[i].substring(args[i].indexOf("=")+1));
                }
            } else if(arg.equals("-m") || arg.startsWith("--mintracks=")) {
                if(minTracksSet) {
                    throw new CommandFormatException("Wrong command format!");
                }
                minTracksSet = true;
                if(arg.equals("-m")) {
                    minTracks = Integer.parseInt(args[i+1]);
                    ++i;
                } else {
                    minTracks = Integer.parseInt(args[i].substring(args[i].indexOf("=")+1));
                }
            } else {
                throw new CommandFormatException("Wrong command format!");
            }
        }
    }

}

