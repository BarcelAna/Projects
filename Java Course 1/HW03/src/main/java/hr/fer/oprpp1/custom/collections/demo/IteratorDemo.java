package hr.fer.oprpp1.custom.collections.demo;


import hr.fer.oprpp1.custom.collections.SimpleHashtable;

public class IteratorDemo {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2); //ne pronalazi ga iterator
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);

		
		for(SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		}

	}

}
