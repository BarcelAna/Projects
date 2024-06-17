package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

/**
 * Class JNotepadPP models simple text file editor frame.
 * It extends JFrame.
 * @author anace
 *
 */
public class JNotepadPP extends JFrame{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Model used for handling added documents
	 */
	private DefaultMultipleDocumentModel docModel;
	
	/**
	 * label which is displaying content length in status bar
	 */
	private JLabel lengthLabel;
	
	/**
	 * label which is displaying caret position in status bar
	 */
	public JLabel caretLabel;
	
	/**
	 * frame localization provider
	 */
	public ILocalizationProvider flp;
	
	/**
	 * action object for adding new document
	 */
	private Action newDocumentAction;
	
	/**
	 * action object for opening document
	 */
	private Action openDocumentAction;
	
	/**
	 * action object for saving document
	 */
	private Action saveDocumentAction;
	
	/**
	 * action object for saving document as
	 */
	private Action saveAsDocumentAction;
	
	/**
	 * action object for exiting application
	 */
	private Action exitAction;
	
	/**
	 * action object for closing current document
	 */
	private Action closeDocumentAction;
	
	/**
	 * action object for copying selected text
	 */
	private Action copyTextAction;
	
	/**
	 * action object for cutting selected text
	 */
	private Action cutTextAction;
	
	/**
	 * action object for pasting selected text
	 */
	private Action pasteTextAction;
	
	/**
	 * action object for displaying info dialog
	 */
	private Action infoAction;
	
	/**
	 * action object for translating program to Croatian
	 */
	private Action translateCroAction;
	
	/**
	 * action object for translating program to English
	 */
	private Action translateEngAction;
	
	/**
	 * action object for translating program to German
	 */
	private Action translateGerAction;
	
	/**
	 * action object for transforming selected text to upper case
	 */
	private Action toUppercaseAction;
	
	/**
	 * action object for transforming selected text to lower case
	 */
	private Action toLowercaseAction;
	
	/**
	 * action object for inverting letter case of selected text
	 */
	private Action invertCaseAction;
	
	/**
	 * action object for applying ascend sort on selected lines
	 */
	private Action ascSortAction;
	
	/**
	 * action object for applying descend sort on selected lines
	 */
	private Action desSortAction;
	
	/**
	 * action object for removing lines which are not unique from selected lines
	 */
	private Action uniqueAction;
	
