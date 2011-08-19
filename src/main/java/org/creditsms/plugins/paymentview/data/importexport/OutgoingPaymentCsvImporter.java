package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.paymentsettings.phone.no.pattern.PhoneNumberPattern;

/**
 * @author Ian Onesmus Mukewa <ian@frontlinesms.com>, Roy
 */
public class OutgoingPaymentCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";
		
	List<OutgoingPayment> importedPaymentsLst;
	public int incorrectCount;

	// > INSTANCE PROPERTIES

	// > CONSTRUCTORS
	public OutgoingPaymentCsvImporter(File importFile) throws CsvParseException {
		super(importFile);
	}

	// > IMPORT METHODS
	/**
	 * Import Payments from a CSV file.
	 * 
	 * @param importFile
	 *            the file to import from
	 * @param contactDao
	 *            ; paymentDao
	 * @param rowFormat
	 * @throws IOException
	 *             If there was a problem accessing the file
	 * @throws CsvParseException
	 *             If there was a problem with the format of the file
	 */
	/**
	 * @param accountDao
	 * @param incomingPaymentDao
	 * @param rowFormat
	 */
	public CsvImportReport importOutgoingPayments(
			OutgoingPaymentDao outgoingPaymentDao, AccountDao accountDao, ClientDao clientDao,
			CsvRowFormat rowFormat) {
		log.trace("ENTER");
		
		List<OutgoingPayment> importedPayments = new ArrayList<OutgoingPayment>();
		PhoneNumberPattern phonePattern = new PhoneNumberPattern();
		incorrectCount = 0;
		for (String[] lineValues : this.getRawValues()) {
			String phoneNumber = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_PHONE_NUMBER);
			phoneNumber = phoneNumber.trim();
			String amountPaid = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_AMOUNT_PAID);
			String paymentId = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT);
			String notes = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_NOTES);
            
			if(phonePattern.formatPhoneNumber(phoneNumber)) {
				OutgoingPayment outgoingPayment = new OutgoingPayment();
				Client outgoingPaymentClient = new Client();
				try{
					BigDecimal amntToPay = new BigDecimal(amountPaid);
					outgoingPayment.setAmountPaid(amntToPay.setScale(0, RoundingMode.HALF_UP));
					outgoingPaymentClient.setPhoneNumber(phonePattern.getNewPhoneNumberPattern());
					outgoingPayment.setClient(outgoingPaymentClient);
					outgoingPayment.setPaymentId(paymentId);
					outgoingPayment.setNotes(notes);
					
					importedPayments.add(outgoingPayment);
				} catch (NumberFormatException ex) {
					incorrectCount++;
				}
			} else {
				incorrectCount++;
			}
		}
		setImportedPaymentsLst(importedPayments);

		log.trace("EXIT");

		return new CsvImportReport();
	}

	public List<OutgoingPayment> getImportedPaymentsLst() {
		return importedPaymentsLst;
	}

	public void setImportedPaymentsLst(List<OutgoingPayment> importedPaymentsLst) {
		this.importedPaymentsLst = importedPaymentsLst;
	}
}
