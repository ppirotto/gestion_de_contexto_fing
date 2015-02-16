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
import edu.fing.context.reasoner.model.Service;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@org.switchyard.component.bean.Service(ConfigurationService.class)
public class ConfigurationServiceBean implements ConfigurationService {

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

		session.getTransaction().commit();
		session.close();

		return Boolean.TRUE;
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

		session.getTransaction().commit();
		session.close();

		return Boolean.TRUE;
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
			ServiceTO serviceTO = new ServiceTO();
			serviceTO.setId(service.getId());
			serviceTO.setServiceName(service.getServiceName());
			serviceTO.setOperationName(service.getOperationName());
			serviceTO.setDescription(service.getDescription());
			serviceTO.setUrl(service.getUrl());
			list.add(serviceTO);
		}
		return list;
	}

	@Override
	public List<SituationTO> getSituations() {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT s ");
		queryString.append("FROM Situation s ");

		Query query = session.createQuery(queryString.toString());

		@SuppressWarnings("unchecked")
		List<Situation> situations = query.list();

		session.getTransaction().commit();
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
	public Boolean createItinerary(ItineraryTO itineraryTO) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		for (AdaptationTO adaptationTO : itineraryTO.getAdaptations()) {
			Adaptation adaptation = new Adaptation();
			adaptation.setName(adaptationTO.getName());
			adaptation.setOrder(adaptationTO.getOrder());
			adaptation.setDescription(adaptationTO.getDescription());

			byte[] data = null;
			Object adaptationData = adaptationTO.getData();
			AdaptationType adaptationType = adaptationTO.getAdaptationType();
			DataType dataType = adaptationType.getDataType();
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
			adaptation.setData(data);

			// adaptation.setAdaptationReference(adaptationReference);
			// adaptation.setService(service);
			// adaptation.setSituation(situation);
			//
			session.save(adaptation);
		}

		session.getTransaction().commit();
		session.close();

		return Boolean.TRUE;

	}

}
