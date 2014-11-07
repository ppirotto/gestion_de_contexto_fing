package edu.fing.cep.engine.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "ACTIVE_CONFIGURATION")
public class ActiveConfiguration {

	private long id;
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
	
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "VERSION_ID", nullable = false)
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
