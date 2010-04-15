package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;

/**
 * Data object representing a mobile network operator
 * @author Emmanuel Kala
 *
 */

@Entity
public class NetworkOperator {
//>	CONSTANTS	
	private static final String FIELD_NAME = "name";
	
	public enum Field implements EntityField<NetworkOperator> {
		/** Field mapping for {@link NetworkOperator#id } */
		ID("id"),
		/** Field mapping for {@link NetworkOperator#operatorName }*/
		NAME(FIELD_NAME);
		
		private final String fieldName;
		
		Field(String fieldName) { this.fieldName = fieldName; }
		
		public String getFieldName() { return this.fieldName; }
	}
	
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)
	private long id;
	
	/** Name of the network operator*/
	@Column(name=FIELD_NAME, unique=true, nullable=false)
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
