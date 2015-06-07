package edu.fing.adaptation.gateway.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.switchyard.Context;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

	@Inject
	private Context context;

	private final SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public String setItinerary(String message) {

		AdaptedMessage adaptedMessage = new AdaptedMessage();
		adaptedMessage.setMessage(message);
		String serviceName = this.context.getPropertyValue("serviceName");
		String serviceUrl = this.context.getPropertyValue("serviceUrl");
		adaptedMessage.setService(serviceUrl);
		String operationName = this.getOperationName(message);
		String userName = this.getWSSecurityUsername();
		String itineraryUris = null;
		if (userName != null && operationName != null) {
			Itinerary itinerary = this.findItineraryByUserAndService(userName, serviceName, operationName);
			if (itinerary != null) {
				ArrayList<AdaptationTO> adaptations = new ArrayList<AdaptationTO>();
				Set<ContextAwareAdaptation> adaptationDirective = itinerary.getAdaptationDirective();
				List<ContextAwareAdaptation> adaptationDirectiveList = new ArrayList<ContextAwareAdaptation>(adaptationDirective);
				Collections.sort(adaptationDirectiveList, ContextAwareAdaptation.ORDER_COMPARATOR);
				List<String> adaptationUris = new ArrayList<String>();
				for (ContextAwareAdaptation contextAwareAdaptation : adaptationDirectiveList) {
					adaptationUris.add(contextAwareAdaptation.getAdaptationType().getUri());
					AdaptationTO adapt = new AdaptationTO();
					adapt.setData(contextAwareAdaptation.getData());
					adaptations.add(adapt);
				}
				itineraryUris = StringUtils.join(adaptationUris, ",");
				adaptedMessage.setAdaptations(adaptations);
			} else {
				itineraryUris = "switchyard://ExternalInvocationService";
			}
		} else {
			itineraryUris = "switchyard://ExternalInvocationService";
		}
		adaptedMessage.setItinerary(itineraryUris);

		return this.adaptationManager.submit(adaptedMessage);
	}

	public String getWSSecurityUsername() {

		String securityHeader = this.context.getPropertyValue("wsse");
		Pattern p = Pattern.compile("<wsse:Username>(\\w*?)</wsse:Username>");
		Matcher m = p.matcher(securityHeader);

		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	private String getOperationName(String message) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(new ByteArrayInputStream(message.getBytes()));
			return dom.getDocumentElement().getNodeName();

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
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
				// Sino esta vencido el itinerario
				if (itinerary.getExpirationDate().after(new Date())) {
					int priority = itinerary.getPriority();
					if (priority < maxPriority) {
						maxPriority = priority;
						itineraryMaxPriority = itinerary;
					}
				}
			}
		}
		if (itineraryMaxPriority != null) {
			StringBuilder queryStr = new StringBuilder();
			queryStr.append("Select i ");
			queryStr.append("from Itinerary i ");
			queryStr.append("join fetch i.adaptationDirective ");
			queryStr.append("WHERE i.id = :itineraryId ");

			Query q = session.createQuery(queryStr.toString());
			q.setParameter("itineraryId", itineraryMaxPriority.getId());

			itineraryMaxPriority = (Itinerary) q.uniqueResult();
		}
		return itineraryMaxPriority;
	}
}
