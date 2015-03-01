package edu.fing.contenxt.management;


public class VirtualServiceDto {

	private String serviceName;
	private String serviceURL;
	private String virtualService;	
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceURL() {
		return serviceURL;
	}
	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	public String getVirtualService() {
		return virtualService;
	}
	public void setVirtualService(String virtualService) {
		this.virtualService = virtualService;
	}
}
