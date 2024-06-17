package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Class StudentDatabes represents a simple implementation of students database.
 * @author anace
 *
 */
public class StudentDatabase {
	/**
	 * List of all student records from database
	 */
	private List<StudentRecord> studentRecords;
	
	/**
	 * Index for fast retrieval of students records when jmbag is known 
	 */
	private Map<String, StudentRecord> index;
	
	/**
	 * Constructor that accepts list of rows of the database file.
	 * It creates internal list of student records and an index. 
	 * @param database
	 */
	public StudentDatabase(List<String> database) {
		index = new TreeMap<String, StudentRecord>();
		studentRecords = new ArrayList<StudentRecord>();
		for(String line : database) {
			String[] arr = line.split("\t");
			
			if(Integer.parseInt(arr[3]) > 5 || Integer.parseInt(arr[3]) < 1) throw new DataBaseException("Grade must be a number between 1 and 5");
			if(index.containsKey(arr[0])) throw new DataBaseException("Duplicate JMBAGs are not allowed!");
			
			StudentRecord r = new StudentRecord(arr[0], arr[1], arr[2], Integer.parseInt(arr[3]));
			studentRecords.add(r);
			index.put(arr[0], r);
		}
	}
	
	/**
	 * Obtains student record using student's jmbag.
	 * If the record does not exist method returns null.
	 * @param jmbag
	 * @return student record or null if the record doesn't exist.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}
	
	/**
	 * Loops through the list of records, adds records to the new list if they are accepted by the given filter and returns the new list.
	 * @return list of accepted records
	 */
	public List<StudentRecord> filter(IFilter filter) {
		return studentRecords.stream().filter(r->filter.accepts(r)).collect(Collectors.toList());
	}
}
