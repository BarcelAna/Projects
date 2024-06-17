package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Program for evaluating postfix expressions.
 * It accepts single command-line argument: expression that should be evaluated.
 * @author anace
 *
 */
public class StackDemo {

	public static void main(String[] args) {
		String expression = args[0];
		ObjectStack stack = new ObjectStack(); 
		String[] elements = expression.trim().split("\\s+");
		
		for(String element : elements) {
			try {
				int number = Integer.parseInt(element);
				stack.push(number);
			} catch(NumberFormatException e) {
				Object o1 = null;
				Object o2 = null;
				Integer number1 = 0;
				Integer number2 = 0;
				if(element.equals("cubed")) {
					o1 = stack.pop();
					number1 = Integer.parseInt(o1.toString());
				} else {
					o1 = stack.pop();
					o2 = stack.pop();		
					number1 = Integer.parseInt(o1.toString());
					number2 = Integer.parseInt(o2.toString());
				}
		
				int result = calculate(number1, number2, element);
				stack.push(result);
			}
		}
		
		if(stack.size() != 1) System.err.print("Error: Invalid expression!");
		else {
			System.out.println("Expression evaluates to " + stack.pop());
		}

	}

	private static int calculate(int number1, int number2, String operator) {
		int result = 0;
		switch(operator) {
			case "+" :
				result = number2 + number1;
				break;
			case "-" :
				result = number2 - number1;
				break;
			case "*" :
				result = number2 * number1;
				break;
			case "/" :
				if(number1 == 0) throw new IllegalArgumentException("Dijeljenje s nulom nije dozvoljeno!");
				result = number2 / number1;
				break;
			case "%" :
				result = number2 % number1;
				break;
			case "bigger" :
				result = (number2 > number1) ? number2 : number1;
				break;
			case "cubed" :
				result = number1 * number1 * number1;
				break;
		}
		return result;
	}

}
