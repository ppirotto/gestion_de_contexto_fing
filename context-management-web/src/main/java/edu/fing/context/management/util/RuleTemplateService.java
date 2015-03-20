package edu.fing.context.management.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.fing.commons.front.dto.RuleTemplateTO;
import edu.fing.context.management.jar.creation.ResourceAccessHelper;


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
		sourcePath += "/RuleTemplate.drl";
		String readFile = readFile(sourcePath, Charset.defaultCharset());
		rule = readFile;
		
		
		return rule;
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}
