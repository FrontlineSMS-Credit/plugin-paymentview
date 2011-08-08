package org.creditsms.plugins.paymentview.importexport;

import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_ACCOUNT;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_AMOUNT_PAID;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PHONE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_TIME_PAID;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.PAYMENT_BY;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.importexport.PaymentViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

import net.frontlinesms.ui.i18n.ClasspathLanguageBundle;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.LanguageBundle;


import thinlet.Thinlet;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.junit.BaseTestCase;


public class ExportUnitTest extends BaseTestCase {
	protected static final String PHONENUMBER_0 = "+254723908000";
	protected static final String PHONENUMBER_1 = "+254723908001";
	protected static final String PHONENUMBER_2 = "+254723908002";
	protected static final String ACCOUNTNUMBER_1 = "00001";
	protected static final String ACCOUNTNUMBER_2 = "00002";
	protected static final String ACCOUNTNUMBER_3 = "00003";
	
	private Client CLIENT_0;
	private Client CLIENT_1;
	private Client CLIENT_2;
	
	private ClientDao clientDao;
	private AccountDao accountDao;
	private PaymentViewPluginController pluginController;
	private ArrayList<Client> clients;
	private File generatedFile;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpDaos();
		
		//Set Up Rules
		when(pluginController.getAccountDao()).thenReturn(accountDao);
		when(pluginController.getClientDao()).thenReturn(clientDao);
		
		//Set Up client, account
//		Set<Account> accounts1 = mockAccounts(ACCOUNTNUMBER_1);
//		Set<Account> accounts2 = mockAccounts(ACCOUNTNUMBER_2);
//		Set<Account> accounts3 = mockAccounts(ACCOUNTNUMBER_3);
		
//	    CLIENT_0 = mockClient(0, PHONENUMBER_0, accounts1, "Client_0_fn", "Client");
//	    CLIENT_1 = mockClient(1, PHONENUMBER_1, accounts2);
//	    CLIENT_2 = mockClient(2, PHONENUMBER_2, accounts3);
		
		CLIENT_0 = new Client("Client0_fn", "Client0_on", PHONENUMBER_0);
		CLIENT_1 = new Client("Client1_fn", "Client1_on", PHONENUMBER_1);
		CLIENT_2 = new Client("Client2_fn", "Client2_on", PHONENUMBER_2);
		
		clients = new ArrayList<Client>();
		clients.add(CLIENT_0);
		clients.add(CLIENT_1);
		clients.add(CLIENT_2);
		
	}
	
	@SuppressWarnings("unchecked")
	private void setUpDaos() {
		clientDao = mock(ClientDao.class);
		accountDao = mock(AccountDao.class);
		customFieldDao = mock(CustomFieldDao.class);
		customValueDao = mock(CustomValueDao.class);
		pluginController = mock(PaymentViewPluginController.class);
		
	}

//	private Client mockClient(long id, String phoneNumber, Set<Account> accounts, String fn, String on, String accountNumberList) {
//		Client client = mock(Client.class);
////		when(c.getId()).thenReturn(id);
////		when(clientDao.getClientByPhoneNumber(phoneNumber)).thenReturn(c);
////		when(accountDao.getAccountsByClientId(id)).thenReturn(new ArrayList<Account>(accounts));
////		when(accountDao.getActiveNonGenericAccountsByClientId(id)).thenReturn(new ArrayList<Account>(accounts));
////		when(c.getPhoneNumber()).thenReturn(phoneNumber);
//		
//		when(client.getFirstName()).thenReturn(fn);
//		when(client.getOtherName()).thenReturn(on);
//		when(client.getPhoneNumber()).thenReturn(phoneNumber);
//		when(PaymentViewUtils.accountsAsString(accountDao.getAccountsByClientId(client.getId()), "; ")).thenReturn(accountNumberList);
//	}
	
	private Set<Account> mockAccounts(String... accountNumbers) {
		ArrayList<Account> accounts = new ArrayList<Account>();
		for(String accountNumber : accountNumbers) {
			Account account = mock(Account.class);
			when(account.getAccountNumber()).thenReturn(accountNumber);
			accounts.add(account);
			when(accountDao.getAccountByAccountNumber(accountNumber)).thenReturn(account);
		}
		return new HashSet<Account>(accounts);
	}
	

	public void testExportClients()	throws IOException {
		// Make sure the English i18n bundle is available to provision the export column names
//		LanguageBundle englishBundle = ClasspathLanguageBundle.create("/org/creditsms/plugins/paymentview/PaymentViewPluginControllerText.properties");
//		Thinlet.DEFAULT_ENGLISH_BUNDLE = englishBundle.getProperties();
//		String outputFileName = this.getClass().getSimpleName() + ".clients.csv";
//		generatedFile = super.getOutputFile(outputFileName);
//		
//		
//		List<CustomField> emptyCustomFields = Collections.emptyList();
//		when(customFieldDao.getAllActiveUsedCustomFields()).thenReturn(emptyCustomFields);
//		
////		List<CustomValue> emptyCustomValues = Collections.emptyList();
////		when(customValueDao.getCustomValuesByClientId(any(Client.class).getId())).thenReturn(emptyCustomValues);
//		
//		PaymentViewCsvExporter.exportClients(generatedFile, clients, accountDao, customFieldDao, customValueDao, getClientExportRowFormat());
//		InputStream inputStream1 = this.getClass().getResourceAsStream(outputFileName);
//		InputStream inputstream2 =new FileInputStream(generatedFile); 
//		//assertEquals("Generated CSV file did not contain the expected values.", this.getClass().getResourceAsStream(outputFileName), new FileInputStream(generatedFile));
//		
	}
	
	private final CsvRowFormat getClientExportRowFormat() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		rowFormat.addMarker(PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME);
		rowFormat.addMarker(PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME);
		rowFormat.addMarker(PaymentViewCsvUtils.MARKER_CLIENT_PHONE);
		for(CustomField cf : customFieldDao.getAllActiveUsedCustomFields()){
			rowFormat.addMarker(PaymentViewUtils.getMarkerFromString(cf.getReadableName()));
		}
		return rowFormat;
	}
	
//	public static void exportIncomingPayment(File exportFile,
//	Collection<IncomingPayment> incomingPayments,
//	CsvRowFormat incomingPaymentFormat) throws IOException {
	public void testExportIncomingPayment() throws IOException {
		
	}
	
//	private final CsvRowFormat getIncomingExportRowFormat() {
//
////	PaymentViewCsvUtils.MARKER_PAYMENT_BY,
////	InternationalisationUtils.getI18nString(PAYMENT_BY),
////	PaymentViewCsvUtils.MARKER_PHONE_NUMBER,
////	InternationalisationUtils
////			.getI18nString(COMMON_PHONE),
////	PaymentViewCsvUtils.MARKER_AMOUNT_PAID,
////	InternationalisationUtils
////			.getI18nString(COMMON_AMOUNT_PAID),
////	PaymentViewCsvUtils.MARKER_TIME_PAID,
////	InternationalisationUtils
////			.getI18nString(COMMON_TIME_PAID),
////	PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT,
////	InternationalisationUtils
////			.getI18nString(COMMON_ACCOUNT));
//	}

}
