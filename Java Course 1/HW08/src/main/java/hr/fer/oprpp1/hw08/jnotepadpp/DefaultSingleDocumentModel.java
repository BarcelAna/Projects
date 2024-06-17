package hr.fer.oprpp1.hw08.jnotepadpp;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Implementation of SingleDocumentModel interface.
 * @author anace
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel{
	/**
	 * modified status
	 */
	private boolean modified;
	
	/**
	 * component used as document text editor 
	 */
	private JTextArea textArea;
	
	/**
	 * path of document
	 */
	private Path path;
	
	/**
	 * list of listeners registered to this model
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * unique id of current document used to create difference between documents which paths are null
	 */
	public int id;

	/**
	 * Constructor which accepts path and text.
	 * It sets document's path to given value and content of JTextArea component to given text.
	 * It also sets modified status to true and initializes listeners list and document's id.
	 * It also registers document listener to JTextArea component so that any change in editor would notify all listeners and set modified status to true;
	 * @param path
	 * @param text
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.modified = true;
		this.listeners = new ArrayList<>();
		this.path = path;
		this.textArea = new JTextArea(text);
		this.id = DefaultMultipleDocumentModel.cnt;
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				modified = true;
				notifyModifiedListeners();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {	
				modified = true;
				notifyModifiedListeners();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				modified = true;
				notifyModifiedListeners();
			}
			
		});	
		
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		if(path == null) throw new NullPointerException("Path must not be null");
		this.path = path;	
		notifyPathListeners();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

	/**
	 * Utility method for notifying all listeners that document has been modified.
	 * It goes through all listeners and calls documentModifyStatusUpdated(this) method on each listener.
	 */
	private void notifyModifiedListeners() {
		for(SingleDocumentListener l : listeners) {
			l.documentModifyStatusUpdated(this);
		}
	}
	
	/**
	 * Utility method for notifying all listeners that document path has been updated.
	 * It goes through all listeners and calls documentFilePathUpdated(this) method on each listener.
	 */
	private void notifyPathListeners() {
		for(SingleDocumentListener l : listeners) {
			l.documentFilePathUpdated(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultSingleDocumentModel other = (DefaultSingleDocumentModel) obj;
		return Objects.equals(id, other.id) ;
	}
	
	
}
