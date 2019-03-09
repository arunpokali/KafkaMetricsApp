package com.fournotfive.app.samples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.fournotfive.app.utils.KafkaAdminClientDelegator;

public class SampleKafkaAdminClient2 {
	private static final String CONSUMER_GROUP = "murali_test_group";
	private static final String KAFKA_BROKERS = "localhost:9092";
	private static final String ZOOKEEPER_HOST = "localhost:2181";

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
		AdminClient ac = AdminClient.create(prop);

		KafkaAdminClientDelegator kcd = new KafkaAdminClientDelegator(ac);

		ac.listTopics().namesToListings().get().forEach((key, value) -> {
			System.out.println(key); // Topic Name
			System.out.println(value.name()); // Topic Name
			System.out.println(value.toString()); // (name=murali_test, internal=false)
		});

		ac.listConsumerGroups().valid().get().forEach(cgl -> {
			System.out.println(cgl.groupId()); // Group Id
			System.out.println(cgl.isSimpleConsumerGroup()); // Boolean value
		});

		// Just to get the active Consumer Group Details No Offset
		ac.describeConsumerGroups(Arrays.asList(CONSUMER_GROUP)).all().get().forEach((key, value) -> {
			System.out.println(key); // Consumer Group Name

			// Active consumer details
			value.members().forEach(md -> {
				System.out.println(md.host()); // Host
				System.out.println(md.clientId()); // Client Id
				System.out.println(md.consumerId()); // Consumer Id

				System.out.println(md.assignment()); // Member Assignment has
				md.assignment().topicPartitions().forEach(tp -> {
					System.out.println(tp.topic()); // Partition number
					System.out.println(tp.partition()); // Topic Name
				});
			});
		});

		KafkaConsumer<String, String> consumer = SampleKafkaAdminClient.createNewConsumer(KAFKA_BROKERS, CONSUMER_GROUP);

		// Gives only Topic, Partition, consumer offset for the given consumer
		ac.listConsumerGroupOffsets(CONSUMER_GROUP).partitionsToOffsetAndMetadata().get().forEach((key, value) -> {
			System.out.println(key); // Topic Name and Partition
			System.out.println(key.topic());
			System.out.println(key.partition());
			
			//System.out.println(value);// Offset and Metadata
			System.out.println(value.offset()); // Consumer offset
			
			consumer.assign(Arrays.asList(key));
			consumer.seekToEnd(Arrays.asList(key));
			System.out.println(consumer.position(key)); // Producer offset
			
		});
		
		consumer.close();

//		System.out.println(kcd.getGroups());
//		System.out.println(kcd.getTopicsByConsumerGroups());
//		System.out.println(kcd.getTopicsByAll());

	}

}
