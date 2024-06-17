package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

public class PopButtonListener implements ActionListener {
	private CalcModelImpl model;

	public PopButtonListener(CalcModelImpl model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(model.stack.isEmpty()) {
			throw new EmptyStackException();
		}
		model.freezeValue(null);
		model.setValue(model.stack.pop());

	}

}
