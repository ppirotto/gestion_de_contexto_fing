package edu.fing.context.reasoner.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "VERSION")
public class Version {

	private Long id;
	private String versionNumber;
	private Date creationDate;

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

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
