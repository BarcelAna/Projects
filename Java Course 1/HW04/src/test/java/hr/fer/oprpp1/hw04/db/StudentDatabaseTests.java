package hr.fer.oprpp1.hw04.db;
import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.hw04.db.ComparisonOperators;
import hr.fer.oprpp1.hw04.db.ConditionalExpression;
import hr.fer.oprpp1.hw04.db.DataBaseException;
import hr.fer.oprpp1.hw04.db.FieldValueGetters;
import hr.fer.oprpp1.hw04.db.IComparisonOperator;
import hr.fer.oprpp1.hw04.db.QueryFilter;
import hr.fer.oprpp1.hw04.db.QueryParser;
import hr.fer.oprpp1.hw04.db.StudentDatabase;
import hr.fer.oprpp1.hw04.db.StudentRecord;

class StudentDatabaseTests {
	
	StudentDatabase db;
	public StudentDatabaseTests() {
		try {
			db = new StudentDatabase(Files.readAllLines(Paths.get("C:\\Users\\anace\\Desktop\\ANA\\FER\\JAVA\\ZADAĆE\\HW04\\src\\main\\resources\\database.txt"),
				StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void forJMBAGTest() {
		assertEquals("0000000001", db.forJMBAG("0000000001").getJmbag());
		assertEquals("Akšamović", db.forJMBAG("0000000001").getLastName());
		assertEquals("Marin", db.forJMBAG("0000000001").getFirstName());
		assertEquals(2, db.forJMBAG("0000000001").getFinalGrade());
	}
	
	@Test
	public void forJMBAG_NullTest() {
		assertNull(db.forJMBAG("0000000088"));
	}
	
	@Test
	public void testFilterEverything() {
		assertEquals(0, db.filter(record -> false).size()); //provjeri jel ovo dobro
	}
	
	@Test
	public void testFilterNothing() {
		assertEquals(63, db.filter(record -> true).size());
	}
	
	@Test
	public void testComparisonOperatorLESS() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		assertTrue(oper.satisfied("Ana", "Jasna"));
		assertFalse(oper.satisfied("Sara", "Sar"));
	}
	
	@Test
	public void testComparisonOperatorLESS_OR_EQUALS() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		assertTrue(oper.satisfied("Ana", "Sara"));
		assertTrue(oper.satisfied("Ana", "Ana"));
		assertFalse(oper.satisfied("Anastazija", "Ana"));
	}
	
	@Test
	public void testComparisonOperatorGREATER() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		assertFalse(oper.satisfied("Ana", "Jasna"));
		assertTrue(oper.satisfied("Mihaela", "Iva"));
	}
	
	@Test
	public void testComparisonOperatorGREATER_OR_EQUALS() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		assertTrue(oper.satisfied("Anastazija", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
		assertFalse(oper.satisfied("Ivana", "Ivona"));
	}
	
	@Test
	public void testComparisonOperatorEQUALS() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		assertFalse(oper.satisfied("Anastazija", "Ana"));
		assertTrue(oper.satisfied("Ana", "Ana"));
	}
	
	@Test
	public void testComparisonOperatorNOT_EQUALS() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		assertTrue(oper.satisfied("Anastazija", "Ana"));
		assertFalse(oper.satisfied("Ana", "Ana"));
	}
	
