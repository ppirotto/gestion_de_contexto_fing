package edu.fing.adaptation.gateway.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.adaptation.gateway.model.ContextAwareAdaptation;
import edu.fing.adaptation.gateway.model.Itinerary;
import edu.fing.adaptation.gateway.util.HibernateUtils;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.ContextReasonerData;
import edu.fing.commons.front.dto.ConfiguredItineraryTO;

@Service(ItineraryService.class)
public class ItineraryServiceBean implements ItineraryService {

	private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public void receiveAdaptations(List<ContextReasonerData> contextReasonerData) {
		this.getItineraries();

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		for (ContextReasonerData data : contextReasonerData) {
			Itinerary itinerary = this.findSameItinerary(data, session);
			if (itinerary == null) {
				itinerary = new Itinerary();
				itinerary.setUser(data.getUser());
				itinerary.setService(data.getService());
				itinerary.setOperation(data.getOperation());
				itinerary.setSituation(data.getSituationName());
				itinerary.setPriority(data.getPriority());
			} else {
				// borro las adaptaciones anteriores
				Set<ContextAwareAdaptation> adaptationDirective = itinerary.getAdaptationDirective();
				for (ContextAwareAdaptation contextAwareAdaptation : adaptationDirective) {
					session.delete(contextAwareAdaptation);
				}
				adaptationDirective.clear();
			}
			itinerary.setExpirationDate(data.getExpirationDate());
			session.save(itinerary);
			session.getTransaction().commit();
			session.beginTransaction();
			for (AdaptationTO adaptationTO : data.getAdaptations()) {
				ContextAwareAdaptation contextAwareAdaptation = new ContextAwareAdaptation();
				contextAwareAdaptation.setName(adaptationTO.getName());
				contextAwareAdaptation.setUri(adaptationTO.getUri());
				contextAwareAdaptation.setOrder(adaptationTO.getOrder());
				if (adaptationTO.getData() instanceof Integer) {
					contextAwareAdaptation.setData(Integer.toString((Integer) adaptationTO.getData()));
				} else {
					contextAwareAdaptation.setData((String) adaptationTO.getData());
				}
				itinerary.getAdaptationDirective().add(contextAwareAdaptation);
				session.save(contextAwareAdaptation);
			}
		}
		HibernateUtils.commit(session);

	}

	private Itinerary findSameItinerary(ContextReasonerData contextReasonerData, Session session) {

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select itinerary ");
		queryString.append("FROM Itinerary itinerary ");
		queryString.append("WHERE itinerary.user = :user ");
		queryString.append("and itinerary.service = :service ");
		queryString.append("and itinerary.operation = :operation ");
		queryString.append("and itinerary.situation = :situation ");
		queryString.append("and itinerary.priority = :priority ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("user", contextReasonerData.getUser());
		query.setParameter("service", contextReasonerData.getService());
		query.setParameter("operation", contextReasonerData.getOperation());
		query.setParameter("situation", contextReasonerData.getSituationName());
		query.setParameter("priority", contextReasonerData.getPriority());

		return (Itinerary) query.uniqueResult();
	}

	@Override
	public List<ConfiguredItineraryTO> getItineraries() {

		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select distinct itinerary ");
		queryString.append("FROM Itinerary itinerary ");
		queryString.append("join fetch itinerary.adaptationDirective ");
		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<Itinerary> itineraries = query.list();

		Map<Long, ConfiguredItineraryTO> configuredItineraryTOs = new HashMap<Long, ConfiguredItineraryTO>();
		for (Itinerary itinerary : itineraries) {
			if (!configuredItineraryTOs.containsKey(itinerary.getId())) {

				ConfiguredItineraryTO itineraryTO = new ConfiguredItineraryTO();
				itineraryTO.setUser(itinerary.getUser());
				itineraryTO.setService(itinerary.getService());
				itineraryTO.setOperation(itinerary.getOperation());
				itineraryTO.setSituation(itinerary.getSituation());
				itineraryTO.setPriority(itinerary.getPriority());
				itineraryTO.setExpirationDate(itinerary.getExpirationDate());

				List<AdaptationTO> adaptationDirective = new ArrayList<AdaptationTO>();
				Set<ContextAwareAdaptation> adaptationDirectiveSet = itinerary.getAdaptationDirective();
				List<ContextAwareAdaptation> adaptationDirectiveList = new ArrayList<ContextAwareAdaptation>(adaptationDirectiveSet);
				Collections.sort(adaptationDirectiveList, ContextAwareAdaptation.ORDER_COMPARATOR);
				List<String> adaptationNames = new ArrayList<String>();
				for (ContextAwareAdaptation contextAwareAdaptation : adaptationDirectiveList) {
					adaptationNames.add(contextAwareAdaptation.getName());
					AdaptationTO adaptationTO = new AdaptationTO();
					adaptationTO.setName(contextAwareAdaptation.getName());
					adaptationTO.setOrder(contextAwareAdaptation.getOrder());
					adaptationTO.setData(contextAwareAdaptation.getData());
					adaptationDirective.add(adaptationTO);
				}
				itineraryTO.setAdaptationNames(StringUtils.join(adaptationNames, " - "));
				itineraryTO.setAdaptationDirective(adaptationDirective);
				configuredItineraryTOs.put(itinerary.getId(), itineraryTO);
			}
		}
		return new ArrayList<ConfiguredItineraryTO>(configuredItineraryTOs.values());
	}
}
