package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;

/**
 * @author Roy
 **/

public interface AccountDao {

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
	public Account getAccountById(long accountId);
	
	/**
	 * Deletes a Account from the system
	 * @param account
	 */
	public void deleteAccount(Account account);

	/**
	 * Saves a Account to the system
	 * @param account
	 */
	public void saveAccount(Account account) throws DuplicateKeyException;
	
	/**
	 * Saves a Account to the system
	 * @param account
	 */
	public void updateAccount(Account account) throws DuplicateKeyException;
	
	/**
	 * @param accNumber
	 * @return
	 */
	public Account getAccountByAccountNumber(long accNumber); 
}
