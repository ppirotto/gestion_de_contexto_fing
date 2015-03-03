package edu.fing.commons.front.dto;


public class CreateRulesVersionResponseTO {

	@Override
	public String toString() {
		return "CreateRulesVersionResponseTO [success=" + success
				+ ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ "]";
	}
	private boolean success;
	private String errorCode;
	private String errorMessage;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
