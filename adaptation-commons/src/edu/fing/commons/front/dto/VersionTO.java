package edu.fing.commons.front.dto;

import java.util.Date;
import java.util.List;

public class VersionTO {

	private Long id;
	private String versionNumber;
	private Date creationDate;
	private List<RuleTO> rules;

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return versionNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<RuleTO> getRules() {
		return rules;
	}

	public void setRules(List<RuleTO> rules) {
		this.rules = rules;
	}

}
