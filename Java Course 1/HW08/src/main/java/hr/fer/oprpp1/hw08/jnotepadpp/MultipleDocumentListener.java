package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener for multiple document model.
 * @author anace
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Called when current document changed.
	 * @param previousModel
	 * @param currentModel
	 * @throws NullPointerException - if both previous and current model are null
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/**
	 * Called when new document is added into multiple document model
	 * @param model
	 */
	void documentAdded(SingleDocumentModel model);
	
	/**
	 * Called when document is removed from multiple document model
	 * @param model
	 */
	void documentRemoved(SingleDocumentModel model);
}
