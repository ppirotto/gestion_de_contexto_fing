package edu.fing.context.management.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.fing.commons.front.dto.ConfiguredItineraryTO;
import edu.fing.context.management.util.RemoteInvokerUtils;
import edu.fing.context.management.util.RemoteInvokerUtils.ServiceIp;

@ManagedBean
@ViewScoped
public class ViewItineraryBean {
	private ConfiguredItineraryTO selectedItinerary;
	private List<ConfiguredItineraryTO> itineraryList = (List<ConfiguredItineraryTO>) RemoteInvokerUtils
			.invoke(RemoteInvokerUtils.AdaptationGatewayConfigService,
					"getItineraries", null, ServiceIp.AdaptationGatewayIp);

	private List<ConfiguredItineraryTO> filteredItineraryList;

	public List<ConfiguredItineraryTO> getFilteredItineraryList() {
		return this.filteredItineraryList;
	}

	public List<ConfiguredItineraryTO> getItineraryList() {
		return this.itineraryList;
	}

	public ConfiguredItineraryTO getSelectedItinerary() {

		return this.selectedItinerary;
	}

	public void setFilteredItineraryList(
			List<ConfiguredItineraryTO> filteredItineraryList) {
		this.filteredItineraryList = filteredItineraryList;
	}

	public void setItineraryList(List<ConfiguredItineraryTO> itineraryList) {
		this.itineraryList = itineraryList;
	}

	public void setSelectedItinerary(ConfiguredItineraryTO selectedItinerary) {
		this.selectedItinerary = selectedItinerary;
	}

}
