package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.CustomDataDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class HibernateCustomValueDao extends BaseHibernateDao<CustomValue>  implements CustomDataDao{
	
	protected HibernateCustomValueDao(){
		super(CustomValue.class);
	}

	public List<CustomValue> getAllOtherDetails() {
		return super.getAll();
	}

	public List<CustomValue> getOtherDetailsByClientId(long id) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", id));
		return super.getList(criteria);
	}

	public CustomValue getOtherClientDetailsById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public void deleteOtherClientDetails(CustomValue otherClientDetails) {
		super.delete(otherClientDetails);
	}

	public void saveOtherClientDetails(CustomValue otherClientDetails) throws DuplicateKeyException {
		super.save(otherClientDetails);
	}

	public void updateOtherClientDetails(CustomValue otherClientDetails) throws DuplicateKeyException {
		super.update(otherClientDetails);
	}

	public List<CustomValue> getOtherDetailsByCustomFieldByValue(long customfieldId,
			String strValue) {
		DetachedCriteria criteria = super.getCriterion()
		.add(Restrictions.disjunction()
		.add(Restrictions.ilike("strValue", strValue.trim(), MatchMode.ANYWHERE)));
		DetachedCriteria customFieldCriteria = criteria.createCriteria("customField");
		customFieldCriteria.add(Restrictions.eq("id",customfieldId));
		return super.getList(criteria);
	}

}
