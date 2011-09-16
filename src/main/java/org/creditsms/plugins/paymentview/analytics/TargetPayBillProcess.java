package org.creditsms.plugins.paymentview.analytics;

import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;

public class TargetPayBillProcess  extends TargetCreationProcess{
	
	public  TargetPayBillProcess(Client client, Date targetStartDate, Date targetEndDate,ServiceItem serviceItem, PaymentViewPluginController pluginController){
		super(client, serviceItem, targetStartDate, targetEndDate, pluginController);
	}
	
	@Override
	public void createTarget(){
		// Check if there are any accounts linked to the client
		if (canCreateTarget()){
			// create new account
			this.setAccount(new Account(createAccountNumber(), client, false, false));
			try {
				this.getAccountDao().saveAccount(this.getAccount());
			} catch (DuplicateKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// create new target
			this.setTarget(new Target(targetStartDate, targetEndDate, this.account));
            this.getTargetDao().saveTarget(this.getTarget());
            this.getAccount().setActiveAccount(true);
            this.getAccountDao().updateAccount(this.getAccount());
		} else{
			this.setAccount(inactiveNonGenericAccounts.get(0));
			this.setTarget(new Target(targetStartDate, targetEndDate, this.account));
            this.getTargetDao().saveTarget(this.getTarget());
            this.getAccount().setActiveAccount(true);
            this.getAccountDao().updateAccount(this.getAccount());
		}
	}
	
	@Override
	public boolean canCreateTarget(){
		this.setInactiveNonGenericAccounts(this.getAccountDao().
		getInactiveNonGenericAccountsByClientId(this.getClient().getId()));
		this.setTotalListAccounts(this.getAccountDao().getAccountsByClientId(
				this.getClient().getId()));
		if(this.getInactiveAccounts().size()==0 || this.getTotalListNonGenericAccounts().size()==0){
			return true;
		}else{
			return false;
		}
	}
}
