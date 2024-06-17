package hr.fer.ooup.lab3;

import hr.fer.ooup.lab3.gui.MyButton;
import hr.fer.ooup.lab3.gui.MyMenuItem;
import hr.fer.ooup.lab3.gui.StatusBar;
import hr.fer.ooup.lab3.gui.TextEditor;
import hr.fer.ooup.lab3.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame implements SelectionObserver, ClipboardObserver, UndoManagerObserver {
	private MyButton undoBtn;
	private MyButton redoBtn;
	private MyButton cutBtn;
	private MyButton copyBtn;
	private MyButton pasteBtn;
	private MyMenuItem deleteItem;
	private MyMenuItem undoItem;
	private MyMenuItem redoItem;
	private MyMenuItem cutItem;
	private MyMenuItem copyItem;
	private MyMenuItem pasteItem;
	private MyMenuItem pasteTakeItem;
	private TextEditorModel model;

	public MainFrame() {
		this.setTitle("T-Editor");
		this.setLocation(30,30);
		this.setSize(500,500);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		model = new TextEditorModel("", this);
		model.getClipboardStack().addObserver(this);
		model.addSelectionObserver(this);
		model.getUndoManager().addObserver(this);

		this.setLayout(new BorderLayout());

		JPanel toolsAndMenus = new JPanel(new GridLayout(2, 1));

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu moveMenu = new JMenu("Move");
		JMenu pluginMenu = new JMenu("Plugins");

		JMenuItem openItem = new MyMenuItem("Open", new Command() {
			@Override
			public void execute() {
				//učitaj sadržaj neke datoteke u model - jednostavnija verzija
			}
		});

		JMenuItem saveItem = new MyMenuItem("Save", new Command() {
			@Override
			public void execute() {
				//spremi sadržaj u file
			}
		});

		JMenuItem exitItem = new MyMenuItem("Exit", new Command() {
			@Override
			public void execute() {
				MainFrame.this.dispose();
			}
		});

		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		undoItem = new MyMenuItem("Undo", new Command() {
			@Override
			public void execute() {
				model.getUndoManager().undo();
			}
		});

		redoItem = new MyMenuItem("Redo", new Command() {
			@Override
			public void execute() {
				model.getUndoManager().redo();
			}
		});

		cutItem = new MyMenuItem("Cut", new Command() {
			@Override
			public void execute() {
				model.cut();
			}
		});

		copyItem = new MyMenuItem("Copy", new Command() {
			@Override
			public void execute() {
				model.copy();
			}
		});

		pasteItem = new MyMenuItem("Paste", new Command() {
			@Override
			public void execute() {
				model.paste();
			}
		});

		pasteTakeItem = new MyMenuItem("Paste and Take", new Command() {
			@Override
			public void execute() {
				model.pasteAndTake();
			}
		});

		JMenuItem deleteItem = new MyMenuItem("Delete selection", new Command() {
			@Override
			public void execute() {
				model.deleteRange(model.getSelectionRange(), false);
			}
		});

		JMenuItem clearItem = new MyMenuItem("Clear document", new Command() {
			@Override
			public void execute() {
				model.clear();
			}
		});

		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(pasteTakeItem);
		editMenu.add(deleteItem);
		editMenu.add(clearItem);
		menuBar.add(editMenu);

		JMenuItem startItem = new MyMenuItem("Cursor to document start", new Command() {
			@Override
			public void execute() {
				model.moveCursorToStart();
			}
		});

		JMenuItem endItem = new MyMenuItem("Cursor to document end", new Command() {
			@Override
			public void execute() {
				model.moveCursorToEnd();
			}
		});

		moveMenu.add(startItem);
		moveMenu.add(endItem);
		menuBar.add(moveMenu);

		try {
			loadPlugins(pluginMenu);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		menuBar.add(pluginMenu);

		JToolBar toolBar = new JToolBar();
		undoBtn = new MyButton("Undo", new Command() {
			@Override
			public void execute() {
				model.getUndoManager().undo();
			}
		});

		redoBtn = new MyButton("Redo", new Command() {
			@Override
			public void execute() {
				model.getUndoManager().redo();
			}
		});

		cutBtn = new MyButton("Cut", new Command() {
			@Override
			public void execute() {
				model.cut();
			}
		});

		copyBtn = new MyButton("Copy", new Command() {
			@Override
			public void execute() {
				model.copy();
			}
		});

		pasteBtn = new MyButton("Paste", new Command() {
			@Override
			public void execute() {
				model.paste();
			}
		});

		toolBar.add(undoBtn);
		toolBar.add(redoBtn);
		toolBar.add(cutBtn);
		toolBar.add(copyBtn);
		toolBar.add(pasteBtn);

		toolsAndMenus.add(menuBar);
		toolsAndMenus.add(toolBar);

		JLabel statusBar = new StatusBar(model);
		statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.getContentPane().add(toolsAndMenus, BorderLayout.NORTH);
		this.getContentPane().add(new TextEditor(model));
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	private void loadPlugins(JMenu pluginMenu) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String dirPath = "C:\\Users\\anace\\Desktop\\ANA\\FER\\2.SEM\\OOUP\\LABOSI\\LAB3\\TextEditor\\src\\main\\java\\hr\\fer\\ooup\\lab3\\plugins";
		File dir = new File(dirPath);
		File[] plugins = dir.listFiles();
		if(plugins!=null) {
			for(File plugin : plugins) {
				String pluginName = plugin.getName().substring(0, plugin.getName().indexOf('.'));
				Class<Plugin> clazz = (Class<Plugin>) Class.forName("hr.fer.ooup.lab3.plugins."+pluginName);
				Plugin p = clazz.newInstance();
				pluginMenu.add(new MyMenuItem(p.getName(), new Command() {
					@Override
					public void execute() {
						p.execute(model, model.getUndoManager(), model.getClipboardStack());
					}
				}));
			}
		}
	}

	@Override
	public void updateClipboard() {
		pasteBtn.setEnabled(!model.getClipboardStack().isEmpty());
		pasteItem.setEnabled(!model.getClipboardStack().isEmpty());
		pasteTakeItem.setEnabled(!model.getClipboardStack().isEmpty());
	}

	@Override
	public void selectionUpdated() {
		cutBtn.setEnabled(!(model.getSelectionRange()==null));
		copyBtn.setEnabled(!(model.getSelectionRange()==null));
		cutItem.setEnabled(!(model.getSelectionRange()==null));
		copyItem.setEnabled(!(model.getSelectionRange()==null));
		deleteItem.setEnabled(!(model.getSelectionRange()==null));
	}

	@Override
	public void managerUpdated() {
		undoBtn.setEnabled(model.getUndoManager().canUndo());
		redoBtn.setEnabled(model.getUndoManager().canRedo());
		undoItem.setEnabled(model.getUndoManager().canUndo());
		redoItem.setEnabled(model.getUndoManager().canRedo());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new MainFrame().setVisible(true);
		});
	}
}
