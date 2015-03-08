package edu.fing.commons.front.dto;

import java.util.List;

public class ContextSourceTO {

	private List<String> contextData;

	private String modeConverter;
	private String eventName;
	private String receiveMode;
	private String url;
	private String cron;

	public List<String> getContextData() {
		return this.contextData;
	}

	public String getCron() {
		return this.cron;
	}

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

	public void setContextData(List<String> contextData) {
		this.contextData = contextData;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setModeConverter(String modeConverter) {
		this.modeConverter = modeConverter;
	}

	public void setReceiveMode(String receiveMode) {
		this.receiveMode = receiveMode;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
