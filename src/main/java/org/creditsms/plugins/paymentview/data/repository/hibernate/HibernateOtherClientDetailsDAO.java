package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.repository.OtherClientDetailsDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class HibernateOtherClientDetailsDao extends BaseHibernateDao<OtherClientDetails>  implements OtherClientDetailsDao{
	
	protected HibernateOtherClientDetailsDao(){
		super(OtherClientDetails.class);
	}

	public List<OtherClientDetails> getAllOtherDetails() {
		return this.getHibernateTemplate().loadAll(OtherClientDetails.class);
	}

	public List<OtherClientDetails> getOtherDetailsByClientId(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OtherClientDetails.class);
			
		Criteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add( Restrictions.eq("id", clientId ));
				
		List<OtherClientDetails> otherClientDetailsLst = criteria.list();

		if (otherClientDetailsLst.size() == 0) {
			return null;
		}
		return otherClientDetailsLst;
	}

	public OtherClientDetails getOtherClientDetailsById(long id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OtherClientDetails.class)
		.add(Restrictions.eq("id", id));
			
		List<OtherClientDetails> otherClientDetailsLst = criteria.list();

		if (otherClientDetailsLst.size() == 0) {
			return null;
		}
		return otherClientDetailsLst.get(0);
	}

	public void deleteOtherClientDetails(OtherClientDetails otherClientDetails) {
		this.getHibernateTemplate().delete(otherClientDetails);
		this.getHibernateTemplate().flush();
	}

	public void saveUpdateOtherClientDetails(OtherClientDetails otherClientDetails) {
		this.getHibernateTemplate().saveOrUpdate(otherClientDetails);
	}

}
