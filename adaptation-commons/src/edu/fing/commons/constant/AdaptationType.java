package edu.fing.commons.constant;

public enum AdaptationType {

	DELAY("switchyard://DelayService"), 
	FILTER("switchyard://EnrichService"), 
	ENRICH("switchyard://EnrichService"), 
	EXTERNAL_TRANSFORMATION("switchyard://ExternalInvocationService"),
	CONTENT_BASED_ROUTER("switchyard://ContentBasedRouterService"), 
	SERVICE_INVOCATION("switchyard://ExternalInvocationService");

	private String uri;

	private AdaptationType(String uri) {
		this.setUri(uri);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		switch(this){
		case CONTENT_BASED_ROUTER:
			return "Content Based Router";
		case DELAY:
			return "Delay";
		case ENRICH:
			return "Enrich";
		case EXTERNAL_TRANSFORMATION:
			return "External Transformation";
		case FILTER:
			return "Filter";
		case SERVICE_INVOCATION:
			return "Service Invocation";
		default:
			break;
		
		}
		return super.toString();
	}
}
