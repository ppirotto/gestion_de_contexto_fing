package edu.fing.context.management.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.fing.commons.front.dto.ContextSourceTO;
import edu.fing.commons.front.dto.RuleTemplateTO;
import edu.fing.context.management.jar.creation.ResourceAccessHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class RuleTemplateService {

	private static String applyTemplate(RuleTemplateTO ruleTemplateTO,
			String strFile) {

		StringWriter outStr = new StringWriter();
		try {
			Template template = new Template("xslt", new StringReader(strFile),
					new Configuration());

			Map<String, Object> input = new HashMap<String, Object>();
			input.put("rule", ruleTemplateTO);

			Writer out = new OutputStreamWriter(System.out);
			template.process(input, out);
			out.flush();

			template.process(input, outStr);
			outStr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return outStr.toString();
	}

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		int bytesRead;
		byte[] buffer = new byte[4 * 1024 * 1024];
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static String createRuleTemplate(RuleTemplateTO ruleTempTO)
			throws IOException {
		String rule = "";
		String sourcePath = ResourceAccessHelper.getWebInfPath();
		sourcePath += "/GenericRuleTemplate.ftl";
		String strFile = readFile(sourcePath, Charset.defaultCharset());
		rule = applyTemplate(ruleTempTO, strFile);

		return rule;
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static List<String> validate(String drl, RuleTemplateTO ruleTempTO) {
		List<String> res = new ArrayList<String>();

		// verifico inputs
		for (ContextSourceTO elem : ruleTempTO.getMappedContextData()) {// recorro
																		// fuentes
																		// de
																		// contexto
																		// seleccionadas
																		// como
																		// inputs
			for (String input : elem.getContextData()) {// para cada input
				String camelInput = input.substring(0, 1).toUpperCase()
						+ input.substring(1);
				Pattern p = Pattern.compile(".*(\\.get\\(\")*" + input
						+ "\"\\)");
				Matcher m = p.matcher(drl);
				if (!m.find()) {
					res.add("WARNING: Input '" + input
							+ "' para la fuente de contexto '"
							+ elem.getEventName()
							+ "' parece no estar utilizándose.");
				}
			}
		}

		// verifico outputs
		for (String output : ruleTempTO.getSelectedOutputData()) {// para cada
																	// input
			Pattern p = Pattern.compile("contextualData\\.put\\(\"" + output
					+ "\",.*\\);");
			Matcher m = p.matcher(drl);
			if (!m.find()) {
				res.add("WARNING: Output '" + output
						+ "' parece no estar utilizándose.");
			}
		}
		return res;
	}

}
