package edu.fing.cep.engine.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.RuleTO;
import edu.fing.commons.front.dto.VersionTO;

@Service(DroolsCore.class)
public class DroolsCoreBean implements DroolsCore {

	private static KieSession kSession = null;
	private static KieServices kServices = null;
	
	
	private KieBaseConfiguration streamModeConfig;

	@Inject
	@Reference
	private ContextReasonerService contextReasonerService;

	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public void intializeDroolsContext() {
		if (kServices == null) {
			try {
				System.out.println("Initializing drools context...");
				// inicializo fields para Drools
				kServices = KieServices.Factory.get();
				streamModeConfig = kServices.newKieBaseConfiguration();
				streamModeConfig.setOption(EventProcessingOption.STREAM);

				// Obtengo version activa para deployar
				VersionTO activeVersion = contextReasonerService.getActiveVersion();

				// Creo el releaseId
				ReleaseId releaseId1 = kServices.newReleaseId(
						"edu.fing.cep.engine", "drools-context-rules",
						activeVersion.getVersionNumber());

				List<String> stringRules = getStringRules(activeVersion);

				FrontResponseTO testDroolsCompiling = this.testDroolsCompiling(activeVersion);
				KieModule kieModule = DroolsUtils.createAndDeployJar(kServices,
						releaseId1, stringRules);

				startSession(kieModule);
				System.out
						.println("Drools context successfully initialized!!!");
			} catch (Throwable t) {
				System.out.println("ERROR initializing drools context..");
				kServices = null;
				t.printStackTrace();
			}
		} else {
			System.out
					.println("Drools context already initialized. Discarding initialization...");
		}
	}

	private void startSession(KieModule kieModule) {
		// Create a session and fire rules
		KieContainer kContainer = kServices.newKieContainer(kieModule.getReleaseId());
		KieBase kBase = kContainer.newKieBase(streamModeConfig);
		try {
			lock.writeLock().lock();
			if (kSession != null) {
				kSession.halt();
				kSession.destroy();
			}
			kSession = kBase.newKieSession();
			new Thread(new Runnable() {
				@Override
				public void run() {
					kSession.fireUntilHalt();
				}
			}).start();
		} finally {
			lock.writeLock().unlock();
		}
	};

	@Override
	public String receiveMessage(ContextualDataTO data) {
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
	public FrontResponseTO deployVersion(VersionTO desiredVersion) {

		ReleaseId newReleaseId = kServices.newReleaseId("edu.fing.cep.engine",
				"drools-context-rules", desiredVersion.getVersionNumber());

		List<String> stringRules = getStringRules(desiredVersion);
		KieModule kieModule;
		try {
			kieModule = DroolsUtils.createAndDeployJar(kServices, newReleaseId,
					stringRules);
		} catch (DroolsCompilingException e) {
			return buildRulesVersionResponseTO(e);
		}

		KieContainer kContainer = kServices.newKieContainer(kieModule.getReleaseId());
		KieBase kBase = kContainer.newKieBase(streamModeConfig);

		startSession(kieModule);
		return buildRulesVersionResponseTO(null);
	}

	/**
	 * Verifica compilacion de las reglas, retorna null si compilan
	 * correctamente
	 * */
	@Override
	public FrontResponseTO testDroolsCompiling(VersionTO versionTO) {
		try {

			// VersionTO versionToTest = this.mapToEntity(versionTO);

			ReleaseId newReleaseId = kServices.newReleaseId(
					"edu.fing.cep.engine", "drools-context-rules",
					versionTO.getVersionNumber());

			List<String> stringRules = getStringRules(versionTO);
			KieModule kieModule = DroolsUtils.createAndDeployJar(kServices,
					newReleaseId, stringRules);

		} catch (DroolsCompilingException e) {
			return buildRulesVersionResponseTO(e);
		}
		return null;

	}

	private FrontResponseTO buildRulesVersionResponseTO(
			DroolsCompilingException e) {
		FrontResponseTO res = new FrontResponseTO();
		if (e == null) {
			res.setSuccess(true);
		} else {
			res.setErrorCode("Error de compilación");
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
