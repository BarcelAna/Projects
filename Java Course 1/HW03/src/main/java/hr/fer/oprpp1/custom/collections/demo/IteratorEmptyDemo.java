package hr.fer.oprpp1.custom.collections.demo;

import java.util.Iterator;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;

public class IteratorEmptyDemo {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		System.out.println(examMarks.size());
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter.remove();
		}
		System.out.printf("Veličina: %d%n", examMarks.size());

	}

}
