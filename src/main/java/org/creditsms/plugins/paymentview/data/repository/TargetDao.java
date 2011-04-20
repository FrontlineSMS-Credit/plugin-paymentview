package org.creditsms.plugins.paymentview.data.repository;

import java.sql.Time;
import java.util.List;
import net.frontlinesms.data.DuplicateKeyException;
import org.creditsms.plugins.paymentview.data.domain.Target;

public interface TargetDao {
	/**
	 * return all the Target in the system
	 * **/
	public List<Target> getAllTarget();
	
	/**
	 * Returns a Target with the same id as the passed id
	 * @param targetId
	 * @return
	 */
	public Target getTargetById(long targetId);
	
	/**
	 * Returns a Target with the same Account id as the passed id
	 * @param targetId
	 * @return
	 */
	public Target getTargetByAccount(long accountId);
	
	/**
	 * Returns a Target with TargetItem between a given date 
	 * @param targetItemId
	 * @return
	 */
	public Target getTargetByEndDateBetweenDates(long targetItemId, Time startDate, Time endDate);
	
	/**
	 * Returns all Target from a particular start index with a maximum number of returned Target set.
	 * @param startIndex index of the first target to fetch
	 * @param limit Maximum number of target to fetch from the start index
	 * @return
	 */
	public List<Target> getAllTargets(int startIndex, int limit);
	
	/**
	 * Returns a list of Target whose name is similar to the specified string
	 * @param target string to be used to match the names
	 * @return
	 */
	public List<Target> getTargetByName(String targetName);
	
	/**
	 * Returns a list of Target of specified targetItem
	 * @param target string to be used to match the names
	 * @return
	 */
	public List<Target> getTargetByTargetItem(long targetItemId);
	
	/**
	 * Returns a list of Target whose name is similar to the specified string from a particular
	 * start index with a maximum number of returned Target set
	 * @param Targetname 
	 * @param startIndex 
	 * @param limit
	 * @return
	 */
	public List<Target> getTargetByName(String targetName, int startIndex, int limit);
	
	/** @return number of Target in the system */
	public int getTargetCount();
	
	/**
	 * Deletes a Target from the system
	 * @param target
	 */
	public void deleteTarget(Target target);

	/**
	 * Saves a Target to the system
	 * @param target
	 */
	public void saveTarget(Target target) throws DuplicateKeyException;

	/**
	 * Update a Target to the system
	 * @param target
	 */
	public void updateTarget(Target target) throws DuplicateKeyException;
}
