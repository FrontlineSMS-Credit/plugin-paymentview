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

public class TargetPayBillProcess  extends TargetCreationProcess{
	
	public  TargetPayBillProcess(Client client, Date targetStartDate, Date targetEndDate, List<TargetServiceItem> targetServiceItems, PaymentViewPluginController pluginController, BigDecimal totalTargetAmount){
		super(client, targetServiceItems, targetStartDate, targetEndDate, pluginController, totalTargetAmount);
	}
	
	@Override
	public void createTarget() throws DuplicateKeyException{
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
			this.setTarget(new Target(targetStartDate, targetEndDate, this.account, totalTargetAmount));
            this.getTargetDao().saveTarget(this.getTarget());
            this.getAccount().setActiveAccount(true);
            this.getAccountDao().updateAccount(this.getAccount());
            for (TargetServiceItem tsi: targetServiceItems){
            	tsi.setTarget(this.getTarget());
            	this.getTargetServiceItemDao().saveTargetServiceItem(tsi);
            }
		} else{
			this.setAccount(inactiveNonGenericAccounts.get(0));
			this.setTarget(new Target(targetStartDate, targetEndDate, this.account, totalTargetAmount));
            this.getTargetDao().saveTarget(this.getTarget());
            this.getAccount().setActiveAccount(true);
            this.getAccountDao().updateAccount(this.getAccount());
            for (TargetServiceItem tsi: targetServiceItems){
            	tsi.setTarget(this.getTarget());
            	this.getTargetServiceItemDao().saveTargetServiceItem(tsi);
            }
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
