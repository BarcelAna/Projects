package hr.fer.oprpp1.java.gui.calc;

import javax.swing.JButton;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hr.fer.oprpp1.java.gui.calc.components.CalcButton;
import hr.fer.oprpp1.java.gui.calc.components.CalculatorLabel;
import hr.fer.oprpp1.java.gui.calc.components.InvCheckBox;

import hr.fer.oprpp1.java.gui.calc.listeners.BinaryOpListener;
import hr.fer.oprpp1.java.gui.calc.listeners.ClrButtonListener;
import hr.fer.oprpp1.java.gui.calc.listeners.DecPointListener;
import hr.fer.oprpp1.java.gui.calc.listeners.DigitsListener;
import hr.fer.oprpp1.java.gui.calc.listeners.EqualsButtonListener;
import hr.fer.oprpp1.java.gui.calc.listeners.PopButtonListener;
import hr.fer.oprpp1.java.gui.calc.listeners.PushButtonListener;
import hr.fer.oprpp1.java.gui.calc.listeners.ResButtonListener;
import hr.fer.oprpp1.java.gui.calc.listeners.SwapSignListener;
import hr.fer.oprpp1.java.gui.calc.listeners.UnaryOpListener;
import hr.fer.oprpp1.java.gui.layouts.CalcLayout;
import hr.fer.oprpp1.java.gui.layouts.RCPosition;

/**
 * Class Calculator represents frame for simple calculator GUI application
 * @author anace
 *
 */
public class Calculator extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * model of calculator
	 */
	CalcModelImpl model = new CalcModelImpl();
	
	public Calculator() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Calculator");
		setLocation(20, 20);
		setSize(700, 400);
		initGUI();
	}
	
	private void initGUI() {
		JPanel p = new JPanel(new CalcLayout(3));
		JButton[] inversableButtons = new JButton[] {new CalcButton("sin", new UnaryOpListener(model)), new CalcButton("log", new UnaryOpListener(model)), new CalcButton("cos", new UnaryOpListener(model)), new CalcButton("ln", new UnaryOpListener(model)), new CalcButton("tan", new UnaryOpListener(model)), new CalcButton("x^n", new BinaryOpListener(model)), new CalcButton("ctg", new UnaryOpListener(model))};
		
		p.add(new CalculatorLabel("0", model), new RCPosition(1, 1));
		p.add(new CalcButton("=", new EqualsButtonListener(model)), new RCPosition(1, 6));
		p.add(new CalcButton("clr", new ClrButtonListener(model)), new RCPosition(1, 7));
		p.add(new CalcButton("1/x", new UnaryOpListener(model)), new RCPosition(2, 1));
		p.add(inversableButtons[0], new RCPosition(2, 2));
		p.add(new CalcButton("7", new DigitsListener(model)), new RCPosition(2, 3));
		p.add(new CalcButton("8", new DigitsListener(model)), new RCPosition(2, 4));
		p.add(new CalcButton("9", new DigitsListener(model)), new RCPosition(2, 5));
		p.add(new CalcButton("/", new BinaryOpListener(model)), new RCPosition(2, 6));
		p.add(new CalcButton("reset", new ResButtonListener(model)), new RCPosition(2, 7));
		p.add(inversableButtons[1], new RCPosition(3, 1));
		p.add(inversableButtons[2], new RCPosition(3, 2));
		p.add(new CalcButton("4", new DigitsListener(model)), new RCPosition(3, 3));
		p.add(new CalcButton("5", new DigitsListener(model)), new RCPosition(3, 4));
		p.add(new CalcButton("6", new DigitsListener(model)), new RCPosition(3, 5));
		p.add(new CalcButton("*", new BinaryOpListener(model)), new RCPosition(3, 6));
		p.add(new CalcButton("push", new PushButtonListener(model)), new RCPosition(3, 7));
		p.add(inversableButtons[3], new RCPosition(4, 1));
		p.add(inversableButtons[4], new RCPosition(4, 2));
		p.add(new CalcButton("1", new DigitsListener(model)), new RCPosition(4, 3));
		p.add(new CalcButton("2", new DigitsListener(model)), new RCPosition(4, 4));
		p.add(new CalcButton("3", new DigitsListener(model)), new RCPosition(4, 5));
		p.add(new CalcButton("-", new BinaryOpListener(model)), new RCPosition(4, 6));
		p.add(new CalcButton("pop", new PopButtonListener(model)), new RCPosition(4, 7));
		p.add(inversableButtons[5], new RCPosition(5, 1));
		p.add(inversableButtons[6], new RCPosition(5, 2));
		p.add(new CalcButton("0", new DigitsListener(model)), new RCPosition(5, 3));
		p.add(new CalcButton("+/-",new SwapSignListener(model)), new RCPosition(5, 4));
		p.add(new CalcButton(".", new DecPointListener(model)), new RCPosition(5, 5));
		p.add(new CalcButton("+", new BinaryOpListener(model)), new RCPosition(5, 6));
		p.add(new InvCheckBox("Inv", model, inversableButtons), new RCPosition(5, 7));
		
		this.getContentPane().add(p);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}
