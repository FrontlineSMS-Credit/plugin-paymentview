package org.creditsms.plugins.paymentview.utils;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.ContactDao;

import org.creditsms.plugins.paymentview.data.domain.Client;

public class MoveClientDetailsToContacts {
	public void addToContact(ContactDao contactDao, Client client) throws NumberFormatException, DuplicateKeyException {
		Contact contact = contactDao.getFromMsisdn(client.getPhoneNumber());
		if (contact != null) {
			contact.setName(client.getFullName());
			contact.setPhoneNumber(client.getPhoneNumber());
			contactDao.updateContact(contact);
		} else {
			contact = new Contact(client.getFullName(), client.getPhoneNumber(), "", "", "", true);
			contactDao.saveContact(contact);
		}
	}
}
