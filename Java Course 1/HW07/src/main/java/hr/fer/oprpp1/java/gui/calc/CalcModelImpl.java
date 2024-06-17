package hr.fer.oprpp1.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

import hr.fer.oprpp1.java.gui.calc.listeners.CalcValueListener;

/**
 * Class CalcModelImple represents implementation of CalcModel interface.
 * @author anace
 *
 */
public class CalcModelImpl implements CalcModel {
	/**
	 * flag specifies if model is editable(swapSign, insertDecimalPoint, insertDigit allowed) or not
	 */
	private boolean editable = true;
	
	/**
	 * flag specifies whether stored number is positive or not(negative)
	 */
	private boolean positive = true;
	
	/**
	 * private variable that stores digits entered in calculator as string
	 */
	private String inputDigitsString = "";
	
	/**
	 * private variable which stores digits entered in calculator as double
	 */
	private double inputDigitsValue = 0.0;
	/**
	 * private variable which stores current frozen value of calculator
	 */
	private String frozenValue = null;
	
	/**
	 * private variable which stores active operand
	 */
	private Double activeOperand = null;
	
	/**
	 * private variable which stores pending operation
	 */
	private DoubleBinaryOperator pendingOperation = null;
	
	/**
	 * private variable which stores listeners subscribed for this model
	 */
	private List<CalcValueListener> listeners = new ArrayList<>();
	
	private boolean inversed = false;
	
	public Stack<Double> stack = new Stack<>();

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if(l == null) {
			throw new NullPointerException("Given listener must not be null");
		}
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		if(l == null) {
			throw new NullPointerException("Given listener must not be null");
		}
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		if(positive)
			return inputDigitsValue;
		else
			return -1 * inputDigitsValue;
	}

	@Override
	public void setValue(double value) {
		if(value < 0) positive = false;
		else positive = true;
		
		this.inputDigitsValue = Math.abs(value);
		
		if(Double.isInfinite(value)) {
			this.inputDigitsString = "Infinity";
		} else if(Double.isNaN(value)) {
			this.inputDigitsString = "NaN";
		} else {
			this.inputDigitsString = Double.toString(this.inputDigitsValue);
		}
		
		editable = false;
		
		for(CalcValueListener l : listeners) {
			l.valueChanged(this);
		}

	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		this.inputDigitsString = "";
		this.inputDigitsValue = 0.0;
		this.editable = true;
		this.positive = true;
		for(CalcValueListener l : listeners) {
			l.valueChanged(this);
		}
	}

	@Override
	public void clearAll() {
		this.clear();
		this.activeOperand = null;
		this.pendingOperation = null;

	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("Can't swap sign because model is not editable.");
		}
		this.frozenValue = null;
		positive = !positive;
		for(CalcValueListener l : listeners) {
			l.valueChanged(this);
		}
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("Can't insert decimal point because model is not editable");
		}
		if(inputDigitsString.contains(".")) {
			throw new CalculatorInputException("Can't insert decimal point because entered number already contains decimal point.");
		}
		if(inputDigitsString.equals("")) {
			throw new CalculatorInputException("Can't insert decimal point because there are no digits enteren before");
		}
		this.frozenValue = null;
		inputDigitsString += ".";
		for(CalcValueListener l : listeners) {
			l.valueChanged(this);
		}
		
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!editable) {
			throw new CalculatorInputException("Digit can't be inserted because model is not editable.");
		}
		if(digit < 0 || digit > 9) {
			throw new CalculatorInputException("Inserted digit must be digit between 0 and 9.");
		}
		
		String tempString = inputDigitsString + digit;
		double temp;
		try {
			temp = Double.parseDouble(tempString);
		} catch(NullPointerException | NumberFormatException e) {
			throw new CalculatorInputException("Can't insert digit because it's value is not parsable.");
		}
		if(Double.isInfinite(temp)) {
			throw new CalculatorInputException("Digit you are trying to enter would cause owerflow");
		}
		this.frozenValue = null;
		if(this.inputDigitsString.equals("0") && digit == 0) {
			return;
		} else if(this.inputDigitsString.equals("0") && digit > 0) {
			this.inputDigitsString = Integer.toString(digit);
			this.inputDigitsValue = digit;
		}else {
			this.inputDigitsString = tempString;
			this.inputDigitsValue = temp;
		}
		
		for(CalcValueListener l : listeners) {
			l.valueChanged(this);
		}
		
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(activeOperand == null) throw new IllegalStateException("Active operand is not set.");
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		this.activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return this.pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;

	}
	
	@Override
	public String toString() {
		if(!hasFrozenValue()) {
			if(inputDigitsString.equals("")) {
				if(positive) {
					return "0";
				} else {
					return "-0";
				}
			} else {
				if(positive) {
					return inputDigitsString;
				} else {
					return "-" + inputDigitsString;
				}
			}
		} else {
			return frozenValue;
		}
	}
	
	@Override
	public void freezeValue(String value) {
		this.frozenValue = value;
	}
	
	@Override
	public boolean hasFrozenValue() {
		return frozenValue != null;
	}
	
	public boolean isInversed() {
		return inversed;
	}

	public void inverse() {
		this.inversed = !this.inversed;
	}
	
	


}
