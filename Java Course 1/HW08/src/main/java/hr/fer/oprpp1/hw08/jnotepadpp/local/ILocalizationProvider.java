package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Localization Provider interface.
 * @author anace
 *
 */
public interface ILocalizationProvider {
	/**
	 * Adds listener to the list of localization listeners.
	 * @param listener
	 */
	public void addLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Removes listener to the list of localization listeners.
	 * @param listener
	 */
	public void removeLocalizationListener(ILocalizationListener listener);
	
	/**
	 * Returns localization for given key.
	 * @param listener
	 * @return value stored at the given key
	 */
	public String getString(String key);
	
	/**
	 * Returns current language
	 * @return current language
	 */
	public String getLanguage();
}
