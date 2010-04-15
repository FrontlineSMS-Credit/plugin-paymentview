package org.creditsms.plugins.paymentview.data.domain;

import java.util.ArrayList;
import java.util.List;

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
public class PaymentService {
//>	COLUMN NAME CONSTANTS
	/** Field name for {@link #serviceName} */
	private static final String FIELD_NAME = "name";
	/** Field name for {@link #smsShortCode} */
	private static final String FIELD_SMS_SHORT_CODE = "sms_short_code";	
	/** Column name for {@link #sendMoneyTextMessage}*/
	private static final String FIELD_SEND_MONEY_TEXT = "send_money_text";
	/** Column name for {@link #withdrawMoneyTextMessage}*/
	private static final String FIELD_WITHDRAW_MONEY_TEXT = "withdraw_money_text";
	/** Column name for {@link #balanceEnquiryTextMessage}*/
	private static final String FIELD_BALANCE_ENQUIRY_TEXT = "balance_enquiry_text";
	
	public enum Field implements EntityField<PaymentService>{
		/** Field mapping for {@link PaymentService#serviceName} */
		NAME(FIELD_NAME);
		
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
	private List<NetworkOperator> networkOperators = new ArrayList<NetworkOperator>();
	/** Short code used to transact on this payment service*/
	@Column(name=FIELD_SMS_SHORT_CODE)
	private String smsShortCode;
	/** SMS template for sending money via the payment service */
	@Column(name=FIELD_SEND_MONEY_TEXT)
	private String sendMoneyTextMessage;
	/** SMS template for withdrawing money via the payment service */
	@Column(name=FIELD_WITHDRAW_MONEY_TEXT)
	private String withdrawMoneyTextMessage;
	/** SMS template for enquiring the account balance on the payment service */
	@Column(name=FIELD_BALANCE_ENQUIRY_TEXT)
	private String balanceEnquiryTextMessage;
	
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
	public List<NetworkOperator> getNetworkOperators(){
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
	public void setNetworkOperators(List<NetworkOperator> operators){
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
	 * Adds new network operator to the list of network operators for this payment service
	 * @param operator {@link NetworkOperator} to be added
	 */
	public void addNetworkOperator(NetworkOperator operator){
		networkOperators.add(operator);
	}
}
