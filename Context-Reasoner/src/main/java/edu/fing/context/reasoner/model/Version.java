package edu.fing.context.reasoner.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "VERSION")
public class Version {

	private Long id;
	private String versionNumber;
	private Date creationDate;
	private Set<RuleVersion> ruleVersions;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "VERSION_NUMBER", nullable = false)
	public String getVersionNumber() {
		return versionNumber;
	}

	@Column(name = "CREATION_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "version")
	public Set<RuleVersion> getRuleVersions() {
		return ruleVersions;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setRuleVersions(Set<RuleVersion> ruleVersions) {
		this.ruleVersions = ruleVersions;
	}

}
