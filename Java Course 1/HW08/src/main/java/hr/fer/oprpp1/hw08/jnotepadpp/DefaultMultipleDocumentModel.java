package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;


import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Implementation of MultipleDocumentModel.
 * It extends JTabbedPane.
 * @author anace
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * List of documents held by this model
	 */
	public List<SingleDocumentModel> listOfDocuments;
	
	/**
	 * Current active document
	 */
	private SingleDocumentModel currentDoc;
	
	/**
	 * List of multiple document listeners
	 */
	public List<MultipleDocumentListener> listeners;
	
	/**
	 * Parent component of this JTabbedPane, main frame of GUI
	 */
	private JFrame parent;
	
	/**
	 * Counter used for tracking number of unnamed documents in model
	 */
	public static int cnt = 0;
	
	/**
	 * Constructor which accpets parent frame and initializes list of documents and list of listeners.
	 * @param parent
	 */
	public DefaultMultipleDocumentModel(JFrame parent) {
		this.listOfDocuments = new ArrayList<>();
		this.listeners = new ArrayList<>();
		
		this.parent = parent;
	}
	
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return listOfDocuments.iterator();
	}

	@Override
	public JComponent getVisualComponent() {
		return this;
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel newDoc = addDocument(null, "(unnamed)");
		return newDoc;
	}
	
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if(path == null) throw new NullPointerException("Path must not be null.");
		if(!Files.isReadable(path)) {
			JOptionPane.showMessageDialog(
					 parent,
					((JNotepadPP)parent).flp.getString("file") + " " + path.toAbsolutePath() + " " + ((JNotepadPP)parent).flp.getString("dontExist"),
					((JNotepadPP)parent).flp.getString("err"),
					JOptionPane.ERROR_MESSAGE
			);
			return null;
		}
		SingleDocumentModel loadDoc = addDocument(path, path.getFileName().toString());
		return loadDoc;
	}


	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDoc;
	}

	
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if(newPath == null) {
			newPath = model.getFilePath();
		}
		if(this.findForPath(newPath) != null && model.getFilePath() != newPath) {
			JOptionPane.showMessageDialog(
					parent,
					((JNotepadPP)parent).flp.getString("pathAlrEx"),
					((JNotepadPP)parent).flp.getString("err"),
					JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		byte[] bytes = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(newPath, bytes);
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(
					parent,
					((JNotepadPP)parent).flp.getString("errWr") + " " + currentDoc.getFilePath(),
					((JNotepadPP)parent).flp.getString("err"),
					JOptionPane.ERROR_MESSAGE
			);
			return;
		}
		JOptionPane.showMessageDialog(
				parent,
				((JNotepadPP)parent).flp.getString("fileS"),
				"Info",
				JOptionPane.INFORMATION_MESSAGE
		);
		model.setModified(false);
		model.setFilePath(newPath);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		if(this.listOfDocuments.size()==1)
			return;
		
		for(MultipleDocumentListener l : this.listeners) {
			l.documentRemoved(model);
		}
		
		this.listOfDocuments.remove(model);
		
		for(MultipleDocumentListener l : listeners) {
			l.currentDocumentChanged(model, this.getDocument(0));
		} 
		
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
		
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
		
	}

	@Override
	public int getNumberOfDocuments() {
		return listOfDocuments.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return listOfDocuments.get(index);
	}

	@Override
	public SingleDocumentModel findForPath(Path path) {
		if(path == null) {
			//return null; //ili baciti iznimku?
			throw new NullPointerException("Given path must not be null");
		}
		for(SingleDocumentModel m : listOfDocuments) {
			if(m.getFilePath() != null && m.getFilePath().equals(path)) {
				return m;
			}
		}
		return null;
	}

	@Override
	public int getIndexOfDocument(SingleDocumentModel doc) {
		for(int i = 0; i < listOfDocuments.size(); ++i) {
			SingleDocumentModel m = listOfDocuments.get(i);
			if(m.equals(doc)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Utility method for setting current document
	 * @param m
	 */
	public void setCurrentDoc(SingleDocumentModel m) {
		this.currentDoc = m;
	}

	/**
	 * Utility method for calculating all data needed for info dialog: number of characters, number of non-blank characters and number of lines.
	 * After everything is calculated, message dialog is shown in parent frame.
	 */
	public void showInfo() {
		int numOfChars = 0;
		int numOfNonBlanks = 0;
		int numOfLines = 1;
		int numOfBlanks = 0;
		String text = this.getCurrentDocument().getTextComponent().getText();
		for(char c : text.toCharArray()) {
			if(c == '\n') {
				++numOfLines;
				++numOfBlanks;
			} else if(c == ' ' || c == '\t') {
				++numOfBlanks;
			}
			++numOfChars;
		}
		numOfNonBlanks = text.toCharArray().length - numOfBlanks;
		JOptionPane.showMessageDialog(
				parent,
				String.format(((JNotepadPP)parent).flp.getString("infoString"), numOfChars, numOfNonBlanks, numOfLines),
				"Info",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	/**
	 * Utility method for changing tab icon from red to green disk, and vise versa.
	 * @param isModified flag
	 */
	private void changeTabIcon(boolean isModified) {
		ImageIcon icon;
		if(isModified) {
			icon = loadFloppyIcon("icons/red_disk.png");
		} else {
			icon = loadFloppyIcon("icons/green_disk.png");
		}
		this.setIconAt(this.getSelectedIndex(), icon);
	}
	
	/**
	 * Utility method for adding new document to JTabbedPane component.
	 * @param path of new document
	 * @param name of new document
	 * @return new document model
	 */
	private SingleDocumentModel addDocument(Path path, String name) {
		boolean alreadyAdded = false;
		
		SingleDocumentModel doc;
		
		if(path == null || (doc=this.findForPath(path)) == null) {
			++cnt;
			doc = new DefaultSingleDocumentModel(path, "");
			listOfDocuments.add(doc);
		} else {
			alreadyAdded = true;
		}
			
		JTextArea textArea = doc.getTextComponent();
		
		if(path != null && !alreadyAdded) {
			byte[] bytes;
			try {
				bytes = Files.readAllBytes(path);
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(
						parent,
						((JNotepadPP)parent).flp.getString("errRe") + " " + path.toAbsolutePath() + ".",
						((JNotepadPP)parent).flp.getString("err"),
						JOptionPane.ERROR_MESSAGE
				);
				return null;
			}
			String text = new String(bytes, StandardCharsets.UTF_8);
			textArea.setText(text);
			doc.setModified(false);
		}
		
		if(!alreadyAdded) {
			doc.addSingleDocumentListener(new SingleDocumentListener() {

				@Override
				public void documentModifyStatusUpdated(SingleDocumentModel model) {
					changeTabIcon(model.isModified());
					//model.setModified(false);
				}


				@Override
				public void documentFilePathUpdated(SingleDocumentModel model) {
					changeTabIcon(model.isModified());
					parent.setTitle(model.getFilePath().getFileName().toString() + " - JNotepad++");
					DefaultMultipleDocumentModel.this.setTitleAt(DefaultMultipleDocumentModel.this.getSelectedIndex(), model.getFilePath().getFileName().toString());
					DefaultMultipleDocumentModel.this.setToolTipTextAt(DefaultMultipleDocumentModel.this.getSelectedIndex(), model.getFilePath().toString());
				}
				
			});
		
		
			doc.getTextComponent().addCaretListener(new CaretListener() {

				@Override
				public void caretUpdate(CaretEvent e) {
					((JNotepadPP)parent).updateStatus();
					if(e.getDot() == e.getMark()) {
						((JNotepadPP)parent).changeMenuState(false);
					} else {
						((JNotepadPP)parent).changeMenuState(true);
					}
					//zašto? jer inače neće prepoznati da se dogodila promjena u dokumentu, tj. da je tekst unešen
					//changeModifiedStatus();
				}

			}); 
			
			
		}
		for(MultipleDocumentListener l : this.listeners) {
			if(alreadyAdded) {
				this.setSelectedIndex(this.getIndexOfDocument(this.findForPath(path)));
				l.currentDocumentChanged(this.currentDoc, this.findForPath(path));
			}else {
				l.documentAdded(doc);
			} 
		}
		return doc;
	}
	
	//nez sta ce mi
	/*private void changeModifiedStatus() {
		this.currentDoc.setModified(true);
		
	}*/
	
	/**
	 * Utility method for adding new document to JTabbedPane component.
	 * @param textArea
	 * @param path
	 * @param name
	 * @param model
	 */
	public void addToTabbedPane(JTextArea textArea, Path path, String name, SingleDocumentModel model) {
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollP = new JScrollPane(textArea);
		panel.add(scrollP, BorderLayout.CENTER);
		this.add(name, panel);
		this.setSelectedComponent(panel);
		this.setToolTipTextAt(this.getSelectedIndex(), path == null ? "(unnamed)" : path.toString());
		changeTabIcon(model.isModified());
	}

	/**
	 * Utility method for loading floppy icon from resources
	 * @param path
	 * @return image icon
	 */
	private ImageIcon loadFloppyIcon(String path) {
		InputStream is = this.getClass().getResourceAsStream(path);
		byte[] bytes = null;
		try {
			bytes = is.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ImageIcon(new ImageIcon(bytes).getImage().getScaledInstance(13, 13, Image.SCALE_SMOOTH));
		
	}


}
