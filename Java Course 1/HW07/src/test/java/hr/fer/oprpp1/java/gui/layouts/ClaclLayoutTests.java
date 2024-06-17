package hr.fer.oprpp1.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

class ClaclLayoutTests {

	@Test
	void testPreferredSizeNoFirstElement() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
	
	@Test
	void testPreferredSizeWithFirstElement() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
	
	/*@Test
	void testRCPositionExceptions() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); 
		//l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel("");
		//l2.setPreferredSize(new Dimension(16,30));
		
		p.add(l2, new RCPosition(3,3));
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(1,8));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(-1,3));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(1,3));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(1,1));
			p.add(l2, new RCPosition(1,1));
		});
	} */
	
	@Test
	void notExistingRCPositionException() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); 
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(-1, 5));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(6, 5));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(4, 0));
		});
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(4, 8));
		});
	}
	
	@Test
	void firstElementPositionException() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); 
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(1, 3));
		});
	}
	
	@Test
	void moreComponentsOnTheSamePositionException() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); 
		JLabel l2 = new JLabel(""); 
		assertThrows(CalcLayoutException.class, () -> {
			p.add(l1, new RCPosition(4, 6));
			p.add(l2, new RCPosition(4, 6));
		});
	}
	
	@Test
	void RCPositionParserTest() {
		assertEquals(1,RCPosition.parse("1,2").getRow());
		assertEquals(2,RCPosition.parse("1,2").getColumn());
		assertThrows(ParsingException.class, ()->{
			RCPosition.parse("1,1,");
		});
	}
	
	@Test
	void addComponentExceptionTest() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		JLabel l2 = new JLabel("");
		assertThrows(NullPointerException.class, ()->{
			p.add(null, "1,1");
		});
		assertThrows(NullPointerException.class, ()->{
			p.add(l1, null);
		});
		assertThrows(IllegalArgumentException.class, ()->{
			p.add(l1, new Object());
		});
		assertThrows(CalcLayoutException.class, ()->{
			p.add(l1, "1,a");
		});
		assertThrows(CalcLayoutException.class, ()->{
			p.add(l1, "1,9");
		});
		assertThrows(CalcLayoutException.class, ()->{
			p.add(l1, "1,1");
			p.add(l2, "1,1");
		});
		assertThrows(CalcLayoutException.class, ()->{
			p.add(l1, "1,2");
		});
	}

}
