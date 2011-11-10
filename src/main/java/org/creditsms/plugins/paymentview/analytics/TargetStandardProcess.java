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

public class TargetStandardProcess extends TargetCreationProcess{
	public  TargetStandardProcess(Client client, Date targetStartDate, Date targetEndDate, List<TargetServiceItem> targetServiceItems, PaymentViewPluginController pluginController, BigDecimal totalTargetAmount){
		super(client, targetServiceItems, targetStartDate, targetEndDate, pluginController, totalTargetAmount);
	}
	
	@Override
	public void createTarget(){
		// Check if there are any accounts linked to the client
		if (this.getTotalListNonGenericAccounts().size()==0){
			// create new account
			this.setAccount(new Account(createAccountNumber(), client, false, false));
			try {
				this.getAccountDao().saveAccount(this.getAccount());
			} catch (DuplicateKeyException e) {
				e.printStackTrace();
			}
			// attach new account to the client
			// create new target
			Target temp= new Target(targetStartDate, targetEndDate, this.account, totalTargetAmount);
			temp.setStatus("inactive");
			this.setTarget(temp);
			client.getTargets().add(temp);
			//this.getClientDao().saveClient(client);
            this.getTargetDao().saveTarget(this.getTarget());
            this.getAccount().setActiveAccount(true);
            this.getAccountDao().updateAccount(this.getAccount());
            for (TargetServiceItem tsi: targetServiceItems){
            	tsi.setTarget(this.getTarget());
            	try {
					this.getTargetServiceItemDao().saveTargetServiceItem(tsi);
				} catch (DuplicateKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
		} else{
			//isActiveTarget
			if(this.getInactiveAccounts().size()!=0){
				this.setAccount(inactiveNonGenericAccounts.get(0));
				this.setTarget(new Target(targetStartDate, targetEndDate, this.account, totalTargetAmount));
	            this.getTargetDao().saveTarget(this.getTarget());
	            this.getAccount().setActiveAccount(true);
	            this.getAccountDao().updateAccount(this.getAccount());
	            for (TargetServiceItem tsi: targetServiceItems){
	            	tsi.setTarget(this.getTarget());
	            	try {
						this.getTargetServiceItemDao().saveTargetServiceItem(tsi);
					} catch (DuplicateKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
			}
		}
	}
	
	@Override
	public boolean canCreateTarget(){

		this.setInactiveNonGenericAccounts(this.getAccountDao().
				getInactiveNonGenericAccountsByClientId(this.getClient().getId()));
	
		this.setTotalListAccounts(this.getAccountDao().getNonGenericAccountsByClientId(
				this.getClient().getId())); 

		if(this.getInactiveAccounts().size()!=0 || this.getTotalListNonGenericAccounts().size()==0){
			return true;
		}else{
			return false;
		}
	}
}
