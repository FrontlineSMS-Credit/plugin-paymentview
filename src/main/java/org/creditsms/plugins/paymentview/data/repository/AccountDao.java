package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

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
	
	/** @return number of Accountsf in the system */
	public int getAccountCount();

	/**
	 * returns all the accounts belonging to a client
	 **/
	public List<Account> getAccountsByClientId(long clientId);
	
	/**
	 * returns generic account belonging to a client
	 **/
	public Account getGenericAccountsByClientId(long clientId);
	
	/**
	 * returns all non-generic accounts belonging to a client
	 **/
	public List<Account> getNonGenericAccountsByClientId(long clientId);
	
	/**
	 * returns all active non-generic accounts belonging to a client
	 **/
	public List<Account> getActiveNonGenericAccountsByClientId(long clientId);
	
	/**
	 * return all the inactive accounts belonging to a client
	 **/
	
	public List<Account> getInactiveNonGenericAccountsByClientId(long clientId);

	/**
	 * returns all the client accounts existing in the database
	 **/
	public List<Account> getAllAcounts();

	/**
	 * Saves a Account to the system
	 * 
	 * @param account
	 * @throws DuplicateKeyException 
	 */
	public void saveAccount(Account account) throws DuplicateKeyException;

	/**
	 * Saves a Account to the system
	 * 
	 * @param account
	 */
	public void updateAccount(Account account);
	
	public String createAccountNumber();
}
