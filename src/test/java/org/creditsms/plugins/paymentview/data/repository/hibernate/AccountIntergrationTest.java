package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 *
 */
public class AccountIntergrationTest extends HibernateTestCase {
	@Autowired
	HibernateAccountDao hibernateAccountDao;
	@Autowired
	HibernateClientDao hibernateClientDao;
	
	public void testSetup() {
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateClientDao);
	}
	
	public void testSave() throws DuplicateKeyException {
		assertNoAccountsInDatabase();
		Account ac = setAccountNumber("103");
		hibernateAccountDao.saveAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testAccountNumberUnique() throws DuplicateKeyException {
		assertNoAccountsInDatabase();
		final Account ac1 = setAccountNumber("101");
		final Account ac2 = setAccountNumber("101");
		hibernateAccountDao.saveAccount(ac1);
		try {
			hibernateAccountDao.saveAccount(ac2);
			fail("Duplicate Account number");
		} catch(DuplicateKeyException di) {
			// expected
		}
	}
	
	public void testDeleteAccount() throws DuplicateKeyException{
		assertNoAccountsInDatabase();
		Account saved = createAndSaveAccount("104", null, 1, true, false);
		hibernateAccountDao.deleteAccount(saved);
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testGetAccountById() throws DuplicateKeyException{
		assertNoAccountsInDatabase();
		createAndSaveAccount("104", null, 1, true, false);
		assertEquals("104", getAccount().getAccountNumber());
	}
	
	public void testGetAccountByAccountNumber() throws DuplicateKeyException {
		createAndSaveAccount("123456", null, 1, true, false);
		assertEquals("123456", this.hibernateAccountDao.getAccountByAccountNumber("123456").getAccountNumber());
	}
	
	public void testGetAccountsByUserSingle() throws DuplicateKeyException{
		Client client = createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount("21", client, 1, true, false);

		assertEquals("21", hibernateAccountDao.getAccountsByClientId(client.getId()).get(0).getAccountNumber());
	}
	
	public void testGetAccountsByUserIdMultiple() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveClient("+2540720000002", 2);

		createAndSaveAccount("12",getExistingClient(0), 1, true, false);
		createAndSaveAccount("13",getExistingClient(0), 2, true, false);
		createAndSaveAccount("14",getExistingClient(1), 3, true, false);
		createAndSaveAccount("15",getExistingClient(1), 4, true,false);
		
		long searchbyClientId = getExistingClient(1).getId();
		assertEquals("14", hibernateAccountDao.getAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}
	
	public void testGetGenericAccountsByClientId() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount("12",getExistingClient(0), 1, true, true);
		long searchbyClientId = getExistingClient(0).getId();
		assertEquals("12", hibernateAccountDao.getGenericAccountsByClientId(searchbyClientId).getAccountNumber());
	}
	
	public void testGetNonGenericAccountsByClientId() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount("12",getExistingClient(0), 1, false, true);
		createAndSaveAccount("13",getExistingClient(0), 2, false, false);
		
		long searchbyClientId = getExistingClient(0).getId();
		assertEquals("13", hibernateAccountDao.getNonGenericAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
		
	}
	
	public void testGetActiveNonGenericAccountsByClientId() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount("12",getExistingClient(0), 1, false, false);
		createAndSaveAccount("13",getExistingClient(0), 2, true, false);
		createAndSaveAccount("14",getExistingClient(0), 3, true, false);

		long searchbyClientId = getExistingClient(0).getId();
		assertEquals("13", hibernateAccountDao.getActiveNonGenericAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}

	public void testInactiveNonGenericAccountsByClientId() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);

		createAndSaveAccount("12",getExistingClient(0), 1, true, false);
		createAndSaveAccount("13",getExistingClient(0), 2, false, false);

		long searchbyClientId = getExistingClient(0).getId();
		assertEquals("13", hibernateAccountDao.getInactiveNonGenericAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}
	
	void assertNoAccountsInDatabase() {
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testCreateAccountNumber() throws DuplicateKeyException {
		assertNoAccountsInDatabase();
		Account ac1 = setAccountNumber(createAccountNumber());
		hibernateAccountDao.saveAccount(ac1);
		Account ac2 = setAccountNumber("00002");
		hibernateAccountDao.saveAccount(ac2);
		Account ac3 = setAccountNumber(createAccountNumber());
		hibernateAccountDao.saveAccount(ac3);
		Account ac4 = setAccountNumber("00005");
		hibernateAccountDao.saveAccount(ac4);
		
		createAccountNumber();
	}
	
	
	private Account setAccountNumber(String accNum){
		Account acc = new Account();
		acc.setAccountNumber(accNum);
		acc.setActiveAccount(true);
		return acc;
	}
	
	private Client createAndSaveClient(String phNoStr, long expectedClientCount) throws DuplicateKeyException{
		Client client = createClientWithPhoneNumber(phNoStr);
		hibernateClientDao.saveClient(client);
		assertEquals(expectedClientCount, hibernateClientDao.getAllClients().size());
		return client;
	}
	
	private Client createClientWithPhoneNumber(String phNoStr){
		Client c = new Client();
		c.setPhoneNumber(phNoStr);
		
		return c;
	}
	
	private Account getAccount(){
		List<Account> lstAccount = hibernateAccountDao.getAllAcounts();
		return this.hibernateAccountDao.getAccountById(lstAccount.get(0).getAccountId());
	}
	
	private Client getExistingClient(int clientPoz){
		return this.hibernateClientDao.getAllClients().get(clientPoz);
	}
	
	private Account createAndSaveAccount(String accNumber, Client client, long expectedAccountCount, boolean accAtatus, boolean genericAccount) throws DuplicateKeyException{
		Account account = createAccountWithAccountNumber(accNumber, client, accAtatus, genericAccount);
		hibernateAccountDao.saveAccount(account);
		assertEquals(expectedAccountCount, hibernateAccountDao.getAllAcounts().size());
		return account;
	}
	
	private Account createAccountWithAccountNumber(String accNumber, Client client, boolean accStatus, boolean genericAccount){
		Account account = new Account();
		account.setAccountNumber(accNumber);
		account.setActiveAccount(accStatus);
		account.setGenericAccount(genericAccount);
		if(client!=null)account.setClient(client);
		return account;
	}
	
	private String createAccountNumber(){
		int accountNumberGenerated = this.hibernateAccountDao.getAccountCount()+1;
		String accountNumberGeneratedStr = String.format("%05d", accountNumberGenerated);
		while (this.hibernateAccountDao.getAccountByAccountNumber(accountNumberGeneratedStr) != null){
			accountNumberGeneratedStr = String.format("%05d", ++ accountNumberGenerated);
			System.out.println("counter incremented as previous generated account number exists:"+ accountNumberGeneratedStr);
		}
		return accountNumberGeneratedStr;
	}
}
