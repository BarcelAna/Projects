package hr.fer.ooup.lab3.exercise1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyGraphicComponent extends JComponent {
	public MyGraphicComponent() {
		this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"close");
		this.getActionMap().put("close", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = (JFrame) MyGraphicComponent.this.getRootPane().getParent();
				frame.dispose();
			}
		});
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.RED);
		g.drawLine(this.getX(), this.getY()+100,this.getWidth(),this.getY()+100);
		g.drawLine(this.getX()+100, this.getY(), this.getX()+100, this.getHeight());
		g.setColor(Color.BLACK);
		g.drawString("Tekst1", this.getWidth()/2, this.getHeight()/2);
		g.drawString("Tekst2", this.getWidth()/2, this.getHeight()/2+100);
	}
}
