package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class implementing ILocalizationProvider interface.
 * @author anace
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/**
	 * List of localization listeners.
	 */
	private List<ILocalizationListener> listeners;
	
	/**
	 * Default constructor which initializes listeners list.
	 */
	public AbstractLocalizationProvider() {
		this.listeners = new ArrayList<>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all listeners about the change of language.
	 */
	public void fire() {
		for(ILocalizationListener l : listeners) {
			l.localizationChanged();
		}
	}
}
