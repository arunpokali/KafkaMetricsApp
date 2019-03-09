package com.fournotfive.autoproducerconsumer.producer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fournotfive.autoproducerconsumer.constants.IKafkaConstants;

public class ProducerCreator {

	public static Producer<Long, String> createProducer(KafkaConfiguration kc, ProducerBean pe) {

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kc.getKafkaBroker());
		props.put(ProducerConfig.CLIENT_ID_CONFIG, pe.getClientid());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}
}