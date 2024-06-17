package hr.fer.ooup.lab3.model;

import hr.fer.ooup.lab3.model.ClipboardStack;
import hr.fer.ooup.lab3.model.TextEditorModel;
import hr.fer.ooup.lab3.model.UndoManager;

public interface Plugin {
	String getName();
	String getDescription();
	void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
