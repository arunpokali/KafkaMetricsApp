package com.fournotfive.app.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropUtils {
	private static String propFileName = "fournotfive.properties";

	public static Map<String, String> get405Props() {

		Map<String, String> props = new HashMap<String, String>();

		final Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propFileName);
			prop.load(input);
			prop.keySet().forEach(key -> props.put(key.toString(), prop.getProperty(key.toString())));
			return props;

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
