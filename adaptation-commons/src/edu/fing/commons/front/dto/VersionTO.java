package edu.fing.commons.front.dto;

import java.util.Date;
import java.util.List;

public class VersionTO {

	private String versionNumber;
	private Date creationDate;
	private List<RuleTO> rules;


	@Override
	public String toString() {
		return versionNumber;
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
