package edu.fing.commons.front.dto;

import java.util.Date;
import java.util.List;

public class AvailableRulesTO {

	private long activeVersionId;
	private Date lastDeployDate;
	private List<VersionTO> versions;

	public long getActiveVersionId() {
		return activeVersionId;
	}

	public void setActiveVersionId(long activeVersionId) {
		this.activeVersionId = activeVersionId;
	}

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

}
