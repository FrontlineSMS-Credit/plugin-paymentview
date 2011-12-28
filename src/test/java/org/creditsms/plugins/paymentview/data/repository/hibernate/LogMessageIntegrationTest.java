package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.LogMessage.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Kim
 */
public class LogMessageIntegrationTest extends HibernateTestCase {
	@Autowired
	HibernateLogMessageDao hibernateLogMessageDao;

	public void testSetup() {
		assertNotNull(hibernateLogMessageDao);
	}
	
	public void testSave() throws DuplicateKeyException {
		assertDatabaseEmpty();
		LogMessage lm = createLogMessage(LogLevel.INFO,"Create Incoming","BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 from\nJOHN KIU 254723908000\non 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance is Ksh1,236");
		
		hibernateLogMessageDao.saveLogMessage(lm);
		assertEquals(1, hibernateLogMessageDao.getLogMessageCount());
	}

	private LogMessage createLogMessage(LogLevel level,String logTitle,String logContent) {
		Calendar calendar = Calendar.getInstance();
		return new LogMessage(calendar.getTimeInMillis(),level,logTitle,logContent);
	}
	
	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateLogMessageDao.getLogMessageCount());
	}
}
