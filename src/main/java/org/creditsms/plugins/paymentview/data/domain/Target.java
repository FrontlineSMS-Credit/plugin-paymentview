package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@OneToOne
	// (fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId")
	private Account account;

	@Column(name = "endDate", nullable = true, unique = false)
	private Date endDate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@ManyToOne
	// (fetch = FetchType.LAZY)
	@JoinColumn(name = "serviceItemId", nullable = true)
	private ServiceItem serviceItem;

	@Column(name = "startDate", nullable = true, unique = false)
	private Date startDate;

	/** Empty constructor required for hibernate. */
	public Target() {
	}

	public Account getAccount() {
		return account;
	}

	public Date getEndDate() {
		return endDate;
	}

	public long getId() {
		return id;
	}

	public ServiceItem getServiceItem() {
		return serviceItem;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setServiceItem(ServiceItem targetItem) {
		this.serviceItem = targetItem;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
