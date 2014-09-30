package edu.fing.switchyard.CEP_Engine.drools;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.switchyard.component.bean.Service;

@Service(DroolsManagerService.class)
public class DroolsManagerServiceBean implements DroolsManagerService {

	private static KieSession kSession;
	private static KieFileSystem kFileSystem;

	@PostConstruct
	void intializeDroolsContext() {
		try {

			KieServices kieServices = KieServices.Factory.get();
			KieResources kieResources = kieServices.getResources();
			KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
			KieRepository kieRepository = kieServices.getRepository();
			
			KieBaseConfiguration config = kieServices.newKieBaseConfiguration();
			config.setOption( EventProcessingOption.STREAM );
			KieFileSystem kfs = kieServices.newKieFileSystem();
			
			InputStream inputStram = new FileInputStream("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\Sample.drl");
			Resource resource = kieServices.getResources().newInputStreamResource(inputStram );
			kfs.write("src/main/resources/rules/Sample.drl",resource);
			
			InputStream inputStram2 = new FileInputStream("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\UserLocation.drl");
			Resource resource2 = kieServices.getResources().newInputStreamResource(inputStram2 );
			kfs.write("src/main/resources/rules/UserLocation.drl",resource2);
			
			InputStream inputStram3 = new FileInputStream("C:\\ProyectoGrado\\gestion_de_contexto_fing\\CEP-Engine\\src\\main\\resources\\rules\\CityWeather.drl");
			Resource resource3 = kieServices.getResources().newInputStreamResource(inputStram3 );
			kfs.write("src/main/resources/rules/CityWeather.drl",resource3);
			
			
			kieServices.newKieBuilder(kfs).buildAll();
			
			KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
			KieBase kBase = kieContainer.newKieBase(config);
			
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
	public void insert(HashMap<String, String> inputMessage){
		kSession.insert(inputMessage);
	}
	
}
