package hr.fer.oprpp1.custom.collections.demo;

import java.util.Iterator;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;

public class IteratorExceptionsDemo {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if(pair.getKey().equals("Ante")) {
				examMarks.remove("Ante");
			}
		}
		

	}

}
