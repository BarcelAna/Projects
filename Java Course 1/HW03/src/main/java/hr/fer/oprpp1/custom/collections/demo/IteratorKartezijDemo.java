package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.SimpleHashtable;

public class IteratorKartezijDemo {

	public static void main(String[] args) {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		for(SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
				System.out.printf("(%s => %d) - (%s => %d)%n", pair1.getKey(), pair1.getValue(), pair2.getKey(), pair2.getValue());
			}
		}

	}

}
