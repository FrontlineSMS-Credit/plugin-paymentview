package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;

public interface NetworkOperatorDao {
	/**
	 * Returns the list of all network operators in the system
	 * @return
	 */
	public List<NetworkOperator> getAllNetworkOperators();
	
	/**
	 * Returns the list of network operators from a particular start index with a maximum number of returned
	 * network operators in the set
	 * @param startIndex Index of the first network operator to fetch
	 * @param limit Number of records to be fetched starting from the start index
	 * @return
	 */
	public List<NetworkOperator> getAllNetworkOperators(int startIndex, int limit);
	
	/**
	 * Retrieves a network operator with the specified name, returns <code>null</null> is none exists
	 * @param name Name of the network operator
	 * @return Network operator with the specified name or <code>null</code> if none exists
	 */
	public NetworkOperator getNetworkOperatorByName(String name);
	
	/**
	 * Gets the total number of network opreators in the system
	 * @return
	 */
	public int getNetworkOperatorCount();
	
	/**
	 * Save a network operator to the system
	 * @param operator The contact to save
	 * @throws DuplicateKeyException if the network operator's name is already in use by another network operator
	 */
	public void saveNetworkOperator(NetworkOperator operator) throws DuplicateKeyException;
	
	/**
	 * Updates the details of the network operator
	 * @param operator the network operator to be updated
	 * @throws DuplicateKeyException if the network operator's name is already in use by another network operator
	 */
	public void updateNetworkOperator(NetworkOperator operator) throws DuplicateKeyException;

	/**
	 * Deletes a network operator from the system
	 * @param operator the network operator to be deleted
	 */
	public void deleteNetworkOperator(NetworkOperator operator);
}
