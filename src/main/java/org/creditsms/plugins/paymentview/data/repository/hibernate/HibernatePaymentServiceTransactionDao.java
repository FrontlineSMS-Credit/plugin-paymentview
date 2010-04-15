package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceTransaction;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceTransactionDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernatePaymentServiceTransactionDao extends BaseHibernateDao<PaymentServiceTransaction> implements PaymentServiceTransactionDao {

	public HibernatePaymentServiceTransactionDao(){
		super(PaymentServiceTransaction.class);
	}
	
	public void deletePaymentServiceTransaction(PaymentServiceTransaction transaction) {
		super.delete(transaction);
	}

	/** @see PaymentServiceTransactionDao#getAllTransactions() */
	public List<PaymentServiceTransaction> getAllTransactions() {
		return super.getAll();
	}

	/** @see PaymentServiceTransactionDao#getAllTransactions(int, int) */
	public List<PaymentServiceTransaction> getAllTransactions(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	/** @see PaymentServiceTransactionDao#getTransactionCount() */
	public int getTransactionCount() {
		return super.countAll();
	}

	/** @see PaymentServiceTransactionDao#getTransactionsByClient(Client) */
	public List<PaymentServiceTransaction> getTransactionsByClient(Client client) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(PaymentServiceTransaction.Field.CLIENT_ID.getFieldName(), new Long(client.getId())));
		return super.getList(criteria);
	}

	/** @see PaymentServiceTransactionDao#getTransactionsByClient(Client, int) */
	public List<PaymentServiceTransaction> getTransactionsByClient(Client client, int transactionType) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(PaymentServiceTransaction.Field.CLIENT_ID.getFieldName(), new Long(client.getId())));
		criteria.add(Restrictions.eq(PaymentServiceTransaction.Field.TRANSACTION_TYPE.getFieldName(), transactionType));
		return super.getList(criteria);
	}

	/** @see PaymentServiceTransactionDao#getTransactionsByNetworkOperator(NetworkOperator) */
	public List<PaymentServiceTransaction> getTransactionsByNetworkOperator(NetworkOperator operator) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(NetworkOperator.Field.ID.getFieldName(), new Long(operator.getId())));
		return super.getList(criteria);
	}

	/** @see PaymentServiceTransactionDao#getTransactionsByType(int) */
	public List<PaymentServiceTransaction> getTransactionsByType(int transactionType) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(PaymentServiceTransaction.Field.TRANSACTION_TYPE.getFieldName(), new Integer(transactionType)));
		return super.getList(criteria);
	}

	/** @see PaymentServiceTransactionDao#savePaymentServiceTransaction(PaymentServiceTransaction) */
	public void savePaymentServiceTransaction(PaymentServiceTransaction transaction) throws DuplicateKeyException {
		super.save(transaction);
	}

	/** @see PaymentServiceTransactionDao#updatePaymentServiceTransaction(PaymentServiceTransaction) */
	public void updatePaymentServiceTransaction(PaymentServiceTransaction transaction) throws DuplicateKeyException {
		super.update(transaction);
	}

}
