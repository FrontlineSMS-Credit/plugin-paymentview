package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy
 * @author ian
 */
@Entity
@Table(name = ServiceItem.TABLE_NAME)
public class ServiceItem {
	public static final String TABLE_NAME = "ServiceItem";
	
	@Id
	@IndexColumn(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = "amount", nullable = false, unique = false)
	private BigDecimal amount;

	@OneToMany
	private Set<Target> target;

	@Column(name = "targetName", nullable = false, unique = false)
	private String targetName;

	/** Empty constructor required for hibernate. */
	public ServiceItem() {}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public long getId() {
		return id;
	}

	public Set<Target> getTarget() {
		return target;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTarget(Set<Target> target) {
		this.target = target;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
	public String toString() {
		return "ServiceItem [id=" + id + ", targetName=" + targetName
				+ ", amount=" + amount + ", target=" + target + "]";
	}
}
