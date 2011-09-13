package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy
 */

@Entity
@Table(name = ResponseRecipient.TABLE_NAME)

public class ResponseRecipient {
	public static final String TABLE_NAME = "ResponseRecipient";

	private static final String FIELD_ID = "id";
	private static final String FIELD_THIRD_PARTY_RESPONSE = "responseId";
	private static final String FIELD_CLIENT = "clientId";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = FIELD_THIRD_PARTY_RESPONSE)
	private ThirdPartyResponse thirdPartyResponse;

	@ManyToOne
	@JoinColumn(name = FIELD_CLIENT)
	private Client client;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ThirdPartyResponse getThirdPartyResponse() {
		return thirdPartyResponse;
	}

	public void setThirdPartyResponse(ThirdPartyResponse thirdPartyResponse) {
		this.thirdPartyResponse = thirdPartyResponse;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
