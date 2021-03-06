package edu.fing.context.reasoner.model;

import java.io.Serializable;
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

@Entity
@Table(name = "SERVICE")
public class Service implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String serviceName;
	private String operationName;
	private String description;
	private String url;
	private Set<Adaptation> adaptations;
	private Set<ServiceSituationPriority> priorities;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	@Column(name = "OPERATION_NAME")
	public String getOperationName() {
		return operationName;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "URL")
	public String getUrl() {
		return url;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "service")
	public Set<Adaptation> getAdaptations() {
		return adaptations;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "SERVICE_ID", nullable = false)
	public Set<ServiceSituationPriority> getPriorities() {
		return priorities;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAdaptations(Set<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}

	public void setPriorities(Set<ServiceSituationPriority> priorities) {
		this.priorities = priorities;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
