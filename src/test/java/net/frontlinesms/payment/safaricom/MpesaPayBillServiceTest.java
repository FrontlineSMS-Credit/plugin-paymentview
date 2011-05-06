package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MpesaPayBillServiceTest extends
		MpesaPaymentServiceTest<MpesaPayBillService> {
	
	@Override
	protected MpesaPayBillService createNewTestClass() {
		return new MpesaPayBillService();
	}

	public void testIncomingPayBillProcessing() throws Exception {
		
		testIncomingPaymentProcessing("BH45UU225 Confirmed.\n"
				+ "on 5/4/11 at 2:45 PM\n"
				+ "Ksh950 received from BORIS BECKER 254723908002.\n"
				+ "Account Number 0700000021\n"
				+ "New Utility balance is Ksh50,802\n"
				+ "Time: 05/04/2011 14:45:34",
				PHONENUMBER_2, ACCOUNTNUMBER_2_1, "950", "BH45UU225",
				"BORIS BECKER", "14:45 5 Apr 2011");
		
		// Check the payment time is processed rather than the balance time
		testIncomingPaymentProcessing("BHT57U225 Confirmed.\n"
				+ "on 5/4/11 at 1:45 PM\n"
				+ "Ksh123 received from ELLY ASAKHULU 254723908002.\n"
				+ "Account Number 0700000022\n"
				+ "New Utility balance is Ksh50,802\n"
				+ "Time: 05/04/2011 16:45:34",
				PHONENUMBER_2, ACCOUNTNUMBER_2_2, "123", "BHT57U225",
				"ELLY ASAKHULU", "13:45 5 Apr 2011");
	}

	@Override
	String[] getValidMessagesText() {
		return new String[] {
				"BH45UU225 Confirmed.\n"
						+ "on 5/4/11 at 2:45 PM\n"
						+ "Ksh950 received from BORIS BECKER 254723908002.\n"
						+ "Account Number 0700000021\n"
						+ "New Utility balance is Ksh50,802\n"
						+ "Time: 05/04/2011 14:45:34",
				"BHT57U225 Confirmed.\n"
						+ "on 5/4/11 at 1:45 PM\n"
						+ "Ksh123 received from ELLY ASAKHULU 254723908002.\n"
						+ "Account Number 0700000022\n"
						+ "New Utility balance is Ksh50,802\n"
						+ "Time: 05/04/2011 16:45:34",
		};
	}
	
	@Override
	String[] getInvalidMessagesText() {
		return new String[] {
				"BH45UU225 Confirmed." // No newline here!
						+ "on 5/4/11 at 2:45 PM\n"
						+ "Ksh950 received from BORIS BECKER 254723908002.\n"
						+ "Account Number 0700000021\n"
						+ "New Utility balance is Ksh50,802\n"
						+ "Time: 05/04/2011 14:45:34",
				"BHT57U225 Confirmed.\n"
						+ "on 5/4/11 at 1:45 PM\n"
						+ "Ksh123 received from ELLY ASAKHULU 254723908002.\n"
						+ "Account Number 0700000022\n"
						+ "New Utility balance is Ksh50,802\n"
						+ "Time: 12/25/2011 16:45:34", // American Christmas
		};
	}
}
