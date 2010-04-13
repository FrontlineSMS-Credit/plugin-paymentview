package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateNetworkOperatorDao extends BaseHibernateDao<NetworkOperator> implements NetworkOperatorDao {

	public HibernateNetworkOperatorDao(){
		super(NetworkOperator.class);
	}
	
	/** @see  NetworkOperatorDao#deleteNetworkOperator(NetworkOperator) */
	public void deleteNetworkOperator(NetworkOperator operator) {
		super.delete(operator);
	}

	/** @see NetworkOperatorDao#getAllNetworkOperators() */
	public List<NetworkOperator> getAllNetworkOperators() {
		return super.getAll();
	}

	/** @see NetworkOperatorDao#getAllNetworkOperators(int, int) */
	public List<NetworkOperator> getAllNetworkOperators(int startIndex,	int limit) {
		return super.getAll(startIndex, limit);
	}

	/** @see NetworkOperatorDao#getNetworkOperatorByName(String) */
	public NetworkOperator getNetworkOperatorByName(String name) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(NetworkOperator.Field.NAME.getFieldName(), name));
		return super.getUnique(criteria);
	}

	/** @see NetworkOperatorDao#getNetworkOperatorCount() */
	public int getNetworkOperatorCount() {
		return super.countAll();
	}

	/** @see NetworkOperatorDao#saveNetworkOperator(NetworkOperator) */
	public void saveNetworkOperator(NetworkOperator operator) throws DuplicateKeyException {
		super.save(operator);
	}

	/** @see NetworkOperatorDao#updateNetworkOperator(NetworkOperator) */
	public void updateNetworkOperator(NetworkOperator operator) throws DuplicateKeyException {
		super.update(operator);
	}

}
