package org.creditsms.plugins.paymentview.data.repository;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;

/**
 * @Author Roy
 */
public interface TargetServiceItemDao {
	
	public List<TargetServiceItem> getAllTargetServiceItemByTarget(long targetId);
	
	public List<TargetServiceItem> getAllTargetServiceItemByTargetFiltered(long targetId, int startIndex, int limit);
	
	/**
	 * Returns all the TargetServiceItems in the system
	 * 
	 * @return List<TargetServiceItem>
	 */
	public List<TargetServiceItem> getAllTargetServiceItems();

	/**
	 * deletes a TargetServiceItem to the system
	 * 
	 * @param targetServiceItem
	 */
	public void deleteTargetServiceItem(TargetServiceItem targetServiceItem);
	
	/**
	 * Saves a TargetServiceItem to the system
	 * 
	 * @param targetServiceItem
	 */
	public void saveTargetServiceItem(TargetServiceItem targetServiceItem) throws DuplicateKeyException;;
	
	/**
	 * Updates a TargetServiceItem to the system
	 * 
	 * @param targetServiceItem
	 */
	public void updateTargetServiceItem(TargetServiceItem targetServiceItem) throws DuplicateKeyException;;
	
	/**
	 * Returns a TargetServiceItem with the same id as the passed id
	 * 
	 * @param targetServiceItemId
	 * @return
	 */
	public TargetServiceItem getTargetServiceItemById(long id);
}
