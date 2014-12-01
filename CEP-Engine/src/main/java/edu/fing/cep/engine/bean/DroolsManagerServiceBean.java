package edu.fing.cep.engine.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.switchyard.component.bean.Service;

import edu.fing.cep.engine.model.ActiveConfiguration;
import edu.fing.cep.engine.model.Rule;
import edu.fing.cep.engine.model.Version;
import edu.fing.cep.engine.utils.DroolsUtils;
import edu.fing.cep.engine.utils.HibernateUtils;
import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsManagerService.class)
public class DroolsManagerServiceBean implements DroolsManagerService {

	private static KieSession kSession;
	private static KieContainer kContainer;
	private static KieServices kServices;
	private KieBaseConfiguration streamModeConfig;

	private static ReentrantReadWriteLock  lock = new ReentrantReadWriteLock();
	@PostConstruct
	void intializeDroolsContext() {
		try {
			//inicializo fields para Drools
			kServices = KieServices.Factory.get();			
			streamModeConfig = kServices.newKieBaseConfiguration();
			streamModeConfig.setOption( EventProcessingOption.STREAM );

			//Obtengo version activa para deployar
			Version activeVersion = getActiveVersion();
			
			//Creo el releaseId
			ReleaseId releaseId1 = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", activeVersion.getVersionNumber());
			
			List<String> stringRules = getStringRules(activeVersion);

			KieModule kieModule = DroolsUtils.createAndDeployJar( kServices, releaseId1, stringRules);
		
			startSession(kieModule);
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}



	private Version getActiveVersion() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT * ");
		queryString.append("FROM ACTIVE_CONFIGURATION");

		Query query = session.createSQLQuery(queryString.toString()).addEntity(ActiveConfiguration.class);
//			query.setParameter("situationName", "InCityRaining");

		@SuppressWarnings("unchecked")
		ActiveConfiguration config = (ActiveConfiguration) query.uniqueResult();
		if (config==null){
			return null;
		}
		Version activeVersion = config.getVersion();
		System.out.println("versionNumber: "+activeVersion.getVersionNumber());
		
		session.getTransaction().commit();
		session.close();
		return activeVersion;
	}



	private void startSession(KieModule kieModule) {
		// Create a session and fire rules
		kContainer = kServices.newKieContainer(kieModule.getReleaseId());
		KieBase kBase = kContainer.newKieBase(streamModeConfig);
		try {
			lock.writeLock().lock();
			if (kSession != null) {
				kSession.halt();
				kSession.destroy();
			}
			kSession = kBase.newKieSession();
			new Thread(new Runnable() {
				public void run() {
					kSession.fireUntilHalt();
				}
			}).start();
		} finally {
			lock.writeLock().unlock();
		}
	};
	
	

	@Override
	public String insert(HashMap<String, String> inputMessage){
		try{
			ClassLoader classLoader=	Thread.currentThread().getContextClassLoader();
			System.out.println(classLoader);
			lock.readLock().lock();
			kSession.insert(inputMessage);
			return "OK";
		} catch(Exception e) {
			return "ERROR";
		} finally{
			lock.readLock().unlock();
		}
	}

	@Override
	public void deployVersion(String versionNumber) {
		
		Version desiredVersion = getVersion(versionNumber);
		
		ReleaseId newReleaseId = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", desiredVersion.getVersionNumber());

		List<String> stringRules = getStringRules(desiredVersion);
		KieModule kieModule = DroolsUtils.createAndDeployJar(kServices, newReleaseId, stringRules);

		kContainer = kServices.newKieContainer( kieModule.getReleaseId() );
		KieBase kBase = kContainer.newKieBase(streamModeConfig);
		
		startSession(kieModule);
		
	}



	private List<String> getStringRules(Version desiredVersion) {
		//Obtengo las reglas asociadas a la version
		Set<Rule> rules = desiredVersion.getRules();
		List<String> stringRules = new ArrayList<String>();
		for(Rule rule : rules){
			stringRules.add(rule.getRule());
		}
		return stringRules;
	}
	
	private Version getVersion(String version) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT * ");
		queryString.append("FROM VERSION ");
		queryString.append("WHERE VERSION_NUMBER = :version");

		Query query = session.createSQLQuery(queryString.toString()).addEntity(Version.class);
		query.setParameter("version", version);

		@SuppressWarnings("unchecked")
		Version desiredVersion = (Version) query.uniqueResult();
		if (desiredVersion==null){
			return null;
		}
		System.out.println("Version versionNumber: "+desiredVersion.getVersionNumber() + " loaded from database.");
		
		session.getTransaction().commit();
		session.close();
		return desiredVersion;
	}



	@Override
	public void updateActiveVersion(String versionNumber) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) query.uniqueResult();
		
		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionNumber);
		
		@SuppressWarnings("unchecked")
		Version newVersion = (Version) queryNewVersion.uniqueResult();
		
		activeConfig.setVersion(newVersion);
		System.out.println("Updating to versionNumber: "+activeConfig.getVersion().getVersionNumber());
		session.update(activeConfig);
		
		session.getTransaction().commit();
		session.close();
	}



	@Override
	public AvailableRulesTO getAvailableRules() {
		// TODO Auto-generated method stub
		AvailableRulesTO res = new AvailableRulesTO();
		
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query activeConfigurationQuery = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) activeConfigurationQuery.uniqueResult();
		
		res.setActiveVersionId(activeConfig.getId());
		res.setLastDeployDate(activeConfig.getLastDeployDate());
		List<VersionTO> versions = new ArrayList<VersionTO>();
		res.setVersions(versions);
		
		Query allVersionsQuery = session.createSQLQuery("SELECT * FROM VERSION").addEntity(Version.class);
		
		@SuppressWarnings("unchecked")
		List<Version> allVersions = allVersionsQuery.list();
		
		for (Version version : allVersions) {
			VersionTO newVersionTO = new VersionTO();
			newVersionTO.setCreationDate(version.getCreationDate());
			newVersionTO.setId(version.getId());
			newVersionTO.setVersionNumber(version.getVersionNumber());
			List<RuleTO> rulesTO = new ArrayList<RuleTO>();
			for (Rule rule : version.getRules()) {
				RuleTO ruleTO = new RuleTO();
				ruleTO.setId(rule.getId());
				ruleTO.setName(rule.getName());
				ruleTO.setRule(rule.getRule());
				rulesTO.add(ruleTO);
			}
			newVersionTO.setRules(rulesTO);
			res.getVersions().add(newVersionTO);
		}

		return res;
	}
	
	
	
}
