package edu.fing.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;




import org.switchyard.remote.RemoteInvoker;
import org.switchyard.remote.RemoteMessage;
import org.switchyard.remote.http.HttpInvoker;

import edu.fing.ContextReasonerData;
import edu.fing.model.Adaptation;
import edu.fing.util.HibernateUtils;

@Service(SituationReceiver.class)
public class SituationReceiverBean implements SituationReceiver {
	
	private static final QName SERVICE = new QName(
			"urn:edu.fing.switchyard:Adaptation-Gateway:1.0", "ItineraryService");

	
	@Override
	public String receiveSituationFromCEP(HashMap<String, Object> input) {

		System.out.println("input:" + input.toString());

		String userId = (String) input.get("userId");
		String situationName = (String) input.get("situationName");
		HashMap<String, String> contextualData = (HashMap<String, String>) input
				.get("contextualData");
		
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT A.* ");
		queryString.append("FROM SITUATION S JOIN ADAPTATION A ON A.SITUATION_ID = S.ID JOIN SERVICE ser ON (ser.service_id = a.id) ");
		queryString.append("WHERE S.name = :situationName ");

		Query query = session.createSQLQuery(queryString.toString());
		query.setParameter("situationName", "InCityRaining");

		@SuppressWarnings("unchecked")
		List<Adaptation> adaptations = query.list();
		
		session.getTransaction().commit();
	
		session.close();
		sessionFactory.close();
	
		return "OK";

		
	}

	@Override
	public String receiveSituationFromCEP2() {
		
		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		
	
		session.beginTransaction();
		
		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT A.* ");
		queryString.append("FROM SITUATION S JOIN ADAPTATION A ON A.SITUATION_ID = S.ID JOIN SERVICE SER ON a.SERVICE_ID = SER.ID ");
		queryString.append("WHERE S.name = :situationName ");

		Query query = session.createSQLQuery(queryString.toString()).addEntity(Adaptation.class);
		query.setParameter("situationName", "InCityRaining");

		@SuppressWarnings("unchecked")
		List<Adaptation> adaptations = query.list();
		String serviceName = adaptations.get(0).getService().getServiceName();
		System.out.println(serviceName);
		
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
	
		List<ContextReasonerData> list = new ArrayList<ContextReasonerData>();
		ContextReasonerData contextReasonerData = new ContextReasonerData();
		contextReasonerData.setUser("Mati");
		contextReasonerData.setService(serviceName);
		list.add(contextReasonerData);
		
		String port = System.getProperty(
				"org.switchyard.component.sca.client.port", "8080");
		RemoteInvoker invoker = new HttpInvoker("http://localhost:" + port + "/switchyard-remote");
		
		RemoteMessage message = new RemoteMessage();
		message.setService(SERVICE).setOperation("receiveAdaptations")
				.setContent(list);

		// Invoke the service
		RemoteMessage reply;
		try {
			reply = invoker.invoke(message);
			if (reply.isFault()) {
				System.err.println("Oops ... something bad happened.  "
						+ reply.getContent());
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return "OK";

	}
}
