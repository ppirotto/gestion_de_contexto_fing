package edu.fing.commons.front.dto;

import edu.fing.commons.dto.AdaptationTO;

public class AdaptationTreeNodeTO {

	private String xpath;
	private AdaptationTO adaptation;
	private AdaptationTreeNodeTO leftNode; // cumple condicion
	private AdaptationTreeNodeTO rightNode; // no cumple condicion

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public AdaptationTreeNodeTO getLeftNode() {
		return this.leftNode;
	}

	public void setLeftNode(AdaptationTreeNodeTO leftNode) {
		this.leftNode = leftNode;
	}

	public AdaptationTreeNodeTO getRightNode() {
		return this.rightNode;
	}

	public void setRightNode(AdaptationTreeNodeTO rightNode) {
		this.rightNode = rightNode;
	}

	public AdaptationTO getAdaptation() {
		return adaptation;
	}

	public void setAdaptation(AdaptationTO adaptation) {
		this.adaptation = adaptation;
	}

}
