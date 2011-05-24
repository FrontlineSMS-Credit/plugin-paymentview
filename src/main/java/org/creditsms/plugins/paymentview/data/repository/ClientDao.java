package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;

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

	/**
	 * Returns a list of clients whose name is similar to the specified string
	 * 
	 * @param clientname
	 *            string to be used to match the names
	 * @return
	 */
	public List<Client> getClientsByName(String clientName);

	/**
	 * Returns a list of clients whose name is similar to the specified string
	 * from a particular start index with a maximum number of returned clients
	 * set
	 * 
	 * @param clientname
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	public List<Client> getClientsByName(String clientName, int startIndex,
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
