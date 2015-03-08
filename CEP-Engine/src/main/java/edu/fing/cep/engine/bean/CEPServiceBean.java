package edu.fing.cep.engine.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIUserConversion;

import edu.fing.cep.engine.model.ActiveConfiguration;
import edu.fing.cep.engine.model.Rule;
import edu.fing.cep.engine.model.Version;
import edu.fing.cep.engine.utils.DroolsCompilingException;
import edu.fing.cep.engine.utils.DroolsUtils;
import edu.fing.cep.engine.utils.HibernateUtils;
import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsManagerService.class)
public class CEPServiceBean implements CEPService {

	@Inject
	@Reference	
	private DroolsManagerService droolsManager;
	
	@Override
	public CreateRulesVersionResponseTO createNewVersion(VersionTO versionTO) {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		CreateRulesVersionResponseTO res = new CreateRulesVersionResponseTO();
		res.setSuccess(true);
		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionTO.getVersionNumber());
		
		@SuppressWarnings("unchecked")
		Version savedVersion = (Version) queryNewVersion.uniqueResult();
		
		if (savedVersion!=null){
			res.setErrorCode("VERSION_ALREADY_EXISTS");
			res.setSuccess(false);
			res.setErrorMessage("The version with versionNumber = '"+versionTO.getVersionNumber()+"' already exists.");
			return res;
		} else if ((res = droolsManager.testDroolsCompiling(versionTO))!=null){
			return res;
		}
		Version newVersion = this.mapToEntity(versionTO);
		session.save(newVersion);
		
		session.getTransaction().commit();
		session.close();
		
		return res;
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
		
		res.setActiveVersionId(activeConfig.getVersion().getId());
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

	private VersionTO getActiveVersion() {
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT * ");
		queryString.append("FROM ACTIVE_CONFIGURATION");

		Query query = session.createSQLQuery(queryString.toString()).addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration config = (ActiveConfiguration) query.uniqueResult();
		if (config==null){
			return null;
		}
		Version activeVersion = config.getVersion();
		System.out.println("versionNumber: "+activeVersion.getVersionNumber());
		
		session.getTransaction().commit();
		session.close();
		return mapToDTO(activeVersion);
	}
	
	
	private VersionTO getVersion(String version) {
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
		return mapToDTO(desiredVersion);
	}
	
	
	private List<String> getStringRules(VersionTO desiredVersion) {
		//Obtengo las reglas asociadas a la version
		List<RuleTO> rules = desiredVersion.getRules();
		List<String> stringRules = new ArrayList<String>();
		for(RuleTO rule : rules){
			stringRules.add(rule.getRule());
		}
		return stringRules;
	}
	
	
	private Version mapToEntity(VersionTO versionTO) {
		Version res = new Version();
		res.setCreationDate(new Date());
		res.setVersionNumber(versionTO.getVersionNumber());
		res.setRules(new HashSet<Rule>());
		for (RuleTO r : versionTO.getRules()) {
			res.getRules().add(mapToEntity(r,res));
		}
		return res;
	}



	private Rule mapToEntity(RuleTO r, Version v) {
		Rule res = new Rule();
		res.setName(r.getName());
		res.setRule(r.getRule());
		res.setVersion(v);
		return res;
	}
	
	private VersionTO mapToDTO(Version version) {
		VersionTO res = new VersionTO();
		res.setCreationDate(version.getCreationDate());
		res.setVersionNumber(version.getVersionNumber());
		res.setRules(new ArrayList<RuleTO>());
		for (Rule r : version.getRules()) {
			res.getRules().add(mapToDTO(r,res));
		}
		return res;
	}



	private RuleTO mapToDTO(Rule r, VersionTO v) {
		RuleTO res = new RuleTO();
		res.setName(r.getName());
		res.setRule(r.getRule());
		res.setId(r.getId());
		return res;
	}
	
	
	
	
}
