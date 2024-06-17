package hr.fer.oprpp1.hw02.prob1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.lexer.NodeType;
import hr.fer.oprpp1.custom.scripting.lexer.P_Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.P_LexerException;
import hr.fer.oprpp1.custom.scripting.lexer.P_Token;
import hr.fer.oprpp1.custom.scripting.lexer.P_TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

class SmartScriptParserTest {

	@Test
	public void testNullInput() {
		assertThrows(NullPointerException.class, () -> new P_Lexer(null));
	}
	
	@Test
	public void testEmpty() {
		P_Lexer lexer = new P_Lexer("");
		
		assertEquals(P_TokenType.EOF, lexer.nextToken().getTokenType(), "Empty input must generate only EOF token.");
	}
	
	@Test
	public void testGetReturnsLastNext() {
		P_Lexer lexer = new P_Lexer("");
		
		P_Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}
	
	@Test
	public void testNextAfterEOF() {
		P_Lexer lexer = new P_Lexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(P_LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testNoActualContent() {
		// When input is only of spaces, tabs, newlines, etc...
		P_Lexer lexer = new P_Lexer("      ");
		
		assertEquals(P_TokenType.EOF, lexer.nextToken().getTokenType(), "Input had no content. Lexer should generated only EOF token.");
	}
	
	
	@Test
	public void testTextContent() {
		P_Lexer lexer = new P_Lexer("This is sample text.\n\r");
		P_Token correctData[] = {
			new P_Token("This is sample text.\n\r", P_TokenType.STRING, NodeType.TEXT)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testTextWithEscapingContent() {
		P_Lexer lexer = new P_Lexer("This is \\\\ sample \\{text \\{$=1}.\n\r");
		P_Token correctData[] = {
			new P_Token("This is \\ sample {text {$=1}.\n\r", P_TokenType.STRING, NodeType.TEXT)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testTextIncorrectEscapingContent() {
		assertThrows(P_LexerException.class, () -> new P_Lexer("This is sample \\1text.\n\r").nextToken());
	}
	
	@Test
	public void testEmptyTagContent() {
		P_Lexer lexer = new P_Lexer("{$= i i * @sin   \"0.000\" @decfmt $}");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.ECHO),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.ECHO),
			new P_Token("*", P_TokenType.OPERATOR, NodeType.ECHO),
			new P_Token("@sin", P_TokenType.FUNCTION, NodeType.ECHO),
			new P_Token("0.000", P_TokenType.STRING, NodeType.ECHO),
			new P_Token("@decfmt", P_TokenType.FUNCTION, NodeType.ECHO),
			new P_Token("$}", P_TokenType.END_OF_TAG, null)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testForTagContent() {
		P_Lexer lexer = new P_Lexer("{$ FOR i -1    \"10\" 1 $}");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),	
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("-1", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("10", P_TokenType.STRING, NodeType.FOR),
			new P_Token("1", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testForTagContentWithMinus() {
		P_Lexer lexer = new P_Lexer("{$ FOR i-1.35bbb\"1\" $}");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("-1.35", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("bbb", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1", P_TokenType.STRING, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testForTagContentPlus() {
		P_Lexer lexer = new P_Lexer("{$ FOR i 1.35bbb\"1\" $}");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1.35", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("bbb", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1", P_TokenType.STRING, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void tagStringsEscapingTest() {
		P_Lexer lexer = new P_Lexer("{$FOR i 1 \"Joe \\\"Long\\\" Smith\"$}.");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("Joe \"Long\" Smith", P_TokenType.STRING, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null),
			new P_Token(".", P_TokenType.STRING, NodeType.TEXT)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void tagStringsEscapingTest2() {
		P_Lexer lexer = new P_Lexer("{$FOR i 1 \"Joe \\\"Long\\\" Smith\n\"$}.");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("Joe \"Long\" Smith\n", P_TokenType.STRING, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null),
			new P_Token(".", P_TokenType.STRING, NodeType.TEXT)
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void twoTagsTest() {
		P_Lexer lexer = new P_Lexer("{$ FOR i 1 10 2$} {$ = i$} ");
		P_Token correctData[] = {
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.FOR),
			new P_Token("1", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("10", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("2", P_TokenType.NUMBER, NodeType.FOR),
			new P_Token("$}", P_TokenType.END_OF_TAG, null),
			new P_Token("{$", P_TokenType.START_OF_TAG, null),
			new P_Token("i", P_TokenType.VARIABLE, NodeType.ECHO),
			new P_Token("$}", P_TokenType.END_OF_TAG, null),
		};
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void invalidForLoopTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR 3 1 10 1 $}{$ END $}");
		});
	}
	
	@Test
	public void invalidForLoopTest2() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR i 1 @sin 1 $}{$ END $}");
		});
	}
	
	@Test
	public void invalidForLoopTest3() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR i 1 + 1 $}{$ END $}");
		});
	}
	
	@Test
	public void tooManyArgsForLoopTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR year 1 10 \"1\" \"10\" $}{$ END $}");
		});
	}
	
	@Test
	public void tooFewArgsForLoopTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR year $}{$ END $}");
		});
	}
	@Test
	public void tooFewArgsForLoopTest2() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ FOR $}{$ END $}");
		});
	}
	
	@Test
	public void tooFewArgsEchoTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ = $}");
		});
	}
	
	@Test
	public void ivalidVariableNameTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$ = 1_b $}");
		});
	}
	
	@Test
	public void invalidFunctionName() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$= ana 1 @1fun $}");
		});
	}
	
	@Test
	public void invalidOperatorTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$= ana 1 % $}");
		});
	}
	
	@Test
	public void notClosedForLoopNodeTest() {
		assertThrows(SmartScriptParserException.class, () -> {
			new SmartScriptParser("{$FOR ana 1 \"10\" $}");
		});
	}
	@Test
	public void testExample1() {
		String text = readExample(1);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(1, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
	}
	
	@Test
	public void testExample2() {
		String text = readExample(2);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(1, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
	}
	
	@Test
	public void testExample3() {
		String text = readExample(3);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(1, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
	}
	
	@Test
	public void testExample4() {
		String text = readExample(4);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(1, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
	}
	
	@Test
	public void testExample5() {
		String text = readExample(5);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(1, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
	}
	
	@Test
	public void testExample6() {
		String text = readExample(6);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(2, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
		assertEquals(EchoNode.class, dn.getChild(1).getClass());
	}
	
	@Test
	public void testExample7() {
		String text = readExample(7);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(2, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
		assertEquals(EchoNode.class, dn.getChild(1).getClass());
	}
	
	@Test
	public void testExample8() {
		String text = readExample(8);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(2, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
		assertEquals(EchoNode.class, dn.getChild(1).getClass());
	}
	
	@Test
	public void testExample9() {
		String text = readExample(9);
		SmartScriptParser parser = new SmartScriptParser(text);
		DocumentNode dn = parser.getDocumentNode();
		assertEquals(2, dn.numberOfChildren());
		assertEquals(TextNode.class, dn.getChild(0).getClass());
		assertEquals(EchoNode.class, dn.getChild(1).getClass());
	}
	
	private void checkTokenStream(P_Lexer lexer, P_Token[] correctData) {
		int counter = 0;
		for(P_Token expected : correctData) {
			P_Token actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			System.out.println("Expected: " + expected.getValue() + ", Actual: " + actual.getValue());
			assertEquals(expected.getTokenType(), actual.getTokenType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			assertEquals(expected.getNodeType(), actual.getNodeType(), msg);
			counter++;
		}
	}
	
	private String readExample(int n) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
		   if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		   byte[] data = is.readAllBytes();
		   String text = new String(data, StandardCharsets.UTF_8);
		   return text;
		} catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}
	

}
