package edu.fing.cep.engine.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

import edu.fing.cep.engine.utils.DroolsCompilingException;
import edu.fing.cep.engine.utils.DroolsUtils;
import edu.fing.commons.dto.ContextualDataTO;
import edu.fing.commons.front.dto.CreateRulesVersionResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsManagerService.class)
public class DroolsManagerServiceBean implements DroolsManagerService {

	private static KieSession kSession;
	private static KieContainer kContainer;
	private static KieServices kServices;
	private KieBaseConfiguration streamModeConfig;

	@Inject
	@Reference
	private CEPService cepService;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@PostConstruct
	public void intializeDroolsContext() {
		try {
			// inicializo fields para Drools
			kServices = KieServices.Factory.get();
			streamModeConfig = kServices.newKieBaseConfiguration();
			streamModeConfig.setOption(EventProcessingOption.STREAM);

			// Obtengo version activa para deployar
			VersionTO activeVersion = cepService.getActiveVersion();

			// Creo el releaseId
			ReleaseId releaseId1 = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", activeVersion.getVersionNumber());

			List<String> stringRules = getStringRules(activeVersion);

			KieModule kieModule = DroolsUtils.createAndDeployJar(kServices, releaseId1, stringRules);

			startSession(kieModule);

		} catch (Throwable t) {
			t.printStackTrace();
		}
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
	public String insert(ContextualDataTO data) {
		try {
			lock.readLock().lock();
			kSession.insert(data);
			return "OK";
		} catch (Exception e) {
			return "ERROR";
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public CreateRulesVersionResponseTO deployVersion(VersionTO desiredVersion) {

		ReleaseId newReleaseId = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", desiredVersion.getVersionNumber());

		List<String> stringRules = getStringRules(desiredVersion);
		KieModule kieModule;
		try {
			kieModule = DroolsUtils.createAndDeployJar(kServices, newReleaseId, stringRules);
		} catch (DroolsCompilingException e) {
			return buildRulesVersionResponseTO(e);
		}

		kContainer = kServices.newKieContainer(kieModule.getReleaseId());
		KieBase kBase = kContainer.newKieBase(streamModeConfig);

		startSession(kieModule);
		return buildRulesVersionResponseTO(null);
	}

	/**
	 * Verifica compilacion de las reglas, retorna null si compilan
	 * correctamente
	 * */
	@Override
	public CreateRulesVersionResponseTO testDroolsCompiling(VersionTO versionTO) {
		try {

			// VersionTO versionToTest = this.mapToEntity(versionTO);

			ReleaseId newReleaseId = kServices.newReleaseId("edu.fing.cep.engine", "drools-context-rules", versionTO.getVersionNumber());

			List<String> stringRules = getStringRules(versionTO);
			KieModule kieModule = DroolsUtils.createAndDeployJar(kServices, newReleaseId, stringRules);

		} catch (DroolsCompilingException e) {
			return buildRulesVersionResponseTO(e);
		}
		return null;

	}

	private CreateRulesVersionResponseTO buildRulesVersionResponseTO(DroolsCompilingException e) {
		CreateRulesVersionResponseTO res = new CreateRulesVersionResponseTO();
		if (e == null) {
			res.setSuccess(true);
		} else {
			res.setErrorCode("COMPILING_ERROR");
			res.setSuccess(false);
			res.setErrorMessage(e.getMessage());
		}
		return res;
	}

	private List<String> getStringRules(VersionTO desiredVersion) {
		// Obtengo las reglas asociadas a la version
		List<RuleTO> rules = desiredVersion.getRules();
		List<String> stringRules = new ArrayList<String>();
		for (RuleTO rule : rules) {
			stringRules.add(rule.getDrl());
		}
		return stringRules;
	}
}
