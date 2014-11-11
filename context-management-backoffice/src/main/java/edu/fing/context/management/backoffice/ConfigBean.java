package edu.fing.context.management.backoffice;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.switchyard.component.bean.Reference;

@Named
@RequestScoped
public class ConfigBean {

	@Inject
	@Reference
	private DroolsConfigurationService configService;

	private String versionNumber;

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String deployVersion(String versionNumber){
//		configService.deployVersion(versionNumber);
		return "";
	}
}
