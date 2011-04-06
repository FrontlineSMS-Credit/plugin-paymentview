package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;
import net.frontlinesms.data.domain.FrontlineMessage;

/**
 * Data object representing a payment service transaction. A transaction may either be a deposit or
 * a withdrawal. Deposits are payments received from clients whereas withdrawals are funds transfers to 
 * clients
 * 
 * @author Emmanuel Kala
 *
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"payment_service_id", "transaction_code"})})
public class PaymentServiceTransaction {
//>	CONSTANTS	
    /** Field name for {@link #transactionType} */
    private static final String FIELD_TRANSACTION_TYPE = "transaction_type";
    /** Field name for {@link #paymentService} */
    private static final String FIELD_PAYMENT_SERVICE_ID = "payment_service_id";
    /** Field name for {@link #transactionCode} */
    private static final String FIELD_TRANSACTION_CODE = "transaction_code";
	
    public enum TransactionType {
        /** Transaction is a deposit of funds */
        DEPOSIT,
        /** Transaction is a withdrawal of funds */
        WITHDRAWAL,
        /** Transaction is a receipt of funds from a third party*/
        RECEIPT,
        /** Transaction is a transfer of funds to a third party*/
        TRANSFER
    }
    
    public enum Field implements EntityField<PaymentServiceTransaction>{
    	/** Field mapping for {@link PaymentServiceTransaction#client }*/
    CLIENT_ID("id"),
    /** Field mapping for {@link PaymentServiceTransaction#paymentService}*/
    PAYMENT_SERVICE_ID("paymentService"),
    /** Field mapping for {@link PaymentServiceTransaction#transactionType} */
    TYPE("transactionType");
    	
    	private final String fieldName;
    	
    	Field(String fieldName) { this.fieldName = fieldName; }
    	
    	public String getFieldName() { return this.fieldName; }
    }
    
    //>	PROPERTIES
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true,nullable=false,updatable=false)
    private long id;
    
    @ManyToOne(targetEntity=ClientNew.class, optional=false)
    private ClientNew client;
    
    @ManyToOne(targetEntity=PaymentService.class, optional=false)
    @JoinColumn(name=FIELD_PAYMENT_SERVICE_ID)
    private PaymentService paymentService;
    
    /** Reference to the SMS that was used to create this transaction */
    @OneToOne(targetEntity=FrontlineMessage.class, optional=false)
    private FrontlineMessage message;
    
    /** Type of transaction; can be DISPERSAL or REPAYMENT */
    @Column(name=FIELD_TRANSACTION_TYPE, nullable=false)
    private TransactionType transactionType;
    
    /** Amount transacted*/
    @Column(nullable=false)
    private double transactionAmount;
    
    /**Date of the transaction*/
    @Column(nullable=false)
    private Date transactionDate;
    
    /** Unique code for the transaction */
    @Column(name=FIELD_TRANSACTION_CODE, nullable=false)
    private String transactionCode;
    
    /** Flag to keep track of whether the transaction has been posted to an MIS */
    @Column(nullable=false)
    private boolean postedToMIS;
    
    /** Date when the transaction was posted to the MIS */
    private Date misPostingDate;
    
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
    public ClientNew getClient(){
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
     * Getst the {@link FrontlineMessage} from which this transaction was created
     * @return
     */
    public FrontlineMessage getMessasge(){
    	return message;
    }
    
    /**
     * Gets the type of this transaction
     * @return
     */
    public TransactionType getTransactionType(){
    	return transactionType;
    }
    
    /**
     * Gets the amount transacted during this transaction
     * @return {@link #transactionAmount}
     */
    public double getAmount(){
    	return transactionAmount;
    }
    
    /**
     * Gets the date of this transaction
     * @return {@link #transactionDate}
     */
    public Date getDate(){
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
     * Whether the transaction has been posted to an MIS
     * @return
     */
    public boolean isPostedToMIS(){
    	return postedToMIS;
    }
    
    /**
     * Gets the date when the transaction was posted to an MIS
     * @return
     */
    public Date getMisPostingDate(){
    	return misPostingDate;
    }
    
    /**
     * Sets the client for this transaction
     * @param client
     */
    public void setClient(ClientNew client){
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
     * Sets the message used to create this transaction
     * @param message a {@link FrontlineMessage} reference
     */
    public void setMessage(FrontlineMessage message){
    	this.message = message;
    }
    
    /**
     * Sets the type of this transaction
     * @param type
     */
    public void setTransactionType(TransactionType type){
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
    
    /**
     * Sets whether a transaction has been posted to an MIS
     * @param posted
     */
    public void setPostedToMIS(boolean posted){
    	postedToMIS = posted;
    }
    
    /**
     * Sets the date when the transacion was posted to an MIS
     * @param date
     */
    public void setMisPostingDate(Date date){
    	misPostingDate = date;
    }
}
