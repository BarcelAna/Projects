package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

/**
 * Listener for unary operation button
 * @author anace
 *
 */
public class UnaryOpListener implements ActionListener {
	private CalcModelImpl model;
	
	public UnaryOpListener(CalcModelImpl model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String text = ((JButton)e.getSource()).getText();
		if(text.equals("1/x")) {
			model.setValue(1 / model.getValue());
		} else if(text.equals("log")) {
			model.setValue(Math.log10(model.getValue()));
		} else if(text.equals("10^x")) {
			model.setValue(Math.pow(10, model.getValue()));
		} else if(text.equals("ln")) {
			model.setValue(Math.log(model.getValue()));
		} else if(text.equals("e^x")) {
			model.setValue(Math.pow(Math.E, model.getValue()));
		} else if(text.equals("sin")) {
			model.setValue(Math.sin(model.getValue()));
		} else if(text.equals("arcsin")) {
			model.setValue(Math.asin(model.getValue()));
		} else if(text.equals("cos")) {
			model.setValue(Math.cos(model.getValue()));
		} else if(text.equals("arccos")) {
			model.setValue(Math.acos(model.getValue()));
		} else if(text.equals("tan")) {
			model.setValue(Math.tan(model.getValue()));
		} else if(text.equals("arctan")) {
			model.setValue(Math.atan(model.getValue()));
		} else if(text.equals("ctg")) {
			model.setValue(1 / Math.tan(model.getValue()));
		} else if(text.equals("arcctg")) {
			model.setValue(1 / Math.atan(model.getValue()));
		}
		//model.freezeValue(model.toString());
		//model.clear();
	}

}
