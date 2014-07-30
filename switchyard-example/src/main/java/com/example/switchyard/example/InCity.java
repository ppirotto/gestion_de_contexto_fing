package com.example.switchyard.example;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InCity implements Serializable {
	
	private String ciudad;
	private String user;
	private long timestamp;
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	

}
