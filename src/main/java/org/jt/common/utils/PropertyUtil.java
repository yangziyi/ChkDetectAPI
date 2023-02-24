package org.jt.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.log4j.Logger;

public class PropertyUtil {
	static Logger logger = Logger.getLogger(PropertyUtil.class);

	public static String getValue(String key) {
		String value = null;
		Properties p = new Properties();
		InputStream in = PropertyUtil.class
				.getResourceAsStream("/application.properties");
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		try {
			p.load(bf);
			value = p.getProperty(key);
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
		return value;
	}
}
