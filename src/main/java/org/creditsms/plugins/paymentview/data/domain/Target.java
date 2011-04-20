package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 */
@Entity
@Table(name = Target.TABLE_NAME)
public class Target {
	public static final String TABLE_NAME = "Target";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;
	
	@Column(name = "startDate", nullable = true, unique = false)
	private Date startDate;

	@Column(name = "endDate", nullable = true, unique = false)
	private Date endDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId")
	private Account account;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "serviceItemId", 
			nullable = true)
	private ServiceItem serviceItem;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ServiceItem getServiceItem() {
		return serviceItem;
	}

	public void setServiceItem(ServiceItem targetItem) {
		this.serviceItem = targetItem;
	}
}
