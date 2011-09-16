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

	public static final int LOG_CONTENT_LENGTH = 2000;
	
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
	private Long logTimestamp; // TODO rename as 'timestamp' - we know it is related to the log

	@Column(name = FIELD_LOG_LEVEL)
	private LogLevel logLevel; // TODO rename as 'level' - we know it is related to the log
	
	@Column(name = FIELD_LOG_TITLE)
	private String logTitle; // TODO rename as 'title' - we know it is related to the log

	@Column(name = FIELD_LOG_CONTENT, length=LOG_CONTENT_LENGTH)
	private String logContent; // TODO rename as 'content' - we know it is related to the log
	
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

	// TODO move this to top of class
	public static enum LogLevel { // TODO rename as Level - we know it is related to the log
		INFO("Info","/icons/log_info.png"),
		WARNING("Warning", "/icons/log_warning.png"),
		ERROR("Error", "/icons/log_error.png");

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
	
	public LogLevel getLogLevel() { // TODO rename as 'getLevel' - we know it is related to the log
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) { // TODO rename as 'setLevel' - we know it is related to the log
		this.logLevel = logLevel;
	}

	public String getLogTitle() { // TODO rename as 'getTitle' - we know it is related to the log
		return logTitle;
	}

	public void setLogTitle(String logTitle) { // TODO rename as 'setTitle' - we know it is related to the log
		this.logTitle = logTitle;
	}

	public String getLogContent() { // TODO rename as 'getContent)(' - we know it is related to the log
		return logContent;
	}

	public void setLogContent(String logContent) { // TODO rename as 'setContent' - we know it is related to the log
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

//> FACTORY METHODS
	public static LogMessage error(String title, String content) {
		return new LogMessage(LogLevel.ERROR, title, content);
	}
}
