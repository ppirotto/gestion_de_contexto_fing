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
		return this.dataType;
	}

	public boolean renderFileUpload() {
		return (this.dataType != null) && (this.dataType.equals(DataType.FILE));

	}

	public boolean renderStringInput() {
		return (this.dataType != null) && (this.dataType.equals(DataType.STRING) || this.dataType.equals(DataType.INT));

	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
}
