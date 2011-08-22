package org.creditsms.plugins.paymentview.analytics;



import java.util.Date;
import java.util.List;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

public abstract class TargetCreationProcess {
	//> DAOs
	private TargetDao targetDao;
	private AccountDao accountDao;
	private ClientDao clientDao;
	
	//> FIELDS
	protected Client client;
	protected Date targetStartDate;
	protected Date targetEndDate;
	protected ServiceItem serviceItem;
	protected Account account;
	protected Target target;
	protected List<Account> inactiveNonGenericAccounts;
	protected List<Account> totalListNonGenericAccounts;
	
	public TargetCreationProcess(Client client, ServiceItem serviceItem, Date targetStartDate, Date targetEndDate, PaymentViewPluginController pluginController) {
		this.client = client;
		this.serviceItem = serviceItem;
		this.targetStartDate = targetStartDate;
		this.targetEndDate = targetEndDate;
		this.accountDao  = pluginController.getAccountDao();
		this.targetDao = pluginController.getTargetDao();
		this.clientDao = pluginController.getClientDao();
	}
	
	public TargetDao getTargetDao() {
		return targetDao;
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}
	
	public abstract void createTarget();
	public abstract boolean canCreateTarget();
	
	public String createAccountNumber(){
		int accountNumberGenerated = this.getAccountDao().getAccountCount()+1;
		String accountNumberGeneratedStr = String.format("%05d", accountNumberGenerated);
		while (this.getAccountDao().getAccountByAccountNumber(accountNumberGeneratedStr) != null){
			accountNumberGeneratedStr = String.format("%05d", ++ accountNumberGenerated);
		}
		return accountNumberGeneratedStr;
	}
	
	//ACCESSORS
	public Client getClient(){
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Date getTargetStartDate() {
		return targetStartDate;
	}

	public void setTargetStartDate(Date targetStartDate) {
		this.targetStartDate = targetStartDate;
	}

	public Date getTargetEndDate() {
		return targetEndDate;
	}

	public void setTargetEndDate(Date targetEndDate) {
		this.targetEndDate = targetEndDate;
	}

	public ServiceItem getServiceItem() {
		return serviceItem;
	}

	public void setServiceItem(ServiceItem serviceItem) {
		this.serviceItem = serviceItem;
	}
	
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	public List<Account> getInactiveAccounts() {
		return inactiveNonGenericAccounts;
	}

	public void setInactiveNonGenericAccounts(List<Account> inactiveNonGenericAccounts) {
		this.inactiveNonGenericAccounts = inactiveNonGenericAccounts;
	}

	public List<Account> getTotalListNonGenericAccounts() {
		return totalListNonGenericAccounts;
	}

	public void setTotalListAccounts(List<Account> totalListNonGenericAccounts) {
		this.totalListNonGenericAccounts = totalListNonGenericAccounts;
	}
}
