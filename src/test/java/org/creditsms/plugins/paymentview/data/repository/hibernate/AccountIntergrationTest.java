package org.creditsms.plugins.paymentview.data.repository.hibernate;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;


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
	
	public void testSave() {
		asserEmptyAccount();
		Account a = new Account();
		hibernateAccountDao.saveUpdateAccount(a);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testAccountNumberUnique(){
		asserEmptyAccount();
		Account ac1 = setAccountNumber(101);
		Account ac2 = setAccountNumber(101);
		
		try{
			hibernateAccountDao.saveUpdateAccount(ac1);
			hibernateAccountDao.saveUpdateAccount(ac2);
			fail("you cannot add more than one account with same accounts number!!");
		}catch(RuntimeException ex){
			
		}
	}
	
	public void testDeleteAccount(){
		asserEmptyAccount();
		Account a = new Account();
		hibernateAccountDao.saveUpdateAccount(a);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		
		hibernateAccountDao.deleteAccount(a);
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
	
	public void testGetAccountById(){
		asserEmptyAccount();
		Account ac1 = setAccountNumber(103);
		Account ac2 = setAccountNumber(104);
		
		hibernateAccountDao.saveUpdateAccount(ac1);
		hibernateAccountDao.saveUpdateAccount(ac2);
		
		assertEquals(104, hibernateAccountDao.getAllAcounts().get(1).getAccountNumber());
	}
	
	public void testGetAccountsByUserSingle(){

		Client c = createClientWithPhoneNumber("+2540720111111");
		hibernateClientDao.saveUpdateClient(c);

		Account account = createAccountWithAccountNumber(21);
		account.setClient(c);
		hibernateAccountDao.saveUpdateAccount(account);

		long searchbyClientId = hibernateClientDao.getAllClients().get(0).getId();

		assertEquals(1, hibernateClientDao.getAllClients().size());
		assertEquals(21, hibernateAccountDao.getAccountsByClientId(searchbyClientId).get(0).getAccountNumber());
	}
	
	public void testGetAccountsByUserIdMultiple() {
		Client c = createClientWithPhoneNumber("+2540720111111");
		Client c1 = createClientWithPhoneNumber("+2540720000002");

		hibernateClientDao.saveUpdateClient(c);
		hibernateClientDao.saveUpdateClient(c1);

		Account account = createAccountWithAccountNumber(12);
		account.setClient(c);
		
		Account account1 = createAccountWithAccountNumber(13);
		account1.setClient(c);

		hibernateAccountDao.saveUpdateAccount(account);
		hibernateAccountDao.saveUpdateAccount(account1);
		
		Account account2 = createAccountWithAccountNumber(14);
		account2.setClient(c1);
		
		Account account3 = createAccountWithAccountNumber(15);
		account3.setClient(c1);
		
		hibernateAccountDao.saveUpdateAccount(account2);
		hibernateAccountDao.saveUpdateAccount(account3);
		
		long searchbyClientId = hibernateClientDao.getAllClients().get(1).getId();

		assertEquals(2, hibernateClientDao.getAllClients().size());
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
	
	private Client createClientWithPhoneNumber(String phNoStr){
		Client c = new Client();
		c.setPhoneNumber(phNoStr);
		
		return c;
	}
	
	private Account createAccountWithAccountNumber(long accNumber){
		Account account = new Account();
		account.setAccountNumber(accNumber);
		
		return account;
	}
	
}
