package com.fournotfive.autoproducerconsumer.consumer;

import java.util.Collections;
import java.util.Properties;

import com.fournotfive.autoproducerconsumer.producer.KafkaConfiguration;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.fournotfive.autoproducerconsumer.constants.IKafkaConstants;

public class ConsumerCreator {

	public static Consumer<Long, String> createConsumer(KafkaConfiguration kafkaConfiguration, ConsumerBean consumerBean) {

		final Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfiguration.getKafkaBroker());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerBean.getGroupid());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConfiguration.getPoll());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfiguration.getOffsetEarliest());

		final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(consumerBean.getTopic()));
		return consumer;
	}

}
