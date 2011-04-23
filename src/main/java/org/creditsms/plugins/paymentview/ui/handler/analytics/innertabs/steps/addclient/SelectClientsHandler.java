package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.steps.addclient;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.AddClientTabHandler;

public class SelectClientsHandler extends BasePanelHandler implements
		PagedComponentItemProvider {
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
	private Object clientsTableComponent;
	private String clientFilter = "";
	private ClientDao clientDao;
	private final AddClientTabHandler addClientTabHandler;

	protected SelectClientsHandler(UiGeneratorController ui,
			AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
	}

	private Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		String accountStr = "";
		for (Account a : client.getAccounts()) {
			accountStr += (Long.toString(a.getAccountNumber()) + ", ");
		}
		ui.add(row, ui.createTableCell(accountStr));
		return row;
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler
				.setCurrentStepPanel(new CreateSettingsHandler(
						(UiGeneratorController) ui, addClientTabHandler).getPanelComponent());
	}

	public void previous() {
		addClientTabHandler
				.setCurrentStepPanel(new SelectTargetSavingsHandler(
						(UiGeneratorController) ui, addClientTabHandler)
						.getPanelComponent());
	}

	public void selectService() {
		previous();
	}

	public void targetedSavings() {
		previous();
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.clientsTableComponent) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private PagedListDetails getClientListDetails(int startIndex, int limit) {
		List<Client> clients = null;
		if (this.clientFilter.equals("")) {
			clients = this.clientDao.getAllClients();
		} else {
			clients = this.clientDao.getClientsByName(clientFilter);
		}

		int totalItemCount = this.clientDao.getClientCount();
		Object[] listItems = toThinletComponents(clients);

		return new PagedListDetails(totalItemCount, listItems);
	}

	private Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}
}
