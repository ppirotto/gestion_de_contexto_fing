package edu.fing.cep.engine.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;

public class DroolsUtils {

	public static KieModule createAndDeployJar(KieServices ks, ReleaseId releaseId, List<String> drls) throws DroolsCompilingException {
		byte[] jar = createKJar(ks, releaseId, null, drls);
		return deployJar(ks, jar);
	}

	public static byte[] createKJar(KieServices ks, ReleaseId releaseId, String pom, List<String> drls) throws DroolsCompilingException {
		KieFileSystem kfs = ks.newKieFileSystem();
		if (pom != null) {
			kfs.write("pom.xml", pom);
		} else {
			kfs.generateAndWritePomXML(releaseId);
		}
		for (int i = 0; i < drls.size(); i++) {
			if (drls.get(i) != null) {
				kfs.write("src/main/resources/r" + i + ".drl", drls.get(i));
			}
		}
		KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
		if (kb.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
			String exceptionMessage = "";
			for (org.kie.api.builder.Message result : kb.getResults().getMessages()) {
				exceptionMessage+=result.getText()+"\n";
				System.out.println(result.getText());
			}
			throw new DroolsCompilingException(exceptionMessage);
		}
		InternalKieModule kieModule = (InternalKieModule) ks.getRepository().getKieModule(releaseId);
		byte[] jar = kieModule.getBytes();
		return jar;
	}

	public static KieModule deployJar(KieServices ks, byte[] jar) {
		// Deploy jar into the repository
		Resource jarRes = ks.getResources().newByteArrayResource(jar);
		KieModule km = ks.getRepository().addKieModule(jarRes);
		return km;
	}

	@SuppressWarnings("resource")
	public static String getFileContent(String filePath) {
		FileInputStream inputStram;
		try {
			inputStram = new FileInputStream(filePath);
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = inputStram.read()) != -1){
				builder.append((char)ch);
			}
			
			return builder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
