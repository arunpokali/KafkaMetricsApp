package com.fournotfive.app.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fournotfive.app.ConfigBean;
import com.fournotfive.app.beans.ConsumerGroupMetricBean;
import com.fournotfive.app.utils.ConfigUtils;
import com.fournotfive.app.utils.KafkaAdminClientDelegator;
import com.fournotfive.app.utils.KafkaHttpMetricsReporterUtil;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Collector.MetricFamilySamples.Sample;
import io.prometheus.client.exporter.common.TextFormat;

@Controller
public class KafkaMetricController {

	@GetMapping("/metrics")
	public void sayHello(HttpServletResponse response, HttpServletRequest request) throws IOException {

		ConfigBean cb = ConfigUtils.getConfigBean();
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, cb.getBrokers());
		AdminClient ac = AdminClient.create(prop);
		KafkaAdminClientDelegator kcd = new KafkaAdminClientDelegator(ac);
		List<ConsumerGroupMetricBean> lcgm = kcd.getMetricsByConsumerGroups(cb);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(TextFormat.CONTENT_TYPE_004);

		CollectorRegistry defaultRegistry = new CollectorRegistry();

		Collector c = new Collector() {
			@Override
			public List<MetricFamilySamples> collect() {
				List<MetricFamilySamples> lmfs = new LinkedList<MetricFamilySamples>();
				List<Sample> samples = new ArrayList<Sample>();
				Sample sample = new Sample("this_is_my_metric_name", Arrays.asList("abcd1", "efgh1"),
						Arrays.asList("abcd2", "efgh2"), 20.4);
				samples.add(sample);
				sample = new Sample("this_is_my_metric_name", Arrays.asList("abcd1", "efgh1"),
						Arrays.asList("abcd22", "efgh22"), 40.4);
				samples.add(sample);
				MetricFamilySamples mfs = new MetricFamilySamples("SomeThingSomething", Type.GAUGE, "Help Help Help",
						samples);
				lmfs.add(mfs);
				return lmfs;
			}
		};

		Collector kafka = new Collector() {
			@Override
			public List<MetricFamilySamples> collect() {
				List<MetricFamilySamples> lmfs = new LinkedList<MetricFamilySamples>();
				List<Sample> samples = new ArrayList<Sample>();
				lcgm.forEach(cgm -> {
					Sample s = new Sample("poffset", Arrays.asList("consumergroup", "partition", "topic"),
							Arrays.asList(cgm.getConsumerGroupId(), cgm.getPartitionNo() + "", cgm.getTopicName()),
							cgm.getEndOffset());
					samples.add(s);
					s = new Sample("coffset", Arrays.asList("consumergroup", "partition", "topic"),
							Arrays.asList(cgm.getConsumerGroupId(), cgm.getPartitionNo() + "", cgm.getTopicName()),
							cgm.getCurrentOffset());
					samples.add(s);
					s = new Sample("lag", Arrays.asList("consumergroup", "partition", "topic"),
							Arrays.asList(cgm.getConsumerGroupId(), cgm.getPartitionNo() + "", cgm.getTopicName()),
							cgm.getLag());
					samples.add(s);
				});
				MetricFamilySamples mfs = new MetricFamilySamples("KafkaMetricsReporterApp", Type.GAUGE,
						"These are the metrics collected by metrics app", samples);
				lmfs.add(mfs);
				return lmfs;
			}
		};

		if (cb.isHttpenabled()) {

			cb.getHttpreporter().forEach(broker -> {

				final Map<String, String> httpMetrics;
				try {
					httpMetrics = KafkaHttpMetricsReporterUtil.assignMetricValues(broker);
				Collector http = new Collector() {
					@Override
					public List<MetricFamilySamples> collect() {
						List<MetricFamilySamples> lmfs = new LinkedList<MetricFamilySamples>();
						List<Sample> samples = new ArrayList<Sample>();
						try {
							httpMetrics.forEach((key, value) -> {
								Sample s = new Sample("httpmetrics", Arrays.asList("jvmmetrics","broker"), Arrays.asList(key,broker),
										Double.parseDouble(value));
								samples.add(s);
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
						MetricFamilySamples mfs = new MetricFamilySamples("KafkaMetricsReporterAppHttp", Type.GAUGE,
								"These are the metrics collected by metrics app", samples);
						lmfs.add(mfs);
						return lmfs;
					}
				};
				defaultRegistry.register(http);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}

		defaultRegistry.register(c);
		defaultRegistry.register(kafka);

		Writer writer = response.getWriter();
		try {
			TextFormat.write004(writer, defaultRegistry.filteredMetricFamilySamples(parse(request)));
			writer.flush();
		} finally {
			writer.close();
		}
		ac.close();
	}

	private Set<String> parse(HttpServletRequest req) {
		String[] includedParam = req.getParameterValues("name[]");
		if (includedParam == null) {
			return Collections.emptySet();
		} else {
			return new HashSet<String>(Arrays.asList(includedParam));
		}
	}
}