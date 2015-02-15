package edu.fing.context.reasoner.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;

import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.ContextReasonerData;
import edu.fing.commons.dto.SituationDetectedTO;
import edu.fing.context.reasoner.model.Adaptation;
import edu.fing.context.reasoner.model.Service;
import edu.fing.context.reasoner.model.ServiceSituationPriority;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@org.switchyard.component.bean.Service(SituationReceiver.class)
public class SituationReceiverBean implements SituationReceiver {

	@Inject
	@Reference
	private AdaptationGatewayService adaptationGatewayService;

	@Override
	public String receiveSituationFromCEP(SituationDetectedTO cepSituation) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT ser ");
		queryString.append("FROM Service ser ");
		queryString.append("inner join fetch ser.adaptations a ");
		queryString.append("inner join fetch a.situation s ");
		queryString.append("WHERE s.name = :situationName ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", cepSituation.getSituationName());

		@SuppressWarnings("unchecked")
		List<Service> servicesAffectedBySituation = query.list();

		session.getTransaction().commit();
		session.close();

		List<ContextReasonerData> list = new ArrayList<ContextReasonerData>();

		for (Service service : servicesAffectedBySituation) {

			ContextReasonerData contextReasonerData = new ContextReasonerData();
			contextReasonerData.setUser(cepSituation.getUserId());
			contextReasonerData.setService(service.getServiceName());
			contextReasonerData.setOperation(service.getOperationName());
			contextReasonerData.setServiceUrl(service.getUrl());

			int priority = this.findPriorityForSituation(cepSituation.getSituationName(), service.getId());
			contextReasonerData.setPriority(priority);

			Iterator<Adaptation> iterator = service.getAdaptations().iterator();
			Situation situation = iterator.next().getSituation();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, situation.getMinuteDuration().intValue());
			contextReasonerData.setExpirationDate(cal.getTime());

			List<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
			for (Adaptation adaptation : service.getAdaptations()) {
				AdaptationTO adaptationTO = new AdaptationTO();
				adaptationTO.setName(adaptation.getName());
				adaptationTO.setUri(adaptation.getAdaptationReference().getUri());
				adaptationTO.setData(adaptation.getData());
				adaptations.add(adaptationTO);
			}

			list.add(contextReasonerData);
		}

		adaptationGatewayService.receiveAdaptations(list);

		return "OK";
	}

	private int findPriorityForSituation(String situationName, Long serviceId) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

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

		session.getTransaction().commit();
		session.close();

		return priority.getPriority();

	}

}
