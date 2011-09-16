package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 */
@Entity
@Table(name = TargetServiceItem.TABLE_NAME)

public class TargetServiceItem {
	public static final String TABLE_NAME = "TargetServiceItem";
	
	public static final String FIELD_TARGET = "targetId";
	public static final String FIELD_PRICE_PER_ITEM = "pricePerItem";
	public static final String FIELD_SERVICE_ITEM = "serviceItemId";
	public static final String FIELD_QTY ="serviceItemQty";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	@Column(name = FIELD_QTY)
	private long serviceItemQty;
	@Column(name = FIELD_PRICE_PER_ITEM, nullable = false, unique = false)
	private BigDecimal amount;
	@ManyToOne
	@JoinColumn(name = FIELD_SERVICE_ITEM)
	private ServiceItem serviceItem;
	@ManyToOne
	@JoinColumn(name = FIELD_SERVICE_ITEM)
	private Target target;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getServiceItemQty() {
		return serviceItemQty;
	}
	public void setServiceItemQty(long serviceItemQty) {
		this.serviceItemQty = serviceItemQty;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public ServiceItem getServiceItem() {
		return serviceItem;
	}
	public void setServiceItem(ServiceItem serviceItem) {
		this.serviceItem = serviceItem;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
}
