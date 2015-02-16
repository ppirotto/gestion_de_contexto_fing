package edu.fing.commons.constant;

public enum AdaptationType {

	DELAY(DataType.INT), FILTER(DataType.STRING), ENRICH(DataType.FILE), TRANSFORM(DataType.FILE), SERVICE_INVOCATION;

	private DataType dataType;

	private AdaptationType() {
	}

	private AdaptationType(DataType dataType) {
		this.setDataType(dataType);
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
}
