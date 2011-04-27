package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.apache.log4j.Logger;

/**
 * @author ian onesmus mukewa- ian@credit.frontlinesms.com
 * adopted from net.frontlinesms.ui.handler.BasePanelHandler
 * 
 */
public abstract class BaseTabbedPaneHandler implements ThinletUiEventHandler {
	/** Logging object */
	protected final Logger log = FrontlineUtils.getLogger(this.getClass());
	/** The Thinlet UI component which methods should be invoked upon. */
	private Object tabbedPaneComponent;

	/** The {@link UiGeneratorController} that shows the tab. */
	protected final FrontlineUI ui;

	// > INITIALISATION METHODS
	/**
	 * Create a new base dialog event handler tied to a specific
	 * {@link UiGeneratorController} instance.
	 * 
	 * @param ui
	 */
	protected BaseTabbedPaneHandler(FrontlineUI ui) {
		this.ui = ui;
	}

	// > UI HELPER METHODS
	/**
	 * Find a ui component within the dialog.
	 * 
	 * @param componentName
	 * @return
	 */
	protected Object find(String componentName) {
		return ui.find(this.tabbedPaneComponent, componentName);
	}

	// > ACCESSORS
	/** @return {@link #tabbedPaneComponent} */
	protected Object getTabbedPaneComponent() {
		return this.tabbedPaneComponent;
	}

	/**
	 * Load the dialog from the specified file, and sets the value of
	 * {@link #tabbedPaneComponent}.
	 * 
	 * @param uiLayoutFilePath
	 *            The classpath path of the layout file to use.
	 */
	protected void loadTabbedPane(String uiLayoutFilePath) {
		// Create a new dialog, add the desired panel and add a close() method
		// to remove the dialog
		this.tabbedPaneComponent = ui.loadComponentFromFile(uiLayoutFilePath,
				this);
	}
}
