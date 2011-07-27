package org.creditsms.plugins.paymentview.data.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.frontlinesms.data.EntityField;
import org.hibernate.annotations.IndexColumn;

/**
 * @Author Kim
 */
@Entity
@Table(name = LogMessage.TABLE_NAME)
public class LogMessage {
	public static final String TABLE_NAME = "LogMessage";

	private static final String FIELD_ID = "id";
	private static final String FIELD_LOG_TIMESTAMP = "timestamp";
	private static final String FIELD_LOG_LEVEL = "logLevel";
	private static final String FIELD_LOG_TITLE = "logTitle";
	private static final String FIELD_LOG_CONTENT = "logContent";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = FIELD_ID, nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_LOG_TIMESTAMP)
	private Long logTimestamp;

	@Column(name = FIELD_LOG_LEVEL)
	private LogLevel logLevel;
	
	@Column(name = FIELD_LOG_TITLE)
	private String logTitle;

	@Column(name = FIELD_LOG_CONTENT)
	private String logContent;
	
	public enum Field implements EntityField<LogMessage> {
		ID(FIELD_ID),
		LOG_TIMESTAMP(FIELD_LOG_TIMESTAMP),
		LOG_LEVEL(FIELD_LOG_LEVEL),
		LOG_TITLE(FIELD_LOG_TITLE),
		LOG_CONTENT(FIELD_LOG_CONTENT);
		
		/** name of a field */
		private final String fieldName;
		/**
		 * Creates a new {@link Field}
		 * @param fieldName name of the field
		 */
		Field(String fieldName) { this.fieldName = fieldName; }
		/** @see EntityField#getFieldName() */
		public String getFieldName() { return this.fieldName; }
	}

	/** Empty constructor required for hibernate. */
	public LogMessage() {
	}

	public LogMessage(LogLevel logLevel, String logTitle, String logContent) {
		this.logTimestamp = Calendar.getInstance().getTimeInMillis();
		this.logLevel = logLevel;
		this.logTitle = logTitle;
		this.logContent = logContent;
	}
	
	public LogMessage(Long logTimestamp, LogLevel logLevel, String logTitle, String logContent) {
		this.logTimestamp = logTimestamp;
		this.logLevel = logLevel;
		this.logTitle = logTitle;
		this.logContent = logContent;
	}

	public static enum LogLevel {
		INFO("Info","/icons/sms_created.png"),
		WARNING("Warning", "/icons/sms_unconfirmed.png"),
		ERROR("Error", "/icons/sms_confirmed.png");

		private String status;
		private String icon;

		private LogLevel(String displayName, String icon) {
			this.status = displayName;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return status;
		}

		public String getIconPath() {
			return icon;
		}
		
		public static LogLevel getStatusFromString(String status){
			for(LogLevel s:LogLevel.values()){
				if(status.equalsIgnoreCase(s.toString())){
					return s;
				}
			}
			return null;
		}
	}

	public Long getTimestamp() {
		return logTimestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.logTimestamp = timestamp;
	}
	
	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public String getLogTitle() {
		return logTitle;
	}

	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogMessage other = (LogMessage) obj;
		if (logTimestamp != other.logTimestamp) {
			return false;
		}
		if (!logTitle.equals(other.logTitle)) {
			return false;
		}
		if (!logContent.equals(other.logContent)) {
			return false;
		}

		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((logTimestamp == null) ? 0 : logTimestamp.hashCode());
		return result;
	}



	@Override
	public String toString() {
		return "Log [id=" + id + ", timestamp =" + logTimestamp + ", logTitle ="
				+ logTitle + ", logContent =" + logContent;
	}


	
	//> Used by the UI; an illusion mimmick show selection ;-)
	@Transient
	private boolean selected = false;
	
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}
}
