package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.*;

import net.frontlinesms.data.domain.Contact;

/**
 * Data object representing a client. A client must be associated with a contact
 * @author Emmanuel Kala
 *
 */

@Entity
public class Client {
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)
	private long id;
	
	/** Contact reference for this client*/
	@OneToOne(targetEntity=Contact.class, optional=false)
	private Contact contact;
	
	/**
	 * Returns the database ID of this client
	 * @return {@link #id}
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Gets the contact associated with this client
	 * @return {@link #contact}
	 */
	public Contact getContact(){
		return contact;
	}
	
	/**
	 * Sets the contact to be associated with this client
	 * @param c Reference to the contact to be associated with this client
	 */
	public void setContact(Contact c){
		this.contact = c;
	}
}
