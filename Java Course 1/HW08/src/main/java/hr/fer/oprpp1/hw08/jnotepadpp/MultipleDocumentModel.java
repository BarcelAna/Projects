package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JComponent;

/**
 * Represents model capable of holding zero, one or more documents.
 * It extends Iterable<SingleDocumentModel> interface.
 * @author anace
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Returns visual component which uses this document model.
	 * Component returned with this method is JTabbedPane which hold all loaded documents.
	 * @return component
	 */
	JComponent getVisualComponent();
	
	/**
	 * Creates new document and adds it in JTabbedPane component.
	 * @return new created document
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Returns current document which is active in JTabbedPane component.
	 * @return current document model
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Loads document from given path and adds it into JTabbesPane component.
	 * @param path to document
	 * @throws NullPointerException - if path is null
	 * @return loaded document model
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Saves given document on gven path.
	 * @param document model to be saved
	 * @param newPath - destination path where to save given document
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Closes given document and removes it from JTabbedPane component.
	 * @param model
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds multiple document listener to the list of listeners.
	 * @param l
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes multiple document listener from the list of listeners.
	 * @param l
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Returns number of documents shown in JTabbedPane component.
	 * @return number of documents
	 */
	int getNumberOfDocuments();
	
	/**
	 * Returns document from given index.
	 * @param index
	 * @return document model or null if there is no such file
	 */
	SingleDocumentModel getDocument(int index);
	
	/**
	 * Finds document on the given path.
	 * @param path
	 * @throws NullPointerException - if given path is null
	 * @return found document or null if there is no such file
	 */
	SingleDocumentModel findForPath(Path path);
	
	/**
	 * Returns index of given document.
	 * @param doc
	 * @return index or -1 if there is no such document
	 */
	int getIndexOfDocument(SingleDocumentModel doc);
	
}
