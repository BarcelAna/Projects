package hr.fer.ooup.lab3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoManager {
	private Stack<EditAction> undoStack;
	private Stack<EditAction> redoStack;
	private static UndoManager instance;
	private List<UndoManagerObserver> observers;

	private UndoManager() {
		undoStack=new Stack<>();
		redoStack=new Stack<>();
		observers=new ArrayList<>();
	}

	public static UndoManager instance() {
		if(instance==null) {
			instance=new UndoManager();
		}
		return instance;
	}

	public void undo() {
		EditAction action = undoStack.pop();
		redoStack.push(action);
		action.execute_undo();
		notifyObservers();
	}

	public void redo() {
		EditAction action = redoStack.pop();
		undoStack.push(action);
		action.execute_do();
		notifyObservers();
	}

	public void push(EditAction c) {
		redoStack.clear();
		undoStack.push(c);
		notifyObservers();
	}

	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	public boolean canRedo() {
		return !redoStack.isEmpty();
	}
	public void addObserver(UndoManagerObserver observer) {
		observers.add(observer);
	}
	public void detachObserver(UndoManagerObserver observer) {
		observers.remove(observer);
	}
	public void notifyObservers() {
		for(UndoManagerObserver o:observers)
			o.managerUpdated();
	}
}