	/**
	 * Default constructor which initializes frame properties and calls method initGUI() for adding components to frame.
	 */
	public JNotepadPP() {
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				boolean close = checkUnsaved();
				if(close) {
					dispose();
				}
			}
			
		});
		this.setLocation(20, 20);
		this.setSize(500, 400);
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		initGUI(); 
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		initializeActions();
		createActions();
		createMenu();
		
		JToolBar toolBar = createToolBar();
		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		
		this.docModel = new DefaultMultipleDocumentModel(this);
		docModel.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				if(previousModel == null && currentModel == null) throw new NullPointerException("Both models can't be null at the same time.");
				docModel.setCurrentDoc(currentModel);
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				docModel.addToTabbedPane(model.getTextComponent(), model.getFilePath(), model.getFilePath() == null ? "(unnamed)" : model.getFilePath().getFileName().toString(), model);
				
				for(MultipleDocumentListener l : docModel.listeners) {
					l.currentDocumentChanged(docModel.getCurrentDocument(), model);
				}
			}

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				docModel.remove(docModel.getIndexOfDocument(model));
			}
			
		});
		docModel.createNewDocument();
		
		JTabbedPane tabbedPane = (JTabbedPane)docModel.getVisualComponent();
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		this.setTitle(docModel.getTitleAt(0) + " - JNotepad++"); //treba li ovo ići tu
		
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				for(MultipleDocumentListener l : docModel.listeners) {
					l.currentDocumentChanged(null, docModel.getDocument(docModel.getSelectedIndex()));
				}
				updateTitle();
				
				updateStatus();
			}
			
		});
		
		createStatusBar();
		
	}
	
	/**
	 * Utility method for defining needed actions.
	 */
	private void initializeActions() {
		newDocumentAction = new LocalizableAction("new", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				docModel.createNewDocument();
			}
			
		};

		openDocumentAction = new LocalizableAction("open", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(flp.getString("open"));
				if(fc.showOpenDialog(JNotepadPP.this)!= JFileChooser.APPROVE_OPTION) {
					return;
				}
				File fileName = fc.getSelectedFile();
				Path filePath = fileName.toPath();
				
				docModel.loadDocument(filePath);
			}
			
		};
		
		saveDocumentAction = new LocalizableAction("save", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SingleDocumentModel currentDoc = docModel.getCurrentDocument();
				Path path = currentDoc.getFilePath();
				if(path==null) {
					saveAsDocumentAction.actionPerformed(e);
					return;
				}
				
				docModel.saveDocument(currentDoc, path);
				//zamjeniti sa notfyTitleListener()
				updateTitle();
			}	
		};
		
		saveAsDocumentAction = new LocalizableAction("saveAs", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SingleDocumentModel currentDoc = docModel.getCurrentDocument();
				if(currentDoc.getFilePath() != null) {
					JOptionPane.showMessageDialog(
							JNotepadPP.this,
							flp.getString("alrSaved") +  " " + currentDoc.getFilePath(),
							flp.getString("warn"),
							JOptionPane.WARNING_MESSAGE
					);
					
				}
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle(flp.getString("saveAs"));
				if(fc.showSaveDialog(JNotepadPP.this)!= JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(
							JNotepadPP.this,
							flp.getString("nothingSaved"),
							flp.getString("warn"),
							JOptionPane.WARNING_MESSAGE
					);
					return;
				}
				Path path = fc.getSelectedFile().toPath();
				docModel.saveDocument(currentDoc, path);
				updateTitle();
			}
		};
		
		
		 exitAction = new LocalizableAction("exit", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				checkUnsaved();
				System.exit(0);
			}
			
		};
		
		closeDocumentAction = new LocalizableAction("close", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				SingleDocumentModel currentDoc = docModel.getCurrentDocument();
				docModel.closeDocument(currentDoc);
				
			}
			
		};
		
		cutTextAction = new LocalizableAction("cut", flp) {

			private static final long serialVersionUID = 1L;
			
			Action action = new DefaultEditorKit.CutAction();

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(e);
			}
			
		};
				
		
		copyTextAction = new LocalizableAction("copy", flp) {

			private static final long serialVersionUID = 1L;
			
			Action action = new DefaultEditorKit.CopyAction();

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(e);
			}
				
		};
		
		 pasteTextAction = new LocalizableAction("paste", flp) {

			private static final long serialVersionUID = 1L;
			
			Action action = new DefaultEditorKit.PasteAction();

			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(e);
			}
				
		};
		
		infoAction = new LocalizableAction("info", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JNotepadPP.this.docModel.showInfo();
			}
			
		};
		
		translateCroAction = new LocalizableAction("croLang", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
			
		};
		
		translateEngAction = new LocalizableAction("engLang", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
			
		};

		
		translateGerAction = new LocalizableAction("gerLang", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
			
		};
		
		
		toUppercaseAction = new LocalizableAction("toUppC", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCase("upper");
			}
			
		};
		
		
		toLowercaseAction = new LocalizableAction("toLowC", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				changeCase("lower");
			}
			
		};
		
		
		
		invertCaseAction = new LocalizableAction("invC", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JNotepadPP.this.changeCase("invert");
			}
			
		};
		
		ascSortAction = new LocalizableAction("ascSort", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sort(true);
				
			}
			
		};
		
		desSortAction = new LocalizableAction("desSort", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				sort(false);
			}
			
		};
		
		uniqueAction = new LocalizableAction("unique", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextArea textArea = docModel.getDocument(docModel.getSelectedIndex()).getTextComponent();
				JTextComponent textComp = textArea;
				
				int caretPos = textComp.getCaretPosition();
				int markPos = textComp.getCaret().getMark();
				
				Document doc = textComp.getDocument();
				Element root = doc.getDefaultRootElement();
				int firstRow = root.getElementIndex(caretPos);
				int lastRow = root.getElementIndex(markPos);
				
				int firstRowStart = 0;
				int lastRowEnd = 0;
				
				try {
					lastRowEnd = textArea.getLineEndOffset(lastRow);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				try {
					firstRowStart = textArea.getLineStartOffset(firstRow);
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
				
				List<String> lines = new ArrayList<>();
				for(int i=firstRow; i <= lastRow; ++i) {
					int lineStart = 0;
					try {
						lineStart = textArea.getLineStartOffset(i);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					int lineEnd = 0;
					try {
						lineEnd = textArea.getLineEndOffset(i);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					String lineText = "";
					try {
						lineText = doc.getText(lineStart, Math.abs(lineEnd-lineStart));
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					if(lineText.endsWith("\n")) {
						lineText = lineText.substring(0, lineText.length()-1);
					}
						
					Locale locale = new Locale(flp.getLanguage());
					Collator collator = Collator.getInstance(locale);

					if(lines.isEmpty()) {
						lines.add(lineText);
					} else {
						boolean keep = true;
						for(String l : lines) {
							if(collator.compare(l, lineText) == 0) {
								keep = false;
								break;
							}
						}
						if(keep) {
							lines.add(lineText);
						}
					}
					
				}

				try {
					doc.remove(firstRowStart, Math.abs(lastRowEnd-firstRowStart));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < lines.size(); ++i) {
					sb.append(lines.get(i) + '\n');
				}
				
				try {
					doc.insertString(firstRowStart, sb.toString(), null);
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}
			
		};
	}
	
	/**
	 * Utility method for sorting selected lines of text accoring to given argument which can be true
	 * for ascending order, or false for descending.
	 * @param ascending
	 */
	private void sort(boolean ascending) {
		JTextArea textArea = docModel.getDocument(docModel.getSelectedIndex()).getTextComponent();
		JTextComponent textComp = textArea;
		int caretPos = textComp.getCaretPosition();
		int markPos = textComp.getCaret().getMark();
		
		Document doc = textComp.getDocument();
		Element root = doc.getDefaultRootElement();
		int firstRow = root.getElementIndex(caretPos);
		int lastRow = root.getElementIndex(markPos);
		int lastRowEnd = 0;
		int firstRowStart = 0;
		try {
			lastRowEnd = textArea.getLineEndOffset(lastRow);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		try {
			firstRowStart = textArea.getLineStartOffset(firstRow);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		List<String> lines = new ArrayList<>();
		
		for(int i=firstRow; i <= lastRow; ++i) {
			int lineStart = 0;
			try {
				lineStart = textArea.getLineStartOffset(i);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			int lineEnd = 0;
			try {
				lineEnd = textArea.getLineEndOffset(i);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			String lineText = "";
			try {
				lineText = doc.getText(lineStart, Math.abs(lineEnd-lineStart));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			if(lineText.endsWith("\n")) {
				lineText = lineText.substring(0, lineText.length()-1);
			}
			
			lines.add(lineText);
		}
		Locale locale = new Locale(flp.getLanguage());
		Collator collator = Collator.getInstance(locale);
		
		if(ascending) {
			lines.sort(collator);
		} else {
			lines.sort(collator.reversed());
		}
		 try {
			doc.remove(firstRowStart, Math.abs(firstRowStart-lastRowEnd));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < lines.size(); ++i) {
			sb.append(lines.get(i) + '\n');
		}
		try {
			doc.insertString(firstRowStart, sb.toString(), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility method for changing selected text case base on given option(lower, upper or invert)
	 * @param option
	 */
	private void changeCase(String option) {
		JTextComponent textComp = docModel.getCurrentDocument().getTextComponent();
		int caretPos = textComp.getCaretPosition();
		int markPos = textComp.getCaret().getMark();
		
		int length = Math.abs(caretPos - markPos);
		int fromPos = Math.min(markPos, caretPos);
		Document doc = textComp.getDocument();

		String text = "";
		String newText = "";
		try {
			text = doc.getText(fromPos, length);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		try {
			doc.remove(fromPos, length);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		if(option.equals("invert")) {
			for(int i = 0; i < text.length(); ++i) {
				char c = text.charAt(i);
				if(Character.isUpperCase(c)) {
					newText += Character.toLowerCase(c);
				} else {
					newText += Character.toUpperCase(c);
				}
			}
			try {
				doc.insertString(fromPos, newText, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			try {
				doc.insertString(fromPos, option.equals("upper") ? text.toUpperCase() : text.toLowerCase(), null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	/**
	 * Utility function for updating frame title
	 */
	private void updateTitle() {
		this.setTitle(this.docModel.getCurrentDocument().getFilePath() == null ? "(unnamed)" :  this.docModel.getCurrentDocument().getFilePath() + " - JNotepad++");
	}
	
	/**
	 * Method for updating status bar content when switching between tabs or moving caret
	 */
	public void updateStatus() {
		JTextArea currentTextArea = docModel.getCurrentDocument().getTextComponent();
		int length = currentTextArea.getText().length();
		lengthLabel.setText(" length:" + length);
		calculateCaretPosition(currentTextArea);
	}
	
	/**
	 * Utility method for calculating current row and column
	 * @param currentTextArea
	 */
	private void calculateCaretPosition(JTextArea currentTextArea) {
		int caretPos = currentTextArea.getCaretPosition();
		int markPos = currentTextArea.getCaret().getMark();
		
		int rowNum = (caretPos == 0) ? 1 : 0;
		for(int offset = caretPos; offset > 0;) {
			try {
				offset = Utilities.getRowStart(currentTextArea, offset)-1;
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			rowNum++;
		}
		
		int offset = 0;
		try {
			offset = Utilities.getRowStart(currentTextArea, caretPos);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		if(offset < 0) offset = 0;
		int colNum = caretPos - offset + 1;
		int sel = Math.abs(markPos - caretPos);
		caretLabel.setText(" Ln:" + rowNum + "  Col:" + colNum + "  Sel:" + sel);
		
	}

	/**
	 * Utility method for creating status bar
	 */
	private void createStatusBar() {
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		JPanel editorInfoPanel = new JPanel(new GridLayout(1, 2)); 
		
		JLabel lengthLabel = new JLabel();
		lengthLabel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		editorInfoPanel.add(lengthLabel);
		this.lengthLabel = lengthLabel;
		
		JLabel caretLabel = new JLabel();
		editorInfoPanel.add(caretLabel);
		this.caretLabel = caretLabel;
		
		JPanel timePanel = new JPanel();
		
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		JLabel dateTimeLabel = new JLabel(dateFormat.format(now.getTime()));

		new Timer(1000, new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent e) {
		           Calendar now = Calendar.getInstance();
		           dateTimeLabel.setText(dateFormat.format(now.getTime()));
		       }
		}).start();
		
		timePanel.add(dateTimeLabel);
		
		statusBar.add(editorInfoPanel, BorderLayout.WEST);
		statusBar.add(timePanel, BorderLayout.EAST);
		updateStatus();
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Utility method for creating actions and adding some properties to it, like accelerator key or mnemonic key.
	 */
	private void createActions() {
		newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, false));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, false));
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X); 
		
		closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, false));
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L); 
		
		cutTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T); 
		
		copyTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C); 
		
		pasteTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P); 
		
		infoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		infoAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I); 
		
		toUppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
		toUppercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		
		toLowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		toLowercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		
		invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, false));
		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		
		ascSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
		ascSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_G);
	
		desSortAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
		desSortAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK, false));
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
		
	}
	

	/**
	 * Utility method for creating tool bar and it's buttons.
	 * @return
	 */
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar("Tools");
		JButton[] buttons = createButtons();
		
		for(int i = 0; i < buttons.length; ++i) {
			if(i == 6 || i == 9) {
				toolBar.addSeparator();
			}
			toolBar.add(buttons[i]);
		}
			
		
		return toolBar;
	}

	/**
	 * Utility method for creating tool bar buttons with appropriate icons.
	 * @return array of created buttons
	 */
	private JButton[] createButtons() {
		JButton[] buttons = new JButton[10];
		int i = 0;
		loadIcon("icons/new_file.png", newDocumentAction, buttons, i);
		++i;
		loadIcon("icons/open_file.png", openDocumentAction, buttons, i);
		++i;
		loadIcon("icons/save_file.png", saveDocumentAction, buttons, i);
		++i;
		loadIcon("icons/save_as.png", saveAsDocumentAction, buttons, i);
		++i;
		loadIcon("icons/close_file.png", closeDocumentAction, buttons, i);
		++i;
		loadIcon("icons/exit_icon.png", exitAction, buttons, i);
		++i;
		loadIcon("icons/cut_icon.png", cutTextAction, buttons, i);
		++i;
		loadIcon("icons/copy_icon.png", copyTextAction, buttons, i);
		++i;
		loadIcon("icons/paste_icon.png", pasteTextAction, buttons, i);
		++i;
		loadIcon("icons/info_icon.png", infoAction, buttons, i);
		++i;
		return buttons;
	}

	/**
	 * Utility method for loading icon from resources.
	 * @param iconPath
	 * @param a - button action
	 * @param buttons - array of created buttons
	 * @param i - number of button
	 */
	private void loadIcon(String iconPath, Action a, JButton[] buttons, Integer i) {
		InputStream is = this.getClass().getResourceAsStream(iconPath);
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
		ImageIcon icon = new ImageIcon(bytes);
		
		JButton b = new JButton(a);
		b.setHideActionText(true);
		b.setIcon(new ImageIcon(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		buttons[i] = b;;
	}

	
	/**
	 * Utility method for creating menu bar and all it's items.
	 */
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		LJMenu fileMenu = new LJMenu("file", flp);
		menuBar.add(fileMenu);
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.add(new JMenuItem(infoAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exitAction));
		
		LJMenu editMenu = new LJMenu("edit", flp);
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem(cutTextAction));
		editMenu.add(new JMenuItem(copyTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));
		
		LJMenu toolsMenu = new LJMenu("tools", flp);
		menuBar.add(toolsMenu);
		
		LJMenu changeCaseSubMenu = new LJMenu("changeCase", flp);
		toolsMenu.add(changeCaseSubMenu);
		
		JMenuItem toUpperCaseItem = new JMenuItem(toUppercaseAction);
		toUpperCaseItem.setEnabled(false);
		changeCaseSubMenu.add(toUpperCaseItem);
		
		JMenuItem toLowerCaseItem = new JMenuItem(toLowercaseAction);
		toLowerCaseItem.setEnabled(false);
		changeCaseSubMenu.add(toLowerCaseItem);
		
		JMenuItem invertCaseItem = new JMenuItem(invertCaseAction);
		invertCaseItem.setEnabled(false);
		changeCaseSubMenu.add(invertCaseItem);
		
		LJMenu sortSubMenu = new LJMenu("sort", flp);
		toolsMenu.add(sortSubMenu);
		
		JMenuItem ascSortItem = new JMenuItem(ascSortAction);
		ascSortItem.setEnabled(false);
		sortSubMenu.add(ascSortItem);
		
		JMenuItem desSortItem = new JMenuItem(desSortAction);
		desSortItem.setEnabled(false);
		sortSubMenu.add(desSortItem);

		JMenuItem uniqueItem = new JMenuItem(uniqueAction);
		uniqueItem.setEnabled(false);
		sortSubMenu.add(uniqueItem);
		
		JMenu langMenu = new JMenu("Languages/Jezici/Sprache");
		menuBar.add(langMenu);
		langMenu.add(new JMenuItem(translateCroAction));
		langMenu.add(new JMenuItem(translateEngAction));
		langMenu.add(new JMenuItem(translateGerAction));
		
		
		
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Method which grant package-private access to status bar length label
	 * @return label
	 */
	JLabel getLengthLabel() {
		return lengthLabel;
	}
	
	/**
	 *  Method which grant package-private access to status bar caret label
	 * @return label
	 */
	public JLabel getCaretLabel() {
		return caretLabel;
	}
	
	/**
	 * Utility method which checks whether there are still some files unsaved before exiting program.
	 * @return false if user canceled exiting process, true otherwise
	 */
	private boolean checkUnsaved() {
		for(SingleDocumentModel d : docModel.listOfDocuments) {
			if(d.isModified()) {
				int answer = JOptionPane.showConfirmDialog(
						this,
						flp.getString("saveQ") + " " + (d.getFilePath() == null ? "(unnamed)" : d.getFilePath()) +" ?",
						flp.getString("optionTitle"),
						JOptionPane.YES_NO_CANCEL_OPTION
				);
				if(answer == JOptionPane.YES_OPTION) {
					if(d.getFilePath()==null) {
						JFileChooser fc = new JFileChooser();
						fc.setDialogTitle(flp.getString("saveAs"));
						if(fc.showSaveDialog(this)!= JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(
									this,
									flp.getString("nothingSaved"),
									flp.getString("warn"),
									JOptionPane.WARNING_MESSAGE
							);
							return false;
						}
						Path path = fc.getSelectedFile().toPath();
						d.setFilePath(path);
					}
					docModel.saveDocument(d, d.getFilePath());
				} else if(answer == JOptionPane.NO_OPTION) {
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Utility method for changing the state of tools menu items.
	 * If some text is selected, this menu items are enabled, otherwise they are not enabled.
	 */
	public void changeMenuState(boolean state) {
		toUppercaseAction.setEnabled(state);
		toLowercaseAction.setEnabled(state);
		invertCaseAction.setEnabled(state);
		ascSortAction.setEnabled(state);
		desSortAction.setEnabled(state);
		uniqueAction.setEnabled(state);
	}
	
	
	/**
	 * Main program for starting this GUI application
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}

}
