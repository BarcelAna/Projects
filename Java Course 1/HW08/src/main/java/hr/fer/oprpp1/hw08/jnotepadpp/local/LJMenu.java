package hr.fer.oprpp1.hw08.jnotepadpp.local;


import javax.swing.JMenu;

public class LJMenu extends JMenu {

	private static final long serialVersionUID = 1L;
	
	private String key;

	public LJMenu(String key, ILocalizationProvider lp) {
		this.key = key;
		
		updateMenu(lp);
		
		lp.addLocalizationListener(new ILocalizationListener() {

			@Override
			public void localizationChanged() {
				updateMenu(lp);
			}

			
		});
	}
	
	private void updateMenu(ILocalizationProvider lp) {
		String translation = lp.getString(key);
		this.setText(translation);
		
	}
	
}
