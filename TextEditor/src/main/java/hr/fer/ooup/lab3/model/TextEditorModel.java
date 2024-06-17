package hr.fer.ooup.lab3.model;

import javax.swing.*;
import java.util.*;

public class TextEditorModel {
	private List<String> lines;
	private LocationRange selectionRange;
	private Location cursorLocation;
	private List<CursorObserver> cursorObservers;
	private TextObserver textObserver;
	private ClipboardStack clipboardStack;
	private UndoManager undoManager;
	private List<SelectionObserver> selectionObservers;
	private JFrame frame;

	public TextEditorModel(String text, JFrame parentFrame) {
		lines = new ArrayList<>(Arrays.asList(text.split("\n")));
		cursorLocation = new Location(lines.size(), lines.get(lines.size()-1).length());
		clipboardStack = new ClipboardStack();
		undoManager = UndoManager.instance();
		cursorObservers = new ArrayList<>();
		selectionObservers=new ArrayList<>();
		frame=parentFrame;
	}

	public Location getCursorLocation() {
		return cursorLocation;
	}

	public int rows() {
		return lines.size();
	}

	public List<String> getLines() {return lines;}
	public void setLines(List<String> lines) {
		this.lines=lines;
		notifyTextObserver();
	}

	public Iterator allLines() {
		return lines.iterator();
	}

	public Iterator linesRange(int index1, int index2) {
		return new Iterator() {
			private int current = index1;
			@Override
			public boolean hasNext() {
				if(index2 >= lines.size() || index1 < 0 || current >= index2) {
					return false;
				}
				return true;
			}

			@Override
			public Object next() {
				String line = lines.get(current);
				++current;
				return line;
			}
		};
	}

	public String line(int i) {
		return lines.get(i);
	}

	public void addCursorObserver(CursorObserver observer) {
		cursorObservers.add(observer);
	}

	public void detachCursorObserver(CursorObserver observer) {
		cursorObservers.remove(observer);
	}

	private void notifyCursorObservers() {
		for(CursorObserver o:cursorObservers) {
			o.updateCursorLocation(cursorLocation);
		}
	}

	public void addTextObserver(TextObserver observer) {
		textObserver=observer;
	}

	public void removeTextObserver() {
		textObserver=null;
	}

	private void notifyTextObserver() {
		textObserver.updateText();
	}

	public void addSelectionObserver(SelectionObserver observer) {
		selectionObservers.add(observer);
	}

	public void detachSelectionObserver(SelectionObserver observer) {
		selectionObservers.remove(observer);
	}

	private void notifySelectionObservers() {
		for(SelectionObserver o:selectionObservers)
			o.selectionUpdated();
	}

	public void moveCursorLeft() {
		if(cursorLocation.column>0) {
			cursorLocation.column--;
		} else if(cursorLocation.column==0 && cursorLocation.row!=1){
			cursorLocation.row--;
			cursorLocation.column = lines.get(cursorLocation.row-1).length();
		}
		notifyCursorObservers();
	}

	public void moveCursorRight() {
		if(cursorLocation.column<lines.get(cursorLocation.row-1).length()) {
			cursorLocation.column++;
			notifyCursorObservers();
		} else if(cursorLocation.column==lines.get(cursorLocation.row-1).length() && cursorLocation.row!=lines.size()) {
			cursorLocation.row++;
			cursorLocation.column=0;
			notifyCursorObservers();
		}
	}

	public void moveCursorUp() {
		if(cursorLocation.row>1) {
			cursorLocation.row--;
			if (cursorLocation.column > lines.get(cursorLocation.row - 1).length()) {
				cursorLocation.column = lines.get(cursorLocation.row - 1).length();
			}
			notifyCursorObservers();
		}
	}

	public void moveCursorDown() {
		if(cursorLocation.row<lines.size()) {
			cursorLocation.row++;
			if (cursorLocation.column > lines.get(cursorLocation.row - 1).length()) {
				cursorLocation.column = lines.get(cursorLocation.row - 1).length();
			}
			notifyCursorObservers();
		}
	}

