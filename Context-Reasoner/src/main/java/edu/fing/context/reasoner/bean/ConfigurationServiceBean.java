package edu.fing.context.reasoner.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.reasoner.model.Adaptation;
import edu.fing.context.reasoner.model.AdaptationReference;
import edu.fing.context.reasoner.model.ContextDatum;
import edu.fing.context.reasoner.model.ContextSource;
import edu.fing.context.reasoner.model.Rule;
import edu.fing.context.reasoner.model.Service;
import edu.fing.context.reasoner.model.ServiceSituationPriority;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@org.switchyard.component.bean.Service(ConfigurationService.class)
public class ConfigurationServiceBean implements ConfigurationService {

	private final SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Inject
	@Reference
	private CEPService cepService;

	@Override
	public List<ServiceTO> getServicesWithSituationsAndAdaptations() {

		Session session = this.sessionFactory.openSession();

		StringBuilder queryStr = new StringBuilder();
		queryStr.append("SELECT ser ");
		queryStr.append("FROM Service ser ");

		Query query = session.createQuery(queryStr.toString());

		@SuppressWarnings("unchecked")
		List<Service> services = query.list();

		List<ServiceTO> serviceList = new ArrayList<ServiceTO>();

		for (Service service : services) {

			ServiceTO serviceTO = this.mapService(service);
			List<SituationTO> situationList = new ArrayList<SituationTO>();

			List<Situation> situations = this.findSituationsByServiceId(service.getId(), session);

			for (Situation situation : situations) {

				SituationTO situationTO = this.mapSituation(situation);
				List<AdaptationTO> adaptationList = new ArrayList<AdaptationTO>();
				List<Adaptation> adaptations = this.findAdaptationsBySituationAndService(situation.getName(), service.getId(), session);

				for (Adaptation adaptation : adaptations) {
					adaptationList.add(this.mapAdaptation(adaptation));
				}
				situationTO.setAdaptations(adaptationList);
				situationList.add(situationTO);
			}
			serviceTO.setSituations(situationList);
			serviceList.add(serviceTO);
		}
		return serviceList;
	}

	@Override
	public List<ServiceTO> getServices() {

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT ser ");
		queryString.append("FROM Service ser ");

		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<Service> services = query.list();

		session.getTransaction().commit();
		session.close();

		List<ServiceTO> list = new ArrayList<ServiceTO>();
		for (Service service : services) {
			list.add(this.mapService(service));
		}
		return list;
	}

	@Override
	public List<SituationTO> getSituations() {

		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT s ");
		queryString.append("FROM Situation s ");

		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<Situation> situations = query.list();

		session.close();

		List<SituationTO> list = new ArrayList<SituationTO>();
		for (Situation situation : situations) {
			SituationTO situationTO = new SituationTO();
			situationTO.setName(situation.getName());
			situationTO.setDescription(situation.getDescription());
			situationTO.setDuration(situation.getDuration());
			list.add(situationTO);
		}
		return list;
	}

	@Override
	public List<String> getContextData() {
		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("FROM ContextDatum ");

		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<ContextDatum> contextData = query.list();

		session.close();

		List<String> list = new ArrayList<String>();
		for (ContextDatum contextDatum : contextData) {
			list.add(contextDatum.getName());
		}
		return list;
	}

	@Override
	public List<String> getContextDataBySituation(String situationName) {
		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT ContextDatum c ");
		queryString.append("FROM Situation sit ");
		queryString.append("fetch join sit.outputContextData c ");
		queryString.append("where sit.name = :situationName ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", situationName);

		@SuppressWarnings("unchecked")
		List<ContextDatum> contextData = query.list();

		session.close();

		List<String> list = new ArrayList<String>();
		if (contextData != null) {
			for (ContextDatum contextDatum : contextData) {
				list.add(contextDatum.getName());
			}
		}
		return list;
	}

	@Override
	public List<String> getContextSources() {
		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("FROM ContextSource ");

		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<ContextSource> contextSources = query.list();

		session.close();

		List<String> list = new ArrayList<String>();
		for (ContextSource contextSource : contextSources) {
			list.add(contextSource.getName());
		}
		return list;
	}

