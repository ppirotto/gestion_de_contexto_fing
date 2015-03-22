package edu.fing.context.management.jar.creation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.SystemUtils;

public class ResourceAccessHelper {
	private static final String WEB_INF_DIR_NAME = "WEB-INF";
	private static String web_inf_path = null;

	public static String getWebInfPath() throws UnsupportedEncodingException {

		web_inf_path = URLDecoder.decode(ResourceAccessHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF8");
		web_inf_path = web_inf_path.substring(0, web_inf_path.lastIndexOf(WEB_INF_DIR_NAME) + WEB_INF_DIR_NAME.length());

		if (SystemUtils.IS_OS_WINDOWS) {
			web_inf_path = web_inf_path.substring(1);
		}
		return web_inf_path;
	}
}
