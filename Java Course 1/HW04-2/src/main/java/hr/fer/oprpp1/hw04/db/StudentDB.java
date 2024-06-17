package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Program emulator of student database.
 * Program expects query commands in standard input and returns data which satisfies given query.
 * @author anace
 *
 */
public class StudentDB {
	/**
	 * instance of student database
	 */
	private StudentDatabase db;
	
	/**
	 * default constructor which reads data from a file and stores it in database object
	 */
	public StudentDB() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("C:\\Users\\anace\\Desktop\\ZADAĆE\\HW04-2\\src\\main\\resources\\database.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		db = new StudentDatabase(lines);
	}
	
	public static void main(String[] args) {
		StudentDB obj = new StudentDB();
		Scanner sc = new Scanner(System.in);
		String line = "";
		System.out.print("> ");
		while(!(line = sc.nextLine()).equals("exit")) {
			if(!line.startsWith("query ")) {
				System.out.println("Incorrect query command format");
			}else {
				line = line.replace("query", "");
				try {
					QueryParser qp = new QueryParser(line);
					if(qp.isDirectQuery()) System.out.println("Using index for record retrieval.");
					List<StudentRecord> filteredRecords = obj.db.filter(new QueryFilter(qp.getQuerry()));
					if(filteredRecords.size() > 0) {
						if(qp.isShowing) {
							List<String> output = RecordFormatter2.format(filteredRecords, qp.showingList);
							output.forEach(System.out::println);
						} else {
							List<String> output = RecordFormatter.format(filteredRecords);
							output.forEach(System.out::println);
						}
						
					}
					System.out.println("Records selected: " + filteredRecords.size());
				}catch(DataBaseException e) {
					System.out.println("Incorrect query command format");
				}
			}
			System.out.print("> ");
		}
		System.out.println("Goodbye!");
		sc.close();
	}

}
