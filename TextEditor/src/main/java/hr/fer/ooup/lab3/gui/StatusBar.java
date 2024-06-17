package hr.fer.ooup.lab3.gui;

import hr.fer.ooup.lab3.model.CursorObserver;
import hr.fer.ooup.lab3.model.Location;
import hr.fer.ooup.lab3.model.TextEditorModel;

import javax.swing.*;

public class StatusBar extends JLabel implements CursorObserver {
	private int rows;
	private int column;

	public StatusBar(TextEditorModel model) {
		super("  Ln:0  Col:0  ");
		model.addCursorObserver(this);
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public void updateCursorLocation(Location location) {
		rows = location.row;
		column = location.column;
		this.setText("  Ln:"+rows+"  Col:"+column+"  ");
	}
}
