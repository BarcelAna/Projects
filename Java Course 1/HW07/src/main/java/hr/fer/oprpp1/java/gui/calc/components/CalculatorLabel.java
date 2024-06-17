package hr.fer.oprpp1.java.gui.calc.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

/**
 * Label used for displaying calculator value
 * @author anace
 */
public class CalculatorLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	CalcModelImpl model;
	
	public CalculatorLabel(String text, CalcModelImpl model) {
		this.setText(text);
		this.setOpaque(true);
		this.setBackground(Color.YELLOW);
		this.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setFont(getFont().deriveFont(30f));
		this.model = model;
		this.model.addCalcValueListener((m)->{
			this.setText(m.toString());
		});
	}

}
