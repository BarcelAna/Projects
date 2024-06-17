package hr.fer.oprpp1.java.gui.calc.components;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;
import hr.fer.oprpp1.java.gui.calc.listeners.InvCheckBoxListener;

/**
 * Checkbox used in calculator for inverting operations
 * @author anace
 *
 */
public class InvCheckBox extends JCheckBox {

	private static final long serialVersionUID = 1L;

	public InvCheckBox(String text, CalcModelImpl model, JButton[] inversableButtons) {
		this.setText(text);
		this.addActionListener(new InvCheckBoxListener(model, inversableButtons));
	}

}
