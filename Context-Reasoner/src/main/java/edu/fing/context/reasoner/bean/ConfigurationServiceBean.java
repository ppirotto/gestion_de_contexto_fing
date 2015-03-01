package edu.fing.context.reasoner.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.constant.DataType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.reasoner.model.Adaptation;
import edu.fing.context.reasoner.model.AdaptationReference;
import edu.fing.context.reasoner.model.Service;
import edu.fing.context.reasoner.model.ServiceSituationPriority;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@org.switchyard.component.bean.Service(ConfigurationService.class)
public class ConfigurationServiceBean implements ConfigurationService {

	@Override
	public List<ServiceTO> getServicesWithSituationsAndAdaptations() {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

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

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
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

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

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
			situationTO.setMinuteDuration(situation.getMinuteDuration());
			list.add(situationTO);
		}
		return list;
	}

	@Override
	public Boolean createSituation(SituationTO situationTO) {

		Situation situation = new Situation();
		situation.setName(situationTO.getName());
		situation.setDescription(situationTO.getDescription());
		situation.setMinuteDuration(situationTO.getMinuteDuration());

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(situation);

		return HibernateUtils.commit(session);
	}

	@Override
	public Boolean createService(ServiceTO serviceTO) {

		Service service = new Service();
		service.setServiceName(serviceTO.getServiceName());
		service.setOperationName(serviceTO.getOperationName());
		service.setDescription(serviceTO.getDescription());
		service.setUrl(serviceTO.getUrl());

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(service);

		return HibernateUtils.commit(session);
	}

	@Override
	public Boolean createItinerary(ItineraryTO itineraryTO) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Service service = this.findServiceById(itineraryTO.getService().getId(), session);
		Situation situation = this.findSituationByName(itineraryTO.getSituationName(), session);

		for (AdaptationTO adaptationTO : itineraryTO.getAdaptations()) {
			Adaptation adaptation = new Adaptation();
			adaptation.setName(adaptationTO.getAdaptationType().name());
			adaptation.setAdaptationOrder(adaptationTO.getOrder());
			adaptation.setDescription(adaptationTO.getDescription());
			if (AdaptationType.SERVICE_INVOCATION.equals(adaptationTO.getAdaptationType())) {
				adaptation.setData(service.getUrl().getBytes());
			} else {
				adaptation.setData(this.getDataByType(adaptationTO.getData(), adaptationTO.getAdaptationType()));
			}
			adaptation.setService(service);
			adaptation.setSituation(situation);
			adaptation.setAdaptationReference(this.findAdaptationReferenceByAdaptationType(session, adaptationTO.getAdaptationType()));
			session.save(adaptation);
		}

		ServiceSituationPriority serviceSituationPriority = new ServiceSituationPriority();
		serviceSituationPriority.setPriority(itineraryTO.getPriority());
		serviceSituationPriority.setSituation(situation);
		service.getPriorities().add(serviceSituationPriority);
		session.save(serviceSituationPriority);
		session.save(service);

		return HibernateUtils.commit(session);
	}

	private byte[] getDataByType(Object adaptationData, AdaptationType adaptationType) {
		byte[] data = null;
		DataType dataType = adaptationType.getDataType();
		if (dataType != null) {
			switch (dataType) {
			case INT:
				data = BigInteger.valueOf((Integer) adaptationData).toByteArray();
				break;
			case STRING:
				data = ((String) adaptationData).getBytes();
				break;
			case FILE:
				data = (byte[]) adaptationData;
				break;
			default:
				break;
			}
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
		situationTO.setMinuteDuration(situation.getMinuteDuration());

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
