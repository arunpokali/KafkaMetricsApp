package com.fournotfive.autoproducerconsumer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConstantsUtil {
	private static String propFileName = "producer_consumer.properties";

	public static String getString(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propFileName);
			prop.load(input);
			return prop.getProperty(key);
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

	public static Integer getInt(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propFileName);
			prop.load(input);
			return Integer.parseInt(prop.getProperty(key));
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
