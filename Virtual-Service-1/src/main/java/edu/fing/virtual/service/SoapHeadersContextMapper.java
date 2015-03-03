package edu.fing.virtual.service;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.switchyard.Context;
import org.switchyard.component.common.label.ComponentLabel;
import org.switchyard.component.common.label.EndpointLabel;
import org.switchyard.component.soap.composer.SOAPBindingData;
import org.switchyard.component.soap.composer.SOAPComposition;
import org.switchyard.component.soap.composer.SOAPContextMapper;
import org.switchyard.component.soap.composer.SOAPHeadersType;
import org.switchyard.config.ConfigurationPuller;

public class SoapHeadersContextMapper extends SOAPContextMapper {

	private static final String[] SOAP_HEADER_LABELS = new String[] { ComponentLabel.SOAP.label(), EndpointLabel.SOAP.label() };
	private static final String[] SOAP_MIME_LABELS = new String[] { ComponentLabel.SOAP.label(), EndpointLabel.HTTP.label() };

	@Override
	public void mapFrom(SOAPBindingData source, Context context) throws Exception {

		SOAPMessage soapMessage = source.getSOAPMessage();
		if (soapMessage.getSOAPBody().hasFault() && (source.getSOAPFaultInfo() != null)) {
			context.setProperty(SOAPComposition.SOAP_FAULT_INFO, source.getSOAPFaultInfo()).addLabels(SOAP_HEADER_LABELS);
		}
		if (source.getStatus() != null) {
			context.setProperty(HTTP_RESPONSE_STATUS, source.getStatus()).addLabels(SOAP_MIME_LABELS);
		}
		@SuppressWarnings("unchecked")
		Iterator<MimeHeader> mimeHeaders = soapMessage.getMimeHeaders().getAllHeaders();
		while (mimeHeaders.hasNext()) {
			MimeHeader mimeHeader = mimeHeaders.next();
			String name = mimeHeader.getName();
			if (matches(name)) {
				String value = mimeHeader.getValue();
				if (value != null) {
					context.setProperty(name, value).addLabels(SOAP_MIME_LABELS);
				}
			}
		}
		@SuppressWarnings("unchecked")
		Iterator<SOAPHeaderElement> soapHeaders = soapMessage.getSOAPHeader().examineAllHeaderElements();
		while (soapHeaders.hasNext()) {
			SOAPHeaderElement soapHeader = soapHeaders.next();
			QName qname = soapHeader.getElementQName();
			if (matches(qname)) {
				final Object value;
				switch (this.getSOAPHeadersType() != null ? this.getSOAPHeadersType() : SOAPHeadersType.VALUE) {
				case CONFIG:
					value = new ConfigurationPuller().pull(soapHeader);
					break;
				case DOM:
					value = soapHeader;
					break;
				case VALUE:
					value = soapHeader.getValue();
					break;
				case XML:
					value = new ConfigurationPuller().pull(soapHeader).toString();
					break;
				default:
					value = null;
				}
				if (value != null) {
					context.setProperty(qname.getPrefix(), value).addLabels(SOAP_HEADER_LABELS);
				}
			}
		}
	}
}
