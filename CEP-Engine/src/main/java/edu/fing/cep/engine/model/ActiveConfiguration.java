package edu.fing.cep.engine.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ACTIVE_CONFIGURATION")
public class ActiveConfiguration {

	private Version version;
	private Date lastDeployDate;
	
	@Column(name = "LAST_DEPLOY_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastDeployDate() {
		return lastDeployDate;
	}
	public void setLastDeployDate(Date lastDeployDate) {
		this.lastDeployDate = lastDeployDate;
	}
	
	@Column(name = "VERSION", nullable = false)
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
}
