package hr.fer.oprpp1.hw02;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;

public class SmartScriptTester2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath = args[0];
	    String docBody = "";
		try {
			docBody = new String(
					Files.readAllBytes(Paths.get(filepath)),
					StandardCharsets.UTF_8
			);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		boolean same = document.equals(document2);
		System.out.println("SAME: " + same);

	}

}
