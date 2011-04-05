package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.repository.OtherClientDetailsDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class OtherClientDetailsDAOImpl extends HibernateDaoSupport implements OtherClientDetailsDAO{

	public List<OtherClientDetails> getAllOtherDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OtherClientDetails> getOtherDetailsByClientId(long clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	public OtherClientDetails getOtherClientDetails(long otherClientDetailsId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteClient(OtherClientDetails otherClientDetails) {
		// TODO Auto-generated method stub
		
	}

	public void saveUpdateClient(OtherClientDetails otherClientDetails) {
		// TODO Auto-generated method stub
		
	}

}
