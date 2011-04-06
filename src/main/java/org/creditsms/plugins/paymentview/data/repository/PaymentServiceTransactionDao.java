package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;


import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.FrontlineMessage;

import org.creditsms.plugins.paymentview.data.domain.ClientNew;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceTransaction;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceTransaction.TransactionType;

public interface PaymentServiceTransactionDao {
	/**
	 * Gets all the payment service transactions in the system
	 * @return
	 */
	public List<PaymentServiceTransaction> getAllTransactions();
	
	/**
	 * Gets all transactions from a particular index up to a specified maximum in the result set
	 * @param startIndex index of the first record to fetch
	 * @param limit number of records to fetch
	 * @return
	 */
	public List<PaymentServiceTransaction> getAllTransactions(int startIndex, int limit);
	
	/**
	 * Gets all the transactions for a particular client
	 * @param client client whose transactions are to be fetched
	 * @return
	 */
	public List<PaymentServiceTransaction> getTransactionsByClient(ClientNew client);
	
	/**
	 * Gets all the client transactions of a particular type
	 * @param client client whose transactions are to fetched
	 * @param transactionType type of transactions to be fetched
	 * @return
	 */
	public List <PaymentServiceTransaction> getTransactionsByClient(ClientNew client, TransactionType transactionType);
	
	/**
	 * Gets all the transactions for a specific network operator. This applies where a payment service works
	 * across multiple networks
	 * @param operator operator whose transactions are to be fetched
	 * @return
	 */
	public List<PaymentServiceTransaction> getTransactionsByNetworkOperator(NetworkOperator operator);
	
	/**
	 * Returns the list of transactions of a particular type
	 * @param transactionType type of the transactions to be fetched
	 * @return
	 */
	public List<PaymentServiceTransaction> getTransactionsByType(TransactionType transactionType);
	
	/**
	 * Returns a list of all the messages that were sent from a registered payment service
	 * and transaction records are yet to be created from them
	 * @return
	 */
	public List<FrontlineMessage> getPendingTransactions();
	
	/**
	 * Gets the total number of transactions in the system
	 * @return
	 */
	public int getTransactionCount();
	
	/**
	 * Saves a new transaction in the system
	 * @param transaction transaction to be saved
	 * @throws DuplicateKeyException if a transaction with the specified hash code already exists
	 */
	public void savePaymentServiceTransaction(PaymentServiceTransaction transaction) throws DuplicateKeyException;
	
	/**
	 * Updates the details of a transaction
	 * @param transaction {@link PaymentServiceTransaction} to be updated
	 * @throws DuplicateKeyException if a transaction with the specified has code already exists
	 */
	public void updatePaymentServiceTransaction(PaymentServiceTransaction transaction) throws DuplicateKeyException;

	/**
	 * Deletes a transaction from the system
	 * @param transaction {@link PaymentServiceTransaction} to be deleted
	 */
	public void deletePaymentServiceTransaction(PaymentServiceTransaction transaction);
}
