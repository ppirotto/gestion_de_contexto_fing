package edu.fing.contenxt.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.contenxt.management.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class SituationBean {

	private String description;
	private String name;
	private long duration;
	private String selectedContextSource;

	private List<String> contextSources;

	private List<String> contextDataList;
	private List<String> selectedInputDatum;
	private List<String> selectedOutputDatum;

	private TreeNode root = new DefaultTreeNode("Fuente de contexto", null);

	private Map<String, ContextSourceTO> mappedContextData = new HashMap<String, ContextSourceTO>();

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	public String crearSituacion() {
		System.out.println("Crear situación");

		SituationTO sit = new SituationTO();
		sit.setName(this.name);
		sit.setDescription(this.getDescription());
		sit.setDuration(this.duration);

		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createSituation", sit, ServiceIp.ContextReasonerIp);
		if (result) {
			String mensaje = "Servicio creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, mensaje));
			// servicio de context reasoner
			// (url servicio, situación, lista de adaptaciones con su data)

		}
		return "inicio";
	}

	public void createDatumForInput() {
		ContextSourceTO csTo = new ContextSourceTO();

		csTo.setEventName(this.selectedContextSource);
		csTo.setContextData(this.selectedInputDatum);

		if (this.getMappedContextData().get(this.selectedContextSource) == null) {
			this.getMappedContextData().put(this.selectedContextSource, csTo);

			// Solo para vista
			TreeNode node0 = new DefaultTreeNode(this.selectedContextSource, this.root);

			for (String data : this.selectedInputDatum) {
				TreeNode node01 = new DefaultTreeNode(data, node0);

			}
		}
	}

	public List<String> getContextDataList() {

		List<String> list = null;
		if (list != null) {
			this.contextDataList = list;

		} else {
			this.contextDataList = mocker();

		}
		return this.contextDataList;
	}

	public List<String> getContextSources() {

		// List<String> list = (List<String>)
		// RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService,
		// "getDatum", null, ServiceIp.ContextReasonerIp);
		this.contextSources = new ArrayList<String>();
		this.contextSources.add("InCity");

		return this.contextSources;
	}

	public String getDescripcion() {
		return this.getDescription();
	}

	public String getDescription() {
		return this.description;
	}

	public long getDuration() {
		return this.duration;
	}

	public Map<String, ContextSourceTO> getMappedContextData() {
		return this.mappedContextData;
	}

	public String getName() {
		return this.name;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public String getSelectedContextSource() {
		return this.selectedContextSource;
	}

	public List<String> getSelectedInputDatum() {
		return this.selectedInputDatum;
	}

	public List<String> getSelectedOutputDatum() {
		return this.selectedOutputDatum;
	}

	public SessionBean getSession() {
		return this.session;
	}

	private List<String> mocker() {
		List<String> list = new ArrayList<String>();
		list.add("city");
		list.add("latitud");
		list.add("longitud");
		list.add("user");
		list.add("city");
		list.add("latitud");
		list.add("longitud");
		list.add("user");
		list.add("city");
		list.add("latitud");
		list.add("longitud");
		list.add("user");
		return list;
	}

	public void setContextDataList(List<String> contextDataList) {
		this.contextDataList = contextDataList;
	}

	public void setContextSources(List<String> contextSources) {
		this.contextSources = contextSources;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setMappedContextData(Map<String, ContextSourceTO> mappedContextData) {
		this.mappedContextData = mappedContextData;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void setSelectedContextSource(String selectedContextSource) {
		this.selectedContextSource = selectedContextSource;
	}

	public void setSelectedInputDatum(List<String> selectedInputDatum) {
		this.selectedInputDatum = selectedInputDatum;
	}

	public void setSelectedOutputDatum(List<String> selectedOutputDatum) {
		this.selectedOutputDatum = selectedOutputDatum;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}
}
