package edu.fing.context.reasoner.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RULE")
public class Rule {

	private Long id;
	private String name;
	private Set<RuleVersion> ruleVersions;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rule")
	public Set<RuleVersion> getRuleVersions() {
		return ruleVersions;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRuleVersions(Set<RuleVersion> ruleVersions) {
		this.ruleVersions = ruleVersions;
	}
}
