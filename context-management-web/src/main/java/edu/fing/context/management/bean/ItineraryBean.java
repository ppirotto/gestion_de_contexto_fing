package edu.fing.context.management.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.AdaptationTreeNodeTO;
import edu.fing.commons.front.dto.FrontResponseTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.management.dto.AdaptationDto;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ItineraryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private FrontResponseTO responseMsg;

	private List<SituationTO> situationsForService = new ArrayList<SituationTO>();
	private AdaptationType adaptSelec;

	private AdaptationType adaptSelecCBR;
	private String description;
	private int priority;

	@SuppressWarnings("unchecked")
	private List<ServiceTO> serviceList = (List<ServiceTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getServices", null, ServiceIp.ContextReasonerIp);

	private AdaptationType[] adaptationTypeList = AdaptationType.values();

	private AdaptationType[] adaptationTypeListCBR = { AdaptationType.DELAY, AdaptationType.ENRICH, AdaptationType.EXTERNAL_TRANSFORMATION, AdaptationType.FILTER, AdaptationType.SERVICE_INVOCATION };

	@SuppressWarnings("unchecked")
	private List<SituationTO> situationList = (List<SituationTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getSituations", null, ServiceIp.ContextReasonerIp);

	private Long selectedService;

	private String selectedSituation;
	private List<UploadedFile> files = new LinkedList<UploadedFile>();

	private List<AdaptationDto> adaptations = new LinkedList<AdaptationDto>();

	private AdaptationDto selectedAdaptation;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean session;

	private TreeNode root;
	private TreeNode selectedNode;

	private String xpath;

	private String xpathParent;

	private String data;
	private String nodeType;

	public void agregarAdaptacion() {
		if (this.adaptSelec != null) {
			AdaptationDto a = new AdaptationDto(this.adaptations.size() + 1, this.adaptSelec, this.description);

			this.adaptations.add(a);

			this.description = null;

		} else {
			String mensaje = "Seleccione un tipo de adaptación";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
		}
	}

	private void crearNodo(boolean bool) {

		AdaptationTreeNodeTO treeNode = new AdaptationTreeNodeTO();

		if (this.nodeType.equals("ADAPTATION")) {
			AdaptationTO ad = new AdaptationTO();
			ad.setAdaptationType(this.adaptSelec);
			ad.setData(this.data);
			treeNode.setAdaptation(ad);
		} else {

			treeNode.setXpath(this.xpath);
		}

		if (bool) {
			treeNode.setNodeType("Si");
			AdaptationTreeNodeTO selATN = (AdaptationTreeNodeTO) this.selectedNode.getData();
			selATN.setLeftNode(treeNode);

			TreeNode node00 = new DefaultTreeNode(treeNode, this.selectedNode);
			node00.setExpanded(true);

		} else {
			treeNode.setNodeType("No");
			AdaptationTreeNodeTO selATN = (AdaptationTreeNodeTO) this.selectedNode.getData();
			selATN.setRightNode(treeNode);
			TreeNode node00 = new DefaultTreeNode(treeNode, this.selectedNode);
			node00.setExpanded(true);
		}

	}

	public void createCBR() {

		AdaptationTreeNodeTO adTN = (AdaptationTreeNodeTO) this.root.getData();
		adTN.setXpath(this.xpathParent);

		this.selectedAdaptation.setTree(adTN);
	}

	public void createFalseNode() {
		crearNodo(false);
	}

	public String createItinerary() {
		System.out.println("Crear itinerario");

		ItineraryTO itinerary = new ItineraryTO();

		itinerary.setPriority(this.priority);
		List<AdaptationTO> list = new ArrayList<AdaptationTO>();
		for (AdaptationDto adapt : this.adaptations) {
			AdaptationTO a = new AdaptationTO();
			a.setAdaptationType(adapt.getAdaptationType());
			a.setOrder(adapt.getId());
			a.setDescription(adapt.getDescription());
			if (adapt.getAdaptationType().equals(AdaptationType.CONTENT_BASED_ROUTER)) {
				a.setData(adapt.getTree());
			} else {
				a.setData(adapt.getData());
			}

			list.add(a);
		}

		itinerary.setAdaptations(list);
		itinerary.setSituationName(this.getSelectedSituation());
		ServiceTO serviceTO = new ServiceTO();
		serviceTO.setId(this.selectedService);
		itinerary.setService(serviceTO);
		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "createItinerary", itinerary, ServiceIp.ContextReasonerIp);
		if (result) {

			String mensaje = "Itinerario creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));
		}
		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	public void createTrueNode() {
		crearNodo(true);
	}

	@PostConstruct
	void generateTree() {
		AdaptationTreeNodeTO treeNode = new AdaptationTreeNodeTO();

		this.root = new DefaultTreeNode(treeNode, null);
		this.root.setExpanded(true);
		;

	}

	public List<AdaptationDto> getAdaptations() {
		return this.adaptations;
	}

	public AdaptationType[] getAdaptationTypeList() {
		return this.adaptationTypeList;
	}

	public AdaptationType[] getAdaptationTypeListCBR() {
		return this.adaptationTypeListCBR;
	}

	public AdaptationType getAdaptSelec() {
		return this.adaptSelec;
	}

	public AdaptationType getAdaptSelecCBR() {
		return this.adaptSelecCBR;
	}

	public String getData() {
		return this.data;
	}

	public String getDescription() {
		return this.description;
	}

	public List<UploadedFile> getFiles() {
		return this.files;
	}

	public String getNodeType() {
		return this.nodeType;
	}

	public int getPriority() {
		return this.priority;
	}

	public FrontResponseTO getResponseMsg() {
		return this.responseMsg;
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public AdaptationDto getSelectedAdaptation() {
		return this.selectedAdaptation;
	}

	public TreeNode getSelectedNode() {
		return this.selectedNode;
	}

	public Long getSelectedService() {
		return this.selectedService;
	}

	public String getSelectedSituation() {
		return this.selectedSituation;
	}

	public List<ServiceTO> getServiceList() {

		return this.serviceList;
	}

	public SessionBean getSession() {
		return this.session;
	}

	public List<SituationTO> getSituationList() {

		return this.situationList;
	}

	public List<SituationTO> getSituationsForService() {
		return this.situationsForService;
	}

	public String getXpath() {
		return this.xpath;
	}

	public String getXpathParent() {
		return this.xpathParent;
	}

	public void onServiceChange() {

		if (this.selectedService != null) {

			this.situationsForService = (List<SituationTO>) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService, "getSituationsWithPriority", this.selectedService,
					ServiceIp.ContextReasonerIp);
		}

	}

	public boolean renderAdap() {

		return "ADAPTATION".equals(this.nodeType);
	}

	public void setAdaptations(List<AdaptationDto> adaptations) {
		this.adaptations = adaptations;
	}

	public void setAdaptationTypeList(AdaptationType[] adaptationTypeList) {
		this.adaptationTypeList = adaptationTypeList;
	}

	public void setAdaptationTypeListCBR(AdaptationType[] adaptationTypeListCBR) {
		this.adaptationTypeListCBR = adaptationTypeListCBR;
	}

	public void setAdaptSelec(AdaptationType adaptSelec) {
		this.adaptSelec = adaptSelec;
	}

	public void setAdaptSelecCBR(AdaptationType adaptSelecCBR) {
		this.adaptSelecCBR = adaptSelecCBR;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setResponseMsg(FrontResponseTO responseMsg) {
		this.responseMsg = responseMsg;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void setSelectedAdaptation(AdaptationDto selectedAdaptation) {
		this.selectedAdaptation = selectedAdaptation;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void setSelectedService(Long selectedService) {
		this.selectedService = selectedService;
	}

	public void setSelectedSituation(String selectedSituation) {
		this.selectedSituation = selectedSituation;
	}

	public void setServiceList(List<ServiceTO> serviceList) {
		this.serviceList = serviceList;
	}

	public void setSession(SessionBean session) {
		this.session = session;
	}

	public void setSituationList(List<SituationTO> situationList) {
		this.situationList = situationList;
	}

	public void setSituationsForService(List<SituationTO> situationsForService) {
		this.situationsForService = situationsForService;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public void setXpathParent(String xpathParent) {
		this.xpathParent = xpathParent;
	}

	public void upload() {
		System.out.println(this.getFiles().size());
		FacesMessage message = null;
		if (!this.adaptations.isEmpty()) {
			message = new FacesMessage("Succesful", " is uploaded.");

		} else {
			message = new FacesMessage("Error", " is uploaded.");
		}

	}

	public void validarEnrich() {

		SituationTO selectedStuationTO = null;
		for (SituationTO situationTO : this.situationList) {
			if (this.selectedSituation.equals(situationTO.getName())) {
				selectedStuationTO = situationTO;
				break;
			}

		}
		List<String> validate = validate(this.selectedAdaptation.getData(), selectedStuationTO.getOutputContextData());
		this.setResponseMsg(new FrontResponseTO());

		if (validate.isEmpty()) {
			this.getResponseMsg().setSuccess(true);
			this.getResponseMsg().setErrorCode("OK");
		} else {
			this.getResponseMsg().setSuccess(false);
			this.getResponseMsg().setErrorCode("Error en el FTL");
			this.getResponseMsg().setErrorMessage(StringUtils.join(validate, "\n"));
		}

	}

	private List<String> validate(String ftl, List<String> outputData) {
		List<String> res = new ArrayList<String>();
		// verifico outputs
		for (String output : outputData) {// para cada input
			if (!ftl.contains("${" + output + "}")) {
				res.add("WARNING: Output '" + output + "' parece no estar utilizándose.");
			}
		}
		return res;
	}
}
