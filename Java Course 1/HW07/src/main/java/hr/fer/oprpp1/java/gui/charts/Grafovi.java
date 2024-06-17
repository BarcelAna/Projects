package hr.fer.oprpp1.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * BarChartDemo represents GUI program for displaying simple bar chart
 * @author anace
 *
 */
public class Grafovi extends JFrame {

	public static BarChart barChartInit;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * title of x axis
	 */
	static String xTitle = "";
	
	/**
	 * title of y axis
	 */
	static String yTitle = "";
	
	/**
	 * list of bar chart's values
	 */
	static List<XYValue> list = new ArrayList<>();
	
	/**
	 * minimum y value in y-axis
	 */
	static int minY = 0;
	
	/**
	 * maximum y value in y-axis
	 */
	static int maxY = 0;
	
	/**
	 * distance between two neighbour y values on y-axis
	 */
	static int deltaY = 0;
	
	private static String path = "";
	
	public static List<BarChart> models;
	
	public static List<BarChartComponent> graphs;
	
	public Grafovi(BarChart barChart) {
		this.setTitle("My bar chart");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(20, 20);
		this.setSize(400, 400);
		initGUI(barChart);
	}

	private void initGUI(BarChart barChart) {
		this.setLayout(new BorderLayout());
		
		JPanel graphPanel = new JPanel(new GridLayout(2,2));
		models = new ArrayList<>();
		graphs = new ArrayList<>();
		
		
		for(int i = 0; i < 4; ++i) {
			BarChart bcModel = new BarChart(Grafovi.list, Grafovi.xTitle, Grafovi.yTitle, Grafovi.minY, Grafovi.maxY, Grafovi.deltaY);
			BarChartComponent c = new BarChartComponent(bcModel);
			models.add(bcModel);
			graphs.add(c);
			graphPanel.add(c);
			System.out.println("Ušla");
		}
		
		JPanel buttonsPanel = new JPanel(new GridLayout(4, 1));
		List<JButton> buttons = new ArrayList<>();
		for(int i = 0; i < 4; ++i) {
			JButton b = new JButton(Integer.toString(i+1));
			b.addActionListener((a)->{
				int index = buttons.indexOf((JButton)(a.getSource()));
				BarChart model = models.get(index);
				List<XYValue> newValues = new ArrayList<>();
				for(int j = 0; j < model.getValues().size(); ++j) {
					int newVY = new Random().nextInt(model.getMaxY())+model.getMinY();
					newValues.add(new XYValue(model.getValues().get(j).getX(), newVY));
				}
				model.values = newValues;
				graphs.get(index).repaint();
			});
			buttons.add(b);
			buttonsPanel.add(b);
		}

		
		for(int i = 0; i < 4; ++i) {
			JButton b = buttons.get(i);
			b.addActionListener((a)->{
				int index = buttons.indexOf((JButton)(a.getSource()));
				BarChart model = models.get(index);
				List<XYValue> newValues = new ArrayList<>();
				for(int j = 0; j < model.getValues().size(); ++j) {
					int newVY = new Random().nextInt(model.getMaxY())+model.getMinY();
					newValues.add(new XYValue(model.getValues().get(j).getX(), newVY));
				}
				model.values = newValues;
				graphs.get(index).repaint();
			});
		}
		this.getContentPane().add(buttonsPanel, BorderLayout.LINE_START);
		this.getContentPane().add(graphPanel, BorderLayout.CENTER);
		
	}

	
	public static void main(String[] args) {
		Path filePath = Path.of(args[0]);
		path = filePath.toString();
		
		if(Files.isDirectory(filePath)) {
			System.out.println("Given path must be file.");
			return;
		}
		if(!Files.isReadable(filePath)) {
			System.out.println("File is not readable.");
			return;
		}
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(
				new BufferedInputStream(
						Files.newInputStream(filePath)),
						"UTF-8")
					)
				) {
			xTitle=br.readLine();
			yTitle=br.readLine();
			String[] xyValues = br.readLine().split("\\s+");
			for(String xyValue : xyValues) {
				String[] values = xyValue.split(",");
				int x;
				int y;
				try {
					x = Integer.parseInt(values[0]);
					y = Integer.parseInt(values[1]);
				} catch(NumberFormatException e) {
					System.out.println("Given values must be numbers.");
					return;
				}
				list.add(new XYValue(x, y));
			}
			try {
				minY = Integer.parseInt(br.readLine());
				maxY = Integer.parseInt(br.readLine());
				deltaY = Integer.parseInt(br.readLine());
			} catch(NumberFormatException e) {
				System.out.println("Given values for miny, maxy and deltay must be numbers.");
				return;
			}
		} catch(Exception e) {
			System.out.println("Given is not in the right format");
			return;
		}
		SwingUtilities.invokeLater(()->{
			new Grafovi(new BarChart(list, xTitle, yTitle, minY, maxY, deltaY)).setVisible(true);
		});
	}
}
