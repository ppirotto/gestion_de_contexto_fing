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

import edu.fing.commons.front.dto.AvailableRulesTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;
import edu.fing.context.reasoner.model.ActiveConfiguration;
import edu.fing.context.reasoner.model.Rule;
import edu.fing.context.reasoner.model.RuleVersion;
import edu.fing.context.reasoner.model.Version;
import edu.fing.context.reasoner.util.HibernateUtils;

@Service(CEPService.class)
public class CEPServiceBean implements CEPService {

	private final SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Inject
	@Reference
	private DroolsManagerService droolsManagerService;

	@Override
	public FrontResponseTO createNewVersion(VersionTO versionTO) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		FrontResponseTO res = null;
		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionTO.getVersionNumber());

		Version savedVersion = (Version) queryNewVersion.uniqueResult();

		if (savedVersion != null) {
			res = new FrontResponseTO();
			res.setErrorCode("Error");
			res.setSuccess(false);
			res.setErrorMessage("La versión '" + versionTO.getVersionNumber() + "' ya existe.");
			return res;
		} else {
			res = this.droolsManagerService.testDroolsCompiling(versionTO);
			if (res != null) {
				return res;
			}
		}
		res = new FrontResponseTO();
		res.setSuccess(true);
		Version newVersion = this.mapToVersion(versionTO, session);
		session.save(newVersion);
		updateLastVersion(newVersion, session);

		HibernateUtils.commit(session);

		return res;
	}

	@Override
	public AvailableRulesTO getAvailableRules() {
		AvailableRulesTO res = new AvailableRulesTO();

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query activeConfigurationQuery = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

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
		VersionTO res = new VersionTO();

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query activeConfigurationQuery = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		ActiveConfiguration activeConfig = (ActiveConfiguration) activeConfigurationQuery.uniqueResult();

		res = mapToVersionTO(activeConfig.getLastVersion());
		return res;
	}

	@Override
	public FrontResponseTO updateActiveVersion(String versionNumber) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		ActiveConfiguration activeConfig = (ActiveConfiguration) query.uniqueResult();

		Query queryNewVersion = session.createSQLQuery("SELECT * FROM VERSION WHERE VERSION_NUMBER = :version ").addEntity(Version.class);
		queryNewVersion.setParameter("version", versionNumber);

		Version newVersion = (Version) queryNewVersion.uniqueResult();

		VersionTO desiredVersion = mapToVersionTO(newVersion);
		FrontResponseTO deployResponseTO = this.droolsManagerService.deployVersion(desiredVersion);
		if (deployResponseTO.isSuccess()) {
			activeConfig.setActiveVersion(newVersion);
			System.out.println("Updating to versionNumber: " + activeConfig.getActiveVersion().getVersionNumber());
			session.update(activeConfig);
		}
		HibernateUtils.commit(session);
		return deployResponseTO;
	}

	private void updateLastVersion(Version version, Session session) {
		Query query = session.createSQLQuery("SELECT * FROM ACTIVE_CONFIGURATION").addEntity(ActiveConfiguration.class);

		ActiveConfiguration activeConfig = (ActiveConfiguration) query.uniqueResult();

		activeConfig.setLastVersion(version);
		System.out.println("Updating to lastVersion to versionNumber: " + activeConfig.getLastVersion().getVersionNumber());
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
		ActiveConfiguration config = (ActiveConfiguration) query.uniqueResult();
		if (config == null) {
			return null;
		}
		Version activeVersion = config.getActiveVersion();
		System.out.println("versionNumber: " + activeVersion.getVersionNumber());

		HibernateUtils.commit(session);
		return mapToVersionTO(activeVersion);
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

		Rule rule = (Rule) query.uniqueResult();

		HibernateUtils.commit(session);
		return rule;
	}

	private Version mapToVersion(VersionTO versionTO, Session session) {
		Version res = new Version();
		res.setCreationDate(new Date());
		res.setVersionNumber(versionTO.getVersionNumber());
		res.setRuleVersions(new HashSet<RuleVersion>());
		for (RuleTO r : versionTO.getRules()) {
			res.getRuleVersions().add(mapToRuleVersion(r, res, session));
		}
		return res;
	}

	private RuleVersion mapToRuleVersion(RuleTO r, Version version, Session session) {
		RuleVersion res = new RuleVersion();
		res.setVersion(version);
		res.setDrl(r.getDrl());
		res.setRule(mapToRule(r, version, session));
		return res;
	}

	private Rule mapToRule(RuleTO r, Version v, Session session) {
		// Busco regla por el nombre
		Rule res = getRule(r.getName());
		if (res == null) {// la regla es nueva, debo crearla
			res = new Rule();
			res.setName(r.getName());
			session.save(res);
		}
		return res;
	}

	private VersionTO mapToVersionTO(Version version) {
		VersionTO res = new VersionTO();
		res.setCreationDate(version.getCreationDate());
		res.setVersionNumber(version.getVersionNumber());
		res.setRules(new ArrayList<RuleTO>());
		for (RuleVersion r : version.getRuleVersions()) {
			res.getRules().add(mapToRuleTO(r, res));
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
