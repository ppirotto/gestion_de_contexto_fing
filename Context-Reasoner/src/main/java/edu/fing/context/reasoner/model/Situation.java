package edu.fing.context.reasoner.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SITUATION")
public class Situation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private Long duration;
	private Set<Adaptation> adaptations;
	private Rule rule;
	private Set<ContextDatum> inputContextData;
	private Set<ContextDatum> outputContextData;
	private Set<ContextSource> contextSources;

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

	@Column(name = "DURATION", nullable = true)
	public Long getDuration() {
		return duration;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "situation")
	public Set<Adaptation> getAdaptations() {
		return adaptations;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RULE_ID", nullable = true)
	public Rule getRule() {
		return rule;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SITUATION_INPUT_CONTEXT_DATUM", joinColumns = { @JoinColumn(name = "SITUATION_ID") }, inverseJoinColumns = { @JoinColumn(
			name = "CONTEXT_DATUM_ID") })
	public Set<ContextDatum> getInputContextData() {
		return inputContextData;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SITUATION_OUTPUT_CONTEXT_DATUM", joinColumns = { @JoinColumn(name = "SITUATION_ID") }, inverseJoinColumns = { @JoinColumn(
			name = "CONTEXT_DATUM_ID") })
	public Set<ContextDatum> getOutputContextData() {
		return outputContextData;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "SITUATION_CONTEXT_SOURCE", joinColumns = { @JoinColumn(name = "SITUATION_ID") }, inverseJoinColumns = { @JoinColumn(
			name = "CONTEXT_SOURCE_ID") })
	public Set<ContextSource> getContextSources() {
		return contextSources;
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

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setAdaptations(Set<Adaptation> adaptations) {
		this.adaptations = adaptations;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setInputContextData(Set<ContextDatum> inputContextData) {
		this.inputContextData = inputContextData;
	}

	public void setOutputContextData(Set<ContextDatum> outputContextData) {
		this.outputContextData = outputContextData;
	}

	public void setContextSources(Set<ContextSource> contextSources) {
		this.contextSources = contextSources;
	}

}
