package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

public class SwapSignListener implements ActionListener {
	private CalcModelImpl model;

	public SwapSignListener(CalcModelImpl model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.swapSign();
	}

}
