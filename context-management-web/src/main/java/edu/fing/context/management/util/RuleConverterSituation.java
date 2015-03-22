package edu.fing.context.management.util;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import edu.fing.commons.front.dto.RuleTO;
import edu.fing.context.management.bean.SituationBean;

@FacesConverter("ruleConverterSituation")
public class RuleConverterSituation implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				SituationBean bean = fc.getApplication().evaluateExpressionGet(fc, "#{situationBean}", SituationBean.class);
				List<RuleTO> rules = bean.getVersionRules().getRules();
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