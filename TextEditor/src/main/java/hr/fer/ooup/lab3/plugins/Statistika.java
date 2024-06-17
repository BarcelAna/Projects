package hr.fer.ooup.lab3.plugins;

import hr.fer.ooup.lab3.model.ClipboardStack;
import hr.fer.ooup.lab3.model.Plugin;
import hr.fer.ooup.lab3.model.TextEditorModel;
import hr.fer.ooup.lab3.model.UndoManager;

import javax.swing.*;
import java.util.Iterator;

public class Statistika implements Plugin {
	@Override
	public String getName() {
		return "statistika";
	}

	@Override
	public String getDescription() {
		return "Counts number of rows, words and letters in the document.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		int rows = model.rows();
		int words = 0;
		int letters = 0;
		Iterator it = model.allLines();
		while(it.hasNext()) {
			String line = (String)it.next();
			if(line.isEmpty()) {
				continue;
			}
			String[] wordsArr = line.split(" +");
 			words += wordsArr.length;
			for(String w:wordsArr) {
				letters+=w.length();
			}
		}

		JOptionPane.showMessageDialog(model.getFrame(), "Rows: " + rows + ", Words: " + words + ", Letters: " + letters);
	}
}
