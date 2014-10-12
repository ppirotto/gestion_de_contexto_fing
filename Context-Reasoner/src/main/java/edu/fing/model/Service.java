package edu.fing.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE")
public class Service  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String serviceName;
	private String operationName;
	private String description;
	
	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}
	
	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}
	
	@Column(name = "OPERATION_NAME")
	public String getOperationName() {
		return operationName;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
