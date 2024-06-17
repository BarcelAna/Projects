package hr.fer.ooup.lab3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
	private Stack<String> texts = new Stack<>();
	private List<ClipboardObserver> clipboardObservers = new ArrayList<>();

	public void push(String text) {
		texts.push(text);
		notifyObservers();
	}

	public String pop() {
		if(isEmpty()) {
			return "";
		}
		String text = texts.pop();
		notifyObservers();
		return text;
	}

	public String peek() {
		if(isEmpty()) {
			return "";
		}
		return texts.peek();
	}

	public boolean isEmpty() {
		return texts.isEmpty();
	}

	public void clear() {
		texts.clear();
		notifyObservers();
	}

	public void addObserver(ClipboardObserver observer) {
		clipboardObservers.add(observer);
	}

	public void detachObserver(ClipboardObserver observer) {
		clipboardObservers.remove(observer);
	}

	public void notifyObservers() {
		for(ClipboardObserver o: clipboardObservers) {
			o.updateClipboard();
		}
	}
}
