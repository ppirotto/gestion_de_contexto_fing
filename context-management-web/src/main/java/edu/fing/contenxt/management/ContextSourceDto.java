package edu.fing.contenxt.management;

public class ContextSourceDto {
	private String modeConverter;
	private String eventName;
	private String receiveMode;
	private String url;
	private String cron;

	public String getEventName() {
		return this.eventName;
	}

	public String getModeConverter() {
		return this.modeConverter;
	}

	public String getReceiveMode() {
		return this.receiveMode;
	}

	public String getUrl() {
		return this.url;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName.trim();
	}

	public void setModeConverter(String modeConverter) {
		this.modeConverter = modeConverter.trim();
	}

	public void setReceiveMode(String receiveMode) {
		this.receiveMode = receiveMode.trim();
	}

	public void setUrl(String url) {
		this.url = url.trim();
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

}
