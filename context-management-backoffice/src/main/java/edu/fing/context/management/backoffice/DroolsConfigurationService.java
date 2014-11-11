package edu.fing.context.management.backoffice;

public interface DroolsConfigurationService {
	
	String deployVersion(String versionNumber);

	String updateActiveVersion(String versionNumber);
}
