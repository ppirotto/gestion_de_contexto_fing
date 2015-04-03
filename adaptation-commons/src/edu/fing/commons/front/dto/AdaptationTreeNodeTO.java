package edu.fing.commons.front.dto;

import java.io.Serializable;

import edu.fing.commons.dto.AdaptationTO;

public class AdaptationTreeNodeTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nodeType;
	private String xpath;
	private AdaptationTO adaptation;
	private AdaptationTreeNodeTO leftNode; // cumple condicion
	private AdaptationTreeNodeTO rightNode; // no cumple condicion

	public AdaptationTO getAdaptation() {
		return this.adaptation;
	}

	public AdaptationTreeNodeTO getLeftNode() {
		return this.leftNode;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public AdaptationTreeNodeTO getRightNode() {
		return this.rightNode;
	}

	public String getXpath() {
		return this.xpath;
	}

	public void setAdaptation(AdaptationTO adaptation) {
		this.adaptation = adaptation;
	}

	public void setLeftNode(AdaptationTreeNodeTO leftNode) {
		this.leftNode = leftNode;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public void setRightNode(AdaptationTreeNodeTO rightNode) {
		this.rightNode = rightNode;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	@Override
	public String toString() {
		if (this.xpath != null) {
			return "[" + this.nodeType + "] " + this.xpath;
		} else if (this.adaptation != null) {
			return "[" + this.nodeType + "] " + this.adaptation.getAdaptationType().toString();
		} else {
			return "Nodo Raíz";
		}
	}
}
