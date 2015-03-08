package edu.fing.context.reasoner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RULE_VERSION")
public class RuleVersion {

	private Long id;
	private String drl;
	private Version version;
	private Rule rule;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "DRL", columnDefinition = "MEDIUMTEXT", nullable = false)
	public String getDrl() {
		return drl;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VERSION_ID", nullable = false)
	public Version getVersion() {
		return version;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RULE_ID", nullable = false)
	public Rule getRule() {
		return rule;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDrl(String drl) {
		this.drl = drl;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}
}
