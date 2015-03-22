package edu.fing.context.reasoner.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import com.google.common.cache.RemovalCause;

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;
import edu.fing.context.reasoner.model.ActiveConfiguration;
import edu.fing.context.reasoner.model.Rule;
import edu.fing.context.reasoner.model.RuleVersion;
import edu.fing.context.reasoner.model.Version;
import edu.fing.context.reasoner.util.HibernateUtils;
import edu.fing.context.reasoner.util.RemoteInvokerUtils;
import edu.fing.context.reasoner.util.RemoteInvokerUtils.ServiceIp;

@Service(CEPService.class)
public class CEPServiceBean implements CEPService {

	
	private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Override
	public FrontResponseTO createNewVersion(VersionTO versionTO) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		FrontResponseTO res = null;
		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionTO.getVersionNumber());
		
		@SuppressWarnings("unchecked")
		Version savedVersion = (Version) queryNewVersion.uniqueResult();
		
		if (savedVersion!=null){
			res = new FrontResponseTO();
			res.setErrorCode("VERSION_ALREADY_EXISTS");
			res.setSuccess(false);
			res.setErrorMessage("The version with versionNumber = '"+versionTO.getVersionNumber()+"' already exists.");
			return res;
		} else {
			res = (FrontResponseTO)RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsManagerService, "testDroolsCompiling", versionTO, ServiceIp.CepEngineIP);
			if (res != null){
				return res;
			}
		}
		res = new FrontResponseTO();
		res.setSuccess(true);
		Version newVersion = this.mapToVersion(versionTO);
		session.save(newVersion);	
		updateLastVersion(newVersion,session);
		
		HibernateUtils.commit(session);
		
		return res;
	}
	
	
	@Override
	public AvailableRulesTO getAvailableRules() {
		// TODO Auto-generated method stub
		AvailableRulesTO res = new AvailableRulesTO();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query activeConfigurationQuery = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) activeConfigurationQuery.uniqueResult();
		
		res.setActiveVersionNumber(activeConfig.getActiveVersion().getVersionNumber());
		res.setLastVersionNumber(activeConfig.getLastVersion().getVersionNumber());
		res.setLastDeployDate(activeConfig.getLastDeployDate());
		List<VersionTO> versions = new ArrayList<VersionTO>();
		res.setVersions(versions);
		
		Query allVersionsQuery = session.createSQLQuery("SELECT * FROM VERSION").addEntity(Version.class);
		
		@SuppressWarnings("unchecked")
		List<Version> allVersions = allVersionsQuery.list();
		
		for (Version version : allVersions) {
			VersionTO newVersionTO = new VersionTO();
			newVersionTO.setCreationDate(version.getCreationDate());
			newVersionTO.setVersionNumber(version.getVersionNumber());
			List<RuleTO> rulesTO = new ArrayList<RuleTO>();
			for (RuleVersion rV : version.getRuleVersions()) {
				RuleTO ruleTO = new RuleTO();
				ruleTO.setName(rV.getRule().getName());
				ruleTO.setDrl(rV.getDrl());
				rulesTO.add(ruleTO);
			}
			newVersionTO.setRules(rulesTO);
			res.getVersions().add(newVersionTO);
		}

		return res;
	}

	
	@Override
	public VersionTO getLastVersion() {
		// TODO Auto-generated method stub
		VersionTO res = new VersionTO();
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query activeConfigurationQuery = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) activeConfigurationQuery.uniqueResult();
		
		res = mapToVersionTO(activeConfig.getLastVersion());		
		return res;
	}
	
	
	@Override
	public FrontResponseTO updateActiveVersion(String versionNumber) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) query.uniqueResult();
		
		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionNumber);
		
		@SuppressWarnings("unchecked")
		Version newVersion = (Version) queryNewVersion.uniqueResult();
		
		VersionTO desiredVersion = mapToVersionTO(newVersion);
		FrontResponseTO deployResponseTO = (FrontResponseTO)RemoteInvokerUtils.invoke(RemoteInvokerUtils.DroolsManagerService, "deployVersion", desiredVersion, ServiceIp.CepEngineIP);
		System.out.println("droolsManager.deployVersion(desiredVersion) response:"+deployResponseTO.toString());
		if (deployResponseTO.isSuccess()){
			activeConfig.setActiveVersion(newVersion);
			System.out.println("Updating to versionNumber: "+activeConfig.getActiveVersion().getVersionNumber());
			session.update(activeConfig);
		}
		HibernateUtils.commit(session);
		return deployResponseTO;
	}

	
	private void updateLastVersion(Version version,Session session) {
		Query query = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		@SuppressWarnings("unchecked")
		ActiveConfiguration activeConfig = (ActiveConfiguration) query.uniqueResult();
		
		activeConfig.setLastVersion(version);
		System.out.println("Updating to lastVersion to versionNumber: "+activeConfig.getLastVersion().getVersionNumber());
		session.update(activeConfig);
	}
	
	@Override
	public VersionTO getActiveVersion() {
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
		Version activeVersion = config.getActiveVersion();
		System.out.println("versionNumber: "+activeVersion.getVersionNumber());
		
		HibernateUtils.commit(session);
		return mapToVersionTO(activeVersion);
	}
	
	
	private VersionTO getVersion(String version) {
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
		
		HibernateUtils.commit(session);
		return mapToVersionTO(desiredVersion);
	}
	
	private Rule getRule(String ruleName) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT * ");
		queryString.append("FROM RULE ");
		queryString.append("WHERE NAME = :ruleName");

		Query query = session.createSQLQuery(queryString.toString()).addEntity(Rule.class);
		query.setParameter("ruleName", ruleName);

		@SuppressWarnings("unchecked")
		Rule rule = (Rule) query.uniqueResult();
				
		HibernateUtils.commit(session);
		return rule;
	}
	
	private List<String> getStringRules(VersionTO desiredVersion) {
		List<RuleTO> rules = desiredVersion.getRules();
		List<String> stringRules = new ArrayList<String>();
		for(RuleTO rule : rules){
			stringRules.add(rule.getDrl());
		}
		return stringRules;
	}
	
	
	private Version mapToVersion(VersionTO versionTO) {
		Version res = new Version();
		res.setCreationDate(new Date());
		res.setVersionNumber(versionTO.getVersionNumber());
		res.setRuleVersions(new HashSet<RuleVersion>());
		for (RuleTO r : versionTO.getRules()) {
			res.getRuleVersions().add(mapToRuleVersion(r, res));
		}		
		return res;
	}



	private RuleVersion mapToRuleVersion(RuleTO r, Version version) {
		RuleVersion res = new RuleVersion();
		res.setVersion(version);
		res.setDrl(r.getDrl());
		res.setRule(mapToRule(r, version));
		return res;
	}


	private Rule mapToRule(RuleTO r, Version v) {
		//Busco regla por el nombre
		Rule res = getRule(r.getName());
		if (res==null){//la regla es nueva, debo crearla
			res = new Rule();
			res.setName(r.getName());
		}
		return res;
	}
	
	private VersionTO mapToVersionTO(Version version) {
		VersionTO res = new VersionTO();
		res.setCreationDate(version.getCreationDate());
		res.setVersionNumber(version.getVersionNumber());
		res.setRules(new ArrayList<RuleTO>());
		for (RuleVersion r : version.getRuleVersions()) {
			res.getRules().add(mapToRuleTO(r,res));
		}
		return res;
	}



	private RuleTO mapToRuleTO(RuleVersion r, VersionTO v) {
		RuleTO res = new RuleTO();
		res.setName(r.getRule().getName());
		res.setDrl(r.getDrl());
		return res;
	}
	
	
	
	
}
