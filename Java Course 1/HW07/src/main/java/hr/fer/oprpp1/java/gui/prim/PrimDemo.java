package hr.fer.oprpp1.java.gui.prim;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 * PrimDemo is a GUI application which displays two lists of prime numbers.
 * On click of a button "Next", next prim number is generated and displayed in both lists
 * @author anace
 *
 */
public class PrimDemo extends JFrame{

	private static final long serialVersionUID = 1L;

	public PrimDemo() {
		this.setLocation(20, 20);
		this.setSize(300, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		
		PrimListModel listModel = new PrimListModel();
		
		this.setLayout(new BorderLayout());
		JPanel listContainer = new JPanel();
		listContainer.setLayout(new GridLayout(1, 2));
		
		JList<Integer> l1 = new JList<>(listModel);
		l1.setBorder(new LineBorder(Color.black));
		JList<Integer> l2 = new JList<>(listModel);
		l2.setBorder(new LineBorder(Color.black));
		listContainer.add(new JScrollPane(l1));
		listContainer.add(new JScrollPane(l2));
		
		ActionListener buttonListener = (e) -> {
			listModel.next();
		};
		JButton nextButton = new JButton("NEXT");
		nextButton.addActionListener(buttonListener);
		
		this.getContentPane().add(listContainer, BorderLayout.CENTER);
		this.getContentPane().add(nextButton, BorderLayout.SOUTH);
		
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new PrimDemo().setVisible(true);
		});

	}

}
