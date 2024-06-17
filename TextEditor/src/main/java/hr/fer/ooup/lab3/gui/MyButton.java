package hr.fer.ooup.lab3.gui;

import hr.fer.ooup.lab3.model.Command;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MyButton extends JButton {
	private String text;
	private Command command;
	public MyButton(String text, Command command) {
		super(text);
		this.command=command;
		setFocusable(false);
		setEnabled(false);
		addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				command.execute();
			}
		});
	}
}
