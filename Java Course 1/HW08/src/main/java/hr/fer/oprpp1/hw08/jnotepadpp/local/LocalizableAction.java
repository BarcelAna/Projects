package hr.fer.oprpp1.hw08.jnotepadpp.local;


import javax.swing.AbstractAction;
import javax.swing.Action;
/**
 * LocalizableAction class extends AbstracAction.
 * It represents action object which communicates with localization provider and updates it's name on 
 * language change.
 * @author anace
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	/**
	 * language key
	 */
	private String key;

	/**
	 * Constructor which accepts language key and localization provider.
	 * @param key
	 * @param lp
	 */
	public LocalizableAction(String key, ILocalizationProvider lp) {
		this.key = key;
		update(lp);
		
		lp.addLocalizationListener(new ILocalizationListener() {
			
			@Override
			public void localizationChanged() {
				update(lp);
			}
			
		});
	
	}
	
	/**
	 * Utility method for updating action name and description to current language.
	 * @param key
	 * @param lp
	 */
	private void update(ILocalizationProvider lp) {
		String translationName = lp.getString(key);
		String translationDes = lp.getString(key+"Des");
		this.putValue(Action.NAME, translationName);
		this.putValue(Action.SHORT_DESCRIPTION, translationDes);
	}


	

}
