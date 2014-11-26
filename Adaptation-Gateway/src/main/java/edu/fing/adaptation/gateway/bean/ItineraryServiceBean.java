package edu.fing.adaptation.gateway.bean;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.adaptation.gateway.util.HibernateUtils;

@Service(ItineraryService.class)
public class ItineraryServiceBean implements ItineraryService {

	@Override
	public void receiveAdaptations(List<String> contextReasonerData) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

		session.beginTransaction();

		StringBuilder queryString = new StringBuilder();

		// String hql = "INSERT INTO Employee(firstName, lastName, salary)" +
		// "SELECT firstName, lastName, salary FROM old_employee";
		// Query query = session.createQuery(hql);
		// int result = query.executeUpdate();
		//
		// queryString.append("INSERT INTO Itinerary ");
		// queryString.append("FROM SITUATION S JOIN ADAPTATION A ON A.SITUATION_ID = S.ID JOIN SERVICE SER ON a.SERVICE_ID = SER.ID ");
		// queryString.append("WHERE S.name = :situationName ");

		// Query query =
		// session.createSQLQuery(queryString.toString()).addEntity(Adaptation.class);
		// query.setParameter("situationName", "InCityRaining");
		//
		// @SuppressWarnings("unchecked")
		// List<Adaptation> adaptations = query.list();
		// String serviceName =
		// adaptations.get(0).getService().getServiceName();
		// System.out.println(serviceName);

		session.getTransaction().commit();
		session.close();
		sessionFactory.close();

	}

}
