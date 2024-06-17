package hr.fer.ooup.lab3.gui;

import hr.fer.ooup.lab3.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class TextEditor extends JComponent implements CursorObserver, TextObserver {
	private TextEditorModel model;

	public TextEditor(TextEditorModel model) {
		this.model=model;

		model.addCursorObserver(this);
		model.addTextObserver(this);

		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"moveRight");
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"moveLeft");
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"moveDown");
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0),"moveUp");

		this.getActionMap().put("moveRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()!=null) model.setSelectionRange(null);
				model.moveCursorRight();
			}
		});

		this.getActionMap().put("moveLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()!=null) model.setSelectionRange(null);
				model.moveCursorLeft();
			}
		});

		this.getActionMap().put("moveUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()!=null) model.setSelectionRange(null);
				model.moveCursorUp();
			}
		});

		this.getActionMap().put("moveDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()!=null) model.setSelectionRange(null);
				model.moveCursorDown();
			}
		});


		this.getInputMap().put(KeyStroke.getKeyStroke("shift LEFT"), "updateRangeLeft");
		this.getInputMap().put(KeyStroke.getKeyStroke("shift RIGHT"), "updateRangeRight");
		this.getInputMap().put(KeyStroke.getKeyStroke("shift U"), "updateRangeUp");
		this.getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"), "updateRangeDown");

		this.getActionMap().put("updateRangeLeft", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) {
					model.setSelectionRange(new LocationRange(model.getCursorLocation().row, model.getCursorLocation().column, model.getCursorLocation().row, model.getCursorLocation().column));
				}
				model.moveCursorLeft();
				model.setSelectionRangeEnd(model.getCursorLocation().row, model.getCursorLocation().column);
			}
		});

		this.getActionMap().put("updateRangeRight", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) {
					model.setSelectionRange(new LocationRange(model.getCursorLocation().row, model.getCursorLocation().column, model.getCursorLocation().row, model.getCursorLocation().column));
				}
				model.moveCursorRight();
				model.setSelectionRangeEnd(model.getCursorLocation().row, model.getCursorLocation().column);
			}
		});

		this.getActionMap().put("updateRangeUp", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) {
					model.setSelectionRange(new LocationRange(model.getCursorLocation().row, model.getCursorLocation().column, model.getCursorLocation().row, model.getCursorLocation().column));
				}
				model.moveCursorUp();
				model.setSelectionRangeEnd(model.getCursorLocation().row, model.getCursorLocation().column);
			}
		});

		this.getActionMap().put("updateRangeDown", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) {
					model.setSelectionRange(new LocationRange(model.getCursorLocation().row, model.getCursorLocation().column, model.getCursorLocation().row, model.getCursorLocation().column));
				}
				model.moveCursorDown();
				model.setSelectionRangeEnd(model.getCursorLocation().row, model.getCursorLocation().column);
			}
		});


		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "delBefore");
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delAfter");

		this.getActionMap().put("delBefore", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) model.deleteBefore(false);
				else model.deleteRange(model.getSelectionRange(), false);
			}
		});
		this.getActionMap().put("delAfter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSelectionRange()==null) model.deleteAfter(false);
				else model.deleteRange(model.getSelectionRange(), false);
			}
		});

		String alphanumericspecial = "ABCDEFGHIJKLMNOPQRSTVWXYZabcdefghijklmnopqrstvwxyz0123456789?<>@{}[]|\\€!\"#$%&/()=*+-_,.;\n ";
		for (char c : alphanumericspecial.toCharArray()) {
			KeyStroke keyStroke = KeyStroke.getKeyStroke(c);
			this.getInputMap().put(keyStroke, "writeText");
			this.getActionMap().put("writeText", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String actionCommand = e.getActionCommand();
					char pressedChar = actionCommand.charAt(actionCommand.length() - 1);
					model.insert(pressedChar, false);
				}
			});
		}

		this.getInputMap().put(KeyStroke.getKeyStroke("control C"), "copy");
		this.getInputMap().put(KeyStroke.getKeyStroke("control X"), "cut");
		this.getInputMap().put(KeyStroke.getKeyStroke("control V"), "paste");
		this.getInputMap().put(KeyStroke.getKeyStroke("control shift V"), "rem_paste");

		this.getActionMap().put("copy", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.copy();
			}
		});

		this.getActionMap().put("cut", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.cut();
			}
		});

		this.getActionMap().put("paste", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.paste();
			}
		});

		this.getActionMap().put("rem_paste", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.pasteAndTake();
			}
		});

		this.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "undo");
		this.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "redo");

		this.getActionMap().put("undo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UndoManager undoManager = model.getUndoManager();
				if(undoManager.canUndo())
					undoManager.undo();
			}
		});

		this.getActionMap().put("redo", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UndoManager undoManager = model.getUndoManager();
				if(undoManager.canRedo())
					undoManager.redo();
			}
		});

	}

	public void paintComponent(Graphics g) {
		if(model.getSelectionRange()!=null) {
			paintSelection(g);
		}
		g.setColor(Color.BLACK);
		int i = 1;
		Iterator it = model.allLines();
		while(it.hasNext()) {
			String line = (String) it.next();
			g.drawString(line, this.getX(), g.getFontMetrics().getHeight()*i);
			++i;
		}
		drawCursor(g);
	}


	@Override
	public void updateCursorLocation(Location location) {
		repaint();
	}

	private void paintSelection(Graphics g) {
		g.setColor(Color.CYAN);

		Location[] startAndEnd = model.getStartAndEnd(model.getSelectionRange());
		Location start = startAndEnd[0];
		Location end = startAndEnd[1];

		for(int currentRow = start.row; currentRow <= end.row; ++currentRow) {
			String line = model.line(currentRow-1);
			int x = 0;
			if(currentRow==start.row) {
				x = g.getFontMetrics().stringWidth(line.substring(0, start.column));
			}
			int y = g.getFontMetrics().getHeight()*(currentRow-1);
			int width;
			if(start.row==end.row) {
				width = g.getFontMetrics().stringWidth(line.substring(start.column, end.column));
			} else if(currentRow==start.row) {
				width = g.getFontMetrics().stringWidth(line.substring(start.column));
			} else if(currentRow==end.row) {
				width = g.getFontMetrics().stringWidth(line.substring(0, end.column));
			} else {
				width = g.getFontMetrics().stringWidth(line);
			}
			int height = g.getFontMetrics().getHeight();
			g.fillRect(x,y,width,height);
		}
	}

	private void drawCursor(Graphics g) {
		if(model.rows()==1 && model.line(0).isEmpty()) return;
		Location cursorLocation = model.getCursorLocation();
		FontMetrics metrics = g.getFontMetrics();
		int cursorH = metrics.getHeight()-5;
		int letters = model.getCursorLocation().column;
		int line = model.getCursorLocation().row;
		String currentLine = model.line(line-1);
		int x = metrics.charsWidth(currentLine.toCharArray(), 0, letters);
		int y = metrics.getHeight()*(line);
		g.drawLine(x,y,x,y-cursorH);
	}
	@Override
	public void updateText() {
		repaint();
	}

}
