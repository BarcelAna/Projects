package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Class derived from LocalizationProviderBridge.
 * @author anace
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

	/**
	 * Constructor which accepts parent localization provider and frame.
	 * It adds window listener to the frame which connects to provider to the bridge when window is opened,
	 * and disconnetct when window is closed so that frame can be properly disposed and collected by GC.
	 * @param parent
	 * @param frame
	 */
	public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
		super(parent);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}


			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}

			
		});
	}

}
