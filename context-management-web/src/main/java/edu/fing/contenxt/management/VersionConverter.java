package edu.fing.contenxt.management;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import edu.fing.commons.front.dto.VersionTO;

@FacesConverter("versionConverter")
public class VersionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				RulesBean bean = fc.getApplication().evaluateExpressionGet(fc, "#{rulesBean}", RulesBean.class);
				List<VersionTO> versions = bean.getRes().getVersions();
				if (versions != null) {
					for (VersionTO v : versions) {
						if (v.getVersionNumber().equals(value)) {
							return v;
						}
					}
				}
				return null;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid version."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return ((VersionTO) object).getVersionNumber();
		} else {
			return null;
		}
	}
}