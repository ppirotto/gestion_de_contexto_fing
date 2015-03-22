package edu.fing.context.management.util;

import java.io.File;
import java.io.FileReader;
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
import java.util.HashMap;
import java.util.Map;

import edu.fing.commons.front.dto.RuleTemplateTO;
import edu.fing.context.management.jar.creation.ResourceAccessHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class RuleTemplateService {

	public static void copy(InputStream input, OutputStream output) throws IOException {
		int bytesRead;
		byte[] buffer = new byte[4 * 1024 * 1024];
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	
	public static String createRuleTemplate(RuleTemplateTO ruleTempTO) throws IOException {
		String rule="";
		String sourcePath = ResourceAccessHelper.getWebInfPath();
		sourcePath += "/GenericRuleTemplate.ftl";
		String strFile = readFile(sourcePath, Charset.defaultCharset());
		rule = applyTemplate(ruleTempTO, strFile);
		
		
		return rule;
	}
	
	private static String applyTemplate(RuleTemplateTO ruleTemplateTO, String strFile) {
		
		StringWriter outStr = new StringWriter();
		try {
			Template template = new Template("xslt", new StringReader(strFile), new Configuration());

			Map<String, Object> input = new HashMap<String, Object>();
			input.put("rule", ruleTemplateTO);

			Writer out = new OutputStreamWriter(System.out);
			template.process(input, out);
			out.flush();

			template.process(input, outStr);
			outStr.flush();
			System.out.println(outStr.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}

		return outStr.toString();
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
