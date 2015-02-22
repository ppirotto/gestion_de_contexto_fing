package edu.fing.context.reasoner.bean;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.component.bean.Reference;

import edu.fing.commons.constant.DataType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.dto.ContextReasonerData;
import edu.fing.commons.dto.SituationDetectedTO;
import edu.fing.context.reasoner.model.Adaptation;
import edu.fing.context.reasoner.model.Service;
import edu.fing.context.reasoner.model.ServiceSituationPriority;
import edu.fing.context.reasoner.model.Situation;
import edu.fing.context.reasoner.util.HibernateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@org.switchyard.component.bean.Service(SituationReceiver.class)
public class SituationReceiverBean implements SituationReceiver {

	@Inject
	@Reference
	private AdaptationGatewayService adaptationGatewayService;

	@Override
	public String receiveSituationFromCEP(/* SituationDetectedTO cepSituation */) {

		SituationDetectedTO cepSituation = new SituationDetectedTO();
		cepSituation.setSituationName("InCityRaining");
		cepSituation.setUserId("Mati");
		Map<String, Object> contextualData = new HashMap<String, Object>();
		contextualData.put("city", "Montevideo");
		cepSituation.setContextualData(contextualData);

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();
		queryString.append("Select distinct service ");
		queryString.append("FROM Service service ");
		queryString.append("INNER JOIN service.adaptations adapts ");
		queryString.append("inner join adapts.situation sit ");
		queryString.append("WHERE sit.name = :situationName ");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", cepSituation.getSituationName());

		@SuppressWarnings("unchecked")
		List<Service> servicesAffectedBySituation = query.list();

		session.close();

		List<ContextReasonerData> list = new ArrayList<ContextReasonerData>();

		for (Service service : servicesAffectedBySituation) {

			ContextReasonerData contextReasonerData = new ContextReasonerData();
			contextReasonerData.setUser(cepSituation.getUserId());
			contextReasonerData.setSituationName(cepSituation.getSituationName());
			contextReasonerData.setService(service.getServiceName());
			contextReasonerData.setOperation(service.getOperationName());
			contextReasonerData.setServiceUrl(service.getUrl());

			int priority = this.findPriorityBySituationAndService(cepSituation.getSituationName(), service.getId());
			contextReasonerData.setPriority(priority);

			List<Adaptation> adaptationsBySituationAndService = this.findAdaptationsBySituationAndService(cepSituation.getSituationName(), service.getId());

			Iterator<Adaptation> iterator = adaptationsBySituationAndService.iterator();
			Situation situation = iterator.next().getSituation();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, situation.getMinuteDuration().intValue());
			contextReasonerData.setExpirationDate(cal.getTime());

			List<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
			for (Adaptation adaptation : adaptationsBySituationAndService) {
				AdaptationTO adaptationTO = new AdaptationTO();
				adaptationTO.setName(adaptation.getName());
				adaptationTO.setUri(adaptation.getAdaptationReference().getUri());
				adaptationTO.setData(this.getAdaptationDataByType(adaptation, cepSituation.getContextualData()));
				adaptations.add(adaptationTO);
			}
			contextReasonerData.setAdaptations(adaptations);
			list.add(contextReasonerData);
		}

		adaptationGatewayService.receiveAdaptations(list);

		return "OK";
	}

	private List<Adaptation> findAdaptationsBySituationAndService(String situationName, Long serviceId) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

		StringBuilder queryString = new StringBuilder();

		queryString.append("SELECT a ");
		queryString.append("FROM Adaptation a ");
		queryString.append("join a.service ser ");
		queryString.append("join a.situation sit ");
		queryString.append("WHERE sit.name = :situationName and ser.id = :serviceId");

		Query query = session.createQuery(queryString.toString());
		query.setParameter("situationName", situationName);
		query.setParameter("serviceId", serviceId);

		@SuppressWarnings("unchecked")
		List<Adaptation> adaptations = query.list();

		session.close();

		return adaptations;
	}

	private int findPriorityBySituationAndService(String situationName, Long serviceId) {

		SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
		Session session = sessionFactory.openSession();

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

		session.close();

		return priority.getPriority();

	}

	private Object getAdaptationDataByType(Adaptation adaptation, Map<String, Object> contextualData) {
		DataType dataType = adaptation.getAdaptationReference().getAdaptationType().getDataType();
		byte[] data = adaptation.getData();
		Object adaptationData = null;
		if (dataType != null) {
			switch (dataType) {
			case INT:
				adaptationData = new BigInteger(data).intValue();
				break;
			case STRING:
				adaptationData = new String(data);
				break;
			case FILE:
				adaptationData = this.applyTemplate(data, contextualData);
				break;
			default:
				break;
			}
		}

		return adaptationData;
	}

	private Object applyTemplate(byte[] data, Map<String, Object> contextualData) {

		String templateStr = new String(data);
		StringWriter outStr = new StringWriter();

		try {
			Template template = new Template("xslt", new StringReader(templateStr), new Configuration());

			Map<String, Object> input = new HashMap<String, Object>();
			Set<String> keySet = contextualData.keySet();
			for (String key : keySet) {
				input.put(key, contextualData.get(key));
			}

			Writer out = new OutputStreamWriter(System.out);
			template.process(input, out);
			out.flush();

			template.process(input, outStr);
			outStr.flush();
			System.out.println(outStr.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return outStr.toString();
	}

}
