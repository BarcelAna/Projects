package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

public class DigitsListener implements ActionListener {
	CalcModelImpl model;
	public DigitsListener(CalcModelImpl model) {
		this.model = model;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		model.insertDigit(Integer.parseInt(((JButton)(e.getSource())).getText()));
	}

}
