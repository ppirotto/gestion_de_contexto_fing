package edu.fing.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ADAPTATION")
public class Adaptation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	enum DataType{
		INTEGER, STRING, XSLT
	}
	
	
	private Long id;
	private String name;
	private String description;
	private Service service;
	private Situation situation;
	private DataType dataType;
	private String data;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SITUATION_ID", nullable = false)
    public Situation getSituation() {
        return this.situation;
    }

	@Lob
	@Column(name = "DATA", columnDefinition = "MEDIUMTEXT")
	public String getData() {
		return data;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DATA_TYPE")
	public DataType getDataType() {
		return dataType;
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

	public void setSituation(Situation situation) {
		this.situation = situation;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
}
