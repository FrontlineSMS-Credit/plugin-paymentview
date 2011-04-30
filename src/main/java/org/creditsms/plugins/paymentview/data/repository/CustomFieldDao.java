package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.CustomField;

public interface CustomFieldDao {
	/**
	 * Deletes a CustomField from the system
	 * 
	 * @param CustomField
	 */
	public void deleteCustomField(CustomField customField);

	/**
	 * return all the CustomFields in the system
	 * **/
	public List<CustomField> getAllCustomFields();

	/**
	 * @return
	 */
	public List<CustomField> getAllActiveCustomFields();

	/**
	 * @return
	 */
	public List<CustomField> getAllActiveUsedCustomFields();
	
	/**
	 * @return
	 */
	public List<CustomField> getAllActiveUnusedCustomFields();

	/**
	 * Returns all CustomFields from a particular start index with a maximum
	 * number of returned CustomFields set.
	 * 
	 * @param startIndex
	 *            index of the first CustomField to fetch
	 * @param limit
	 *            Maximum number of CustomFields to fetch from the start index
	 * @return
	 */
	public List<CustomField> getAllCustomFields(int startIndex, int limit);

	/**
	 * Returns a CustomField with the same id as the passed id
	 * 
	 * @param Id
	 * @return
	 */
	public CustomField getCustomFieldById(long id);

	/** @return number of CustomFields in the system */
	public int getCustomFieldCount();

	/**
	 * Returns a list of CustomFields whose name is similar to the specified
	 * string
	 * 
	 * @param srtname
	 *            string to be used to match the names
	 * @return
	 */
	public List<CustomField> getCustomFieldsByName(String strName);

	/**
	 * Returns a list of CustomFields whose name is similar to the specified
	 * string from a particular start index with a maximum number of returned
	 * CustomFields set
	 * 
	 * @param srtName
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	public List<CustomField> getCustomFieldsByName(String strName,
			int startIndex, int limit);

	public List<CustomField> getCustomFieldsByReadableName(String strName);

	public List<CustomField> getCustomFieldsByReadableName(String strName,
			int startIndex, int limit);

	/**
	 * Saves a CustomField to the system
	 * 
	 * @param customField
	 */
	public void saveCustomField(CustomField customField)
			throws DuplicateKeyException;

	/**
	 * Update a CustomField to the system
	 * 
	 * @param CustomField
	 */ 
	public void updateCustomField(CustomField customField)
			throws DuplicateKeyException;

}
