package com.fournotfive.autoproducerconsumer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fournotfive.autoproducerconsumer.consumer.ConsumerBean;
import com.fournotfive.autoproducerconsumer.producer.KafkaConfiguration;
import com.fournotfive.autoproducerconsumer.producer.ProducerBean;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.fournotfive.autoproducerconsumer.constants.IKafkaConstants;
import com.fournotfive.autoproducerconsumer.consumer.ConsumerCreator;
import com.fournotfive.autoproducerconsumer.producer.ProducerCreator;

public class App {

	public static void main(String[] args) throws InterruptedException {

		ObjectMapper objectMapper = new ObjectMapper();
		KafkaConfiguration kafkaConfiguration = null;
		try {
			kafkaConfiguration = objectMapper.readValue(new File("./producer_consumer.json"), KafkaConfiguration.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<ProducerBean> producerBeanList = kafkaConfiguration.getPjobs();

		for (ProducerBean pbean : producerBeanList) {
			final Producer<Long, String> producer = ProducerCreator.createProducer(kafkaConfiguration, pbean);

			Runnable p = () -> {
				runProducer(producer, pbean);
			};
			new Thread(p).start();
		}

		Thread.sleep(5000L);

		List<ConsumerBean> consumerBeanList = kafkaConfiguration.getCjobs();

		for (ConsumerBean cbean : consumerBeanList) {
			final Consumer<Long, String> consumer = ConsumerCreator.createConsumer(kafkaConfiguration, cbean);

			Runnable c = () -> {
				runConsumer(consumer, cbean);
			};
			new Thread(c).start();
		}

	}

	static void runConsumer(Consumer<Long, String> consumer, ConsumerBean consumerBean) {

		int noMessageToFetch = 0;
		boolean flag = true;
		while (flag) {
			try {
				Thread.sleep(consumerBean.getDelay() * 1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

			/*
			 * if (consumerRecords.count() == 0) { noMessageToFetch++; if (noMessageToFetch
			 * > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT) break; else continue; }
			 */

			consumerRecords.forEach(record -> {
				System.out.print("---- Record Key " + record.key());
				System.out.print(" - Record value " + record.value());
				System.out.print(" - Record partition " + record.partition());
				System.out.println(" - Record offset " + record.offset());
			});
			consumer.commitAsync();
		}
		consumer.close();
	}

	static void runProducer(Producer<Long, String> producer, ProducerBean pbean) {

		int count = 0;
//This 100 not considered as we do index++ and index--
		for (int index = 0; index < 100; index++) {
			try {
				Thread.sleep(pbean.getDelay() * 1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(pbean.getTopicname(),
					"This is record " + count);
			try {
				RecordMetadata metadata = producer.send(record).get();
				System.out.println("Record sent with key " + count + " : to Topic" + pbean.getTopicname()
						+ " to partition " + metadata.partition() + " with offset " + metadata.offset());
			} catch (ExecutionException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			} catch (InterruptedException e) {
				System.out.println("Error in sending record");
				System.out.println(e);
			}
			index--;
			count++;
		}
	}
}
