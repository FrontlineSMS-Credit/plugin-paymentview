package org.creditsms.plugins.paymentview.data.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;

/**
 * Data object representing a payment service. A payment service is usually associated with
 * a network operator but this does not have to be the case
 * 
 * @author Emmanuel Kala
 *
 */

@Entity
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames={"id","repaymentConfirmationKeyword"}),
		@UniqueConstraint(columnNames={"id","dispersalConfirmationKeyword"})
		}
)
public class PaymentService {
//>	COLUMN NAME CONSTANTS
	/** Field name for {@link #serviceName} */
	private static final String FIELD_NAME = "name";
	/** Field name for {@link #smsShortCode} */
	private static final String FIELD_SMS_SHORT_CODE = "smsShortCode";
	
	public enum Field implements EntityField<PaymentService>{
		/** Field mapping for {@link PaymentService#serviceName} */
		NAME(FIELD_NAME),
		/** Field mapping for {@link PaymentService#smsShortCode}*/
		SHORT_CODE(FIELD_SMS_SHORT_CODE);
		
		private final String fieldName;
		
		Field(String fieldName) { this.fieldName = fieldName; }
		
		public String getFieldName() { return this.fieldName; }
	}
	
//>	PROPERTIES
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)	
	private long id;
	/** Name of the payment system*/
	@Column(name=FIELD_NAME, unique=true,nullable=false)
	private String serviceName;
	/** PIN Number used to transact using this payment service*/
	private String pinNumber;
	/** The networks in which {@link PaymentService} operates in*/
	@OneToMany(targetEntity=NetworkOperator.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<NetworkOperator> networkOperators = new HashSet<NetworkOperator>();
	/** Short code used to transact on this payment service*/
	@Column(name=FIELD_SMS_SHORT_CODE, unique=true)
	private String smsShortCode;
	/** SMS template for sending money via the payment service */
	private String sendMoneyTextMessage;
	/** SMS template for withdrawing money via the payment service */
	private String withdrawMoneyTextMessage;
	/** SMS template for enquiring the account balance on the payment service */
	private String balanceEnquiryTextMessage;
	/** SMS template for the text message received when money is received from a client*/
	private String repaymentConfirmationText;
	/** Keyword to flag the confirmation message as relating to a repayment */
	private String repaymentConfirmationKeyword;
	/** SMS template for the text message received after sending money */
	private String dispersalConfirmationText;
	/** Keyword used to flag the confirmation text as relating to a dierspal*/
	private String dispersalConfirmationKeyword;
	
	/**
	 * Returns the database ID of this payment service
	 * @return {@link #id}
	 */
	public long getId(){
		return id;
	}
	
	/**
	 * Gets the name of the payment service
	 * @return {@link #serviceName}
	 */
	public String getServiceName(){
		return serviceName;
	}
	
	/**
	 * Gets the pin number for this payment service
	 * @return {@link #pinNumber}
	 */
	public String getPinNumber(){
		return pinNumber;
	}
	
	/**
	 * Gets the network operators for this payment service
	 * @return {@link #networkOperators}
	 */
	public Set<NetworkOperator> getNetworkOperators(){
		return networkOperators;
	}
	
	/**
	 * Gets the SMS short code used by this payment service
	 * @return {@link #smsShortCode}
	 */
	public String getSmsShortCode(){
		return smsShortCode;
	}
	
	/**
	 * Gets the SMS template for sending money
	 * @return
	 */
	public String getSendMoneyTextMessage(){
		return sendMoneyTextMessage;
	}
	
	/**
	 * Gets the SMS template for withdrawing money
	 * @return
	 */
	public String getWithdrawMoneyTextMessage(){
		return withdrawMoneyTextMessage;
	}
	
	/**
	 * Gets the SMS template for balance enquiry
	 */
	public String getBalanceEnquiryTextMessage(){
		return balanceEnquiryTextMessage;
	}
	
	/**
	 * Gets the SMS template for the text message received when a dispersal is made
	 * @return
	 */
	public String getDispersalConfirmationText(){
		return dispersalConfirmationText;
	}
	
	/**
	 * Gets the SMS template for the text message received when a repayment is received (from a {@link Clinet})
	 * @return
	 */
	public String getRepaymentConfirmationText(){
		return repaymentConfirmationText;
	}
	
	/**
	 * Gets the keyword used to flag the received text message as relating to a repayment
	 * @return
	 */
	public String getRepaymentConfirmationKeyword(){
		return repaymentConfirmationKeyword;
	}
	
	/**
	 * Gets the keyword used to flag the received text message as relating to a disperal
	 * @return
	 */
	public String getDispersalConfirmationKeyword(){
		return dispersalConfirmationKeyword;
	}
	
	/**
	 * Sets the name for this payment service
	 * @param name new value for {@link #serviceName}
	 */
	public void setServiceName(String name){
		serviceName = name;
	}
	
	/**
	 * Sets the pin number for this payment service
	 * @param pin new value for {@link #pinNumber}
	 */
	public void setPinNumber(String pin){
		pinNumber = pin;
	}
	
	/**
	 * Sets the network operators for this payment service
	 * @param operators new value for {@link #networkOperators}
	 */
	public void setNetworkOperators(Set<NetworkOperator> operators){
		networkOperators = operators;
	}
	
	/**
	 * Sets the SMS short code for this payment service
	 * @param shortCode new value for {@link #smsShortCode}
	 */
	public void setSmsShortCode(String shortCode){
		smsShortCode = shortCode;
	}
	
	/**
	 * Sets the SMS template for the send money text message
	 * @param template new value for {@link #sendMoneyTextMessage}
	 */
	public void setSendMoneyTextMessage(String template){
		sendMoneyTextMessage = template;
	}
	
	/**
	 * Sets the SMS template for the withdraw money text message
	 * @param template
	 */
	public void setWithdrawMoneyTextMessage(String template){
		withdrawMoneyTextMessage = template;
	}
	
	/**
	 * Sets the SMS template for the balance enquiry text message
	 * @param template
	 */
	public void setBalanceEnquiryTextMessage(String template){
		balanceEnquiryTextMessage = template;
	}
	
	/**
	 * Sets the text message received when a repayment is received from a {@link Client}
	 * @param text
	 */
	public void setRepaymentConfirmationText(String text){
		repaymentConfirmationText = text;
	}
	
	/**
	 * Sets the SMS template for the text message received when a dispersal is made
	 * @param text
	 */
	public void setDispersalConfirmationText(String text){
		dispersalConfirmationText = text;
	}
	
	/**
	 * Sets the keyword used to flag a text message as the confirmation text for a repayment
	 * @param keyword
	 */
	public void setRepaymentConfirmationKeyword(String keyword){
		repaymentConfirmationKeyword = keyword;
	}
	
	/**
	 * Sets the keyword used to flag a text message as the confirmation text for a dispersal
	 * @param keyword
	 */
	public void setDispersalConfirmationKeyword(String keyword){
		dispersalConfirmationKeyword = keyword;
	}
	
	/**
	 * Adds new network operator to the list of network operators for this payment service
	 * @param operator {@link NetworkOperator} to be added
	 */
	public void addNetworkOperator(NetworkOperator operator){
		networkOperators.add(operator);
	}
}
