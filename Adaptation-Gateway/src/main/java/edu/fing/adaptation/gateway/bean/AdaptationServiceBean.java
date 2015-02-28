package edu.fing.adaptation.gateway.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

import edu.fing.adaptation.gateway.model.ContextAwareAdaptation;
import edu.fing.adaptation.gateway.model.Itinerary;
import edu.fing.adaptation.gateway.util.HibernateUtils;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.AdaptedMessage;

@Service(AdaptationService.class)
public class AdaptationServiceBean implements AdaptationService {

	@Inject
	@Reference
	private AdaptationManager adaptationManager;

	private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public String setItinerary(String message) {

		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage(message);

		String itineraryUris = null;
		Itinerary itinerary = this.findItineraryByUserAndService("Mati", "AttractionsService", "getAttractions");
		if (itinerary != null) {
			ArrayList<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
			Set<ContextAwareAdaptation> adaptationDirective = itinerary.getAdaptationDirective();
			List<ContextAwareAdaptation> adaptationDirectiveList = new ArrayList<ContextAwareAdaptation>(adaptationDirective);
			Collections.sort(adaptationDirectiveList, ContextAwareAdaptation.ORDER_COMPARATOR);
			List<String> adaptationUris = new ArrayList<String>();
			for (ContextAwareAdaptation contextAwareAdaptation : adaptationDirectiveList) {
				adaptationUris.add(contextAwareAdaptation.getUri());
				AdaptationTO adapt = new AdaptationTO();
				adapt.setName(adapt.getName());
				adapt.setData(adapt.getData());
				adaptations.add(adapt);
			}
			itineraryUris = StringUtils.join(adaptationUris, ",");
			adaptedMessage.setAdaptations(adaptations);

		} else {
			itineraryUris = "switchyard://ExternalInvocationService";
		}

		adaptedMessage.setItinerary(itineraryUris);

		adaptedMessage.setService("http://localhost:8080/attractions-provider/AttractionsService");

		return this.adaptationManager.submit(adaptedMessage);
	}

	private Itinerary findItineraryByUserAndService(String user, String service, String operation) {
		Session session = this.sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select itinerary ");
		queryString.append("FROM Itinerary itinerary ");
		queryString.append("WHERE itinerary.user = :user ");
		queryString.append("and itinerary.service = :service ");
		queryString.append("and itinerary.operation = :operation ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("user", user);
		query.setParameter("service", service);
		query.setParameter("operation", operation);

		@SuppressWarnings("unchecked")
		List<Itinerary> itineraries = query.list();
		Itinerary itineraryMaxPriority = null;
		if (itineraries != null) {
			int maxPriority = Integer.MAX_VALUE;
			for (Itinerary itinerary : itineraries) {
				int priority = itinerary.getPriority();
				if (priority < maxPriority) {
					maxPriority = priority;
					itineraryMaxPriority = itinerary;
				}

			}
		}
		if (itineraryMaxPriority != null) {
			StringBuilder queryStr = new StringBuilder();
			queryStr.append("Select i ");
			queryStr.append("from Itinerary i ");
			queryStr.append("join fetch i.contextAwareAdaptation ");
			queryStr.append("WHERE i.id = :itineraryId ");

			Query q = session.createQuery(queryStr.toString());
			q.setParameter("itineraryId", itineraryMaxPriority.getId());

			itineraryMaxPriority = (Itinerary) q.uniqueResult();
		}
		return itineraryMaxPriority;
	}
}
