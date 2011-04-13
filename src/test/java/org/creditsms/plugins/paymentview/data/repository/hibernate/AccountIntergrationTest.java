package org.creditsms.plugins.paymentview.data.repository.hibernate;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;


import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

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
		asserEmptyAccount();
		Account ac = setAccountNumber(103);
		hibernateAccountDao.saveAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testAccountNumberUnique(){
		asserEmptyAccount();
		Account ac1 = setAccountNumber(101);
		Account ac2 = setAccountNumber(101);
		boolean passedTest = false;
		
		try{
			hibernateAccountDao.saveAccount(ac1);
			hibernateAccountDao.saveAccount(ac2);
			fail("you cannot add more than one account with same accounts number!!");
			passedTest = false;
		}catch (DuplicateKeyException e) {
			passedTest = true;
			assertTrue(passedTest);
		}
	}
	
	public void testDeleteAccount() throws DuplicateKeyException{
		asserEmptyAccount();
		createAndSaveAccount(104, null, 1);
		hibernateAccountDao.deleteAccount(getExistingAccount(0));
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testGetAccountById() throws DuplicateKeyException{
		asserEmptyAccount();
		createAndSaveAccount(104, null, 1);
		assertEquals(104, getExistingAccount(0).getAccountNumber());
	}
	
	public void testGetAccountsByUserSingle() throws DuplicateKeyException{
		createAndSaveClient("+2540720111111", 1);
		createAndSaveAccount(21,getExistingClient(0), 1);

		long searchbyClientId = getExistingAccount(0).getClient().getId();
		assertEquals(21, hibernateAccountDao.getAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}
	
	public void testGetAccountsByUserIdMultiple() throws DuplicateKeyException {
		createAndSaveClient("+2540720111111", 1);
		createAndSaveClient("+2540720000002", 2);

		createAndSaveAccount(12,getExistingClient(0), 1);
		createAndSaveAccount(13,getExistingClient(0), 2);
		createAndSaveAccount(14,getExistingClient(1), 3);
		createAndSaveAccount(15,getExistingClient(1), 4);
		
		long searchbyClientId = getExistingClient(1).getId();
		assertEquals(14, hibernateAccountDao.getAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}

	void asserEmptyAccount(){
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	private Account setAccountNumber(long accNum){
		Account acc = new Account();
		acc.setAccountNumber(accNum);
		
		return acc;
	}
	
	private void createAndSaveClient(String phNoStr, long expectedClientCount) throws DuplicateKeyException{
		hibernateClientDao.saveClient(createClientWithPhoneNumber(phNoStr));
		assertEquals(expectedClientCount, hibernateClientDao.getAllClients().size());
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
	
	private void createAndSaveAccount(long accNumber, Client client, long expectedAccountCount) throws DuplicateKeyException{
		hibernateAccountDao.saveAccount(createAccountWithAccountNumber(accNumber, client));
		assertEquals(expectedAccountCount, hibernateAccountDao.getAllAcounts().size());
	}
	
	private Account createAccountWithAccountNumber(long accNumber, Client client){
		Account account = new Account();
		account.setAccountNumber(accNumber);
		if(client!=null)account.setClient(client);
		return account;
	}
}
