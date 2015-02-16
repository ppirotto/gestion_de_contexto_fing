package edu.fing.context.reasoner.bean;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Service;

import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;

@Service(ConfigurationService.class)
public class ConfigurationServiceBean implements ConfigurationService {

	@Override
	public void createSituation(SituationTO situationTO) {

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
	}
}
