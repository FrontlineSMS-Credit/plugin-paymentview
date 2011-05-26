package org.creditsms.plugins.paymentview.analytics;



import java.util.Date;
import java.util.List;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

public abstract class TargetCreationProcess {
	
	
	//> DAOs
	TargetDao targetDao;
    AccountDao accountDao;
	
	//> FIELDS
	protected Client client;
	protected Date targetStartDate;
	protected Date targetEndDate;
	protected ServiceItem serviceItem;
	protected Account account;
	protected Target target;
	protected List<Account> inactiveAccounts;
	protected List<Account> totalListAccounts;
	
	public TargetCreationProcess(Client client, ServiceItem serviceItem, Date targetStartDate, Date targetEndDate, PaymentViewPluginController pluginController) {
		this.client = client;
		this.serviceItem = serviceItem;
		this.targetStartDate = targetStartDate;
		this.targetEndDate = targetEndDate;
		this.accountDao  = pluginController.getAccountDao();
		this.targetDao = pluginController.getTargetDao();
	}
	
	public TargetDao getTargetDao() {
		return targetDao;
	}

	public AccountDao getAccountDao() {
		return accountDao;
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
		return inactiveAccounts;
	}

	public void setInactiveAccounts(List<Account> inactiveAccounts) {
		this.inactiveAccounts = inactiveAccounts;
	}

	public List<Account> getTotalListAccounts() {
		return totalListAccounts;
	}

	public void setTotalListAccounts(List<Account> totalListAccounts) {
		this.totalListAccounts = totalListAccounts;
	}
}
