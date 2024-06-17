package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

public class RecordFormatter {

	public static List<String> format(List<StudentRecord> records) {
		List<String> result = new ArrayList<>();
		
		int maxLastName = 0;
		int maxFirstName = 0;
		for(StudentRecord r : records) {
			if(r.getFirstName().length() > maxFirstName) {
				maxFirstName = r.getFirstName().length();
			}
			if(r.getLastName().length() > maxLastName) {
				maxLastName = r.getLastName().length();
			}
		}
		
		String border = designBorder(records, maxLastName, maxFirstName);
		
		result.add(border);
		for(StudentRecord r : records) {
			String row = designRow(r, maxLastName, maxFirstName);
			result.add(row);
		}
		result.add(border);
		return result;
	}

	private static String designRow(StudentRecord r, int maxLastName, int maxFirstName) {
		String row = "";
		row += "| " + r.getJmbag() + " | " + r.getLastName();
		int numOfSpacesForLastName = (maxLastName + 1) - r.getLastName().length();
		for(int i = 0; i < numOfSpacesForLastName; ++i) {
			row += " ";
		}
		row += "| " + r.getFirstName();
		int numOfSpacesForFirstName = (maxFirstName + 1) - r.getFirstName().length();
		for(int i = 0; i < numOfSpacesForFirstName; ++i) {
			row += " ";
		}
		row += "| " + r.getFinalGrade() + " |";
		return row;
	}

	private static String designBorder(List<StudentRecord> records, int maxLastName, int maxFirstName) {
		String border = "+============+";
		
		for(int i = 0; i < (maxLastName+2); ++i) {
			border += "=";
		}
		border += "+";
		
		for(int i = 0; i < (maxFirstName+2); ++i) {
			border += "=";
		}
		border += "+";
		
		border += "===+";
		
		return border;
	}

}
