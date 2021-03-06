package edu.fing.context.reasoner.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.fing.commons.constant.AdaptationType;

@Entity
@Table(name = "ADAPTATION", uniqueConstraints = { @UniqueConstraint(columnNames = { "SITUATION_ID", "SERVICE_ID" }) })
public class Adaptation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String description;
	private int adaptationOrder;
	private Service service;
	private Situation situation;
	private AdaptationType adaptationType;
	private String data;

	@Column(name = "ADAPTATION_ORDER")
	public int getAdaptationOrder() {
		return this.adaptationOrder;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ADAPTATION_TYPE")
	public AdaptationType getAdaptationType() {
		return adaptationType;
	}

	@Lob
	@Column(name = "DATA", columnDefinition = "MEDIUMTEXT")
	public String getData() {
		return this.data;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return this.id;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "SERVICE_ID", nullable = false)
	public Service getService() {
		return this.service;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SITUATION_ID", nullable = false)
	public Situation getSituation() {
		return this.situation;
	}

	public void setAdaptationOrder(int adaptationOrder) {
		this.adaptationOrder = adaptationOrder;
	}

	public void setAdaptationType(AdaptationType adaptationType) {
		this.adaptationType = adaptationType;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

}
