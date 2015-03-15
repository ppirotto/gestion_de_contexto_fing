package edu.fing.context.management.jar.creation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.context.management.dto.VirtualServiceDto;

public class JarCreationService {

	public static void copy(InputStream input, OutputStream output) throws IOException {
		int bytesRead;
		byte[] buffer = new byte[4 * 1024 * 1024];
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static void copyAndCustomize(InputStream input, OutputStream output, ContextSourceTO cS) throws IOException {
		byte[] buffer = new byte[4 * 1024 * 1024];
		int bytesRead = input.read(buffer);
		String aux = new String(buffer, 0, bytesRead);
		aux = aux.replace("${modeConverter}", cS.getModeConverter());
		aux = aux.replace("${eventName}", cS.getEventName());
		aux = aux.replace("${url}", cS.getUrl());
		aux = aux.replace("${cron}", cS.getCron());
		bytesRead = aux.length();
		output.write(aux.getBytes(), 0, bytesRead);
	}

	private static void copyAndCustomize(InputStream input, OutputStream output, VirtualServiceDto vS) throws IOException {
		byte[] buffer = new byte[4 * 1024 * 1024];
		int bytesRead = input.read(buffer);
		String aux = new String(buffer, 0, bytesRead);
		aux = aux.replace("${serviceName}", vS.getServiceName());
		aux = aux.replace("${serviceUrl}", vS.getServiceURL());
		aux = aux.replace("${virtualService}", vS.getVirtualService());
		bytesRead = aux.length();
		output.write(aux.getBytes(), 0, bytesRead);
	}

	public static void createContextSource(ContextSourceTO cS) throws IOException {
		String sourcePath = ResourceAccessHelper.getWebInfPath();
		boolean isPolling = cS.getReceiveMode().equals("POLLING");
		if (isPolling) {
			sourcePath += "/Context-Source-Polling-Template.jar";
		} else {
			sourcePath += "/Context-Source-Template.jar";
		}

		ZipFile jar = new ZipFile(sourcePath);
		ZipOutputStream customizedJar = new ZipOutputStream(new FileOutputStream("../standalone/deployments/context-source-" + (isPolling ? "polling-" : "") + cS.getEventName() + ".jar"));

		Enumeration<? extends ZipEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();
			System.out.println("copy: " + e.getName());
			if (e.isDirectory()) {
				customizedJar.putNextEntry(e);
			} else {
				if (e.getName().contains("config.properties") || e.getName().contains("switchyard.xml") || e.getName().contains("pom.xml")) {
					ZipEntry newE = new ZipEntry(e.getName());
					customizedJar.putNextEntry(newE);
					copyAndCustomize(jar.getInputStream(e), customizedJar, cS);
				} else {
					customizedJar.putNextEntry(e);
					copy(jar.getInputStream(e), customizedJar);
				}
			}
			customizedJar.closeEntry();
		}

		// close
		jar.close();
		customizedJar.close();

	}

	public static void createVirtualService(VirtualServiceDto vS) throws IOException {
		String sourcePath = ResourceAccessHelper.getWebInfPath();
		sourcePath += "/Virtual-Service-Template.jar";

		ZipFile jar = new ZipFile(sourcePath);
		ZipOutputStream customizedJar = new ZipOutputStream(new FileOutputStream("../standalone/deployments/virtual-service-" + vS.getServiceName() + ".jar"));

		Enumeration<? extends ZipEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();
			System.out.println("copy: " + e.getName());
			if (e.isDirectory()) {
				customizedJar.putNextEntry(e);
			} else {
				if (e.getName().contains("invokeAG.xml") || e.getName().contains("switchyard.xml") || e.getName().contains("pom.xml")) {
					ZipEntry newE = new ZipEntry(e.getName());
					customizedJar.putNextEntry(newE);
					copyAndCustomize(jar.getInputStream(e), customizedJar, vS);
				} else {
					customizedJar.putNextEntry(e);
					copy(jar.getInputStream(e), customizedJar);
				}
			}
			customizedJar.closeEntry();
		}

		// close
		jar.close();
		customizedJar.close();
	}
}
