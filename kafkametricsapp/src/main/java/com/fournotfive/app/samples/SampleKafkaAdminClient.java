package com.fournotfive.app.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.fournotfive.app.utils.PropUtils;

public class SampleKafkaAdminClient {

	private static final String KAFKA_BROKERS = "localhost:9092";
	private static final String ZOOKEEPER_HOST = "localhost:2181";
	private static final String TOPIC = "murali_auto_topic";
	private static final String GROUP = "consumerGroup10";

	public static Map<String, String> props = null;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		long startTime = System.currentTimeMillis();
		props = PropUtils.get405Props();
		test1();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime * 1.0D / 1000);
	}

	public static void test1() throws InterruptedException, ExecutionException {
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
		AdminClient ac = AdminClient.create(prop);

		// ac.metrics().forEach((key, value) -> {
		// System.out.println(key);
		// System.out.println(value.metricValue());
		// });

		ac.describeTopics(Arrays.asList(TOPIC)).all().get().forEach((key, value) -> {
			System.out.println(key);  // Topic name is given
			value.partitions().forEach(tp -> {
				System.out.println(tp+"====");
			});
		});

		
		KafkaConsumer<String, String> consumer = createNewConsumer("localhost:9092", "murali_test_group");

		ac.listConsumerGroupOffsets(GROUP).partitionsToOffsetAndMetadata().get().forEach((key, value) -> {
			System.out.println(key);
			System.out.println(value);

			consumer.assign(Arrays.asList(key));
			consumer.seekToEnd(Arrays.asList(key));
			System.out.println(consumer.position(key));

			// System.out.println(consumer.committed(key));

			// consumer.listTopics().forEach((key1, value1) -> {
			// System.out.println(key1);
			// value1.forEach(pi -> System.out.println(pi.toString()));
			// });

		});

		ac.listConsumerGroups().all().get().forEach(cg -> {
			System.out.println(cg);
		});

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

	public static long getLogEndOffset(KafkaConsumer<String, String> consumer, String topic,
			int partition, TopicPartition tp) {
		TopicPartition topicPartition = new TopicPartition(topic, partition);
		List<TopicPartition> partitions = new ArrayList<TopicPartition>();
		partitions.add(topicPartition);
		consumer.assign(partitions);
		consumer.seekToEnd(partitions);
		long logEndOffset = consumer.position(topicPartition);

		// System.out.println(consumer.position(tp)+"yyyy");
		// System.out.println(consumer.committed(tp)+"zzzz");

		return logEndOffset;
	}
}
