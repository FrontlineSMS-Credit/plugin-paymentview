package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.repository.OtherClientDetailsDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class OtherClientDetailsDAOImpl extends HibernateDaoSupport implements OtherClientDetailsDAO{

	public List<OtherClientDetails> getAllOtherDetails() {
		return this.getHibernateTemplate().loadAll(OtherClientDetails.class);
	}

	public List<OtherClientDetails> getOtherDetailsByClientId(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OtherClientDetails.class);
			
		Criteria clientCriteria = criteria.createCriteria("clientNew");
		clientCriteria.add( Restrictions.eq("clientId", clientId ));
				
		List<OtherClientDetails> otherClientDetailsLst = criteria.list();

		if (otherClientDetailsLst.size() == 0) {
			return null;
		}
		return otherClientDetailsLst;
	}

	public OtherClientDetails getOtherClientDetails(long otherClientDetailsId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OtherClientDetails.class)
		.add(Restrictions.eq("detailsId", otherClientDetailsId));
			
		List<OtherClientDetails> otherClientDetailsLst = criteria.list();

		if (otherClientDetailsLst.size() == 0) {
			return null;
		}
		return otherClientDetailsLst.get(0);
	}

	public void deleteClient(OtherClientDetails otherClientDetails) {
		this.getHibernateTemplate().delete(otherClientDetails);
		this.getHibernateTemplate().flush();
	}

	public void saveUpdateClient(OtherClientDetails otherClientDetails) {
		this.getHibernateTemplate().saveOrUpdate(otherClientDetails);
		this.getHibernateTemplate().flush();
		this.getHibernateTemplate().refresh(otherClientDetails);
	}

}
