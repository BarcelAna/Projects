package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

/**
 * Button listener for equals button
 * @author anace
 *
 */
public class EqualsButtonListener implements ActionListener {
	private CalcModelImpl model;
	
	public EqualsButtonListener(CalcModelImpl model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
		model.setValue(result);
		model.clearActiveOperand();
		model.setPendingBinaryOperation(null);
		
	}

}