	public void deleteBefore(boolean createAction) {
		final Character[] deleted = {null};
		if(cursorLocation.column==0 && cursorLocation.row != 1) {
			moveCursorLeft();
			String line = lines.remove(cursorLocation.row);
			String previousLine = lines.remove(cursorLocation.row-1);
			String newLine = previousLine+line;
			lines.add(cursorLocation.row-1, newLine);
			deleted[0]='\n';
		} else {
			char[] line = lines.remove(cursorLocation.row-1).toCharArray();
			StringBuilder newLine = new StringBuilder();
			for(int i = 0; i < line.length; ++i) {
				if(i!=cursorLocation.column-1)
					newLine.append(line[i]);
				else
					deleted[0] =line[i];
			}
			lines.add(cursorLocation.row-1, newLine.toString());
			moveCursorLeft();
		}
		if(!createAction) {
			EditAction action = new EditAction() {
				Location cursor = new Location(cursorLocation.row, cursorLocation.column);
				TextEditorModel model = TextEditorModel.this;
				Character character = deleted[0];

				@Override
				public void execute_do() {
					model.cursorLocation = cursor;
					model.deleteBefore(true);
				}

				@Override
				public void execute_undo() {
					model.cursorLocation = cursor;
					if(deleted==null) {
						model.insert('\n', true);
					} else {
						model.insert(deleted[0], true);
					}
				}
			};
			undoManager.push(action);
		}
		notifyTextObserver();
	}
	public void deleteAfter(boolean createAction) {
		if(lines.size()==1 && lines.get(0).isEmpty()) return;

		final Character[] deleted = {null};
		if(lines.get(cursorLocation.row-1).isEmpty()) {
			lines.remove(cursorLocation.row-1);
			moveCursorLeft();
			deleted[0]='\n';
		} else if(cursorLocation.column==lines.get(cursorLocation.row-1).length() && cursorLocation.row<lines.size()) {
			moveCursorRight();
			deleteBefore(false);
			deleted[0]='\n';
		} else {
			char[] line = lines.remove(cursorLocation.row-1).toCharArray();
			StringBuilder newLine = new StringBuilder();
			for(int i = 0; i < line.length; ++i) {
				if(i!=cursorLocation.column)
					newLine.append(line[i]);
				else
					deleted[0]=line[i];
			}
			lines.add(cursorLocation.row-1, newLine.toString());
		}
		if(!createAction) {
			EditAction action = new EditAction() {
				Location cursor = new Location(cursorLocation.row, cursorLocation.column);
				TextEditorModel model = TextEditorModel.this;
				char character = deleted[0];

				@Override
				public void execute_do() {
					model.cursorLocation = cursor;
					model.deleteAfter(true);
				}

				@Override
				public void execute_undo() {
					model.cursorLocation=cursor;
					model.insert(character, true);
					model.moveCursorLeft();
				}
			};
			undoManager.push(action);
		}
		notifyTextObserver();
	}

	public void deleteRange(LocationRange r, boolean createAction) {
		Location[] startAndEnd = getStartAndEnd(r);
		Location start = startAndEnd[0];
		Location end = startAndEnd[1];
		String deleted = getSelectedText();
		if(cursorLocation.equals(end)) { //unaprijedni
			while(!cursorLocation.equals(start))
				deleteBefore(true);
		} else { //unazadni
			int length = deleted.length()+(end.row-start.row);
			for(int i = 0; i < length; ++i) {
				deleteAfter(true);
			}
		}
		if(!createAction) {
			EditAction editAction = new EditAction() {
				LocationRange range = new LocationRange(selectionRange.getStart().row, selectionRange.getStart().column, selectionRange.getEnd().row, selectionRange.getEnd().column);
				String deletedText = deleted;
				Location cursor = getStartAndEnd(range)[0];

				@Override
				public void execute_do() {
					deleteRange(selectionRange, true);
				}

				@Override
				public void execute_undo() {
					cursorLocation=cursor;
					insert(deletedText, true);
					setSelectionRange(range);
				}
			};
			undoManager.push(editAction);
		}
		setSelectionRange(null);
	}

