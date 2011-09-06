package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;
import org.creditsms.plugins.paymentview.data.repository.ThirdPartyResponseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Roy
 * 
 */

public class HibernateThirdPartyResponseDao extends BaseHibernateDao<ThirdPartyResponse> implements
ThirdPartyResponseDao{

	protected HibernateThirdPartyResponseDao() {
		super(ThirdPartyResponse.class);
	}

	public ThirdPartyResponse getThirdPartyResponseByClientId(long clientId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getUnique(criteria);
	}

	public void deleteThirdPartyResponse(ThirdPartyResponse thirdPatyResponse) {
		super.delete(thirdPatyResponse);
	}

	public void updateThirdPartyResponse(ThirdPartyResponse thirdPatyResponse) throws DuplicateKeyException{
		super.update(thirdPatyResponse);
	}

	public void saveThirdPartyResponse(ThirdPartyResponse thirdPatyResponse)
			throws DuplicateKeyException {
		super.save(thirdPatyResponse);
	}

}
