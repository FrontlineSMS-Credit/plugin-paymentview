package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
import org.creditsms.plugins.paymentview.data.domain.Account;

/**
 * @author Roy
 **/

public interface AccountDAO {

	/**
	 * returns all the client accounts existing in the database
	 **/
	public List<Account> getAllAcounts();
	
	/**
	 * returns all the accounts belonging to a client
	 **/
	public List<Account> getAccountsByClientId(long clientId);
	
	/**
	 * Retrieves the Account with
	 * @param accountId
	 **/
	public Account getAccountByAccountId(long accountId);
	
	/**
	 * Deletes a Account from the system
	 * @param account
	 */
	public void deleteAccount(Account account);

	/**
	 * Saves a Account to the system
	 * @param account
	 */
	public void saveUpdateAccount(Account account);
}
