package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

public class PushButtonListener implements ActionListener {
	private CalcModelImpl model;

	public PushButtonListener(CalcModelImpl model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.stack.push(model.getValue());
		model.clear();
	}

}
