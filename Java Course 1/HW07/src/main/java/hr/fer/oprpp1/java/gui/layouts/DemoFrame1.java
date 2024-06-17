package hr.fer.oprpp1.java.gui.layouts;

import java.awt.Color;


import java.awt.Component;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Class DemoFrame1 extends JFrame and represents simple frame for our gui application.
 * @author anace
 *
 */
public class DemoFrame1 extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public DemoFrame1() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//setSize(500, 500);
		initGUI();
		pack();
	}

	/**
	 * Initialization method for GUI. Sets layout and adds components.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		((JComponent)cp).setBorder( 
		        BorderFactory.createMatteBorder(10, 10, 10, 10, Color.blue ) 
		 );
		
		//cp = new JPanel(new CalcLayout(3));
		/*cp.setLayout(new CalcLayout(3));
		cp.add(new JLabel("-273.351"), new RCPosition(1, 1));
		cp.add(new JButton("="), new RCPosition(1, 6));
		cp.add(new JButton("clr"), new RCPosition(1, 7));
		cp.add(new JButton("1/x"), new RCPosition(2, 1));
		cp.add(new JButton("sin"), new RCPosition(2, 2));
		cp.add(new JButton("7"), new RCPosition(2, 3));
		cp.add(new JButton("8"), new RCPosition(2, 4));
		cp.add(new JButton("9"), new RCPosition(2, 5));
		cp.add(new JButton("/"), new RCPosition(2, 6));
		cp.add(new JButton("res"), new RCPosition(2, 7));
		cp.add(new JButton("log"), new RCPosition(3, 1));
		cp.add(new JButton("cos"), new RCPosition(3, 2));
		cp.add(new JButton("4"), new RCPosition(3, 3));
		cp.add(new JButton("5"), new RCPosition(3, 4));
		cp.add(new JButton("6"), new RCPosition(3, 5));
		cp.add(new JButton("*"), new RCPosition(3, 6));
		cp.add(new JButton("push"), new RCPosition(3, 7));
		cp.add(new JButton("ln"), new RCPosition(4, 1));
		cp.add(new JButton("tan"), new RCPosition(4, 2));
		cp.add(new JButton("1"), new RCPosition(4, 3));
		cp.add(new JButton("2"), new RCPosition(4, 4));
		cp.add(new JButton("3"), new RCPosition(4, 5));
		cp.add(new JButton("-"), new RCPosition(4, 6));
		cp.add(new JButton("pop"), new RCPosition(4, 7));
		cp.add(new JButton("x^n"), new RCPosition(5, 1));
		cp.add(new JButton("ctg"), new RCPosition(5, 2));
		cp.add(new JButton("0"), new RCPosition(5, 3));
		cp.add(new JButton("+/-"), new RCPosition(5, 4));
		cp.add(new JButton("."), new RCPosition(5, 5));
		cp.add(new JButton("+"), new RCPosition(5, 6));
		cp.add(new JCheckBox("Inv"), new RCPosition(5, 7)); 
		//cp.add(p);
		pack();*/
		
		/*JPanel p = new JPanel(new CalcLayout(3));
		p.add(l("x"), new RCPosition(1,1));
		p.add(l("y"), new RCPosition(2,3));
		p.add(l("z"), new RCPosition(2,7));
		p.add(l("w"), new RCPosition(4,2));
		p.add(l("a"), new RCPosition(4,5));
		p.add(l("b"), new RCPosition(4,7));
		this.getContentPane().add(p);
		pack();  */
		
		//JPanel p = new JPanel(new CalcLayout(3));
		/*p.add(new JLabel("x"), "1,1");
		p.add(new JLabel("y"), "2,3");
		p.add(new JLabel("z"), "2,7");
		p.add(new JLabel("w"), "4,2");
		p.add(new JLabel("a"), "4,5");
		p.add(new JLabel("b"), "4,7");
		cp.add(p);
		pack();*/
		cp.setLayout(new CalcLayout(3));
		cp.add(l("tekst 1"), new RCPosition(1,1));
		cp.add(l("tekst 2"), new RCPosition(2,3));
		cp.add(l("tekst stvarno najdulji"), new RCPosition(2,7));
		cp.add(l("tekst kraći"), new RCPosition(4,2));
		cp.add(l("tekst srednji"), new RCPosition(4,5));
		cp.add(l("tekst"), new RCPosition(4,7));
	}

	/**
	 * Private class for initializing JLable component used in GUI app.
	 * @param text - label text
	 * @return JLabel component
	 */
	private Component l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new DemoFrame1().setVisible(true);
		});
	}
}


