package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.Order;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;

public interface ClientDao {
	/**
	 * Deletes a client from the system
	 * 
	 * @param client
	 */
	public void deleteClient(Client client);

	/**
	 * return all the clients in the system
	 * **/
	public List<Client> getAllClients();

	/**
	 * return all the clients in the system sorted
	 * **/
	public List<Client> getAllActiveClientsSorted(int startIndex, int limit,
			Field sortBy, Order order);

	public List<Client> getAllClientsSorted(Field sortBy, Order order);

	public List<Client> getAllActiveClientsSorted(Field sortBy, Order order);

	/**
	 * Returns all clients from a particular start index with a maximum number
	 * of returned clients set.
	 * 
	 * @param startIndex
	 *            index of the first client to fetch
	 * @param limit
	 *            Maximum number of clients to fetch from the start index
	 * @return
	 */
	public List<Client> getAllClients(int startIndex, int limit);

	/**
	 * Returns all active clients from a particular start index with a maximum
	 * number of returned clients set.
	 * 
	 * @return
	 */
	public List<Client> getAllActiveClients();

	/**
	 * Returns all active clients from a particular start index with a maximum
	 * number of returned clients set.
	 * 
	 * @param startIndex
	 *            index of the first client to fetch
	 * @param limit
	 *            Maximum number of clients to fetch from the start index
	 * @return
	 */
	public List<Client> getAllActiveClients(int startIndex, int limit);

	/**
	 * Returns a client with the same id as the passed id
	 * 
	 * @param clientId
	 * @return
	 */
	public Client getClientById(long clientId);

	/**
	 * Retrieves the client with
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public Client getClientByPhoneNumber(String phoneNumber);

	/** @return number of clients in the system */
	public int getClientCount();

	public List<Client> getAllClientsByNameFilter(String filter);

	public List<Client> getAllClientsByFilter(String filter, int startIndex,
			int limit);

	/**
	 * Returns a list of clients whose name,phone number or custom value are
	 * similar to the specified string
	 * 
	 * @param filter
	 * @return
	 */
	public List<Client> getClientsByFilter(String filter);

	public List<Client> getClientsByNameFilter(String filter);

	/**
	 * Returns a list of clients whose name,phone number or custom value are
	 * similar to the specified string from a particular start index with a
	 * maximum number of returned clients
	 * 
	 * @param filter
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	public List<Client> getClientsByFilter(String filter, int startIndex,
			int limit);

	/**
	 * Saves a client to the system
	 * 
	 * @param client
	 */
	public void saveClient(Client client);

	/**
	 * Update a client to the system
	 * 
	 * @param client
	 */
	public void updateClient(Client client);

}