	@Override
	public FrontResponseTO createSituation(SituationTO situationTO) {

		FrontResponseTO response = this.cepService.createNewVersion(situationTO.getVersionTO());
		if (response.isSuccess()) {

			Session session = this.sessionFactory.openSession();
			session.beginTransaction();

			Situation situation = new Situation();
			situation.setName(situationTO.getName());
			situation.setDescription(situationTO.getDescription());
			situation.setDuration(situationTO.getDuration());

			Set<ContextDatum> inputContextData = new HashSet<ContextDatum>();
			List<ContextSourceTO> contextSources = situationTO.getContextSources();
			for (ContextSourceTO contextSourceTO : contextSources) {
				ContextSource contextSource = this.findContextSourceByName(contextSourceTO.getEventName(), session);
				List<String> contextData = contextSourceTO.getContextData();
				for (String contextDatumName : contextData) {
					ContextDatum contextDatum = this.findContextDatumByName(contextDatumName, session);
					contextSource.getContextData().add(contextDatum);
					inputContextData.add(contextDatum);
				}
			}
			situation.setInputContextData(inputContextData);

			Set<ContextDatum> outputContextData = new HashSet<ContextDatum>();
			List<String> contextData = situationTO.getOutputContextData();
			for (String contextDatumName : contextData) {
				ContextDatum contextDatum = this.findContextDatumByName(contextDatumName, session);
				outputContextData.add(contextDatum);
			}
			situation.setOutputContextData(outputContextData);
			situation.setRule(this.findRuleByName(situationTO.getName(), session));

			session.save(situation);
			boolean result = HibernateUtils.commit(session);
			response.setSuccess(result);
			if (!result) {
				response.setErrorCode("ERROR_PROCESING_DATA");
				response.setErrorMessage("Error procesando los datos");
			}
		}
		return response;
	}

	@Override
	public Boolean createService(ServiceTO serviceTO) {

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		List<String> operationNames = serviceTO.getOperationNames();
		for (String operationName : operationNames) {
			Service service = new Service();
			service.setServiceName(serviceTO.getServiceName());
			service.setOperationName(operationName);
			service.setDescription(serviceTO.getDescription());
			service.setUrl(serviceTO.getUrl());
			session.save(service);
		}

		return HibernateUtils.commit(session);
	}

	@Override
	public Boolean createContextDatum(String contextDatumName) {

		ContextDatum contextDatum = new ContextDatum();
		contextDatum.setName(contextDatumName);

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();
		session.save(contextDatum);

		return HibernateUtils.commit(session);
	}

