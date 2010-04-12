package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.*;

/**
 * Data object representing a mobile network operator
 * @author Emmanuel Kala
 *
 */

@Entity
public class NetworkOperator {
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)
	private long id;
	
	/** Name of the network operator*/
	@Column(nullable=false)
	private String operatorName;
	
	/**
	 * Returns the database ID of this network operator
	 * @return {@link #id}
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Gets the name of this network operator
	 * @return {@link #operatorName}
	 */
	public String getOperatorName(){
		return operatorName;
	}
	
	/**
	 * Sets the name of this network operator
	 * @param name New value for {@link #operatorName}
	 */
	public void setOperatorName(String name){
		operatorName = name;
	}
}
