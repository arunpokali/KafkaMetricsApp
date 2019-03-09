package com.fournotfive.app.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.fournotfive.app.ConfigBean;

public class KafkaHttpMetricsReporterUtil {
	// metrics
	public static String totalInit;
	public static String totalUsed;
	public static String totalMax;
	public static String totalCommitted;
	public static String heapInit;
	public static String heapUsed;
	public static String heapMax;
	public static String heapCommitted;
	public static String heap_usage;
	public static String non_heap_usage;
	// public static String LogEndOffset;
	// public static String LogStartOffset;
	// public static String NumLogSegments;
	// public static String Size;

	public static String getMetricsJson(String restUrl) throws IOException {
		//String restUrl = cb.getHttpreporter();
		// String restUrl="http://localhost:8080/api/metrics";
		String json = Jsoup.connect(restUrl).ignoreContentType(true).execute().body();
		return json;
	}

	public static Object getValueFromJsonKey(String JsonData, String Key) {
		String[] keys = Key.split(",");
		JSONObject subJson = new JSONObject(JsonData);
		JSONObject val = null;
		for (String k : keys) {
			JSONObject obj = subJson;
			Object result = obj.get(k);
			if (result instanceof JSONObject) {
				val = new JSONObject(obj.get(k).toString());
				subJson = val;
			} else {
				return obj.get(k);
			}
		}
		return (Object) val;
	}

	public static Map<String, String> assignMetricValues(String restUrl) throws IOException {
		String json = KafkaHttpMetricsReporterUtil.getMetricsJson(restUrl);
		HashMap<String, String> hmap = new HashMap<String, String>();

		// totalInit
		String input1 = "jvm,memory,totalInit";
		totalInit = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input1).toString();
		hmap.put(input1.replace(',', '.'), totalInit);
		// System.out.println(totalUtil);

		// totalUsed
		String input2 = "jvm,memory,totalUsed";
		totalUsed = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input2).toString();
		hmap.put(input2.replace(',', '.'), totalUsed);
		// System.out.println(totalUsed);

		// totalMax
		String input3 = "jvm,memory,totalMax";
		totalMax = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input3).toString();
		hmap.put(input3.replace(',', '.'), totalMax);
		// System.out.println(totalMax);

		// totalCommitted
		String input4 = "jvm,memory,totalCommitted";
		totalCommitted = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input4).toString();
		hmap.put(input4.replace(',', '.'), totalCommitted);
		// System.out.println(totalCommitted);

		// heapInit
		String input5 = "jvm,memory,heapInit";
		heapInit = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input5).toString();
		hmap.put(input5.replace(',', '.'), heapInit);
		// System.out.println(heapInit);

		// heapUsed
		String input6 = "jvm,memory,heapUsed";
		heapUsed = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input6).toString();
		hmap.put(input6.replace(',', '.'), heapUsed);
		// System.out.println(heapUsed);

		// heapMax
		String input7 = "jvm,memory,heapMax";
		heapMax = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input7).toString();
		hmap.put(input7.replace(',', '.'), heapMax);
		// System.out.println(heapMax);

		// heapCommitted
		String input8 = "jvm,memory,heapCommitted";
		heapCommitted = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input8).toString();
		hmap.put(input8.replace(',', '.'), heapCommitted);
		// System.out.println(heapCommitted);

		// heap_usage
		String input9 = "jvm,memory,heap_usage";
		heap_usage = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input9).toString();
		hmap.put(input9.replace(',', '.'), heap_usage);
		// System.out.println(heap_usage);

		// non_heap_usage
		String input10 = "jvm,memory,non_heap_usage";
		non_heap_usage = KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json, input10).toString();
		hmap.put(input10.replace(',', '.'), non_heap_usage);
		// System.out.println(non_heap_usage);
		return hmap;

		// LogEndOffset
		// String
		// input11="kafka.log.Log.partition.0.topic.murali_auto_topic.LogEndOffset.value";
		// LogEndOffset=KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json,
		// input11).toString();
		// System.out.println(LogEndOffset);

		// LogStartOffset
		// String
		// input12="kafka.log.Log.partition.0.topic.murali_auto_topic.LogStartOffset.value";
		// LogStartOffset=KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json,
		// input12).toString();
		// System.out.println(LogStartOffset);

		// NumLogSegments
		// String
		// input13="kafka.log.Log.partition.0.topic.murali_auto_topic.NumLogSegments.value";
		// NumLogSegments=KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json,
		// input13).toString();
		// System.out.println(NumLogSegments);

		// Size
		// String
		// input14="kafka.log.Log.partition.0.topic.murali_auto_topic.Size.value";
		// Size=KafkaHttpMetricsReporterUtil.getValueFromJsonKey(json,
		// input14).toString();
		// System.out.println(Size);
	}
}
