package edu.fing.contenxt.management;

import edu.fing.contenxt.management.ItineraryBean.AdaptationType;

public class AdaptationDto {
	private int id;
	private AdaptationType adaptationType;
	private Byte[] data;
	private String extraData;

	public AdaptationDto(int id, AdaptationType adaptationType) {
		super();
		this.id = id;
		this.adaptationType = adaptationType;
	}

	public AdaptationType getAdaptationType() {
		return this.adaptationType;
	}

	public Byte[] getData() {
		return this.data;
	}

	public String getExtraData() {
		return this.extraData;
	}

	public int getId() {
		return this.id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(Byte[] data) {
		this.data = data;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public void setId(int id) {
		this.id = id;
	}

}
