package edu.fing.adaptation.gateway.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ITINERARY")
public class Itinerary {

	private Long id;
	private String user;
	private String service;
	private String operation;
	private String situation;
	private int priority;
	private Date expirationDate;
	private Set<ContextAwareAdaptation> adaptationDirective = new HashSet<ContextAwareAdaptation>();

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "USER")
	public String getUser() {
		return user;
	}

	@Column(name = "SERVICE")
	public String getService() {
		return service;
	}

	@Column(name = "OPERATION")
	public String getOperation() {
		return operation;
	}

	@Column(name = "SITUATION")
	public String getSituation() {
		return situation;
	}

	@Column(name = "PRIORITY")
	public int getPriority() {
		return priority;
	}

	@Column(name = "EXPIRATION_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getExpirationDate() {
		return expirationDate;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ITINERARY_ID", nullable = false)
	public Set<ContextAwareAdaptation> getAdaptationDirective() {
		return adaptationDirective;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setAdaptationDirective(Set<ContextAwareAdaptation> adaptationDirective) {
		this.adaptationDirective = adaptationDirective;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

}
