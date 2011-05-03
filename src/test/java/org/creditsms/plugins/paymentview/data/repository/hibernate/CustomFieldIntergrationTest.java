package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

/**
 * 
 * @author Roy
 *
 */
public class CustomFieldIntergrationTest extends HibernateTestCase{
	@Autowired                     
	HibernateCustomFieldDao hibernateCustomFieldDao;
	
	public void testSetup() {
		assertNotNull(hibernateCustomFieldDao);
	}
	
	public void testSaveCustomFields() throws DuplicateKeyException{
		assertDatabaseEmpty();
		CustomField cf = createCustomField("location");
		hibernateCustomFieldDao.saveCustomField(cf);
		assertEquals(1, hibernateCustomFieldDao.getCustomFieldCount());
	}
	
	public void testDeleteCustomField() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveCustomField("Ikolomani", 1);
		hibernateCustomFieldDao.deleteCustomField(getCustomField());
		assertEquals(0, hibernateCustomFieldDao.getCustomFieldCount());
	}
	
	public void testGetCustomFieldById() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveCustomField("Lokichogio", 1);
		List<CustomField> lstCustomField = hibernateCustomFieldDao.getAllCustomFields();
		assertEquals("Lokichogio", hibernateCustomFieldDao.getCustomFieldById(lstCustomField.get(0).getId()).getStrName());
	}
	
	public void testGetCustomFieldsByName() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveCustomField("Ikolomani", 1);
		createAndSaveCustomField("Karen", 2);
		assertEquals(1, hibernateCustomFieldDao.getCustomFieldsByName("kol").size());
	}
	
	private CustomField getCustomField(){
		assertEquals(1, this.hibernateCustomFieldDao.getAllCustomFields().size());
		return this.hibernateCustomFieldDao.getAllCustomFields().get(0);
	}
	
	private void createAndSaveCustomField(String strName, int expectedCount) throws DuplicateKeyException{
		CustomField cf = createCustomField(strName);
		hibernateCustomFieldDao.saveCustomField(cf);
		assertEquals(expectedCount, hibernateCustomFieldDao.getCustomFieldCount());
	}

	private CustomField createCustomField(String strName){
		CustomField cf = new CustomField(StringUtil.toCamelCase(strName), strName);
		return cf;
	}
	
	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateCustomFieldDao.getCustomFieldCount());
	}
}
