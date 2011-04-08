package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = OtherClientDetails.TABLE_NAME)

public class OtherClientDetails {
	public static final String TABLE_NAME = "OtherClientDetails";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="detailsId",
            nullable=false,
            unique=true)
	private long detailsId;

	@Column(name="location",
			nullable=true,
			unique=false)
	private String location;
	
	@Column(name="group",
			nullable=true,
			unique=false)
	private String group;
	
	@Column(name="branchOffice",
			nullable=true,
			unique=false)
	private String branchOffice;
	
	@Column(name="representatives",
			nullable=true,
			unique=false)
	private String representatives;	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", 
			nullable = true)
	private Client clientNew;

	public long getDetailsId() {
		return detailsId;
	}

	public void setDetailsId(long detailsId) {
		this.detailsId = detailsId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getBranchOffice() {
		return branchOffice;
	}

	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}

	public String getRepresentatives() {
		return representatives;
	}

	public void setRepresentatives(String representatives) {
		this.representatives = representatives;
	}

	public Client getClientNew() {
		return clientNew;
	}

	public void setClientNew(Client clientNew) {
		this.clientNew = clientNew;
	}

}
