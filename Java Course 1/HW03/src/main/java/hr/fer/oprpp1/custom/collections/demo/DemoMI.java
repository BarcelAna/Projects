package hr.fer.oprpp1.custom.collections.demo;


import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;

public class DemoMI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Collection<String> prva = new ArrayIndexedCollection<>();
		Collection<Object> druga = new ArrayIndexedCollection<>();
		prva.add("Ivo");
		prva.add("Ivka");
		prva.copyTransformedInfoIfAllowed(druga, Object::hashCode, n-> n.intValue()%2==0);
		druga.forEach(System.out::println);
	}

}
