package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Locale;

import java.util.ResourceBundle;

import javax.swing.UIManager;

/**
 * Concrete implementation of ILocalizationProvider.
 * This class extends AbstractLocalizationProvider and offers information about current selected language.
 * It is based on singleton design pattern.
 * @author anace
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider{
	/**
	 * current language
	 */
	private String language;
	
	/**
	 * current resource bundle used for translations
	 */
	private ResourceBundle bundle;
	
	/**
	 * the only instance of localization provider since this class is a singleton
	 */
	private static LocalizationProvider instance;
	
	/**
	 * Default constructor which sets current language to English and load it's bundle
	 */
	private LocalizationProvider() {
		this.language = "en";
		setBundle();	
	}
	
	private void setBundle() {
		Locale locale = Locale.forLanguageTag(language);
		this.bundle = ResourceBundle.getBundle("hr.fer.oprpp1.hw08.jnotepadpp.local.prijevodi", locale);
		setOptionPaneLocalization();
	}

	/**
	 * Utility method for accomplishing localization of option pane dialogs' buttons.
	 */
	private void setOptionPaneLocalization() {
		UIManager.put("OptionPane.yesButtonText", bundle.getString("yes"));
		UIManager.put("OptionPane.noButtonText", bundle.getString("no"));
		UIManager.put("OptionPane.cancelButtonText", bundle.getString("cancel"));
		UIManager.put("FileChooser.saveButtonText", bundle.getString("save"));
		UIManager.put("FileChooser.cancelButtonText", bundle.getString("cancel"));
		UIManager.put("FileChooser.openButtonText", bundle.getString("open"));
	}

	/**
	 * Return the one instance of LocalizationProvider class.
	 * If instance is null then instance is being initialized with private constructor.
	 * @return provider instance
	 */
	public static LocalizationProvider getInstance() {
		if(instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
	}
	
	@Override
	public String getLanguage() {
		return language;
	}
	
	/**
	 * Sets current language to given value, load it's bundle and notifies all listeners about the change of language;
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
		setBundle();
		fire();
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

	

}
