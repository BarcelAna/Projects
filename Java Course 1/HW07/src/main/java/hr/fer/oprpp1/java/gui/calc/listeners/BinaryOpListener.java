package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

/**
 * Listener for some binary operations buttons such as +, -, *, /, etc.
 * @author anace
 *
 */
public class BinaryOpListener implements ActionListener {
	CalcModelImpl model;
	
	public BinaryOpListener(CalcModelImpl model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(model.isActiveOperandSet()) {
			new EqualsButtonListener(model).actionPerformed(e);
		} 
		model.setActiveOperand(model.getValue());
		String text = ((JButton)e.getSource()).getText();
		if(text.equals("+")) {
			model.setPendingBinaryOperation(Double::sum);
		} else if(text.equals("-")) {
			model.setPendingBinaryOperation((left, right) -> left-right);
		} else if(text.equals("*")) {
			model.setPendingBinaryOperation((left, right)->left*right);
		} else if(text.equals("/")) {
			model.setPendingBinaryOperation((left, right)->left/right);
		} else if(text.equals("x^n")) {
			model.setPendingBinaryOperation((left, right)->Math.pow(left, right));
		} else if(text.equals("x^(1/n)")) {
			model.setPendingBinaryOperation((left, right)->Math.pow(left, 1 / right));
		}
		model.freezeValue(model.toString());
		model.clear();
	}

}
