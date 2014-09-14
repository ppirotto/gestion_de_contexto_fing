package edu.fing.switchyard.CEP_Engine.drools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.PostConstruct;


















import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.core.impl.InternalKnowledgeBase;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.cdi.KSession;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.persistence.jpa.KieStoreServices;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.conf.KieSessionOption;
import org.switchyard.component.bean.Service;

import edu.fing.switchyard.CEP_Engine.DroolsInputMessage;

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
			InputStream inputStram = new FileInputStream("C:\\Eclipse Kepler personal\\workspace\\CEP-Engine\\src\\main\\resources\\rules\\Sample.drl");
			Resource resource = kieServices.getResources().newInputStreamResource(inputStram );
			kfs.write("src/main/resources/rules/Sample.drl",resource);
			
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
	public void insert(DroolsInputMessage inputMessage){
		kSession.insert(inputMessage);
	}
	
}
