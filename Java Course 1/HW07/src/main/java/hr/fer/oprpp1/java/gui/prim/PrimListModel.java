package hr.fer.oprpp1.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Class PrimListModel represents list model for list displaying prime numbers.
 * It implements ListModel<Integer> interface
 * @author anace
 *
 */
public class PrimListModel implements ListModel<Integer> {
	/**
	 * List for storing elements of list
	 */
	private List<Integer> list;
	
	/**
	 * Last generated prime number
	 */
	private int lastPrim;
	
	/**
	 * List of ListDataListener objects
	 */
	private List<ListDataListener> listeners;
	
	/**
	 * Default constructor
	 */
	public PrimListModel() {
		list = new ArrayList<>();
		listeners = new ArrayList<>();
		list.add(1);
		lastPrim = 1;
	}

	/**
	 * Generates next prime number, adds it to list and notifies all listeners
	 */
	public void next() {
		int current = lastPrim;
		boolean continueSearch = true;
		while(continueSearch) {
			current++;
			continueSearch = false;
			for(int i = 2; i < current; ++i) {
				if(current % i == 0) {
					continueSearch = true;
					break;
				}
			}
		}
		list.add(current);
		for(ListDataListener l : listeners) {
			l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, list.size()-1, list.size()-1));
		}
		lastPrim = current;
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}


}
