package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
import org.creditsms.plugins.paymentview.data.domain.ClientNew;

public interface NewClientDAO {
	/**
	 * return all the clients in the system
	 * **/
	public List<ClientNew> getAllClient();
	
	/**
	 * Returns all clients from a particular start index with a maximum number of returned clients set.
	 * @param startIndex index of the first client to fetch
	 * @param limit Maximum number of clients to fetch from the start index
	 * @return
	 */
	public List<ClientNew> getAllClient(int startIndex, int limit);
	
	/**
	 * Returns a list of clients whose name is similar to the specified string
	 * @param name string to be used to match the names
	 * @return
	 */
	public List<ClientNew> getClientByName(String clientName);
	
	/**
	 * Returns a list of clients whose name is similar to the specified string from a particular
	 * start index with a maximum number of returned clients set
	 * @param name 
	 * @param startIndex 
	 * @param limit
	 * @return
	 */
	public List<ClientNew> getClientByName(String clientname, int startIndex, int limit);
	
	/**
	 * Retrieves the client with 
	 * @param phoneNumber
	 * @return
	 */
	public ClientNew getClientByPhoneNumber(long phoneNumber);
	
	/** @return number of clients in the system */
	public int getClientCount();
	
	/**
	 * Deletes a client from the system
	 * @param client
	 */
	public void deleteClient(ClientNew client);

	/**
	 * Saves a client to the system
	 * @param client
	 * @throws DuplicateKeyException
	 */
	public void saveUpdateClient(ClientNew client);
	
}
