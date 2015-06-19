package edu.fing.context.management.bean;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import edu.fing.commons.constant.AdaptationType;
import edu.fing.commons.dto.AdaptationTO;
import edu.fing.commons.front.dto.AdaptationTreeNodeTO;
import edu.fing.commons.front.dto.ItineraryTO;
import edu.fing.commons.front.dto.ServiceTO;
import edu.fing.commons.front.dto.SituationTO;
import edu.fing.context.management.dto.AdaptationDto;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;
import freemarker.template.Configuration;
import freemarker.template.Template;

@ManagedBean
@ViewScoped
public class ItineraryBean {

	private static final TransformerFactory tFactory = TransformerFactory.newInstance();
	private List<SituationTO> situationsForService = new ArrayList<SituationTO>();
	private AdaptationType adaptSelec;
	private AdaptationType adaptSelecCBR;
	private String description;
	private int priority;

	@SuppressWarnings("unchecked")
	private List<ServiceTO> serviceList = (List<ServiceTO>) RemoteInvokerUtils.invoke(
			RemoteInvokerUtils.ContextReasonerConfigService, "getServices", null, ServiceIp.ContextReasonerIp);

	private AdaptationType[] adaptationTypeList = AdaptationType.values();

	private AdaptationType[] adaptationTypeListCBR = { AdaptationType.DELAY, AdaptationType.ENRICH,
			AdaptationType.EXTERNAL_TRANSFORMATION, AdaptationType.FILTER, AdaptationType.SERVICE_INVOCATION };

	@SuppressWarnings("unchecked")
	private List<SituationTO> situationList = (List<SituationTO>) RemoteInvokerUtils.invoke(
			RemoteInvokerUtils.ContextReasonerConfigService, "getSituations", null, ServiceIp.ContextReasonerIp);

	private Long selectedService;

	private String selectedSituation;
	private List<UploadedFile> files = new LinkedList<UploadedFile>();

	private List<AdaptationDto> adaptations = new LinkedList<AdaptationDto>();

