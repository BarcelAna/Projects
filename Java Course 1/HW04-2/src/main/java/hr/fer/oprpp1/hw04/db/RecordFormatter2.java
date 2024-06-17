package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

public class RecordFormatter2 {
	public static List<StudentRecord> recordsI;

	public static List<String> format(List<StudentRecord> records, List<String> fields) {
		recordsI = records;
		List<String> result = new ArrayList<>();
		
		int[] maximums = new int[fields.size()];
		
		for(int i = 0; i < fields.size(); ++i) {
			maximums[i] = findMaxForField(fields.get(i));
		}
		
		String border = designBorder(records, maximums, fields);
		result.add(border);
		
		for(StudentRecord r : records) {
			String row = designRow(r, maximums, fields);
			result.add(row);
		}
		result.add(border);
		
		return result;
	}
	
	private static String designBorder(List<StudentRecord> records, int[] maximums, List<String> fields) {
		String border = "+";
		for(int max:maximums) {
			for(int i = 0; i < max+2; ++i) {
				border+= "=";
			}
			border +="+";
		}
		return border;
	}
	
	private static String designRow(StudentRecord r, int[] maximums, List<String> fields) {
		String row = "";
		for(int i = 0; i < fields.size(); ++i) {
			if(fields.get(i).equals("jmbag")) {
				row += "| " + r.getJmbag();
				int numOfSpaces= (maximums[i] + 1) - r.getJmbag().length();
				for(int j = 0; j < numOfSpaces; ++j) {
					row += " ";
				}
			} else if(fields.get(i).equals("firstName")) {
				row += "| " + r.getFirstName();
				int numOfSpaces = (maximums[i] + 1) - r.getFirstName().length();
				for(int j = 0; j < numOfSpaces; ++j) {
					row += " ";
				}
			} else if(fields.get(i).equals("lastName")) {
				row += "| " + r.getJmbag();
				int numOfSpaces = (maximums[i] + 1) - r.getLastName().length();
				for(int j = 0; j < numOfSpaces; ++j) {
					row += " ";
				}
			}
		}
		return row;
		
	}

	private static int findMaxForField(String field) {
		int max = 0;
		for(StudentRecord r : recordsI) {
			if(field.equals("lastName")) {
				if(r.getLastName().length() > max) {
					max = r.getLastName().length();
				}
			} else if(field.equals("firstName")) {
				if(r.getFirstName().length() > max) {
					max = r.getFirstName().length();
				}
			} else if(field.equals("jmbag")) {
				if(r.getJmbag().length() > max) {
					max = r.getJmbag().length();
				}
			}
		}
		return max;
	}
}


	

