package edu.fing.context.reasoner.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RuleVersion {

	private Long id;
	private String drl;
	private Version version;

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

	public void setId(Long id) {
		this.id = id;
	}

	public void setDrl(String drl) {
		this.drl = drl;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
}
