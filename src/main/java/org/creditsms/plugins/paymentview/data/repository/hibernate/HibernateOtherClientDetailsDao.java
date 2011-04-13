package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.repository.OtherClientDetailsDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateOtherClientDetailsDao extends BaseHibernateDao<OtherClientDetails>  implements OtherClientDetailsDao{
	
	protected HibernateOtherClientDetailsDao(){
		super(OtherClientDetails.class);
	}

	public List<OtherClientDetails> getAllOtherDetails() {
		return super.getAll();
	}

	public List<OtherClientDetails> getOtherDetailsByClientId(long id) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", id));
		return super.getList(criteria);
	}

	public OtherClientDetails getOtherClientDetailsById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public void deleteOtherClientDetails(OtherClientDetails otherClientDetails) {
		super.delete(otherClientDetails);
	}

	public void saveOtherClientDetails(OtherClientDetails otherClientDetails) throws DuplicateKeyException {
		super.save(otherClientDetails);
	}

	public void updateOtherClientDetails(OtherClientDetails otherClientDetails) throws DuplicateKeyException {
		super.update(otherClientDetails);
	}

}
