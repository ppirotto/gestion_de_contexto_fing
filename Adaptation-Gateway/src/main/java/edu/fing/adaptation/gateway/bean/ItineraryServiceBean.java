package edu.fing.adaptation.gateway.bean;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.adaptation.gateway.model.ContextAwareAdaptation;
import edu.fing.adaptation.gateway.model.Itinerary;
import edu.fing.adaptation.gateway.util.HibernateUtils;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.ContextReasonerData;

@Service(ItineraryService.class)
public class ItineraryServiceBean implements ItineraryService {

	private SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public void receiveAdaptations(List<ContextReasonerData> contextReasonerData) {

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
				itinerary.getAdaptationDirective().clear();
			}
			itinerary.setExpirationDate(data.getExpirationDate());
			session.save(itinerary);

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
}
