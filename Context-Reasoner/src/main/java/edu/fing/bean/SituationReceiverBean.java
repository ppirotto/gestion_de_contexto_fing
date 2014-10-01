package edu.fing.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.switchyard.component.bean.Service;

import edu.fing.model.Adaptation;
import edu.fing.util.HibernateUtils;

@Service(SituationReceiver.class)
public class SituationReceiverBean implements SituationReceiver {

	@Override
	public String receiveSituationFromCEP(HashMap<String, Object> input) {
		
		System.out.println("input:" + input.toString());
		
		String userId = (String) input.get("userId");
		String situationName = (String) input.get("situationName");
		HashMap<String, String> contextualData = (HashMap<String, String>) input.get("contextualData");
		
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("select a ");
		queryString.append("from Situation sit join sit.adaptations a join a.service s ");
		queryString.append("where sit.name = :situationName");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("situationName", situationName);

        Session session = HibernateUtils.getSession();
        Query query = session.createQuery(queryString.toString());
        List<Adaptation> adaptations = query.list();
		return null;
	}
	
	public static void main(String[] args) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("select a ");
		queryString.append("from Situation sit join sit.adaptations a join a.service s ");
		queryString.append("where sit.name = :situationName");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("situationName", args[0]);

        Session session = HibernateUtils.getSession();
        Query query = session.createQuery(queryString.toString());
        List<Adaptation> adaptations = query.list();
	}
}
