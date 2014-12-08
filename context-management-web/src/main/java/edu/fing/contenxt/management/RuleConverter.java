package edu.fing.contenxt.management;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import edu.fing.commons.front.dto.RuleTO;

@FacesConverter("ruleConverter")
public class RuleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				RulesBean bean = fc.getApplication().evaluateExpressionGet(fc, "#{rulesBean}", RulesBean.class);
				List<RuleTO> rules = bean.getSelectedVersion().getRules();
				if (rules != null) {
					for (RuleTO r : rules) {
						if (r.getName().equals(value)) {
							return r;
						}
					}
				}
				return null;
			} catch (NumberFormatException e) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid ruleName."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return ((RuleTO) object).getName();
		} else {
			return null;
		}
	}
}