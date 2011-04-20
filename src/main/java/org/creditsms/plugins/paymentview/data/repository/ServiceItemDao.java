package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.ServiceItem;

public interface ServiceItemDao {
	/**
	 * return all the ServiceItem in the system
	 * **/
	public List<ServiceItem> getAllServiceItem();
	
	/**
	 * Returns a ServiceItem with the same id as the passed id
	 * @param ServiceItemId
	 * @return
	 */
	public ServiceItem getServiceItemById(long serviceItemId);
	
	/**
	 * Returns all ServiceItem from a particular start index with a maximum number of returned ServiceItem set.
	 * @param startIndex index of the first ServiceItem to fetch
	 * @param limit Maximum number of ServiceItem to fetch from the start index
	 * @return
	 */
	public List<ServiceItem> getAllServiceItem(int startIndex, int limit);
	
	/**
	 * Returns a list of ServiceItem whose name is similar to the specified string
	 * @param ServiceItemname string to be used to match the names
	 * @return
	 */
	public List<ServiceItem> getServiceItemByName(String serviceItemName);
	
	/**
	 * Returns a list of ServiceItem whose name is similar to the specified string from a particular
	 * start index with a maximum number of returned ServiceItem set
	 * @param ServiceItemname 
	 * @param startIndex 
	 * @param limit
	 * @return
	 */
	public List<ServiceItem> getServiceItemByName(String serviceItemName, int startIndex, int limit);
	
	/** @return number of ServiceItem in the system */
	public int getServiceItemCount();
	
	/**
	 * Deletes a ServiceItem from the system
	 * @param serviceItem
	 */
	public void deleteServiceItem(ServiceItem serviceItem);

	/**
	 * Saves a serviceItem to the system
	 * @param serviceItem
	 */
	public void saveServiceItem(ServiceItem serviceItem) throws DuplicateKeyException;

	/**
	 * Update a ServiceItem to the system
	 * @param serviceItem
	 */
	public void updateServiceItem(ServiceItem serviceItem) throws DuplicateKeyException;
}
