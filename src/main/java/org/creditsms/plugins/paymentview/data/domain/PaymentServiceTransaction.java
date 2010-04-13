package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;

/**
 * Data object representing a payment service transaction. A transaction may either be a deposit or
 * a withdrawal. Deposits are payments received from clients whereas withdrawals are funds transfers to 
 * clients
 * 
 * @author Emmanuel Kala
 *
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"paymentService_id", "transaction_code"})})
public class PaymentServiceTransaction {
//>	CONSTANTS
	/** Flags the transaction as a deposit */
	public static final int TYPE_DEPOSIT = 0;
	/** Flags the transaction as a withdrawal */
	public static final int TYPE_WITHDRAWAL = 1;
	/** Flags the transaction as a balance enquiry */
	public static final int TYPE_BALANCE_ENQUIRY = 2;
	
	/** Field name for {@link #transactionType} */
	private static final String FIELD_TRANSACTION_TYPE = "transaction_type";
	/** Field name for {@link #paymentService} */
	//private static final String FIELD_PAYMENT_SERVICE_ID = "payment_service_id";
	/** Field name for {@link #transactionCode} */
	private static final String FIELD_TRANSACTION_CODE = "transaction_code";
	
	public enum Field implements EntityField<PaymentServiceTransaction>{
		/** Field mapping for {@link PaymentServiceTransaction#transactionType} */
		TRANSACTION_TYPE(FIELD_TRANSACTION_TYPE);
		
		private final String fieldName;
		
		Field(String fieldName) { this.fieldName = fieldName; }
		
		public String getFieldName() { return this.fieldName; }
	}
	
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true,nullable=false,updatable=false)
	private long id;
	
	@ManyToOne(targetEntity=Client.class, optional=false)
	private Client client;
	
	@ManyToOne(targetEntity=PaymentService.class, optional=false)
	//@Column(name=FIELD_PAYMENT_SERVICE)
	private PaymentService paymentService;
	
	@Column(name=FIELD_TRANSACTION_TYPE, nullable=false)
	private int transactionType;
	
	@Column(nullable=false)
	private double transactionAmount;
	
	@Column(nullable=false)
	private Date transactionDate;
	
	@Column(name=FIELD_TRANSACTION_CODE, nullable=false)
	private String transactionCode;
	
	/**
	 * Returns the database ID of this transaction
	 * @return {@link #id}
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Gets the client associated with this transaction
	 * @return {@link #client}
	 */
	public Client getClient(){
		return client;
	}
	
	/**
	 * Gets the mobile payment service used for this transaction
	 * @return {@link #paymentService}
	 */
	public PaymentService getPaymentService(){
		return paymentService;
	}
	
	/**
	 * Gets the type of this transaction
	 * @return
	 */
	public int getTransactionType(){
		return transactionType;
	}
	
	/**
	 * Gets the amount transacted during this transaction
	 * @return {@link #transactionAmount}
	 */
	public double getTransactionAmount(){
		return transactionAmount;
	}
	
	/**
	 * Gets the date of this transaction
	 * @return {@link #transactionDate}
	 */
	public Date getTransactionDate(){
		return transactionDate;
	}
	
	/**
	 * Gets the transaction code for this transaction
	 * @return {@link #transactionCode}
	 */
	public String getTransactionCode(){
		return transactionCode;
	}
	
	/**
	 * Sets the client for this transaction
	 * @param client
	 */
	public void setClient(Client client){
		this.client = client;
	}
	
	/**
	 * Sets the payment service for this transaction
	 * @param service
	 */
	public void setPaymentService(PaymentService service){
		paymentService = service;
	}
	
	/**
	 * Sets the type of this transaction
	 * @param type
	 */
	public void setTransactionType(int type){
		transactionType = type;
	}
	
	/**
	 * Sets the amount for this transaction
	 * @param amount
	 */
	public void setTransactionAmount(double amount){
		transactionAmount = amount;
	}
	
	/**
	 * Sets the date for this transaction
	 * @param date
	 */
	public void setTransactionDate(Date date){
		transactionDate = date;
	}
	
	/**
	 * Sets the transaction code for this transaction
	 * @param code
	 */
	public void setTransactionCode(String code){
		transactionCode = code;
	}
}
