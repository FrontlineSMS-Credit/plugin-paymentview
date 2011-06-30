/**
 * 
 */
package net.frontlinesms.payment.safaricom;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public class MpesaStandardServiceTest extends MpesaPaymentServiceTest<MpesaPersonalService> {
	@Override
	protected MpesaPersonalService createNewTestClass() {
		return new MpesaPersonalService();
	}
	
	public void testIncomingPaymentProcessingWithNoAccount() {
		testIncomingPaymentProcessorIgnoresMessage("+254721656788", "BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 from\nJOHN KIU 254723908000\non 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236");
		testIncomingPaymentProcessorIgnoresMessage("+254701103438", "BL99BD339 Confirmed.\nYou have received Ksh50.00 from\n"+
				"ROY ONYANGO 254701103438\non 29/6/11 at 1:19 PM\nNew M-PESA balance is Ksh67,236");
	}
	
	public void testIncomingPaymentProcessing() {
		testIncomingPaymentProcessing("BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 30/5/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236",
				PHONENUMBER_1, ACCOUNTNUMBER_1_1, "1235", "BI94HR849",
				"JOHN KIU", "30/5/11 10:35 PM");
		
		testIncomingPaymentProcessing("BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 from\nyohan mwenyewe alibamba 254723908001\non 3/5/11 at 8:35 PM\n" +
				"New M-PESA balance is Ksh1,236",
				PHONENUMBER_1, ACCOUNTNUMBER_1_1, "1235", "BI94HR849",
				"yohan mwenyewe alibamba", "3/5/11 8:35 PM");
	}
	
	public void testOutgoingPaymentProcessing() {
		testOutgoingPaymentProcessing("BC77RI604 Confirmed.\n" +
				"Ksh1,235 sent to DACON OMONDI 254723908001 on 22/5/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236",
				PHONENUMBER_1, ACCOUNTNUMBER_1_1, "1235", "BC77RI604",
				"DACON OMONDI", "22/5/11 10:35 PM", OutgoingPayment.Status.CONFIRMED);
	}
	
	@Override
	String[] getValidMessagesText() {
		return new String[] {
				"Real text from a MPESA Standard confirmation",
				"BI94HR849 Confirmed.\n" +
						"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 3/5/11 at 10:35 PM\n" +
						"New M-PESA balance is Ksh1,236",
						
				"Real text from a MPESA Standard confirmation; Testing 20/5/11",
				"BI94HR849 Confirmed.\n" +
						"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 20/5/11 at 10:35 PM\n" +
						"New M-PESA balance is Ksh1,236",
						
				"Test in case someone has only one name",
				"BI94HR849 Confirmed.\n" +
						"You have received Ksh1,235 from\nKIU 254723908001\non 10/5/11 at 10:35 PM\n" +
						"New M-PESA balance is Ksh1,236",
						
				"Test in case confirmation codes are made longer",
				"BI94HR849XXX Confirmed.\n" +
						"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 30/5/11 at 10:35 PM\n" +
						"New M-PESA balance is Ksh1,236",
		};
	}
	
	@Override
	String[] getInvalidMessagesText() {
		return new String[] {
				"No newline after 'Confirmed.'",
				"BI94HR849 Confirmed." +
				"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236",
						
				"American Christmas",
				"BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 from\nJOHN KIU 254723908001\non 12/25/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236",
		};
	}
}
