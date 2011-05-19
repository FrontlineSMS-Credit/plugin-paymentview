package org.creditsms.plugins.paymentview.data.repository;

import java.sql.Time;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Target;

public interface TargetDao {
	/**
	 * Deletes a Target from the system
	 * 
	 * @param target
	 */
	public void deleteTarget(Target target);

	/**
	 * return all the Target in the system
	 * **/
	public List<Target> getAllTargets();

	/**
	 * Returns all Target from a particular start index with a maximum number of
	 * returned Target set.
	 * 
	 * @param startIndex
	 *            index of the first target to fetch
	 * @param limit
	 *            Maximum number of target to fetch from the start index
	 * @return
	 */
	public List<Target> getAllTargets(int startIndex, int limit);

	/**
	 * Returns a Target with the same Account id as the passed id
	 * 
	 * @param targetId
	 * @return
	 */
	public Target getTargetByAccount(String accountNumber);

	/**
	 * Returns a Target with ServiceItem between a given date
	 * 
	 * @param serviceItemId
	 * @return
	 */
	public Target getTargetByEndDateBetweenDates(long serviceItemId,
			Time startDate, Time endDate);

	/**
	 * Returns a Target with the same id as the passed id
	 * 
	 * @param targetId
	 * @return
	 */
	public Target getTargetById(long targetId);

	/**
	 * Returns a list of Target whose name is similar to the specified string
	 * 
	 * @param target
	 *            string to be used to match the names
	 * @return
	 */
	public List<Target> getTargetsByName(String targetName);

	/**
	 * Returns a list of Target whose name is similar to the specified string
	 * from a particular start index with a maximum number of returned Target
	 * set
	 * 
	 * @param Targetname
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	public List<Target> getTargetsByName(String targetName, int startIndex,
			int limit);

	/**
	 * Returns a list of Target of specified ServiceItem
	 * 
	 * @param target
	 *            string to be used to match the names
	 * @return
	 */
	public List<Target> getTargetsByServiceItem(long serviceItemId);

	/** @return number of Target in the system */
	public int getTargetCount();

	/**
	 * Saves a Target to the system
	 * 
	 * @param target
	 */
	public void saveTarget(Target target);

	/**
	 * Update a Target to the system
	 * 
	 * @param target
	 */
	public void updateTarget(Target target) throws DuplicateKeyException;
}
