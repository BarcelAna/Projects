package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Class StudentRecord represents a single line of students database.
 * It stores information about student jmbag, last name, first name and final grade.
 * @author anace
 *
 */
public class StudentRecord {
	private String jmbag;
	private String lastName;
	private String firstName;
	private int finalGrade;
	
	/**
	 * Constructor that accepts jmbag, last name, first name and final grade.
	 * Creates new instance of student record with given data.
	 * @param jmbag
	 * @param lastName
	 * @param firstName
	 * @param finalGrade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Getter for student's jmbag.
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for student's last name.
	 * @return last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for student's first name.
	 * @return first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for student's final grade.
	 * @return finale grade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Calculates hash code using student's jmbag.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}
	
	/**
	 * Compares two StudentRecord objects by comparing their jmbags.
	 * @return true if objects have equals jmbags or false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
	
	
}
