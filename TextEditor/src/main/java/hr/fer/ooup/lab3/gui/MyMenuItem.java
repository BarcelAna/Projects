package hr.fer.ooup.lab3.gui;

import hr.fer.ooup.lab3.model.Command;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MyMenuItem extends JMenuItem {
	private String text;
	private Command command;

	public MyMenuItem(String text, Command command) {
		super(text);
		this.command=command;
		addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeCommand();
			}
		});
	}

	private void executeCommand() {
		command.execute();
	}
}
