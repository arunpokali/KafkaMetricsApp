package com.fournotfive.app.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.netty.util.internal.StringUtil;

import com.fournotfive.app.ConfigBean;
import com.fournotfive.app.constants.I405Constants;

public class ConfigUtils {
	public static ConfigBean getConfigBean() throws JsonParseException, JsonMappingException, IOException {
		String path = StringUtils.defaultIfBlank(System.getenv(I405Constants.FOURNOTFIVE_ENV_HOME),
				StringUtils.defaultIfBlank(I405Constants.FOURNOTFIVE_LOCAL, StringUtils.EMPTY));
		String filePath=path+File.separator+I405Constants.FOURNOTFIVE_JSON;
		return JsonUtils.jsonToObject(JsonUtils.readFromJsonFile(filePath), ConfigBean.class);
		
	}
}
