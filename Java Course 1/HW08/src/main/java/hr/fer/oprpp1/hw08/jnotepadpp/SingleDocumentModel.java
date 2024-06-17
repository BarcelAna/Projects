package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Reperesent a model of a single document, storing information about file path, document modification status and referece to Swing component which is used as text editor.
 * @author anace
 *
 */
public interface SingleDocumentModel {
	/**
	 * Returns JTextArea component used as a text editor in this model
	 * @return component
	 */
	JTextArea getTextComponent();
	
	/**
	 * Returns path of this file.
	 * @return path
	 */
	Path getFilePath();
	
	/**
	 * Sets file path to given new path and notifies all listeners about the change.
	 * @param path
	 * @throws NullPointerException - if path is null
	 */
	void setFilePath(Path path);
	
	/**
	 * Returns modified status.
	 * @return true if file is modified, false otherwise.
	 */
	boolean isModified();
	
	/**
	 * Sets modified status to given value and notifies all listeners about the change.
	 * @param modified
	 */
	void setModified(boolean modified);
	
	/**
	 * Adds single document listener to the list of listeners.
	 * @param l
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Removes single document listener from the list of listeners.
	 * @param l
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
