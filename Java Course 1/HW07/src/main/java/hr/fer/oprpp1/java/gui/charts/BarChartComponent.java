package hr.fer.oprpp1.java.gui.charts;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * Class BarChartComponent represents GUI component which displays simple bar chart
 * @author anace
 *
 */
public class BarChartComponent extends JComponent{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * model of bar chart
	 */
	private BarChart model;
	
	/**
	 * Fixed space between some elements of component
	 */
	private static final int SPACE = 10;
	
	
	/**
	 * Constructor which accpets bar chart model
	 * @param model
	 */
	public BarChartComponent(BarChart model) {
		this.model = model;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		List<XYValue> list = model.getValues();
		
		Font labelFont = new Font("Arial", Font.BOLD, 14);
		FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);
		
		Font titleFont = new Font("Arial", Font.PLAIN, 14);
		FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
		
		int top = SPACE;
		int bottom = titleFontMetrics.getHeight() + SPACE +labelFontMetrics.getHeight() + SPACE;
		int left = titleFontMetrics.getHeight() + SPACE + labelFontMetrics.stringWidth(Integer.toString(model.getMaxY())) + SPACE;
		double scale = (this.getHeight() - top -bottom) / (model.getMaxY() - model.getMinY());
		int barWidth = (this.getWidth() - left - SPACE) / list.size();
		
		g.setFont(titleFont);
		g.setColor(Color.BLACK);
		
		//write x-axis title
		int titleXWidth = titleFontMetrics.stringWidth(model.getxTitle());
		int titleXX = ((this.getWidth()-left - SPACE) - titleXWidth) / 2 + left;
		int titleXY = this.getHeight() - titleFontMetrics.getDescent();
		g.drawString(model.getxTitle(), titleXX, titleXY);
		
		//write y-axis title
		Font yTitleFont = new Font("Arial", Font.PLAIN, 20);
		FontMetrics yTitleFontMetrics = g.getFontMetrics(yTitleFont);
		
		int titleYWidth = yTitleFontMetrics.stringWidth(model.getyTitle());
		System.out.println(titleYWidth);
		int titleYX = ((this.getHeight() - top - bottom)-titleYWidth) / 2 + 2*titleYWidth + SPACE; 
		int titleYY = yTitleFontMetrics.getHeight();
		
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform defaultAt = g2d.getTransform();
		
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI/2);
		g2d.setTransform(at);
		g2d.setFont(yTitleFont);
		g2d.drawString(model.getyTitle(), -titleYX, titleYY);
		g2d.setTransform(defaultAt);
		
		//write y-axis labels
		g.setFont(labelFont);
		for(int i = 0; i < (model.getMaxY()-model.getMinY()) / model.getDeltaY() + 1;++i) {
			int num = model.getMinY() + i * model.getDeltaY();
			int numX = left - SPACE - labelFontMetrics.stringWidth(Integer.toString(num));
			int numY = (int)((model.getMaxY()-i * model.getDeltaY())*scale) + labelFontMetrics.getHeight();
			g.drawString(Integer.toString(num), numX, numY);
		}
		
		//draw x-axis
		int xAxisX1 = left;
		int xAxisX2 = this.getWidth() - SPACE + 2;
		int xAxisY = (int)(this.model.getMaxY() * scale) + top + 1;
		drawArrow((Graphics2D)g, xAxisX1, xAxisY, xAxisX2, xAxisY, 5, 5);
		
		//draw y-axis
		int yAxisX = left;
		int yAxisY1 = (int)(this.model.getMaxY() * scale) + top + 1;;
		int yAxisY2 = top/2;
		drawArrow((Graphics2D)g, yAxisX, yAxisY1, yAxisX, yAxisY2, 5, 5);
		
		//draw network
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 1; i < (model.getMaxY()-model.getMinY());++i) {
			int lineY = (int)((model.getMaxY()-i * model.getDeltaY())*scale) + labelFontMetrics.getAscent();
			int lineX1 = left - SPACE/2;
			int lineX2 = this.getWidth() - SPACE ;
			g.drawLine(lineX1, lineY, lineX2, lineY);
		}
		
		for(int i = 1; i < (model.getValues().size());++i) {
			int lineX = i * barWidth + left;
			int lineY1 = (int)(this.model.getMaxY()*scale) + top + SPACE/2;
			int lineY2 = top;
			g.drawLine(lineX, lineY1, lineX, lineY2);
		}
		
		//draw bars
		for(int i = 0; i < list.size(); ++i) {
			XYValue v = list.get(i);
			int valueX = left + i * barWidth + 1;
			int valueY = top + (int)((model.getMaxY()-v.getY() + model.getMinY()) * scale);
			int height = (int)((v.getY()-model.getMinY()) * scale);
			
			g.setColor(new Color(244, 119, 72));
			g.fillRect(valueX, valueY, barWidth - 2, height);
			
			g.setColor(Color.BLACK);
			g.setFont(labelFont);
			int labelWidth = labelFontMetrics.stringWidth(Integer.toString(v.getX()));
			int y = (int)(this.model.getMaxY() * scale) + top + SPACE + SPACE;
			int x = left + i * barWidth + (barWidth - labelWidth) / 2;
			g.drawString(Integer.toString(v.getX()), x, y);
		}
	}
	
	/**
	 * Utility method for drawing line with arrow at the end
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param d
	 * @param h
	 */
	private void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - d, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		
		g.drawLine(x1, y1, x2, y2);
		g.fillPolygon(xpoints, ypoints, 3);

	}
		

}
