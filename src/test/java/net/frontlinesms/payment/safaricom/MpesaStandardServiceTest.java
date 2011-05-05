/**
 * 
 */
package net.frontlinesms.payment.safaricom;

public class MpesaStandardServiceTest extends MpesaPaymentServiceTest<MpesaStandardService> {
	@Override
	protected MpesaStandardService createNewTestClass() {
		return new MpesaStandardService();
	}
	
	public void testIncomingPaymentProcessingWithNoAccount() {
		testIncomingPaymentProcessorIgnoresMessage("+254721656788", "BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 JOHN KIU 254723908000 on 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance Ksh1,236");
	}
	
	public void testIncomingPaymentProcessing() {
		
		testIncomingPaymentProcessing("BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 JOHN KIU 254723908001 on 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance Ksh1,236",
				PHONENUMBER_1, ACCOUNTNUMBER_1_1, "1235", "BI94HR849",
				"JOHN KIU", 1304451300000L);
	}
}
