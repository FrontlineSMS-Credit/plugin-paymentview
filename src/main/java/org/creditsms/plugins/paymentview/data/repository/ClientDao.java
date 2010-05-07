package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Client;

public interface ClientDao {
	/**@return all clients in the system */
	public List<Client> getAllClients();
	
	/**
	 * Returns all clients from a particular start index with a maximum number of returned clients set.
	 * @param startIndex index of the first client to fetch
	 * @param limit Maximum number of clients to fetch from the start index
	 * @return
	 */
	public List<Client> getAllClients(int startIndex, int limit);
	
	/**
	 * Returns a list of clients whose name is similar to the specified string
	 * @param name string to be used to match the names
	 * @return
	 */
	public List<Client> filterClientsByName(String name);
	
	/**
	 * Returns a list of clients whose name is similar to the specified string from a particular
	 * start index with a maximum number of returned clients set
	 * @param name 
	 * @param startIndex 
	 * @param limit
	 * @return
	 */
	public List<Client> filterClientsByName(String name, int startIndex, int limit);
	
	/**
	 * Retrieves the client with a specific name
	 * @param name Name of the client to be retrieved
	 * @return
	 */
	public Client getClientByName(String name);
	
	/**
	 * Retrieves the client with 
	 * @param phoneNumber
	 * @return
	 */
	public Client getClientByPhoneNumber(String phoneNumber);
	
	/** @return number of clients in the datasource */
	public int getClientCount();
	
	/**
	 * Gets the number of clients whose name includes part of the specified string
	 * @param name
	 * @return 
	 */
	public int getFilteredClientCount(String name);
	/**
	 * Saves a client to the system
	 * @param client
	 * @throws DuplicateKeyException
	 */
	public void saveClient(Client client) throws DuplicateKeyException;
	
	
	/**
	 * Updates the details of an existing client
	 * @param client {@link Client} whose details are to be updated
	 * @throws DuplicateKeyException if the details of the new client conflict with those of an existing client
	 */
	public void updateClient(Client client) throws DuplicateKeyException;
	
	/**
	 * Deletes a client from the system
	 * @param client
	 * @throws DuplicateKeyException
	 */
	public void deleteClient(Client client) throws DuplicateKeyException;
}
