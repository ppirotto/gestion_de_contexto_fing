package edu.fing.switchyard.CEP_Engine.drools;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.switchyard.component.bean.Service;

import edu.fing.switchyard.CEP_Engine.DroolsUtils;

@Service(DroolsManagerService.class)
public class DroolsManagerServiceBean implements DroolsManagerService {

	private static KieSession kSession;
//	private static KieFileSystem kFileSystem;
	private static KieContainer kContainer;
	private static KieServices kServices;
	private KieBaseConfiguration streamModeConfig;

	@PostConstruct
	void intializeDroolsContext() {
		try {

			kServices = KieServices.Factory.get();			
			streamModeConfig = kServices.newKieBaseConfiguration();
			streamModeConfig.setOption( EventProcessingOption.STREAM );
			
			//TODO le deberia pedir al repositorio la version activa
			ReleaseId releaseId1 = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", "1");
			
			String drl1 = DroolsUtils.getFileContent("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\ContextReasonerNotification.drl");

			KieModule kieModule = DroolsUtils.createAndDeployJar( kServices, releaseId1, drl1/*, drl2, drl3*/ );
		
			// Create a session and fire rules
			kContainer = kServices.newKieContainer( kieModule.getReleaseId() );
			KieBase kBase = kContainer.newKieBase(streamModeConfig);

			kSession = kBase.newKieSession();
			
			new Thread(new Runnable() {
				public void run() {
	        	   	kSession.fireUntilHalt();
	           	}
			}).start();
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	};
	
	

	@Override
	public String insert(HashMap<String, String> inputMessage){
		try{
			ClassLoader classLoader=	Thread.currentThread().getContextClassLoader();
			System.out.println(classLoader);
			kSession.insert(inputMessage);
			return "OK";
		} catch(Exception e) {
			return "ERROR";
		}
	}

	@Override
	public void addRule() {

		// TODO pedir version activa al repo
		String activeVersion = "2";

		ReleaseId newReleaseId = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", activeVersion);

		// TODO pedir files al repo
		String drl1 = DroolsUtils.getFileContent("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\ContextReasonerNotification.drl");
		String drl2 = DroolsUtils.getFileContent("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\UserLocation.drl");
		String drl3 = DroolsUtils.getFileContent("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\CityWeather.drl");

		KieModule kieModule = DroolsUtils.createAndDeployJar(kServices, newReleaseId, drl1, drl2, drl3);

		kContainer = kServices.newKieContainer( kieModule.getReleaseId() );
		KieBase kBase = kContainer.newKieBase(streamModeConfig);
		
		kSession.halt();
		kSession.destroy();

		kSession = kBase.newKieSession();
		
		new Thread(new Runnable() {
			public void run() {
        	   	kSession.fireUntilHalt();
           	}
		}).start();
		System.out.println("asdasdasdasads");
	}
	
}
