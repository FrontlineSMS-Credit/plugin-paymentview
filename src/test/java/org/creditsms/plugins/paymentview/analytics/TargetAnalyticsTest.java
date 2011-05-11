package org.creditsms.plugins.paymentview.analytics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

import net.frontlinesms.junit.BaseTestCase;

public class TargetAnalyticsTest extends BaseTestCase {
	private TargetAnalytics targetAnalytics = new TargetAnalytics();
	
	public void testCalculatePercentageToGo() {
		testPercentageToGo("9000", "12000", "75.00");
		testPercentageToGo("75", "100", "75.00");
		testPercentageToGo("145000", "200000", "72.500");
	}
	
	private void testPercentageToGo(String paid, String target, String expectedPercentage) { 
		BigDecimal totalTargetCost = new BigDecimal(target);
		BigDecimal amountPaid = new BigDecimal(paid);
		BigDecimal actual = this.targetAnalytics.calculatePercentageToGo(totalTargetCost, amountPaid);
		assertEquals(new BigDecimal(expectedPercentage), actual);
	}
	
	public void testCalculateAmount() {
		testAmount("9000", "4500", "2500", "2000");
		testAmount("620000", "600000", "2500", "2000", "5000", "10500");
	}
	
	private void testAmount(String expectedTotal, String... installments) {
		List<IncomingPayment> incomingPayments = new ArrayList<IncomingPayment>();
		for(String installmentValue : installments) {
			IncomingPayment payment = new IncomingPayment();
			payment.setAmountPaid(new BigDecimal(installmentValue));
			incomingPayments.add(payment);
		}
		
		BigDecimal calculatedTotal = targetAnalytics.calculateAmount(incomingPayments );
		assertEquals(new BigDecimal(expectedTotal), calculatedTotal );
	}
}
