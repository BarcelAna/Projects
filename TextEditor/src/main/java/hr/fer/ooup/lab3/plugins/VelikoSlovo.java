package hr.fer.ooup.lab3.plugins;

import hr.fer.ooup.lab3.model.ClipboardStack;
import hr.fer.ooup.lab3.model.Plugin;
import hr.fer.ooup.lab3.model.TextEditorModel;
import hr.fer.ooup.lab3.model.UndoManager;

import java.util.ArrayList;
import java.util.List;

public class VelikoSlovo implements Plugin {
	@Override
	public String getName() {
		return "veliko slovo";
	}

	@Override
	public String getDescription() {
		return "Replaces every first letter of the word with a capital of the letter.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		List<String> lines = new ArrayList<>(model.getLines());
		for(int i = 0; i < model.getLines().size(); ++i) {
			String line = lines.remove(i);
			String newLine = "";
			String[] words = line.split(" +");
			for(String w:words) {
				char[] letters = w.toCharArray();
				letters[0] = Character.toUpperCase(letters[0]);
				String newWord = new String(letters);
				newLine += newWord;
				if(w!=words[words.length-1]) {
					newLine+=" ";
				}
			}
			lines.add(i, newLine);
		}
		model.setLines(lines);
	}
}
