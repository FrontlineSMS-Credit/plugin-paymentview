package org.creditsms.plugins.paymentview.ui.handler;

import static net.frontlinesms.FrontlineSMSConstants.COMMON_UNDEFINED;
import static net.frontlinesms.FrontlineSMSConstants.DEFAULT_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_BT_SAVE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_END_DATE;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_TF_START_DATE;
import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.keyword.KeywordTabHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.FormatterMarkerType;

/**
 * Base class containing shared attributes and behaviour of {@link KeywordAction} edit dialogs. 
 * @author aga
 */
public abstract class BaseActionDialog implements ThinletUiEventHandler {
	
//> CONSTANTS
	/** UI XML Layout file: date panel */
	public static final String UI_FILE_DATE_PANEL = "/ui/core/keyword/pnDate.xml";

//> INSTANCE PROPERTIES
	/** Log */
	protected Logger log = FrontlineUtils.getLogger(this.getClass());
	/** UI */
	protected final UiGeneratorController ui;
	/** The UI dialog component */
	private Object dialogComponent;
	/**
	 * The object that this dialog is dealing with.  This should either be a {@link Keyword}, if
	 * we are creating a new action, or a {@link KeywordAction} if we are editing an existing action.
	 */
	private Object targetObject;

//> CONSTRUCTORS
	/**
	 * Create a new instance, setting required fields.
	 * @param ui the UI which this is tied to
	 * @param owner the {@link KeywordTabHandler} which spawned this
	 */
	protected BaseActionDialog(UiGeneratorController ui) {
		this.ui = ui;
	}
	
	/** Show the dialog */
	public void show() {
		this.ui.add(this.dialogComponent);
	}
	
	public void init() {
		loadDialogFromFile();
	}

	/** Load the dialog for displaying. */
	private void loadDialogFromFile() {
		this.dialogComponent = ui.loadComponentFromFile(getLayoutFilePath(), this);
	}
	
	/** Perform any post-removal tasks, such as cleaning up references to this instance. */
	protected abstract void handleRemoved();
	
//> ACCESSORS
	/** @return the path to the Thinlet XML layout file for this dialog */
	protected abstract String getLayoutFilePath();
	
	/** @return the dialog component */
	protected Object getDialogComponent() {
		return dialogComponent;
	}
	
	/**
	 * @param <T> {@link Keyword} or {@link KeywordAction}
	 * @param clazz the class of {@link #targetObject}
	 * @return the {@link #targetObject}, cast to a particular class
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getTargetObject(Class<T> clazz) {
		return (T) targetObject;
	}
	
//> INSTANCE HELPER METHODS
	/** @return <code>true</code> if we are editing an existing {@link KeywordAction}, <code>false</code> if we are creating a new one. */
	protected boolean isEditing() {
		return true;//TODO: Remove Just a hack
	}
	
//> UI EVENT METHODS
	/** Remove the dialog from display. */
	public void removeDialog() {
		ui.remove(this.dialogComponent);
		this.handleRemoved();
	}
	
//> UI HELPER METHODS
	/** 
	 * Find a thinlet component within the {@link #dialogComponent}.
	 * @param componentName The name of the component
	 * @return the component with the given name, or <code>null</code> if none could be found.
	 */
	protected Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
	
//> UI PASSTHROUGH METHODS
	/**
	 * Adds a constant substitution marker to the text of an email action's text area (a thinlet component).
	 * @param currentText 
	 * @param textArea 
	 * @param type The constant that should be inserted
	 */
	public void addConstantToCommand(String currentText, Object textArea, String type) {
		addConstantToCommand(ui, currentText, textArea, type);
		textChanged(ui.getText(textArea));
	}
	
	public static void addConstantToCommand(UiGeneratorController ui, String currentText, Object textArea, String type) {
		String toAdd = "";
		switch (FormatterMarkerType.valueOf(type)) {
			case CLIENT_NAME:
				toAdd = PaymentViewCsvUtils.CLIENT_NAME;
				break;
			case AMOUNT_PAID:
				toAdd = PaymentViewCsvUtils.AMOUNT_PAID;
				break;
			case AMOUNT_REMAINING:
				toAdd = PaymentViewCsvUtils.AMOUNT_REMAINING;
				break;
			case DATE_PAID:
				toAdd = PaymentViewCsvUtils.DATE_PAID;
				break;
			case DAYS_REMAINING:
				toAdd = PaymentViewCsvUtils.DAYS_REMAINING;
				break;
			case MONTHLY_DUE:
				toAdd = PaymentViewCsvUtils.MONTHLY_DUE;
				break;
			case MONTHLY_DUEDATE:
				toAdd = PaymentViewCsvUtils.MONTHLY_DUEDATE;
				break;
			case MONTHLY_SAVINGS:
				toAdd = PaymentViewCsvUtils.MONTHLY_SAVINGS;
				break;
			case RECEPIENT_NAME:
				toAdd = PaymentViewCsvUtils.RECEPIENT_NAME;
				break;
			case TARGET_ENDDATE:
				toAdd = PaymentViewCsvUtils.TARGET_ENDDATE;
				break;
		}
		
		StringBuilder sb = new StringBuilder(currentText);
		int caretPosition = ui.getCaretPosition(textArea);
		sb.insert(caretPosition, toAdd);
		String newText = sb.toString();
		
		ui.setText(textArea, newText);
		ui.setCaretPosition(textArea, caretPosition + toAdd.length());
		ui.setFocus(textArea);
	}
	
	public void textChanged (String text) {
		boolean enableSaveButton = (text != null && !text.equals(""));
		this.ui.setEnabled(this.find(COMPONENT_BT_SAVE), enableSaveButton);
	}
	
}
