package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.ResponseRecipient;
import org.creditsms.plugins.paymentview.data.repository.ResponseRecipientDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Roy
 * 
 */

public class HibernateResponseRecipientDao extends BaseHibernateDao<ResponseRecipient> implements
ResponseRecipientDao{

	protected HibernateResponseRecipientDao() {
		super(ResponseRecipient.class);
	}

	public ResponseRecipient getResponseRecipientByThirdPartyResponseId(
			long thirdPartyId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria thirdPartyResponseCriteria = criteria.createCriteria("thirdPartyResponse");
		thirdPartyResponseCriteria.add(Restrictions.eq("id", thirdPartyId));
		return super.getUnique(criteria);
	}

	public void deleteResponseRecipient(ResponseRecipient responseRecipient) {
		super.delete(responseRecipient);
	}

	public void updateResponseRecipient(ResponseRecipient responseRecipient) throws DuplicateKeyException {
		super.update(responseRecipient);
	}

	public void saveResponseRecipient(ResponseRecipient responseRecipient)
			throws DuplicateKeyException {
		super.save(responseRecipient);
	}
}
