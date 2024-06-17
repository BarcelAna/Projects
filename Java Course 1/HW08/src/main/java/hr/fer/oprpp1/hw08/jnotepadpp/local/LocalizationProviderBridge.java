package hr.fer.oprpp1.hw08.jnotepadpp.local;

/**
 * Class LocalizationProviderBridge is a decorator for some other localization provider.
 * It allows decorated localization provider to connect and register a listener, and disconnect and deregister listener from decorated object.
 * @author anace
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	/**
	 * connected status
	 */
	private boolean connected;
	
	/**
	 * decorated localization provider
	 */
	private ILocalizationProvider parent;
	
	/**
	 * listener registered on decorated object
	 */
	private ILocalizationListener listener;
	
	/**
	 * current language
	 */
	private String currentLanguage;
	
	/**
	 * Constructor which sets parent to given object.
	 * @param parent
	 */
	public LocalizationProviderBridge(ILocalizationProvider parent) {
		this.parent = parent;
	}
	
	/**
	 * Removes listener from parent object and sets connected status to false.
	 */
	public void disconnect() {
		parent.removeLocalizationListener(listener);
		connected = false;
	}
	
	/**
	 * If no one is connected to the bridge it registers listener to parent object and sets connected status to true.
	 */
	public void connect() {
		if(!connected) {
			ILocalizationListener listener = new ILocalizationListener() {

				@Override
				public void localizationChanged() {
					fire();
				}
				
			};
			parent.addLocalizationListener(listener);
			if(!parent.getLanguage().equals(currentLanguage)) {
				currentLanguage = parent.getLanguage();
				fire();
			}
			connected = true;
		}
		
	}
	
	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

	@Override
	public String getLanguage() {
		return currentLanguage;
	}

}
