package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Account;

/**
 * @author Roy
 **/

public interface AccountDao {

	/**
	 * Deletes a Account from the system
	 * 
	 * @param account
	 */
	public void deleteAccount(Account account);

	/**
	 * @param accNumber
	 * @return
	 */
	public Account getAccountByAccountNumber(String accNumber);

	/**
	 * Retrieves the Account with
	 * 
	 * @param accountId
	 **/
	public Account getAccountById(long accountId);

	/**
	 * returns all the accounts belonging to a client
	 **/
	public List<Account> getAccountsByClientId(long clientId);

	/**
	 * returns all the client accounts existing in the database
	 **/
	public List<Account> getAllAcounts();

	/**
	 * Saves a Account to the system
	 * 
	 * @param account
	 */
	public void saveAccount(Account account);

	/**
	 * Saves a Account to the system
	 * 
	 * @param account
	 */
	public void updateAccount(Account account);
}
