package edu.fing.context.reasoner.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CONTEXT_SOURCE")
public class ContextSource {

	private Long id;
	private String name;
	private String description;
	private String properties;
	private Set<ContextDatum> contextData;

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

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Column(name = "PROPERTIES", columnDefinition = "MEDIUMTEXT", nullable = true)
	public String getProperties() {
		return properties;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "CONTEXT_SOURCE_DATUM", joinColumns = { @JoinColumn(name = "CONTEXT_SOURCE_ID") }, inverseJoinColumns = { @JoinColumn(name = "CONTEXT_DATUM_ID") })
	public Set<ContextDatum> getContextData() {
		return contextData;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setContextData(Set<ContextDatum> contextData) {
		this.contextData = contextData;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

}
