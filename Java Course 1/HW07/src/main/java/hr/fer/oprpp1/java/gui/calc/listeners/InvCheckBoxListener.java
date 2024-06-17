package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

public class InvCheckBoxListener implements ActionListener {
	CalcModelImpl model;
	JButton[] iButtons;

	public InvCheckBoxListener(CalcModelImpl model, JButton[] inversableButtons) {
		this.model = model;
		this.iButtons = inversableButtons;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.inverse();
		for(JButton b : iButtons) {
			if(b.getText().equals("log")) {
				b.setText("10^x");
			}else if(b.getText().equals("ln")) {
				b.setText("e^x");
			}else if(b.getText().equals("x^n")) {
				b.setText("x^(1/n)");
			}else if(b.getText().equals("sin")) {
				b.setText("arcsin");
			}else if(b.getText().equals("cos")) {
				b.setText("arccos");
			}else if(b.getText().equals("tan")) {
				b.setText("arctan");
			} else if(b.getText().equals("ctg")) {
				b.setText("arcctg");
			} else if(b.getText().equals("10^x")) {
				b.setText("log");
			} else if(b.getText().equals("e^x")) {
				b.setText("ln");
			} else if(b.getText().equals("arcsin")) {
				b.setText("sin");
			} else if(b.getText().equals("arccos")) {
				b.setText("cos");
			} else if(b.getText().equals("arctan")) {
				b.setText("tan");
			} else if(b.getText().equals("arcctg")) {
				b.setText("ctg");
			} else if(b.getText().equals("x^(1/n)")) {
				b.setText("x^n");
			}
		}

	}

}
