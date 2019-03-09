package com.fournotfive.app.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.fournotfive.app.ConfigBean;
import com.fournotfive.app.beans.ConsumerGroupMetricBean;

public class KafkaAdminClientDelegator {
	private AdminClient ac;

	public KafkaAdminClientDelegator(AdminClient ac) {
		super();
		this.ac = ac;
	}

	public List<String> getTopicsByAll() throws InterruptedException, ExecutionException {
		return ac.listTopics().names().get().stream().collect(Collectors.toList());
	}

	public List<String> getTopicsByConsumerGroups() throws InterruptedException, ExecutionException {
		List<String> list = new ArrayList<String>();

		/*
		 * ac.listConsumerGroups().valid().get().forEach(cgl -> { try {
		 * list.addAll(ac.listConsumerGroupOffsets(cgl.groupId()).
		 * partitionsToOffsetAndMetadata().get().keySet() .stream().map(tp ->
		 * tp.topic()).collect(Collectors.toList())); } catch (InterruptedException |
		 * ExecutionException e) { e.printStackTrace(); } });
		 */

		ac.listConsumerGroups().valid().get().forEach(cgl -> {
			try {
				list.addAll(getTopicsFromGroup(cgl.groupId()));
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		list.stream().distinct();
		return list;
	}

	public List<String> getGroups() throws InterruptedException, ExecutionException {
		return ac.listConsumerGroups().valid().get().stream().map(cgl -> cgl.groupId()).collect(Collectors.toList());
	}

	public List<String> getTopicsFromGroup(String group) throws InterruptedException, ExecutionException {
		return ac.listConsumerGroupOffsets(group).partitionsToOffsetAndMetadata().get().keySet().stream()
				.map(tp -> tp.topic()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
	}

	public List<String> getGroupsFromTopic(String topic) {
		// looks like this not supported
		return null;
	}

	public List<ConsumerGroupMetricBean> getMetricsByConsumerGroups(ConfigBean cb) {
		List<ConsumerGroupMetricBean> metrics = new ArrayList<ConsumerGroupMetricBean>();
		try {
			ac.listConsumerGroups().valid().get().stream().filter(cgl -> cb.getGroups() == null
					|| cb.getGroups().size() == 0 || cb.getGroups().contains(cgl.groupId())).forEach(cgl -> {
						try {
							ac.listConsumerGroupOffsets(cgl.groupId()).partitionsToOffsetAndMetadata().get()
									.forEach((key, value) -> {
										if (cb.getTopics() == null || cb.getTopics().size() == 0
												|| cb.getTopics().contains(key.topic())) {
											KafkaConsumer<String, String> consumer = KafkaAdminClientDelegator
													.createNewConsumer(cb.getBrokers(), cgl.groupId());
											// consumer.assignment().clear();
											consumer.assign(Arrays.asList(key));
											consumer.seekToEnd(Arrays.asList(key));
											System.out.println(consumer.position(key)); // Producer offset
											ConsumerGroupMetricBean cgmb = new ConsumerGroupMetricBean(cgl.groupId(),
													key.topic(), key.partition(), value.offset(),
													consumer.position(key));
											metrics.add(cgmb);
										}
									});
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					});
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return metrics;
	}

	public static KafkaConsumer<String, String> createNewConsumer(String brokers, String group) {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		return consumer;
	}
}