	@Test
	public void testComparisonOperatorLIKE() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertFalse(oper.satisfied("Zagreb", "Aba*"));
		assertFalse(oper.satisfied("AAA", "AA*AA"));
		assertTrue(oper.satisfied("AAAA", "AA*AA"));
		assertFalse(oper.satisfied("Barcelona", "*eloa"));
		assertTrue(oper.satisfied("Ana", "*Ana"));
	}
	
	@Test
	public void fieldValueGettersTest() {
		List<StudentRecord> records = db.filter((record)->true);
		StudentRecord record = records.get(1);
		assertEquals("Petra", FieldValueGetters.FIRST_NAME.get(record));
		assertEquals("Bakamović", FieldValueGetters.LAST_NAME.get(record));
		assertEquals("0000000002", FieldValueGetters.JMBAG.get(record));
	}
	
	@Test
	public void conditionalExpressionTest() {
		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*", ComparisonOperators.LIKE);
		List<StudentRecord> records = db.filter((record)->true);
		StudentRecord record = records.get(2);
		assertTrue(expr.getCompOperator().satisfied(expr.getGetterStrategy().get(record), expr.getLiteral()));
	}
	
	@Test
	public void isDirectQueryTest() {
		QueryParser qp = new QueryParser("   jmbag     =\"0123456789\"    ");
		assertTrue(qp.isDirectQuery());
		QueryParser qp2 = new QueryParser("   jmbag     =\"0123456789\" and lastName=\"Cepic\"   ");
		assertFalse(qp2.isDirectQuery());
	}
	
	@Test
	public void getQueriedJMBAGTest() {
		QueryParser qp = new QueryParser("   jmbag     =\"0123456789\"    ");
		assertEquals("0123456789", qp.getQueriedJMBAG());
	}
	
	@Test
	public void getQueriedJMBAGExceptionTest() {
		QueryParser qp = new QueryParser("   jmbag     =\"0123456789\" and firstName=\"Ana\"   ");
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());
	}
	
	@Test
	public void getQueryTest() {
		QueryParser qp = new QueryParser("   jmbag     =\"0123456789\" and firstName=\"Ana\"   ");
		assertEquals(2, qp.getQuerry().size());
	}
	
	@Test
	public void parsingTest() {
		QueryParser qp = new QueryParser("firstName>\"A\"and firstName<\"C\" ANd lastName LIKE \"B*ć\" and jmbag   >\"0000000002\"  ");
		assertEquals(4, qp.getQuerry().size());
		
		List<ConditionalExpression> list = qp.getQuerry();
		
		List<ConditionalExpression> correctData = new ArrayList<>();
		correctData.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "A", ComparisonOperators.GREATER));
		correctData.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "C", ComparisonOperators.LESS));
		correctData.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*ć", ComparisonOperators.LIKE));
		correctData.add(new ConditionalExpression(FieldValueGetters.JMBAG, "0000000002", ComparisonOperators.GREATER));
		checkTokenStream(correctData, list);
	}
	
	@Test
	public void parsingExceptionTest1() {
		assertThrows(DataBaseException.class, () -> new QueryParser("   Jmbag=  \"123456789\"   "));
	}
	
	@Test
	public void parsingExceptionTest2() {
		assertThrows(DataBaseException.class, () -> new QueryParser("   jmbag \"123456789\"   "));
	}
	
	@Test
	public void parsingExceptionTest3() {
		assertThrows(DataBaseException.class, () -> new QueryParser("   jmbag > \"123456789\" And !=  "));
	}
	
	@Test
	public void parsingExceptionTest4() {
		assertThrows(DataBaseException.class, () -> new QueryParser("   jmbag > \"123456789\" And And lastName = \"Cepic\" "));
	}
	
	@Test
	public void parsingExceptionTest5() {
		assertThrows(DataBaseException.class, () -> new QueryParser("jmbag"));
	}
	
	@Test
	public void parsingExceptionTest6() {
		assertThrows(DataBaseException.class, () -> new QueryParser("!= jmbag \"123456789\"  "));
	}
	
	@Test
	public void parsingExceptionTest7() {
		assertThrows(DataBaseException.class, () -> new QueryParser("jmbag = \"123456789\" and "));
	}
	
	@Test
	public void parsingExceptionTest8() {
		assertThrows(DataBaseException.class, () -> new QueryParser("jmbag = \"123456789\" and firstName="));
	}
	
	@Test
	public void queryFilterTest() {
		QueryParser qp = new QueryParser("firstName>\"A\" ANd lastName LIKE \"B*ć\" and jmbag   >=\"0000000002\"  ");
		List<ConditionalExpression> list = qp.getQuerry();
		
		List<StudentRecord> records = db.filter((record)->true);
		StudentRecord record = records.get(1);
		
		QueryFilter filter = new QueryFilter(list);
		assertTrue(filter.accepts(record));
		
		record = records.get(5);
		assertFalse(filter.accepts(record));
	}
	
	private void checkTokenStream(List<ConditionalExpression> correctData, List<ConditionalExpression> actualData) {
		int counter = 0;
		for(ConditionalExpression expected : correctData) {
			ConditionalExpression actual = actualData.get(counter);
			assertEquals(expected.getCompOperator(), actual.getCompOperator());
			assertEquals(expected.getGetterStrategy(), actual.getGetterStrategy());
			assertEquals(expected.getLiteral(), actual.getLiteral());
			counter++;
		}
	}

}