	@Override
	public Boolean createContextSource(ContextSourceTO contextSourceTO) {

		ContextSource contextSource = new ContextSource();
		contextSource.setName(contextSourceTO.getEventName());
		contextSource.setDescription(contextSourceTO.getDescription());

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String properties = objectMapper.writeValueAsString(contextSourceTO);
			contextSource.setProperties(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Session session = this.sessionFactory.openSession();
		session.beginTransaction();
		session.save(contextSource);

		return HibernateUtils.commit(session);
	}

	@Override
	public FrontResponseTO createItinerary(ItineraryTO itineraryTO) {

		FrontResponseTO frontResponseTO = new FrontResponseTO();
		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		Service service = this.findServiceById(itineraryTO.getService().getId(), session);
		Situation situation = this.findSituationByName(itineraryTO.getSituationName(), session);
		boolean exists = this.existsPriotityForServiceAndSituation(itineraryTO.getSituationName(), itineraryTO.getService().getId(), session);
		if (exists) {
			frontResponseTO.setSuccess(false);
			frontResponseTO.setErrorCode("ITINERARY_ALREADY_EXISTS");
			frontResponseTO.setErrorMessage("El itinerario ya existe para la situación y servicio seleccionados");
			return frontResponseTO;
		}

		for (AdaptationTO adaptationTO : itineraryTO.getAdaptations()) {
			Adaptation adaptation = new Adaptation();
			adaptation.setName(adaptationTO.getAdaptationType().name());
			adaptation.setAdaptationOrder(adaptationTO.getOrder());
			adaptation.setDescription(adaptationTO.getDescription());
			adaptation.setService(service);
			adaptation.setSituation(situation);
			adaptation.setAdaptationReference(this.findAdaptationReferenceByAdaptationType(session, adaptationTO.getAdaptationType()));
			adaptation.setData(this.getDataByAdaptationType(adaptationTO.getData(), adaptationTO.getAdaptationType(), service.getUrl()));
			session.save(adaptation);
		}

		ServiceSituationPriority serviceSituationPriority = new ServiceSituationPriority();
		serviceSituationPriority.setPriority(itineraryTO.getPriority());
		serviceSituationPriority.setSituation(situation);
		service.getPriorities().add(serviceSituationPriority);
		session.save(serviceSituationPriority);
		session.save(service);

		boolean success = HibernateUtils.commit(session);
		frontResponseTO.setSuccess(success);
		if (!success) {
			frontResponseTO.setErrorCode("ERROR_PROCESING_DATA");
			frontResponseTO.setErrorMessage("Error procesando los datos");
		}
		return frontResponseTO;
	}

	private String getDataByAdaptationType(Object adaptationData, AdaptationType adaptationType, String serviceUrl) {
		String data = null;
		switch (adaptationType) {
		case CONTENT_BASED_ROUTER:
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				data = objectMapper.writeValueAsString(adaptationData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case DELAY:
		case ENRICH:
		case EXTERNAL_TRANSFORMATION:
		case FILTER:
			data = (String) adaptationData;
			break;
		case SERVICE_INVOCATION:
			data = serviceUrl;
			break;
		default:
			break;
		}
		return data;
	}

	private AdaptationReference findAdaptationReferenceByAdaptationType(Session session, AdaptationType adaptationType) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT ar ");
		queryString.append("FROM AdaptationReference ar ");
		queryString.append("WHERE ar.adaptationType = :adaptationType ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("adaptationType", adaptationType.name());

		return (AdaptationReference) query.uniqueResult();
	}

	private Situation findSituationByName(String situationName, Session session) {
		StringBuilder queryStringSituation = new StringBuilder();
		queryStringSituation.append("SELECT sit ");
		queryStringSituation.append("FROM Situation sit ");
		queryStringSituation.append("WHERE sit.name = :situationName ");

		Query querySituation = session.createQuery(queryStringSituation.toString());
		querySituation.setParameter("situationName", situationName);

		return (Situation) querySituation.uniqueResult();
	}

	private Service findServiceById(Long serviceId, Session session) {

		StringBuilder queryStringService = new StringBuilder();
		queryStringService.append("SELECT ser ");
		queryStringService.append("FROM Service ser ");
		queryStringService.append("WHERE ser.id = :serviceId ");

		Query queryService = session.createQuery(queryStringService.toString());
		queryService.setParameter("serviceId", serviceId);

		return (Service) queryService.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	private List<Situation> findSituationsByServiceId(Long serviceId, Session session) {

		StringBuilder querySituation = new StringBuilder();
		querySituation.append("SELECT distinct sit ");
		querySituation.append("FROM Situation sit ");
		querySituation.append("JOIN sit.adaptations adapts ");
		querySituation.append("WHERE adapts.service.id = :serviceId ");

		Query querySit = session.createQuery(querySituation.toString());
		querySit.setParameter("serviceId", serviceId);

		return querySit.list();
	}

	@SuppressWarnings("unchecked")
	private List<Adaptation> findAdaptationsBySituationAndService(String situationName, Long serviceId, Session session) {

		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT a ");
		queryString.append("FROM Adaptation a ");
		queryString.append("join a.service ser ");
		queryString.append("join a.situation sit ");
		queryString.append("WHERE sit.name = :situationName and ser.id = :serviceId");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", situationName);
		query.setParameter("serviceId", serviceId);

		return query.list();

	}

	private boolean existsPriotityForServiceAndSituation(String situationName, Long serviceId, Session session) {

		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT ssp ");
		queryString.append("FROM Service ser ");
		queryString.append("join ser.priorities ssp ");
		queryString.append("join ssp.situation sit ");
		queryString.append("WHERE sit.name = :situationName and ser.id = :serviceId");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", situationName);
		query.setParameter("serviceId", serviceId);

		ServiceSituationPriority priority = (ServiceSituationPriority) query.uniqueResult();

		if (priority != null) {
			return true;
		} else {
			return false;
		}
	}

	private ContextSource findContextSourceByName(String name, Session session) {

		StringBuilder queryStringService = new StringBuilder();
		queryStringService.append("SELECT cs ");
		queryStringService.append("FROM ContextSource cs ");
		queryStringService.append("left join fetch cs.contextData ");
		queryStringService.append("WHERE cs.name = :name ");

		Query queryService = session.createQuery(queryStringService.toString());
		queryService.setParameter("name", name);

		return (ContextSource) queryService.uniqueResult();
	}

	private ContextDatum findContextDatumByName(String name, Session session) {

		StringBuilder queryStringService = new StringBuilder();
		queryStringService.append("SELECT cd ");
		queryStringService.append("FROM ContextDatum cd ");
		queryStringService.append("WHERE cd.name = :name ");

		Query queryService = session.createQuery(queryStringService.toString());
		queryService.setParameter("name", name);

		return (ContextDatum) queryService.uniqueResult();
	}

	private Rule findRuleByName(String name, Session session) {

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT rule ");
		queryString.append("FROM Rule rule ");
		queryString.append("WHERE rule.name = :name ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("name", name);

		return (Rule) query.uniqueResult();
	}

	private ServiceTO mapService(Service service) {
		ServiceTO serviceTO = new ServiceTO();
		serviceTO.setId(service.getId());
		serviceTO.setServiceName(service.getServiceName());
		serviceTO.setOperationName(service.getOperationName());
		serviceTO.setDescription(service.getDescription());
		serviceTO.setUrl(service.getUrl());

		return serviceTO;
	}

	private SituationTO mapSituation(Situation situation) {
		SituationTO situationTO = new SituationTO();
		situationTO.setName(situation.getName());
		situationTO.setDescription(situation.getDescription());
		situationTO.setDuration(situation.getDuration());

		return situationTO;
	}

	private AdaptationTO mapAdaptation(Adaptation adaptation) {
		AdaptationTO adaptationTO = new AdaptationTO();
		adaptationTO.setName(adaptation.getName());
		adaptationTO.setDescription(adaptation.getDescription());
		adaptationTO.setOrder(adaptation.getAdaptationOrder());
		adaptationTO.setUri(adaptation.getAdaptationReference().getUri());
		if (adaptation.getData() != null) {
			adaptationTO.setData(new String(adaptation.getData()));
		}

		return adaptationTO;
	}

}
