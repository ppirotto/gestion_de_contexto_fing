package edu.fing.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SITUATION")
public class Situation {

	private Long id;
	private String name;
	private String description;
	private Set<Adaptation> adaptations;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return this.id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "situation")
	public Set<Adaptation> getAdaptations() {
		return adaptations;
	}
	public void setSituationId(String situationId) {
		this.name = situationId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAdaptations(Set<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}
	
}
