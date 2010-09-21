package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.*;

import net.frontlinesms.data.EntityField;

/**
 * Object representing errors/exceptions registered by PaymentView
 * @author Emmanuel Kala
 *
 */

@Entity
public class PaymentViewError {
//> CONSTANTS
    public enum ErrorType {
        /** General plugin errors */
        GENERAL_FAILURE,
        /** Error type for payments received from unrecognized phone numbers */
        UNRECOGNIZED_PHONE_NUMBER
    }
    
    /** Details of the fields that this class has*/
    public enum Field implements EntityField<PaymentViewError> {
        TYPE("errorType");
        
        /** Name of a field */
        private final String fieldName;
        
        /**
         * Creates a new {@link Field}
         * @param fieldName Name of the field
         */
        Field(String fieldName) { this.fieldName = fieldName; }
        
        /** @see {@link EntityField#getFieldName()} */
        public String getFieldName() { return this.fieldName; }
    }
    
//> INSTANCE PROPERTIES    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true, nullable=false, updatable=false)
    private long id;
    
    /** Error type */
    @Column(nullable=false)
    private ErrorType errorType;
    
    /** Date when the error was logged */
    @Column(nullable=false,updatable=false)
    private long errorDate;
    
    /** Description of the error */
    @Column(nullable=false)
    private String errorDescription;
 
//> CONSTRUCTORS    
    /**
     * Default constructor for hibernate
     */
    PaymentViewError() {
        
    }
    
    public PaymentViewError(ErrorType type, String description) {
        setErrorType(type);
        setErrorDescription(description);
    }
 
//> ACCESSOR METHODS
    /**
     * Gets the unique id of the error
     * @return
     */
    public long getId() {
        return id;
    }
    
    /**
     * Gets the type of the error
     * @return
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Gets the date & time when the error was reported
     * @return
     */
    public long getErrorDate() {
        return this.errorDate;
    }
    
    /**
     * Gets the description of the error
     * @return
     */
    public String getErrorDescription() {
        return errorDescription;
    }

//> MUTATORS    
    /**
     * Sets the type of error
     * @param type
     */
    public void setErrorType(ErrorType type) {
        this.errorType = type;
    }
    
    /**
     * Sets the date when the error occurred
     * @param date
     */
    public void setErrorDate(long date) {
        this.errorDate = date;
    }
    
    /**
     * Sets the description of the error
     * @param description
     */
    public void setErrorDescription(String description) {
        this.errorDescription = description;
    }
}