	private AdaptationDto selectedAdaptation;

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
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
		}
	}

	public void createCBR() {

		AdaptationTreeNodeTO adTN = (AdaptationTreeNodeTO) this.root.getData();
		adTN.setXpath(this.xpathParent);

		this.selectedAdaptation.setTree(adTN);
	}

	public void createFalseNode() {
		createNodo(false);
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
		Boolean result = (Boolean) RemoteInvokerUtils.invoke(RemoteInvokerUtils.ContextReasonerConfigService,
				"createItinerary", itinerary, ServiceIp.ContextReasonerIp);
		if (result) {
			String mensaje = "Itinerario creado con éxito";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(mensaje, null));
		}
		// servicio de context reasoner
		// (url servicio, situación, lista de adaptaciones con su data)
		return "inicio";
	}

	private void createNodo(boolean bool) {

		if (!validationMessages(this.xpathParent, AdaptationType.CONTENT_BASED_ROUTER, getSelectedSituationTO())) {
			RequestContext currentInstance = RequestContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "XPath nodo raíz inválido");
			currentInstance.showMessageInDialog(message);

			return;
		}

		String dataToValidate = this.xpath;
		if (this.nodeType.equals("ADAPTATION")) {
			dataToValidate = this.data;
		}

		if (selectedNode == null) {
			RequestContext currentInstance = RequestContext.getCurrentInstance();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Seleccione nodo");
			currentInstance.showMessageInDialog(message);
			return;
		}

		if (validationMessages(dataToValidate, AdaptationType.CONTENT_BASED_ROUTER, getSelectedSituationTO())) {

			AdaptationTreeNodeTO treeNode = new AdaptationTreeNodeTO();

			if (this.nodeType.equals("ADAPTATION")) {
				if (!validationMessages(this.data, adaptSelecCBR, getSelectedSituationTO())) {
					return;
				}
				AdaptationTO ad = new AdaptationTO();
				ad.setAdaptationType(this.adaptSelecCBR);
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
	}

	public void createTrueNode() {
		createNodo(true);
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

	private SituationTO getSelectedSituationTO() {
		SituationTO selectedStuationTO = null;
		for (SituationTO situationTO : this.situationList) {
			if (this.selectedSituation.equals(situationTO.getName())) {
				selectedStuationTO = situationTO;
				break;
			}

		}
		return selectedStuationTO;
	}

	public List<ServiceTO> getServiceList() {

		return this.serviceList;
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

	@SuppressWarnings("unchecked")
	public void onServiceChange() {

		if (this.selectedService != null) {

			this.situationsForService = (List<SituationTO>) RemoteInvokerUtils.invoke(
					RemoteInvokerUtils.ContextReasonerConfigService, "getSituationsWithPriority", this.selectedService,
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

	public void validate() {

		SituationTO selectedStuationTO = getSelectedSituationTO();

		String data = this.selectedAdaptation.getData();
		validationMessages(data, this.selectedAdaptation.getAdaptationType(), selectedStuationTO);

	}

	public List<String> validateFTL(String ftl, List<String> outputData) {
		List<String> res = new ArrayList<String>();
		Pattern p = Pattern.compile("\\$\\{(\\w*)\\}");
		Matcher m = p.matcher(ftl);
		int index = 1;
		while (m.find()) {
			String group = m.group(index);
			if (!outputData.contains(group)) {
				res.add("ERROR: \\'" + group + "\\' no es un dato contextual válido.");
			}
		}

		try {
			new Template("xslt", new StringReader(ftl), new Configuration());
		} catch (IOException e) {
			res.add("ERROR: El template no es válido");

			return res;
		}

		return res;
	}

	public List<String> validateXSLT(String xslt) {
		List<String> res = new ArrayList<String>();
		try {
			tFactory.newTemplates(new StreamSource(new StringReader(xslt)));
		} catch (TransformerConfigurationException e) {
			res.add("El XSLT no es válido: ");

			String[] split = e.getLocalizedMessage().split(";");
			for (int i = 0; i < split.length; i++) {
				res.add(split[i]);
			}

			return res;
		}
		return res;
	}

	private boolean validationMessages(String data, AdaptationType adapType, SituationTO selectedStuationTO) {

		List<String> validate = new ArrayList<String>();
		RequestContext currentInstance = RequestContext.getCurrentInstance();
		switch (adapType) {
		case ENRICH:

			validate = validateFTL(data, selectedStuationTO.getOutputContextData());
			validate.addAll(validateXSLT(data));
			if (validate.isEmpty()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "El template es válido");
				currentInstance.showMessageInDialog(message);

			} else {
				String mensajeconcatenado = StringUtils.join(validate, "<br/>");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el FTL",
						mensajeconcatenado);
				currentInstance.showMessageInDialog(message);
				return false;
			}

			break;
		case CONTENT_BASED_ROUTER:

			if (!validateCBRXPath(data)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el XPath",
						"El XPath no es válido");
				currentInstance.showMessageInDialog(message);
				return false;
			}
			break;
		case DELAY:
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(data);
			if (m.find()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "Número válido");
				currentInstance.showMessageInDialog(message);
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Número no válido");
				currentInstance.showMessageInDialog(message);
				return false;
			}

			break;
		case EXTERNAL_TRANSFORMATION:
			break;
		case FILTER:
			validate.addAll(validateXSLT(data));
			if (validate.isEmpty()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "El template es válido");
				currentInstance.showMessageInDialog(message);

			} else {
				String mensajeconcatenado = StringUtils.join(validate, "<br/>");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error en el FTL",
						mensajeconcatenado);

				currentInstance.showMessageInDialog(message);
				return false;
			}
			break;
		case SERVICE_INVOCATION:
			break;
		default:
			break;

		}
		return true;
	}

	private boolean validateCBRXPath(String data) {
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			xpath.compile(data);
			return true;

		} catch (XPathExpressionException e) {
			return false;
		}
	}

	private boolean validateCBRTree(AdaptationTreeNodeTO node, XPath xpath) {
		if (node == null)
			return true;

		if (node.getXpath() != null) {
			try {
				xpath.compile(node.getXpath());
				return this.validateCBRTree(node.getLeftNode(), xpath)
						&& this.validateCBRTree(node.getRightNode(), xpath);
			} catch (XPathExpressionException e) {
				return false;
			}

		} else {
			return true;
		}
	}
}
