package hr.fer.oprpp1.java.gui.calc.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.fer.oprpp1.java.gui.calc.CalcModelImpl;

/**
 * Button listener for clear button
 * @author anace
 *
 */
public class ClrButtonListener implements ActionListener {
	private CalcModelImpl model;
	
	public ClrButtonListener(CalcModelImpl model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.clear();
	}

}
