package edu.fing.adaptation.gateway.bean;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.adaptation.gateway.model.ContextAwareAdaptation;
import edu.fing.adaptation.gateway.model.Itinerary;
import edu.fing.adaptation.gateway.util.HibernateUtils;

@Service(CleanItineraryService.class)
public class CleanItineraryServiceBean implements CleanItineraryService {

	private final SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public void deleteInactiveItineraries() {

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select distinct itinerary ");
		queryString.append("FROM Itinerary itinerary ");
		queryString.append("WHERE itinerary.expirationDate < sysdate() ");

		Query query = session.createQuery(queryString.toString());
		@SuppressWarnings("unchecked")
		List<Itinerary> list = query.list();

		for (Itinerary itinerary : list) {

			Set<ContextAwareAdaptation> adaptationDirective = itinerary.getAdaptationDirective();

			// StringBuilder queryStr = new StringBuilder();
			// queryStr.append("Select ContextAwareAdaptation c ");
			// queryStr.append("FROM Itinerary i ");
			// queryStr.append("JOIN fetch i.adaptationDirective c ");
			// queryStr.append("WHERE i.id = :itineraryId ");
			//
			// Query queryAdaptation = session.createQuery(queryStr.toString());
			// queryAdaptation.setParameter("itineraryId", itinerary.getId());
			//
			// @SuppressWarnings("unchecked")
			// Set<ContextAwareAdaptation> adaptationDirective =
			// (Set<ContextAwareAdaptation>) queryAdaptation.list();

			for (ContextAwareAdaptation contextAwareAdaptation : adaptationDirective) {
				session.delete(contextAwareAdaptation);
			}
			session.delete(itinerary);
		}

		HibernateUtils.commit(session);
	}

}
