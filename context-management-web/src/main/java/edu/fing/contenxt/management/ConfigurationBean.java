package edu.fing.contenxt.management;

import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import edu.fing.commons.front.dto.ServiceTO;

@ManagedBean
@ViewScoped
public class ConfigurationBean {

	private String description;
	private String serviceName;

	private Set<ServiceTO> services;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String getDescription() {
		return this.description;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public Set<ServiceTO> getServices() {
		return this.services;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setServices(Set<ServiceTO> services) {
		this.services = services;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
}
