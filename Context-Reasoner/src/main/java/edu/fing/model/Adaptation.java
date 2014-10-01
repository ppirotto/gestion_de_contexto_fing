package edu.fing.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ADAPTATION")
public class Adaptation {

	private Long id;
	private String name;
	private String description;
	private Service service;
	
	private byte[] file;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "SERVICE_ID", nullable = false)
	public Service getService() {
		return service;
	}

	@Lob
	@Column(name = "FILE", nullable = true)
	public byte[] getAdaptationFile() {
		return file;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setAdaptationFile(byte[] adaptationFile) {
		this.file = adaptationFile;
	}
	
	
}
