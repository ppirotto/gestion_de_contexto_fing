package edu.fing.context.management.jar.creation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import edu.fing.contenxt.management.ContextSourceDto;


public class JarCreationService {

    public static void copy(InputStream input, OutputStream output) throws IOException {
    	int bytesRead;
        byte[] buffer = new byte[4 * 1024 * 1024];
    	while ((bytesRead = input.read(buffer))!= -1) {
    		output.write(buffer, 0, bytesRead);
    	}
    }
    
    
    public static void copyAndCustomize(InputStream input, OutputStream output, ContextSourceDto cS) throws IOException {
        byte[] buffer = new byte[4 * 1024 * 1024];
    	int bytesRead = input.read(buffer);
		String aux = new String(buffer,0,bytesRead);
		aux = aux.replace("${modeConverter}", cS.getModeConverter());
		aux = aux.replace("${eventName}", cS.getEventName());
		aux = aux.replace("${url}", cS.getUrl());
		aux = aux.replace("${cron}", cS.getCron());
		bytesRead = aux.length();
		output.write(aux.getBytes(),0,bytesRead);

    }
    
	public static void createContextSource(ContextSourceDto cS) throws IOException{
		String sourcePath = ResourceAccessHelper.getWebInfPath();
		boolean isPolling = cS.getReceiveMode().equals("POLLLING");
		if (isPolling){
			sourcePath += "/Contextual-Data-Input-Polling-Template.jar";
		} else {
			sourcePath += "/Contextual-Data-Input-Template.jar";
		}
		
        ZipFile jar = new ZipFile(sourcePath);
        ZipOutputStream customizedJar = new ZipOutputStream(new FileOutputStream("../standalone/deployments/context-source-"+(isPolling?"polling-":"")+cS.getEventName()+".jar"));

        Enumeration<? extends ZipEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();
            System.out.println("copy: " + e.getName());
            if (e.isDirectory()) {
            	customizedJar.putNextEntry(e);
            } else{
            	if (e.getName().contains("config.properties") || e.getName().contains("switchyard.xml") || e.getName().contains("pom.xml")){
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
}
