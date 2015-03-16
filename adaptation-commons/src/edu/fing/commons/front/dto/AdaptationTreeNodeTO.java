package edu.fing.commons.front.dto;

public class AdaptationTreeNodeTO {

	private String xpath;
	private String url;
	private AdaptationTreeNodeTO leftNode; // cumple condicion
	private AdaptationTreeNodeTO rightNode; // no cumple condicion

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
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

}
