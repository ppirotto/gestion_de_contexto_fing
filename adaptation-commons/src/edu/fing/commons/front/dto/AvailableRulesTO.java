package edu.fing.commons.front.dto;

import java.util.Date;
import java.util.List;

public class AvailableRulesTO {	

	private String activeVersionNumber;
	private Date lastDeployDate;
	private String lastVersionNumber;
	
	private List<VersionTO> versions;

	
	public Date getLastDeployDate() {
		return lastDeployDate;
	}

	public void setLastDeployDate(Date lastDeployDate) {
		this.lastDeployDate = lastDeployDate;
	}

	public List<VersionTO> getVersions() {
		return versions;
	}

	public void setVersions(List<VersionTO> versions) {
		this.versions = versions;
	}

	public String getActiveVersionNumber() {
		return activeVersionNumber;
	}

	public void setActiveVersionNumber(String activeVersionNumber) {
		this.activeVersionNumber = activeVersionNumber;
	}

	public String getLastVersionNumber() {
		return lastVersionNumber;
	}

	public void setLastVersionNumber(String lastVersionNumber) {
		this.lastVersionNumber = lastVersionNumber;
	}

}