	public void insert(char c, boolean createAction) {
		if(selectionRange!=null) {
			deleteRange(selectionRange, false);
		}
		if(c=='\n') {
			newLine();
		} else {
			char[] line = lines.remove(cursorLocation.row-1).toCharArray();
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < line.length; ++i) {
				if(i== cursorLocation.column) {
					sb.append(c);
				}
				sb.append(line[i]);
			}
			if(cursorLocation.column==line.length) {
				sb.append(c);
			}
			lines.add(cursorLocation.row-1, sb.toString());
			moveCursorRight();
		}
		if(!createAction) {
			EditAction action = new EditAction() {
				Location cursor = new Location(cursorLocation.row, cursorLocation.column);
				char character = c;

				TextEditorModel model = TextEditorModel.this;

				@Override
				public void execute_do() {
					model.cursorLocation=cursor;
					model.insert(character, true);
				}

				@Override
				public void execute_undo() {
					model.cursorLocation=cursor;
					model.deleteBefore(true);
				}
			};
			undoManager.push(action);
		}
		notifyTextObserver();
	}

	public void insert(String text, boolean createAction) {
		char[] line = lines.remove(cursorLocation.row-1).toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < line.length; ++i) {
			if(i==cursorLocation.column) {
				for(char c:text.toCharArray()) {
					sb.append(c);
				}
			}
			sb.append(line[i]);
		}
		if(cursorLocation.column==line.length) {
			for(char c:text.toCharArray()) {
				sb.append(c);
			}
		}
		String[] newLines = sb.toString().split("\n");
		for(int i = 0, j = cursorLocation.row; i < newLines.length; ++i, ++j) {
			lines.add(j-1, newLines[i]);
		}
		for(int i = 0; i < text.length(); ++i)
			moveCursorRight();
		notifyTextObserver();
	}

	public LocationRange getSelectionRange() {
		return selectionRange;
	}

	public void setSelectionRangeEnd(int endRow, int endColumn) {
		selectionRange.setEnd(endRow, endColumn);
		notifyTextObserver();
		notifySelectionObservers();

	}

	public void setSelectionRange(LocationRange range) {
		selectionRange=range;
		notifySelectionObservers();
	}

	private void newLine() {
		String line = lines.remove(cursorLocation.row-1);
		String firstPart = line.substring(0, cursorLocation.column);
		String secondPart = line.substring(cursorLocation.column);
		lines.add(cursorLocation.row-1, firstPart);
		lines.add(cursorLocation.row, secondPart);
		moveCursorRight();
		notifyTextObserver();
	}

	public ClipboardStack getClipboardStack() {
		return clipboardStack;
	}

	public String getSelectedText() {
		Location[] startAndEnd = getStartAndEnd(selectionRange);
		Location start = startAndEnd[0];
		Location end = startAndEnd[1];
		StringBuilder sb = new StringBuilder();
		for(int currentRow = start.row; currentRow <= end.row; ++currentRow) {
			if(start.row==end.row) {
				sb.append(line(start.row-1), start.column, end.column);
			} else if(currentRow==start.row) {
				sb.append(line(currentRow-1).substring(start.column));
				sb.append("\n");
			} else if(currentRow==end.row) {
				sb.append(line(currentRow-1), 0, end.column);
			} else {
				sb.append(line(currentRow-1));
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public Location[] getStartAndEnd(LocationRange range) {
		Location[] startAndEnd = new Location[2];
		Location start;
		Location end;
		if(range.getStart().row==range.getEnd().row) {
			if (range.getStart().column < range.getEnd().column) {
				start = range.getStart();
				end = range.getEnd();
			} else {
				start = range.getEnd();
				end = range.getStart();
			}
		} else if(range.getStart().row<range.getEnd().row) {
			start = range.getStart();
			end = range.getEnd();
		} else {
			start = range.getEnd();
			end = range.getStart();
		}
		startAndEnd[0]=start;
		startAndEnd[1]=end;
		return startAndEnd;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public void copy() {
		LocationRange range = getSelectionRange();
		if(range!=null) {
			String line = getSelectedText();
			getClipboardStack().push(line);
		}
	}


	public void cut() {
		LocationRange range = getSelectionRange();
		if(range!=null){
			String line = getSelectedText();
			getClipboardStack().push(line);
			deleteRange(getSelectionRange(), false);
		}
	}

	public void paste() {
		String line = getClipboardStack().peek();
		insert(line, false);
	}

	public void pasteAndTake() {
		String line = getClipboardStack().pop();
		insert(line, false);
	}

	public void clear() {
		lines = new ArrayList<>();
		lines.add("");
		moveCursorToStart();
		notifyTextObserver();
	}

	public void moveCursorToStart() {
		cursorLocation=new Location(1,0);
		notifyCursorObservers();
	}

	public void moveCursorToEnd() {
		cursorLocation=new Location(rows(), line(rows()-1).length());
		notifyCursorObservers();
	}

	public JFrame getFrame() {
		return frame;
	}
}
