package net.frontlinesms.payment.safaricom;

public class MpesaPayBillServiceTest extends
		MpesaPaymentServiceTest<MpesaPayBillService> {
	
	@Override
	protected MpesaPayBillService createNewTestClass() {
		return new MpesaPayBillService();
	}

	public void testIncomingPayBillProcessing() {
		
		testIncomingPaymentProcessing("BH45UU225 Confirmed.\n"
				+ "on 5/4/11 at 2:45 PM\n"
				+ "Ksh950 received from BORIS BECKER 254723908002.\n"
				+ "Account Number 0700000021\n"
				+ "New Utility balance is Ksh50,802\n"
				+ "Time: 05/04/2011 14:45:34", PHONENUMBER_2,
				ACCOUNTNUMBER_2_1, "950", "BH45UU225", "BORIS BECKER", 1302003900000L);
		
		testIncomingPaymentProcessing("BHT57U225 Confirmed.\n"
				+ "on 5/4/11 at 2:45 PM\n"
				+ "Ksh123 received from ELLY ASAKHULU 254723908002.\n"
				+ "Account Number 0700000022\n"
				+ "New Utility balance is Ksh50,802\n"
				+ "Time: 05/04/2011 14:45:34", PHONENUMBER_2,
				ACCOUNTNUMBER_2_2, "123", "BHT57U225", "ELLY ASAKHULU", 1302003900000L);
	}
}
