package edu.fing.context.reasoner.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ACTIVE_CONFIGURATION")
public class ActiveConfiguration {

	private long id;
	private Version activeVersion;
	private Version lastVersion;
	private Date lastDeployDate;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACTIVE_VERSION_ID", nullable = false)
	public Version getActiveVersion() {
		return activeVersion;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LAST_VERSION_ID", nullable = false)
	public Version getLastVersion() {
		return lastVersion;
	}

	@Column(name = "LAST_DEPLOY_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastDeployDate() {
		return lastDeployDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLastDeployDate(Date lastDeployDate) {
		this.lastDeployDate = lastDeployDate;
	}

	public void setActiveVersion(Version activeVersion) {
		this.activeVersion = activeVersion;
	}

	public void setLastVersion(Version lastVersion) {
		this.lastVersion = lastVersion;
	}
}
