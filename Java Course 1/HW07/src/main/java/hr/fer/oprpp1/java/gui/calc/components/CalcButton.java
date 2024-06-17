package hr.fer.oprpp1.java.gui.calc.components;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * CalcButton represents calculator button
 * @author anace
 *
 */
public class CalcButton extends JButton{

	private static final long serialVersionUID = 1L;
	
	public CalcButton(String text, ActionListener a) {
		setText(text);
		addActionListener(a);
	}

}
