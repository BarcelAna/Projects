package hr.fer.oprpp1.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * BarChartDemo represents GUI program for displaying simple bar chart
 * @author anace
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
	 * title of x axis
	 */
	public static String xTitle = "";
	
	/**
	 * title of y axis
	 */
	public static String yTitle = "";
	
	/**
	 * list of bar chart's values
	 */
	public static List<XYValue> list = new ArrayList<>();
	
	/**
	 * minimum y value in y-axis
	 */
	public static int minY = 0;
	
	/**
	 * maximum y value in y-axis
	 */
	public static int maxY = 0;
	
	/**
	 * distance between two neighbour y values on y-axis
	 */
	public static int deltaY = 0;
	
	private static String path = "";
	
	public BarChartDemo(BarChart barChart) {
		this.setTitle("My bar chart");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(20, 20);
		this.setSize(400, 400);
		initGUI(barChart);
	}

	private void initGUI(BarChart barChart) {
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new BarChartComponent(barChart), BorderLayout.CENTER);
		JLabel pathLabel = new JLabel(path);
		pathLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pathLabel.setBackground(Color.LIGHT_GRAY);
		pathLabel.setOpaque(true);
		c.add(pathLabel, BorderLayout.PAGE_START);
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
			new BarChartDemo(new BarChart(list, xTitle, yTitle, minY, maxY, deltaY)).setVisible(true);
		});
	}
}
