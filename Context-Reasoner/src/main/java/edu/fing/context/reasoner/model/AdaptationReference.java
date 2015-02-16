package edu.fing.context.reasoner.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.fing.commons.constant.AdaptationType;

@Entity
@Table(name = "ADAPTATION_REFERENCE")
public class AdaptationReference implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private AdaptationType adaptationType;
	private String uri;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ADAPTATION_TYPE")
	public AdaptationType getAdaptationType() {
		return adaptationType;
	}

	@Column(name = "URI")
	public String getUri() {
		return uri;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
