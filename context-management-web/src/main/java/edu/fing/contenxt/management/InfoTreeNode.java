package edu.fing.contenxt.management;

import java.io.Serializable;

public class InfoTreeNode implements Serializable, Comparable<InfoTreeNode> {

	private static final long serialVersionUID = 1L;

	private String name;

	private String data;

	private String type;

	public InfoTreeNode(String name, String data, String type) {
		this.name = name;
		this.setData(data);
		this.type = type;
	}

	@Override
	public int compareTo(InfoTreeNode document) {
		return this.getName().compareTo(document.getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		InfoTreeNode other = (InfoTreeNode) obj;
		if (this.data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!this.data.equals(other.data)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}

	public String getData() {
		return this.data;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.name;
	}
}