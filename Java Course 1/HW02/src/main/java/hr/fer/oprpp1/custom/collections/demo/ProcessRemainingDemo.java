package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ElementsGetter;
//import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;

public class ProcessRemainingDemo {
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		//Collection col = new LinkedListIndexedCollection();
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		ElementsGetter getter = col.createElementsGetter();
		getter.getNextElement();
		getter.processRemaining(System.out::println);
	}
}
