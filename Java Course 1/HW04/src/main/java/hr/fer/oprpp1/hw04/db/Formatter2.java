package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

public class Formatter2 {

	public static List<String> format(List<StudentRecord> filteredRecords, String[] fields) {
		List<String> result = new ArrayList<>();
		if(fields.length != 0) {
			result.add(designBorder(filteredRecords, fields));
		}
		for(StudentRecord r : filteredRecords) {
			String row = designRow(r, fields, filteredRecords);
			result.add(row);
		}
		result.add(designBorder(filteredRecords, fields));
		return result;
	}
	
	private static String designRow(StudentRecord r,  String[] fields, List<StudentRecord> records) {
		String row = "";
		for(String f : fields) {
			int max = 0;
			if(f.equals("firstName")) {
				row += "| " + r.getFirstName() + " | ";
				max = getMaxForField(f, records);
				int numOfSpaces = (max + 1) - r.getFirstName().length();
				for(int i = 0; i < numOfSpaces; ++i) {
					row += " ";
				}
			}
			else if(f.equals("lastName")) {
				row += "| " + r.getLastName() + " | ";
				max = getMaxForField(f, records);
				int numOfSpaces = (max + 1) - r.getLastName().length();
				for(int i = 0; i < numOfSpaces; ++i) {
					row += " ";
				}
			} else if(f.equals("jmbag")) {
				row += "| " + r.getJmbag() + " | ";
				max = getMaxForField(f, records);
				int numOfSpaces = (max + 1) - r.getJmbag().length();
				for(int i = 0; i < numOfSpaces; ++i) {
					row += " ";
				}
			}
		}
		return row;
	}
	
	
	private static int getMaxForField(String f, List<StudentRecord> records) {
		int max = 0;
		for(StudentRecord r : records) {
			if(f.equals("firstName")) {
				if(r.getFirstName().length() > max) {
					max = r.getFirstName().length();
				}
			} else if(f.equals("lastName")) {
				if(r.getLastName().length() > max) {
					max = r.getLastName().length();
				}
			}else if(f.equals("jmbag")) {
				if(r.getJmbag().length() > max) {
					max = r.getJmbag().length();
				}
			}
		}
		return max;
	}

	private static String designBorder(List<StudentRecord> records, String[] fields) {
		String border = "+";
		for(String f : fields) {
			int max = getMaxForField(f, records);
			for(int i = 0; i < (max+2); ++i) {
				border += "=";
			}
			border += "+";
		}
		
		return border;
	}

}
