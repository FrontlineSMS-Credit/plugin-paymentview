package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

	/**
	 * @Author Roy
	 */
	@Entity
	@Table(name = ServiceItem.TABLE_NAME)
	public class ServiceItem {
		public static final String TABLE_NAME = "TargetItem";
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id",
	            nullable=false,
	            unique=true)
		private long id;
		
		@Column(name="targetName",
				nullable=false,
				unique=false)
		private String targetName;
		
		@Column(name="amount",
				nullable=false,
				unique=false)
		private BigDecimal amount;

		@OneToMany
		private Set<Target> target = new HashSet<Target>();

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTargetName() {
			return targetName;
		}

		public void setTargetName(String targetName) {
			this.targetName = targetName;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public Set<Target> getTarget() {
			return target;
		}

		public void setTarget(Set<Target> target) {
			this.target = target;
		}

		@Override
		public String toString() {
			return "ServiceItem [id=" + id + ", targetName=" + targetName
					+ ", amount=" + amount + ", target=" + target + "]";
		}
}
