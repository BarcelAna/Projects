package hr.fer.oprpp1.hw08.jnotepadpp;

/**
 * Listener for single document model
 * @author anace
 *
 */
public interface SingleDocumentListener {
	/**
	 * Called when modify status is updated.
	 * @param model
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Called when file path is updated.
	 * @param model
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
