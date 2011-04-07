package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;
import net.frontlinesms.data.domain.Contact;

/**
 * Data object representing a client. A client must be associated with a contact
 * @author Emmanuel Kala
 *
 */

@Entity
public class Client {
	/** Field name for {@link #name} */
	private static final String FIELD_NAME = "name";
	/** Field name for {@link #contact} */
	private static final String FIELD_CONTACT_ID = "contact_id";
	
	public enum Field implements EntityField<Client>{
		/** Field mapping for {@link Client#name} */
		NAME(FIELD_NAME),
		/** Field mapping for {@link Client#phoneNumber} */
		PHONE_NUMBER("phoneNumber"),
		/** Field mapping for {@link Client#contact} */
		CONTACT(FIELD_CONTACT_ID);
		
		private final String fieldName;
		
		Field(String fieldName) { this.fieldName = fieldName; }
		
		public String getFieldName() { return this.fieldName; }
	}
	
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)
	private long id;
	/** Name of this client */
	private String name;
	/** Phone number of this client */
	@Column(unique=true, updatable=true)
	private String phoneNumber;
	/** Secondary phone number of this client */
	private String otherPhoneNumber;
	/** Email address of this client */
	private String emailAddress;
	/** Contact reference for this client*/
	@OneToOne(targetEntity=Contact.class, optional=true)
	private Contact contact;
	
	/**
	 * Returns the database ID of this client
	 * @return {@link #id}
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Returns the name of this client
	 * @return {@link #name}
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * Gets this client's phone number
	 * @return {@link #phoneNumber}
	 */
	public String getPhoneNumber(){
		return phoneNumber;
	}
	
	/**
	 * Gets the other phone number registered to this client
	 * @return {@link #otherPhoneNumber}
	 */
	public String getOtherPhoneNumber(){
		return otherPhoneNumber;
	}
	
	/**
	 * Gets the email address for this client
	 * @return {@link #emailAddress}
	 */
	public String getEmailAddress(){
		return emailAddress;
	}
	
	/**
	 * Gets the contact associated with this client
	 * @return {@link #contact}
	 */
	public Contact getContact(){
		return contact;
	}
	
	/**
	 * Sets the name of this client
	 * @param name new value for {@link #name}
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Sets the primary phone number for this client
	 * @param phone new value for {@link #phoneNumber}
	 */
	public void setPhoneNumber(String phone){
		this.phoneNumber = phone;
	}
	
	/**
	 * Sets the secondary phone number for this client
	 * @param phone new value for the other phone number
	 */
	public void setOtherPhoneNumber(String phone){
		this.otherPhoneNumber = phone;
	}
	
	/**
	 * Sets the email address for this client
	 * @param email new value for the email addres
	 */
	public void setEmailAddress(String email){
		this.emailAddress = email;
	}
	
	/**
	 * Sets the contact to be associated with this client
	 * @param c Reference to the contact to be associated with this client
	 */
	public void setContact(Contact c){
		this.contact = c;
	}

	public void setId(long id) {
		this.id = id;
	}
}
