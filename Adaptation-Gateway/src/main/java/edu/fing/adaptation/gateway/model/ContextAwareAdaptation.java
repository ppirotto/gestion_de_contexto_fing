package edu.fing.adaptation.gateway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "CONTEXT_AWARE_ADAPTATION")
public class ContextAwareAdaptation {

	private Long id;
	private String name;
	private String uri;
	private String data;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	@Column(name = "URI")
	public String getUri() {
		return uri;
	}

	@Lob
	@Column(name = "DATA", columnDefinition = "MEDIUMTEXT")
	public String getData() {
		return data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setData(String data) {
		this.data = data;
	}

}
