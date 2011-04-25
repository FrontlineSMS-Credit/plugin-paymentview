package org.creditsms.plugins.paymentview.data.repository.hibernate;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;


import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

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
		Account ac1 = setAccountNumber("101");
		Account ac2 = setAccountNumber("101");

		hibernateAccountDao.saveAccount(ac1);
		try{
			hibernateAccountDao.saveAccount(ac2);
			fail("you cannot add more than one account with same accounts number!!");
		}catch (DuplicateKeyException e) {
			// expected
		}
	}
	
	public void testDeleteAccount() throws DuplicateKeyException{
		assertNoAccountsInDatabase();
		Account saved = createAndSaveAccount("104", null, 1);
		hibernateAccountDao.deleteAccount(saved);
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testGetAccountById() throws DuplicateKeyException{
		assertNoAccountsInDatabase();
		createAndSaveAccount("104", null, 1);
		assertEquals(104, getExistingAccount(0).getAccountNumber());
	}
	
	public void testGetAccountByAccountNumber() throws DuplicateKeyException {
		Account acc = createAndSaveAccount("123456", null, 1);
		assertEquals(123456, this.hibernateAccountDao.getAccountByAccountNumber("123456").getAccountNumber());
	}
	
	public void testGetAccountsByUserSingle() throws DuplicateKeyException{
		Client client = createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount("21", client, 1);

		assertEquals(21, hibernateAccountDao.getAccountsByClientId(client.getId()).get(0).getAccountNumber());
	}
	
	public void testGetAccountsByUserIdMultiple() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveClient("+2540720000002", 2);

		createAndSaveAccount("12",getExistingClient(0), 1);
		createAndSaveAccount("13",getExistingClient(0), 2);
		createAndSaveAccount("14",getExistingClient(1), 3);
		createAndSaveAccount("15",getExistingClient(1), 4);
		
		long searchbyClientId = getExistingClient(1).getId();
		assertEquals(14, hibernateAccountDao.getAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}

	void assertNoAccountsInDatabase() {
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	private Account setAccountNumber(String accNum){
		Account acc = new Account();
		acc.setAccountNumber(accNum);
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
	
	private Account getExistingAccount(int clientPoz){
		return this.hibernateAccountDao.getAllAcounts().get(clientPoz);
	}
	
	private Client getExistingClient(int clientPoz){
		return this.hibernateClientDao.getAllClients().get(clientPoz);
	}
	
	private Account createAndSaveAccount(String accNumber, Client client, long expectedAccountCount) throws DuplicateKeyException{
		Account account = createAccountWithAccountNumber(accNumber, client);
		hibernateAccountDao.saveAccount(account);
		assertEquals(expectedAccountCount, hibernateAccountDao.getAllAcounts().size());
		return account;
	}
	
	private Account createAccountWithAccountNumber(String accNumber, Client client){
		Account account = new Account();
		account.setAccountNumber(accNumber);
		if(client!=null)account.setClient(client);
		return account;
	}
}
