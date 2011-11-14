package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.LogMessage;

public interface LogMessageDao {

	/**
	 * return all the log messages in the system
	 * **/
	public List<LogMessage> getAllLogMessage();

	/**
	 * Returns all log messages from a particular start index with a maximum number
	 * of returned clients set.
	 * 
	 * @param startIndex
	 *            index of the first log message to fetch
	 * @param limit
	 *            Maximum number of log messages to fetch from the start index
	 * @return
	 */
	public List<LogMessage> getAllLogMessage(int startIndex, int limit);
	

	/** @return number of clients in the system */
	public int getLogMessageCount();

	/**
	 * Saves a log message to the system
	 * 
	 * @param logMessage
	 */
	public void saveLogMessage(LogMessage logMessage);
	
	public void info(String title, String content);
	public void info(String title, Throwable t);	
	public void warn(String title, String content);
	public void warn(String title, Throwable t);
	public void error(String title, String content);
	public void error(String title, Throwable t);
}
