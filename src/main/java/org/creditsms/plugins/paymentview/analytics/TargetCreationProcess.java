package org.creditsms.plugins.paymentview.analytics;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;

public abstract class TargetCreationProcess {
	//> DAOs
	private TargetDao targetDao;
	private AccountDao accountDao;
	private ClientDao clientDao;
	private TargetServiceItemDao targetServiceItemDao;

	//> FIELDS
	protected Client client;
	protected Date targetStartDate;
	protected Date targetEndDate;
	protected List<TargetServiceItem> targetServiceItems;
	protected Account account;
	protected Target target;
	protected List<Account> inactiveNonGenericAccounts;
	protected List<Account> totalListNonGenericAccounts;
	protected BigDecimal totalTargetAmount; 
	
	public TargetCreationProcess(Client client, List<TargetServiceItem> targetServiceItems, Date targetStartDate, Date targetEndDate, PaymentViewPluginController pluginController, BigDecimal totalTargetAmount) {
		this.client = client;
		this.targetServiceItems = targetServiceItems;
		this.targetStartDate = targetStartDate;
		this.targetEndDate = targetEndDate;
		this.accountDao  = pluginController.getAccountDao();
		this.targetDao = pluginController.getTargetDao();
		this.clientDao = pluginController.getClientDao();
		this.targetServiceItemDao = pluginController.getTargetServiceItemDao();
		this.totalTargetAmount = totalTargetAmount;
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
	
	public TargetServiceItemDao getTargetServiceItemDao() {
		return targetServiceItemDao;
	}
	
	public abstract void createTarget() throws DuplicateKeyException;
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
	public List<TargetServiceItem> getTargetServiceItems() {
		return targetServiceItems;
	}

	public void setTargetServiceItems(List<TargetServiceItem> targetServiceItems) {
		this.targetServiceItems = targetServiceItems;
	}
	
	public BigDecimal getTotalTargetAmount() {
		return totalTargetAmount;
	}

}
