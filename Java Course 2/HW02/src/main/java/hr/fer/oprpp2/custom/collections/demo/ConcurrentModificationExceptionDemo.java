package hr.fer.oprpp2.custom.collections.demo;


import hr.fer.oprpp2.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp2.custom.collections.Collection;
import hr.fer.oprpp2.custom.collections.ElementsGetter;
//import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;

public class ConcurrentModificationExceptionDemo {
	public static void main(String[] args) {
		Collection col = new ArrayIndexedCollection();
		//Collection col = new LinkedListIndexedCollection();
		
		col.add("Ivo");
		col.add("Ana");
		col.add("Jasna");
		
		ElementsGetter getter = col.createElementsGetter();
		
		System.out.println("Jedan element: " + getter.getNextElement());
		System.out.println("Jedan element: " + getter.getNextElement());
		col.clear();
		System.out.println("Jedan element: " + getter.getNextElement());
		}
}
