package hr.fer.oprpp1.java.gui.calc.listeners;

import hr.fer.oprpp1.java.gui.calc.CalcModel;

/**
 * Observer model which is interested in changes in calculatora
 * 
 * @author marcupic
 */
public interface CalcValueListener {
	/**
	 * Called when calculator value is changed 
	 * 
	 * @param model -  calculator model in which change happened, can't be null.
	 */
	void valueChanged(CalcModel model);
}
