package hr.fer.ooup.lab3.exercise1;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
	public MainFrame() {
		this.setLocation(30,30);
		this.setSize(500,500);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Main Frame");
		initGui();
	}

	private void initGui() {
		setLayout(new BorderLayout());
		JComponent myComponent = new MyGraphicComponent();
		this.getContentPane().add(myComponent);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()-> {
			new MainFrame().setVisible(true);
		});
	}
}