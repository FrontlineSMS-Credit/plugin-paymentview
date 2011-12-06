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
	 * return all the Target in the system
	 * **/
	public List<Target> getAllActiveTargets();
	
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
	public List<Target> getTargetsByAccount(String accountNumber);

	/**
	 * Returns a Targets with the same status as the passed
	 * 
	 * @param status
	 * @return
	 */
	public List<Target> getTargetsByStatus(String status);
	
	/**
	 * Returns an active Target with Account id
	 * 
	 * @param serviceItemId
	 * @return
	 */
	public Target getActiveTargetByAccount(String string);
	
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