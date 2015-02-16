package edu.fing.context.reasoner.bean;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@Service(ConfigurationService.class)
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

		edu.fing.context.reasoner.model.Service service = new edu.fing.context.reasoner.model.Service();
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
}
