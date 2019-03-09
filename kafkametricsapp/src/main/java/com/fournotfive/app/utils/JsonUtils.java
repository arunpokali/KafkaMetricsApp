package com.fournotfive.app.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {
	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static String readFromJsonFile(String filePath) throws IOException {

		String json = new String(Files.readAllBytes(Paths.get(filePath)),
				StandardCharsets.UTF_8);
		return json;
	}
	public static <T> T jsonToObject(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(json, clazz);
	}
}
