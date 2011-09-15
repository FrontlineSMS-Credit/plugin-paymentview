package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy
 */

@Entity
@Table(name = ThirdPartyResponse.TABLE_NAME)

public class ThirdPartyResponse {
	public static final String TABLE_NAME = "ThirdPartyResponse";

	private static final String FIELD_ID = "id";
	private static final String FIELD_CLIENT = "clientId";
	private static final String FIELD_MESSAGE = "message";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@OneToOne
	@JoinColumn(name = FIELD_CLIENT, unique = true)
	private Client client;
	
	@Column(name = FIELD_MESSAGE)
	private String message;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
